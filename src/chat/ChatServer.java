package src.chat;


import java.net.*;
import java.io.*;
import java.util.*;

public class ChatServer extends Thread{
    private ServerSocket serverSocket;
    private HashMap<Socket,Thread> clientNodes  = new HashMap<Socket,Thread>();
    private boolean connected = true;

    public ChatServer(int port) throws IOException{
        serverSocket = new ServerSocket(port);
    }

    public void run(){
        while(connected){
            try{
                System.out.println("SUCCESS || waiting for connections");

                Socket newClient = serverSocket.accept();
                Thread receiver = createReceiver(newClient);
                
                clientNodes.put(newClient, receiver);
                receiver.start();

                System.out.println("SUCCESS || just connected to " + newClient.getRemoteSocketAddress());
            }catch(IOException e){
                System.out.println("  ERROR || client was disconnected while waiting for input");
                connected = false;
            }
        }
    }

    private Thread createReceiver(Socket s){
        Thread receiver = new Thread() {
            
            private Socket server = s;

            public void run(){
                while(!server.isClosed()){
                    try{
                        DataInputStream in = new DataInputStream(server.getInputStream());
                        String msg = in.readUTF();
                        
                        System.out.println("SUCCESS || message receive from " + server.getRemoteSocketAddress());

                        broadcastMessage(msg, server.getRemoteSocketAddress().toString()); 
                    }catch(SocketException e){
                        closeServer(server);
                        System.out.println("  ERROR || unexpected socket close");
                    }catch(IOException e){
                        closeServer(server);
                        System.out.println("  ERROR || a client was disconnected");
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
                            clientNodes.remove(add);
                            System.out.println("  ERROR || a client was disconnected and cannot send message");
                        }
                    }
                }
            }
        };

        return receiver;
    }

    private void closeServer(Socket server){
        try{
            connected = false;
            server.close();
        }catch(IOException e){
            System.out.println("  ERROR || closing a server");
        }
    }


/*    public static void main(String[] args){
        try{
            ChatServer server = new ChatServer(8000);
            server.start();
        }catch(Exception e){
            e.printStackTrace();
        }
    }*/
}
