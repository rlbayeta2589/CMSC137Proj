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

public class ChatClient{
    public static void main(String [] args){
        try{
            Scanner input = new Scanner(System.in);
            String serverName = args[0]; //get IP address of server from first param
            String message = "";
            int port = 8000;


            Socket client = new Socket(serverName, port);


            System.out.println("Just connected to " + client.getRemoteSocketAddress());
            
            Thread sender = new Thread(){
                public void run(){
                    while(true){
                        try{
                            InputStream inFromServer = client.getInputStream();
                            DataInputStream in = new DataInputStream(inFromServer);
                            System.out.println("Server says " + in.readUTF());
                        }catch(Exception e){
                            e.printStackTrace();
                            System.out.println("in from serer Error");
                        }
                    }
                }
            };

            sender.start();

            while(true){

                message = input.nextLine();

                try{
                    OutputStream outToServer = client.getOutputStream();
                    DataOutputStream out = new DataOutputStream(outToServer);
                    out.writeUTF("Client " + client.getLocalSocketAddress()+" says: " +message);
                }catch(Exception e){
                    e.printStackTrace();
                    System.out.println("OUt to server Error");
                }
            }


        }catch(IOException e){
            e.printStackTrace();
            System.out.println("Cannot find (or disconnected from) Server");
        }catch(ArrayIndexOutOfBoundsException e){
            System.out.println("Usage: java GreetingClient <server ip> <port no.> '<your message to the server>'");
        }
    }
}
