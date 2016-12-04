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
    private static JLabel armorField;
    private static JLabel scoreField;
    private static JLabel bulletField;

    public StatField(String uname){
        super();
        setLayout(null);


        username = new JLabel(uname, SwingConstants.CENTER);
        healthField = new JLabel("", SwingConstants.CENTER);
        damageField = new JLabel("", SwingConstants.CENTER);
        armorField = new JLabel("", SwingConstants.CENTER);
        scoreField = new JLabel("", SwingConstants.CENTER);
        bulletField = new JLabel("", SwingConstants.LEFT);

        ImageIcon image = new ImageIcon("./src/img/statBackground.jpg");
        Image img = Util.resizeImage(image, 200, 300);
        JLabel background = new JLabel();
        ImageIcon icon = new ImageIcon(img);

        background.setIcon(icon);
        background.setBounds(0, 0, 200, 300);
        background.setOpaque(false);

        username.setBounds(63,43,120,30);
        healthField.setBounds(66,100,100,30);
        damageField.setBounds(60,163,40,30);
        armorField.setBounds(135,163,40,30);
        scoreField.setBounds(66,225,100,30);
        bulletField.setBounds(90,10,100,30);

        username.setFont(new Font("Serif", Font.BOLD, 20));
        healthField.setFont(new Font("Serif", Font.BOLD, 20));
        damageField.setFont(new Font("Serif", Font.BOLD, 20));
        armorField.setFont(new Font("Serif", Font.BOLD, 20));
        scoreField.setFont(new Font("Serif", Font.BOLD, 20));
        bulletField.setFont(new Font("Serif", Font.BOLD, 15));
        
        bulletField.setForeground(Color.YELLOW);

        setPreferredSize(new Dimension(200,300));
        add(bulletField);
        add(username);
        add(healthField);
        add(damageField);
        add(armorField);
        add(scoreField);
        add(background);
    }

    public static void setBullet(int count){
        String temp = "";
        int i;
        for(i=0;i<count;i++){
            temp += "|";
        }

        bulletField.setText(temp);
    }

    public static void setHealth(int health){
        healthField.setText(health+"");
    }

    public static void setDamage(int damage){
        damageField.setText(damage+"");
    }

    public static void setScore(int score){
        scoreField.setText(score+"");
    }

    public static void setArmor(int armor){
        armorField.setText(armor+"");
    }

}