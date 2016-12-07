package src.game;

import java.io.*;
import java.net.*;
import java.util.*;

public class Server implements Runnable{

	private static final int GAME_START=0;
	private static final int IN_PROGRESS=1;
	private final int GAME_END=2;
	private final int WAITING_FOR_PLAYERS=3;

    private HashMap<String,Player> players = new HashMap<String,Player>();
    private Player boss;
    private DatagramSocket serverSocket = null;
	private int gameStage=WAITING_FOR_PLAYERS;
	private boolean connected = false;
	private String playerData;
	private int playerCount=0;
	private int numDeadPlayers = 0;
	private int numPlayers;
	private Thread t = new Thread(this);

	private int currX = 100;
	private int currY = 0;

	public Server(int numPlayers, int port){
		this.numPlayers = numPlayers;
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

	public void run(){
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
						}else if (playerData.startsWith("BOSSDEAD")) {
							broadcast("ENDGAME");
							println("BOSS DEAD");
						}else if(numDead == playerCount){
							broadcast("ENDGAME");
							println("PLAYERS DEAD")
						}

						broadcast(inGameData);
						break;
			}				  
		}
	}	
}