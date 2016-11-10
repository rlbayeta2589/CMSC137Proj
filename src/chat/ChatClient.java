package src.chat;

import java.net.*;
import java.io.*;
import java.util.*;

public class ChatClient extends Thread{

    private boolean connected = true;
    private String username;
    private Socket client;

    public ChatClient(String serverName, String username, int port){

        this.client = initializeSocket(serverName, port);
        this.username = username;

        System.out.println("SUCCESS || connection to " + client.getRemoteSocketAddress());
    }

    public void run(){

        while(connected){
            try{
                InputStream inFromServer = client.getInputStream();
                DataInputStream in = new DataInputStream(inFromServer);
                ChatField.displayMessage(in.readUTF()+"\n");
                //System.out.println(in.readUTF());
            }catch(Exception e){
                System.out.println("  ERROR || server was disconnected");
                connected = false;
            }

        }
    }

    private Socket initializeSocket(String serverName, int port){
        try{
            return new Socket(serverName, port);
        }catch(IOException e){
            System.out.println("  ERROR || initializing socket");
            return null;
        }
    }

    public void sendMessage(String message){
        try{
            OutputStream outToServer = client.getOutputStream();
            DataOutputStream out = new DataOutputStream(outToServer);
            out.writeUTF(this.username + ": " + message);
        }catch(Exception e){
            System.out.println("  ERROR || send message to server");
        }
    }


/*    public static void main(String [] args){
        try{
            Scanner input = new Scanner(System.in);
            ChatClient client = new ChatClient(args[0], args[1], 8000);
            client.start();
          

            while(true){         //kapag nasaloob na ng laro ang chat di na kailangan to kaya sa main
                                // ko na lang nilagay
                client.sendMessage(input.nextLine());
            }
        }catch(ArrayIndexOutOfBoundsException e){
            System.out.println("Usage: java ChatClient <server ip> <username>");
        }
    }
*/}