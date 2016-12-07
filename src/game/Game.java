package src.game;

import src.GameGUI;
import src.chat.*;

import java.awt.*;
import javax.swing.*;
import java.awt.image.*;
import java.awt.event.*;
import java.io.*;
import java.net.*;
import java.util.*;

public class Game extends Canvas implements Runnable {

	public static int WIDTH, HEIGHT;
	public static boolean GAME_OVER = false;

	private static HashMap<String,SpaceShip> spaceships;
	private static HashMap<String,Integer> scores;
	private static Boss boss;

	private ImageIcon background;
	private Thread thread;
	private Thread client;

    private static DatagramSocket socket;
	private static boolean connected = false;
	private static boolean running = false;
	
	private static String serverData;
	private String server="localhost";
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

		spaceships = new HashMap<String,SpaceShip>();
		scores = new HashMap<String,Integer>();
	
		try{
			socket = new DatagramSocket();
			socket.setSoTimeout(100);
		}catch(Exception e){
			System.out.println("ERROR");
		}
		
		setFocusTraversalKeysEnabled(false);
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
        	
        	if(!GAME_OVER) socket.send(packet);
        }catch(Exception e){}
	}

	public static void killAll(){
		running = false;

		if(socket!=null){
			socket.close();
			socket = null;
		}

		serverData = "";
		spaceships.clear();
		scores.clear();
		GAME_OVER = false;
		connected = false;
		running = false;

		Server.killAll();
	}

    private Thread createClientReceiver(){

        Thread receiver = new Thread() {

            public synchronized void run(){
				while(!GAME_OVER){
					
					byte[] buf = new byte[256];
					DatagramPacket packet = new DatagramPacket(buf, buf.length);
					try{
		     			if(!GAME_OVER) socket.receive(packet);
					}catch(Exception ioe){}
					
					serverData="";
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
                			ChatField.displayMessage("           = = = GAME START = = =   \n\n");
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
									StatField.setBullet(30);
									StatField.setArmor(0);
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
									handler.addObject(new Bullet(uname,xxx,yyy,handler,game,ObjectId.Bullet,5,dmg));
								}
							}else if(dataType.equals("BOSS") && !type.equals("SERVER")){
								boss.setX(Float.parseFloat(inGameData[2]));
								boss.setY(Float.parseFloat(inGameData[3]));
							}else if(dataType.equals("BOSSBULLET") && !type.equals("SERVER")){
								float xxx = Float.parseFloat(inGameData[2]);
								float yyy = Float.parseFloat(inGameData[3]);
								int dmg = Integer.parseInt(inGameData[4]);
								handler.addObject(new BossBullet(xxx,yyy,handler,ObjectId.BossBullet,dmg));
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
							}else if(dataType.equals("BARRIER")){
								String uname = inGameData[2];
								if(!uname.equals(name)){
									SpaceShip temp = spaceships.get(inGameData[2]);
									temp.setShielded(inGameData[3].equals("ON")?true:false);
								}
							}else if(dataType.equals("POWERUP") && !type.equals("SERVER")){
								String puptype = inGameData[2];
								float xxx = Float.parseFloat(inGameData[3]);
								float yyy = Float.parseFloat(inGameData[4]);
								float velX = Float.parseFloat(inGameData[5]);
								int value = Integer.parseInt(inGameData[6]);
								handler.addObject(new PowerUp(xxx,yyy,velX,handler,ObjectId.PowerUp,puptype,value));
							}else if(dataType.equals("REGEN") && !type.equals("SERVER")){
								boss.setHealth(Integer.parseInt(inGameData[2]));
							}else if(dataType.equals("BUFF") && !type.equals("SERVER")){
								boss.setDamage(Integer.parseInt(inGameData[2]));
							}else if(dataType.equals("MAXHEALTH") && !type.equals("SERVER")){
								boss.setOrigHealth(Integer.parseInt(inGameData[2]));
							}else if(dataType.equals("SCORE")){
								String uname = inGameData[2];
								Integer point = Integer.parseInt(inGameData[3]);
								SpaceShip temp = spaceships.get(uname);
								
								if(uname.equals(name)){
									temp.addScore(point);
								}

								if(scores.containsKey(uname)){
									scores.put(uname,scores.get(uname)+point);
								}else{
									scores.put(uname,point);
								}

								String scoreString = Util.entriesSortedByValues(scores).toString();
								scoreString = scoreString.substring(1, scoreString.length() - 1);

								GameGUI.updateScores(scoreString);
							}

						}else if(serverData.startsWith("GAME_OVER")){
							String[] inGameData = serverData.split("#");
							GAME_OVER = true;



							System.out.println("GAME_OVER");
							ChatField.displayMessage("           = = =  GAME END  = = =   \n");
							ChatField.displayMessage("    -     Enter \"BYE\" to Exit Game   -   \n\n");
							GameGUI.showBoard();
							GameGUI.showBanner(inGameData[1]);

							System.out.println(inGameData[1]);

							send("GAME_IN_ETERNAL_VOID");
						}
					}
	            }

	            socket.close();
				socket = null;
			}
        };

        return receiver;
    }

    public Handler getHandler(){
    	return this.handler;
    }
}