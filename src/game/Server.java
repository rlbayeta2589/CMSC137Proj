package src.game;

import java.io.*;
import java.net.*;
import java.util.*;

public class Server implements Runnable{

	private static final int GAME_START=0;
	private static final int IN_PROGRESS=1;
	private static final int GAME_END=2;
	private static final int WAITING_FOR_PLAYERS=3;

    private static DatagramSocket serverSocket = null;
	private static boolean connected = false, bossIsDead = false;
    
    private static HashMap<String,Player> players = new HashMap<String,Player>();
    private static Player boss;
	private static int gameStage=WAITING_FOR_PLAYERS;
	private static String playerData;
	private static int playerCount=0;
	private static int numDeadPlayers = 0;
	private static int numPlayers = 0;
	private static Thread t;

	private static int currX = 100;
	private static int currY = 0;

	public Server(int numPlayers, int port){
		this.numPlayers = numPlayers;
		bossIsDead = false;
		connected = false;
		bossIsDead = false;

		gameStage=WAITING_FOR_PLAYERS;

		playerCount=0;
		numDeadPlayers = 0;
		numPlayers = 0;

		currX = 100;
		currY = 0;

		t = new Thread(this);

		try {
            serverSocket = new DatagramSocket(port);
			serverSocket.setSoTimeout(100);
		} catch (IOException e) {
            System.err.println("Could not listen on port: "+port);
            System.exit(-1);
		}catch(Exception e){
			e.printStackTrace();
		}
		t.start();
	}

	public void broadcast(String msg){
		for(String name : players.keySet()){
			Player player = players.get(name);
			send(player,msg);	
        }
	}

	public void send(Player player, String msg){
		DatagramPacket packet;
		byte buf[] = msg.getBytes();		
		packet = new DatagramPacket(buf, buf.length, player.getAddress(),player.getPort());
		try{
			serverSocket.send(packet);
		}catch(IOException ioe){
			System.out.println("ERROR || Game Server send");
			ioe.printStackTrace();
		}
	}

	public static void killAll(){
		connected = false;
    
		if(serverSocket!=null){
			serverSocket.close();
			serverSocket = null;
		}

    	players.clear();
    	boss = null;
		gameStage=WAITING_FOR_PLAYERS;
		bossIsDead = false;
		playerData = "";
		playerCount = 0;
		numDeadPlayers = 0;
		numPlayers = 0;

		currX = 100;
		currY = 0;
	}

	public synchronized void run(){
		connected = true;
		while(connected){

			byte[] buf = new byte[256];
			DatagramPacket packet = new DatagramPacket(buf, buf.length);
			try{
     			serverSocket.receive(packet);
			}catch(Exception ioe){}
			
			playerData=new String(buf);
			
			playerData = playerData.trim();

			switch(gameStage){
				  case WAITING_FOR_PLAYERS:
						//System.out.println("Game State: Waiting for players...");
						if (playerData.startsWith("CONNECT")){
							currY += 100;
							if(currY > 500){
								currY = 100;
								currX += 100;
							}

							String tokens[] = playerData.split(" ");
							String name = tokens[1].trim();
							Player player = new Player(name,packet.getAddress(),packet.getPort());
							player.setX(currX);
							player.setY(currY);
							players.put(name,player);

							System.out.println("Player connected: "+name);
							broadcast("CONNECTED "+name);
							
							playerCount++;
							if (playerCount==numPlayers){
								gameStage=GAME_START;
							}
						}
					  break;	
				  case GAME_START:
				  	String startData = "START";

				  	boss = new Player("==BOSS==",null,0);
				  	boss.setX(Game.WIDTH-150);
				  	boss.setY(200);

					for(String name : players.keySet()){
						Player player = players.get(name);
						startData += "#" + name + " " + player.getX() + " " + player.getY();
					}

					startData += "#==BOSS==" + " " + boss.getX() + " " + boss.getY() + " " + (5000+((numPlayers-1)*3000));

					broadcast(startData);
					gameStage=IN_PROGRESS;
					break;
				  case IN_PROGRESS:
						String inGameData = "INGAME";
						int x=0, y=0, damage=0;

						if(playerData.equals("")) break;

						if (playerData.startsWith("GAMEOVER_BOSSDEAD")) {
							bossIsDead = true;
							gameStage=GAME_END;
							break;
						}

						if(numDeadPlayers==numPlayers){
							gameStage=GAME_END;
							break;
						}

						String[] playerInfo = playerData.split(" ");					  
						String pname = playerInfo[1];

						try{
							x = (int)Float.parseFloat(playerInfo[2].trim());
							y = (int)Float.parseFloat(playerInfo[3].trim());
							damage = Integer.parseInt(playerInfo[4].trim());
						}catch(Exception e){}

						if (playerData.startsWith("PLAYER")){
							inGameData += "#PLAYER#" + pname + "#" + x + "#" + y + "#" + damage;
						}else if(playerData.startsWith("BOSSBULLET")){
							inGameData += "#BOSSBULLET#"+ x + "#" + y + "#" + damage;
						}else if(playerData.startsWith("BOSS")){
							inGameData += "#BOSS#" + x + "#" + y + "#" + damage;
						}else if(playerData.startsWith("BULLET")){
							inGameData += "#BULLET#" + pname + "#" + x + "#" + y + "#" + damage;
						}else if(playerData.startsWith("DEAD")){
							inGameData += "#DEAD#" + pname;
							numDeadPlayers++;
						}else if(playerData.startsWith("BARRIER")){
							inGameData += "#BARRIER#" + pname + "#" + (damage==1?"ON":"OFF");
						}else if(playerData.startsWith("POWERUP")){
							inGameData += "#POWERUP#" + pname + "#" + x + "#" + y + "#";
							inGameData += Float.parseFloat(playerInfo[4]) + "#" + Integer.parseInt(playerInfo[5]);
						}else if(playerData.startsWith("REGEN")){
							inGameData += "#REGEN#" + damage;
						}else if(playerData.startsWith("BUFF")){
							inGameData += "#BUFF#" + damage;
						}else if(playerData.startsWith("MAXHEALTH")){
							inGameData += "#MAXHEALTH#" + damage;
						}else if(playerData.startsWith("SCORE")){
							inGameData += "#SCORE#" + pname + "#" + damage;
						}

						broadcast(inGameData);
						break;

				  case GAME_END:

				  		if(numDeadPlayers==numPlayers){
				  			broadcast("GAME_OVER#LOSE");
				  		}else if(bossIsDead){
				  			broadcast("GAME_OVER#WIN");
				  		}else{
				  			broadcast("GAME_OVER#LOSE");
				  		}


				  		if(playerData.startsWith("GAME_IN_ETERNAL_VOID")){
				  			connected = false;
				  			break;
				  		}

				  		break;
			}				  
		}

		serverSocket.close();
		serverSocket = null;
		killAll();
	}	
}