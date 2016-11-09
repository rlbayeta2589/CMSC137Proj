package src.chat;




import java.net.*;
import java.io.*;
import java.util.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;
import javax.swing.text.*;

public class ChatClient{
    
    JFrame      chat    = new JFrame("chat");
    JTextField  messageBox;
    static JTextArea   messages;
    
    public static void main(String [] args){
        try{
            Scanner input = new Scanner(System.in);
            String serverName = args[0]; //get IP address of server from first param
            String username = args[1];
            String message = "";
            int port = 8000;


            Socket client = new Socket(serverName, port);


            System.out.println("Just connected to " + client.getRemoteSocketAddress());
            
            Thread sender = new Thread(){
                public void run(){
	                ChatClient chatClient = new ChatClient();
	                chatClient.chatBox(username, client);
	                
	                while(true){
                        try{
                            InputStream inFromServer = client.getInputStream();
                            DataInputStream in = new DataInputStream(inFromServer);
                           //System.out.println(in.readUTF());
                            	
                            messages.append(in.readUTF() + "\n");
                        }catch(Exception e){
                            e.printStackTrace();
                            System.out.println("in from server Error");
                        }
                    }
					
                }
            };

            sender.start();

            


        }catch(IOException e){
            e.printStackTrace();
            System.out.println("Cannot find (or disconnected from) Server");
        }catch(ArrayIndexOutOfBoundsException e){
            System.out.println("Usage: java GreetingClient <server ip> <port no.> '<your message to the server>'");
        }
    }


    public void chatBox(String username, Socket client) {
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());

        JPanel southPanel = new JPanel();
       	
        messageBox = new JTextField(30);
        messageBox.requestFocusInWindow();
        messageBox.addActionListener(new sendMessageListener(username,client));

        messages = new JTextArea();
        messages.setEditable(false);
        messages.setLineWrap(true);

        DefaultCaret caret = (DefaultCaret) messages.getCaret();
		caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);

        mainPanel.add(new JScrollPane(messages), BorderLayout.CENTER);

        southPanel.add(messageBox);
        
        mainPanel.add(BorderLayout.SOUTH, southPanel);

        chat.add(mainPanel);
        chat.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        chat.setSize(500, 500);
        chat.setVisible(true);
    
    }

    class sendMessageListener implements ActionListener {
        String username;
        Socket client;
        sendMessageListener(String username, Socket client){
        	this.username = username;
        	this.client = client;
        }
        
        public void actionPerformed(ActionEvent event) {
        	messages.append("You:   " + messageBox.getText() + "\n");
            try{
                OutputStream outToServer = client.getOutputStream();
                DataOutputStream out = new DataOutputStream(outToServer);
                out.writeUTF(username + ":   " + messageBox.getText());
            } catch(Exception e){}
            messageBox.setText("");
        
            messageBox.requestFocusInWindow();
        }
    }

}
