//package com.project.game.chat;


/*
    TODO :
    Catching Exceptions
    Simplify Code
    Learn how to use package
*/

import java.net.*;
import java.io.*;
import java.util.*;

public class ChatServer extends Thread{
    private ServerSocket serverSocket;
    private HashMap<Socket,Thread> clientNodes  = new HashMap<Socket,Thread>();

    public ChatServer(int port) throws IOException{
        serverSocket = new ServerSocket(port);
        //serverSocket.setSoTimeout(10000);
    }

    public void run(){
        boolean connected = true;
        while(connected){
            try{
                Socket server = serverSocket.accept();

                System.out.println("Just connected to " + server.getRemoteSocketAddress());

                Thread receiver = createReceiver(server);

                clientNodes.put(server,receiver);

                receiver.start();

            }catch(Exception e){
                e.printStackTrace();
                //System.out.println("Input/Output Error!");
                //possible cause: client was disconnected while waiting for input
                break;
            }
        }
    }

    private Thread createReceiver(Socket s){
        Thread receiver = new Thread() {
            
            private Socket server = s;

            public void run(){
                while(true){
                    try{
                        DataInputStream in = new DataInputStream(server.getInputStream()); 
                        String msg = in.readUTF();
                        System.out.println(msg);
                        broadcastMessage(msg, server.getRemoteSocketAddress().toString()); 
                    }catch(IOException e){
                        e.printStackTrace();
                        System.out.println("Receiver Error");
                    }
                }
            }

            private void broadcastMessage(String message, String address){
                for(Socket add : clientNodes.keySet()){
                    if(!add.getRemoteSocketAddress().toString().equals(address)){
                        try{
                            DataOutputStream out = new DataOutputStream(add.getOutputStream());
                            out.writeUTF(message);
                        }catch(IOException e){
                            e.printStackTrace();
                            System.out.println("Receiver Error");
                        }
                    }
                }
            }
        };

        return receiver;
    }


    public static void main(String[] args){
        try{
            int port = 8000;
            Thread t = new ChatServer(port);
            t.start();
        }catch(IOException e){
            //e.printStackTrace();
            System.out.println("Usage: java GreetingServer <port no.>\n"+
                    "Make sure to use valid ports (greater than 1023)");
        }catch(ArrayIndexOutOfBoundsException e){
            //e.printStackTrace();
            System.out.println("Usage: java GreetingServer <port no.>\n"+
                    "Insufficient arguments given.");
        }
    }
}
