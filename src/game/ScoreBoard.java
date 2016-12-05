package src.game;

import src.GameGUI;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.text.*;

public class ScoreBoard extends JPanel{

    private static JLabel user1;
    private static JLabel score1;

    private static JLabel user2;
    private static JLabel score2;

    private static JLabel user3;
    private static JLabel score3;

    private static JLabel user4;
    private static JLabel score4;

    private static JLabel user5;
    private static JLabel score5;

    public ScoreBoard(String uname){
        super();
        setLayout(null);


        user1 = new JLabel("username", SwingConstants.CENTER);
        user2 = new JLabel("username", SwingConstants.CENTER);
        user3 = new JLabel("username", SwingConstants.CENTER);
        user4 = new JLabel("username", SwingConstants.CENTER);
        user5 = new JLabel("username", SwingConstants.CENTER);
        
        score1 = new JLabel("999999", SwingConstants.CENTER);
        score2 = new JLabel("999999", SwingConstants.CENTER);
        score3 = new JLabel("999999", SwingConstants.CENTER);
        score4 = new JLabel("999999", SwingConstants.CENTER);
        score5 = new JLabel("999999", SwingConstants.CENTER);

        ImageIcon image = new ImageIcon("./src/img/scoreBoard.png");
        Image img = Util.resizeImage(image, 200, 300);
        JLabel background = new JLabel();
        ImageIcon icon = new ImageIcon(img);

        background.setIcon(icon);
        background.setBounds(0, 0, 600, 400);
        background.setOpaque(false);

        user1.setBounds(63,43,120,30);
        /*healthField.setBounds(66,100,100,30);
        damageField.setBounds(60,163,40,30);
        armorField.setBounds(135,163,40,30);
        scoreField.setBounds(66,225,100,30);
        bulletField.setBounds(90,10,100,30);*/

        user1.setFont(new Font("Serif", Font.BOLD, 20));
        user2.setFont(new Font("Serif", Font.BOLD, 20));
        user3.setFont(new Font("Serif", Font.BOLD, 20));
        user4.setFont(new Font("Serif", Font.BOLD, 20));
        user5.setFont(new Font("Serif", Font.BOLD, 20));
        score1.setFont(new Font("Serif", Font.BOLD, 20));
        score2.setFont(new Font("Serif", Font.BOLD, 20));
        score3.setFont(new Font("Serif", Font.BOLD, 20));
        score4.setFont(new Font("Serif", Font.BOLD, 20));
        score5.setFont(new Font("Serif", Font.BOLD, 20));
        

        setPreferredSize(new Dimension(600,400));
        add(user1);
        add(user2);
        add(user3);
        add(user4);
        add(user5);
        add(score1);
        add(score2);
        add(score3);
        add(score4);
        add(score5);
        add(background);
    }
}