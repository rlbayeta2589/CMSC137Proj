package src.chat;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.text.*; 

public class ChatField extends JPanel{

    private JTextField messageField;
    private JPanel sendingArea;
    private ActionListener messageAction;
    private static JTextArea messageBox;
    private static ChatClient client;

    public ChatField(){
        super();

        setLayout(new BorderLayout());

        messageField = new JTextField(30);
        messageBox = new JTextArea();
        sendingArea = new JPanel();

        messageField.addActionListener(createMessageAction());

        messageBox.setEditable(false);
        messageBox.setLineWrap(true);

        sendingArea.add(messageField);

        DefaultCaret caret = (DefaultCaret) messageBox.getCaret();
        caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);

        add(new JScrollPane(messageBox),BorderLayout.CENTER);
        add(sendingArea, BorderLayout.SOUTH);
    }

    private ActionListener createMessageAction(){
        ActionListener temp = new ActionListener(){
            public void actionPerformed(ActionEvent ae) {
                displayMessage("You: " + messageField.getText() + "\n");
                     
                client.sendMessage(messageField.getText());

                messageField.setText("");

                messageField.requestFocusInWindow();
            }
        };

        return temp;
    }

    public static void displayMessage(String message){
        messageBox.append(message);
    }

    public static void setChatClient(ChatClient cc){
        client = cc;
    }
}