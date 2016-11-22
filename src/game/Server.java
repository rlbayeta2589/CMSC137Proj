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
    private DatagramSocket serverSocket = null;
	private int gameStage=WAITING_FOR_PLAYERS;
	private boolean connected = false;
	private String playerData;
	private int playerCount=0;
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
		}catch(Exception e){}

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
							Player player = new Player(tokens[1],packet.getAddress(),packet.getPort());
							player.setX(currX);
							player.setY(currY);
							System.out.println("Player connected: "+tokens[1]);
							players.put(tokens[1],player);
							broadcast("CONNECTED "+tokens[1]);
							
							playerCount++;
							if (playerCount==numPlayers){
								gameStage=GAME_START;
							}
						}
					  break;	
				  case GAME_START:
					// System.out.println("Game State: START");
				  	String startData = "START";

					for(String name : players.keySet()){
						Player player = players.get(name);
						startData += "#" + name + " " + player.getX() + " " + player.getY();
					}

					broadcast(startData);
					gameStage=IN_PROGRESS;
					break;
				  case IN_PROGRESS:
					  // System.out.println("Game State: IN_PROGRESS");
					  String inGameData = "INGAME";

					  if (playerData.startsWith("PLAYER")){
					  	System.out.println(playerData);

						String[] playerInfo = playerData.split(" ");					  
						String pname =playerInfo[1];
						int x = (int)Float.parseFloat(playerInfo[2].trim());
						int y = (int)Float.parseFloat(playerInfo[3].trim());
						int damage = Integer.parseInt(playerInfo[4].trim());
						if(!pname.equals("BULLET")){
							Player player=players.get(pname);					  
							player.setX(x);
							player.setY(y);

						  	players.put(pname,player);
					  	}

					  	for(String name : players.keySet()){
							Player temp = players.get(name);
							inGameData += "#" + name + " " + temp.getX() + " " + temp.getY();
						}

						if(pname.equals("BULLET")){
							inGameData += "#BULLET " + x + " " + y + " " + damage;
						}

						broadcast(inGameData);
					  }
					  break;
			}				  
		}
	}	
}