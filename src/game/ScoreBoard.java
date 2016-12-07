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

	public ScoreBoard(){
		super();
		setLayout(null);

		setBackground(Color.BLACK);

		user1 = new JLabel("-----", SwingConstants.CENTER);
		user2 = new JLabel("-----", SwingConstants.CENTER);
		user3 = new JLabel("-----", SwingConstants.CENTER);
		user4 = new JLabel("-----", SwingConstants.CENTER);
		user5 = new JLabel("-----", SwingConstants.CENTER);
		
		score1 = new JLabel("00000", SwingConstants.CENTER);
		score2 = new JLabel("00000", SwingConstants.CENTER);
		score3 = new JLabel("00000", SwingConstants.CENTER);
		score4 = new JLabel("00000", SwingConstants.CENTER);
		score5 = new JLabel("00000", SwingConstants.CENTER);

		ImageIcon image = new ImageIcon("./src/img/scoreBoard.png");
		Image img = Util.resizeImage(image, 400, 250);
		JLabel background = new JLabel();
		ImageIcon icon = new ImageIcon(img);

		background.setIcon(icon);
		background.setBounds(0, 0, 400, 250);
		background.setOpaque(false);

		user1.setBounds(50,112,120,30);
		user2.setBounds(50,139,120,30);
		user3.setBounds(50,167,120,30);
		user4.setBounds(50,195,120,30);
		user5.setBounds(50,223,120,30);

		score1.setBounds(230,112,120,30);
		score2.setBounds(230,139,120,30);
		score3.setBounds(230,167,120,30);
		score4.setBounds(230,195,120,30);
		score5.setBounds(230,223,120,30);

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
		

		setPreferredSize(new Dimension(400,250));
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

	public static void updateData(String data){
		String[] playerScores = data.split(", ");
		int top = 0;

		if(playerScores.length>=5) top = 5;
		else top = playerScores.length;

		switch (top) {
			case 5:
				user5.setText(playerScores[4].split("=")[0]);
				score5.setText(playerScores[4].split("=")[1]);
			case 4:
				user4.setText(playerScores[3].split("=")[0]);
				score4.setText(playerScores[3].split("=")[1]);
			case 3:
				user3.setText(playerScores[2].split("=")[0]);
				score3.setText(playerScores[2].split("=")[1]);
			case 2:
				user2.setText(playerScores[1].split("=")[0]);
				score2.setText(playerScores[1].split("=")[1]);
			case 1:
				user1.setText(playerScores[0].split("=")[0]);
				score1.setText(playerScores[0].split("=")[1]);
		}

	}

}