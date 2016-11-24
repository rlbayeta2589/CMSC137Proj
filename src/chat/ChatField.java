package src.chat;

import src.GameGUI;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.text.*;
import javax.swing.plaf.basic.*;

public class ChatField extends JPanel{

    private static JPanel sendingArea;
    private static ActionListener messageAction;
    private static JScrollPane scrollArea;
    private static JTextField messageField;
    private static JTextArea messageBox;
    private static ChatClient client;
    
    public ChatField(){
        super();
        setOpaque(true);

        setLayout(new BorderLayout());

        messageField = new JTextField(15);
        messageBox = new JTextArea();
        sendingArea = new JPanel();

        messageField.addActionListener(createMessageAction());

        messageBox.setEditable(false);
        messageBox.setLineWrap(true);

        sendingArea.add(messageField);

        messageField.setBackground(Color.BLACK);
        messageBox.setBackground(new Color(0,0,0,255));
        sendingArea.setBackground(new Color(0,0,0,255));

        messageField.setForeground(Color.BLACK);
        messageBox.setForeground(Color.WHITE);

        DefaultCaret caret = (DefaultCaret) messageBox.getCaret();
        caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);

        scrollArea = new JScrollPane(messageBox);
        scrollArea.setBackground(Color.BLACK);

        scrollArea.getVerticalScrollBar().setUI(new BasicScrollBarUI(){   
            @Override
            protected JButton createDecreaseButton(int orientation) {
                return createZeroButton();
            }

            @Override    
            protected JButton createIncreaseButton(int orientation) {
                return createZeroButton();
            }
            @Override 
            protected void configureScrollBarColors(){
                this.thumbColor = new Color(40,40,40,255);
            }

        });

        setPreferredSize(new Dimension(200,400));
        add(scrollArea,BorderLayout.CENTER);
        add(sendingArea, BorderLayout.SOUTH);
    }

    private ActionListener createMessageAction(){
        ActionListener temp = new ActionListener(){
            public void actionPerformed(ActionEvent ae) {
                if(!messageField.getText().equals("")){
                    displayMessage("You: " + messageField.getText() + "\n");
                         
                    client.sendMessage(messageField.getText());

                    messageBox.setCaretPosition(messageBox.getDocument().getLength());
                    messageField.setText("");
                }

                messageField.setBackground(Color.BLACK);
                GameGUI.focusOnGame();
            }
        };

        return temp;
    }

    public static void focusOnChat(){
        messageField.setBackground(Color.WHITE);
        messageField.requestFocusInWindow();
    }

    public static void displayMessage(String message){
        messageBox.setSize(400,100);
        messageBox.append(message);
    }

    public static void setChatClient(ChatClient cc){
        client = cc;
    }

    private JButton createZeroButton() {
        JButton zero = new JButton();
        zero.setPreferredSize(new Dimension(0, 0));
        zero.setMinimumSize(new Dimension(0, 0));
        zero.setMaximumSize(new Dimension(0, 0));
        return zero;
    }

}