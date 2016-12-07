package src.game;

import src.GameGUI;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.text.*;

public class GameOverBanner extends JPanel{

	public GameOverBanner(String data){
		super();
		setLayout(null);

		ImageIcon image = new ImageIcon("./src/img/gameOverWin.png");

		if(data=="LOSE"){
			image = new ImageIcon("./src/img/gameOverLose.png");			
		}

		Image img = Util.resizeImage(image, 400, 130);
		JLabel background = new JLabel();
		ImageIcon icon = new ImageIcon(img);

		background.setIcon(icon);
		background.setBounds(0, 0, 400, 130);
		background.setOpaque(false);

		setPreferredSize(new Dimension(400,130));
		add(background);
	}

}