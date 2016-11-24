package src.game;

import src.GameGUI;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.text.*;

public class StatField extends JPanel{

    private static JLabel username;
    private static JLabel healthField;
    private static JLabel damageField;
    private static JLabel scoreField;

    public StatField(String uname){
        super();
        setLayout(null);


        username = new JLabel(uname, SwingConstants.CENTER);
        healthField = new JLabel("", SwingConstants.CENTER);
        damageField = new JLabel("", SwingConstants.CENTER);
        scoreField = new JLabel("", SwingConstants.CENTER);

        ImageIcon image = new ImageIcon("./src/img/statBackground.jpg");
        Image img = Util.resizeImage(image, 200, 300);
        JLabel background = new JLabel();
        ImageIcon icon = new ImageIcon(img);

        background.setIcon(icon);
        background.setBounds(0, 0, 200, 300);
        background.setOpaque(false);

        username.setBounds(63,43,120,30);
        healthField.setBounds(66,100,100,30);
        damageField.setBounds(66,163,100,30);
        scoreField.setBounds(66,225,100,30);

        username.setFont(new Font("Serif", Font.BOLD, 20));
        healthField.setFont(new Font("Serif", Font.BOLD, 20));
        damageField.setFont(new Font("Serif", Font.BOLD, 20));
        scoreField.setFont(new Font("Serif", Font.BOLD, 20));

        setPreferredSize(new Dimension(200,300));
        add(username);
        add(healthField);
        add(damageField);
        add(scoreField);
        add(background);
    }

    public static void setHealth(int health){
        healthField.setText(health + "/250");
    }

    public static void setDamage(int damage){
        damageField.setText(damage+"");
    }

    public static void setScore(int score){
        scoreField.setText(score+"");
    }

}