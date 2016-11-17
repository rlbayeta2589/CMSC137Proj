package src.chat;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.text.*; 

public class ChatField extends JPanel{

    private JTextField messageField;
    private JPanel sendingArea;
    private ActionListener messageAction;
    private JScrollPane scrollArea;
    private static JTextArea messageBox;
    private static ChatClient client;
    
    private int hideTime = 5;
    private boolean hidden = true;
    private javax.swing.Timer timerLife;

    public ChatField(){
        super();
        setOpaque(true);

        timerLife = initializeTimer();

        setLayout(new BorderLayout());

        messageField = new JTextField(30);
        messageBox = new JTextArea();
        sendingArea = new JPanel();

        messageField.addActionListener(createMessageAction());

        messageBox.setEditable(false);
        messageBox.setLineWrap(true);

        sendingArea.add(messageField);

        messageField.setBackground(new Color(0,0,0,255));
        messageBox.setBackground(new Color(0,0,0,255));
        sendingArea.setBackground(new Color(0,0,0,255));

        messageField.setForeground(Color.WHITE);
        messageBox.setForeground(Color.WHITE);

        DefaultCaret caret = (DefaultCaret) messageBox.getCaret();
        caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);

        scrollArea = new JScrollPane(messageBox);

        setPreferredSize(new Dimension(400,200));
        add(scrollArea,BorderLayout.CENTER);
        add(sendingArea, BorderLayout.SOUTH);
    }

    private ActionListener createMessageAction(){
        ActionListener temp = new ActionListener(){
            public void actionPerformed(ActionEvent ae) {
                displayMessage("You: " + messageField.getText() + "\n");
                     
                client.sendMessage(messageField.getText());

                messageBox.setCaretPosition(messageBox.getDocument().getLength());
                messageField.setText("");

                messageField.requestFocusInWindow();
            }
        };

        return temp;
    }

    private javax.swing.Timer initializeTimer(){
        javax.swing.Timer temp = new javax.swing.Timer(1000, new ActionListener(){
            public void actionPerformed(ActionEvent ae){
                hideTime-=1;
                if(hideTime==0){
                    hidden = true;
                }
            }
        });

        return temp;
    }

    public static void displayMessage(String message){
        messageBox.setSize(400,100);
        messageBox.append(message);
    }

    public static void setChatClient(ChatClient cc){
        client = cc;
    }

    public void setHidden(boolean hide){
        hidden = hide;
    }
}