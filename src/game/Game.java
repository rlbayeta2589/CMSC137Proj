package src.game;

import src.GameGUI;

import java.awt.*;
import javax.swing.*;
import java.awt.image.*;
import java.awt.event.*;
import java.io.*;
import java.net.*;
import java.util.*;

public class Game extends Canvas implements Runnable {

	public static int WIDTH, HEIGHT;

	private static HashMap<String,SpaceShip> spaceships = new HashMap<String,SpaceShip>();
	private static Boss boss;

	private ImageIcon background;
	private boolean running = false;
	private Thread thread;
	private Thread client;

    private DatagramSocket socket;
	private String server="localhost";
	private boolean connected = false;
	private String serverData;
	private int PORT;

	public String name = "";
	public String type = "";
	public int maxPlayers = 3;

	private Game game;
	private Handler handler;

	private JLabel bossHealth;

	public Game(String uname, String utype, int max, String servername, int port){
		super();
		name = uname;
		type = utype;
		game = this;
		maxPlayers = max;
		server = servername;
		PORT = port;

		try{
			socket = new DatagramSocket();
				socket.setSoTimeout(100);
		}catch(Exception e){
			System.out.println("ERROR");
		}

		setPreferredSize(new Dimension(1200, 600));
	}

	public void init(){
		handler = new Handler();

		WIDTH = getWidth();
		HEIGHT = getHeight();

		if(type=="SERVER"){
			new Server(maxPlayers,PORT);
		}

		client = createClientReceiver();
		client.start();

		this.addKeyListener(new KeyInput(handler,game,name));
	}

	public synchronized void start(){
		if(running)
			return;

		running = true;
		thread = new Thread(this);
		thread.start();
	}

	public void run(){
		init();
		this.requestFocus();
		long lastTime = System.nanoTime();
		double amountOfTicks = 60.0;
		double ns = 1000000000 / amountOfTicks;
		double delta = 0;
		long timer = System.currentTimeMillis();
		int updates = 0;
		int frames = 0;

		while(running){
			long now = System.nanoTime();
			delta += (now - lastTime) / ns;
			lastTime = now;
			while(delta >= 1){
				tick();
				updates++;
				delta--;
			}
			render();
			frames++;
					
			if(System.currentTimeMillis() - timer > 1000){
				timer += 1000;
				GameGUI.changeTitle(GameGUI.TITLE + " (FPS:" + frames + " TICKS:" + updates + ")");
				frames = 0;
				updates = 0;
			}
		}
	}

	public void tick(){
		handler.tick();
	}

	public void render(){
		BufferStrategy bs = this.getBufferStrategy();
		if(bs == null){
			this.createBufferStrategy(3);
			return;
		}

		Graphics g = bs.getDrawGraphics();

		background = new ImageIcon("./src/img/gameBackground.jpg");
		Image newIMG = Util.resizeImage(background,WIDTH,HEIGHT);
		background = new ImageIcon(newIMG);
		g.drawImage(background.getImage(),0,0,null);

		handler.render(g);

		g.dispose();
		bs.show();
	}

	public void send(String msg){
		try{
			byte[] buf = msg.getBytes();
        	InetAddress address = InetAddress.getByName(server);
        	DatagramPacket packet = new DatagramPacket(buf, buf.length, address, 8000);
        	socket.send(packet);
        }catch(Exception e){}
	}

    private Thread createClientReceiver(){

        Thread receiver = new Thread() {

            public synchronized void run(){
				while(true){
					
					byte[] buf = new byte[256];
					DatagramPacket packet = new DatagramPacket(buf, buf.length);
					try{
		     			socket.receive(packet);
					}catch(Exception ioe){}
					
					serverData=new String(buf);
					serverData=serverData.trim();

					if (!connected && serverData.startsWith("CONNECTED "+name)){
						String[] connectData = serverData.split(" ");
						connected=true;
						System.out.println("Connected.");
					}else if (!connected){
						System.out.println("Connecting..");				
						send("CONNECT "+name);
					}else if (connected){
						if (serverData.startsWith("CONNECTED")){
							String[] connectData = serverData.split(" ");
							if(!connectData[1].equals(name)){
								System.out.println("connected " + serverData);
							}
						}
						if(serverData.startsWith("START")){
							String[] startData = serverData.split("#");
							SpaceShip temp;

							for(int i=1;i<startData.length;i++){
								String[] playerData = startData[i].split(" ");
								String uuname = playerData[0];
								if(uuname.equals("==BOSS==")){
									boss = new Boss(Float.parseFloat(playerData[1]),Float.parseFloat(playerData[2]),ObjectId.Boss,Integer.parseInt(playerData[3]),game,type);
									handler.addObject(boss);
								}else if(uuname.equals(name)){
									temp = new SpaceShip(Float.parseFloat(playerData[1]),Float.parseFloat(playerData[2]),ObjectId.SpaceShip,game,uuname);
									handler.addObject(temp);
									spaceships.put(uuname,temp);
									StatField.setHealth(250);
									StatField.setDamage(10);
									StatField.setScore(0);
								}else{
									temp = new SpaceShip(Float.parseFloat(playerData[1]),Float.parseFloat(playerData[2]),null,game,uuname);
									handler.addObject(temp);
									spaceships.put(uuname,temp);
								}

							}

						}
						if (serverData.startsWith("INGAME")){
							String[] inGameData = serverData.split("#");

							String dataType = inGameData[1];

							if(dataType.equals("PLAYER")){
								String uname = inGameData[2];
								if(!uname.equals(name)){
									SpaceShip temp = spaceships.get(inGameData[2]);
									temp.setX(Float.parseFloat(inGameData[3]));
									temp.setY(Float.parseFloat(inGameData[4]));
								}
							}else if(dataType.equals("BULLET")){
								String uname = inGameData[2];
								if(!uname.equals(name)){
									float xxx = Float.parseFloat(inGameData[3]);
									float yyy = Float.parseFloat(inGameData[4]);
									int dmg = Integer.parseInt(inGameData[5]);
									handler.addObject(new Bullet(xxx,yyy,handler,ObjectId.Bullet,5,dmg));
								}
							}else if(dataType.equals("BOSS") && !type.equals("SERVER")){
								boss.setX(Float.parseFloat(inGameData[2]));
								boss.setY(Float.parseFloat(inGameData[3]));
							}else if(dataType.equals("BOSSBULLET") && !type.equals("SERVER")){
								float xxx = Float.parseFloat(inGameData[2]);
								float yyy = Float.parseFloat(inGameData[3]);
								handler.addObject(new BossBullet(xxx,yyy,handler,ObjectId.BossBullet, -2));
							}else if(dataType.equals("DEAD")){
								String uname = inGameData[2];
								if(!uname.equals(name)){
									SpaceShip temp = spaceships.get(inGameData[2]);
									temp.explode();

									new java.util.Timer().schedule(new TimerTask(){
										public void run(){
											handler.removeObject(temp);
											spaceships.remove(temp);
										}
									},3000);
								}
							}

						}
					}
	            }
			}
        };

        return receiver;
    }

    public Handler getHandler(){
    	return this.handler;
    }
}