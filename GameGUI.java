package src;

import src.menu.MainMenu;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class GameGUI extends JFrame{
	
	private static JPanel cards = new JPanel();


	public GameGUI(String title) throws Exception{
		super(title);

		cards.setLayout(new CardLayout());
		cards.add(new MainMenu(), "Start");
		/*
			Insert more display here
			(e.g.) the panel that will be displayed when create
						game is clicked
				   the panel that will be displayed when join
				   		game is clicked
		*/
		cards.setOpaque(false);
		
		getContentPane().add(cards);

		setSize(800,600);
		setResizable(false);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setVisible(true);
	}

	public static JPanel getCards(){
		return cards;
	}

	public static void updateCards() throws Exception{  // this will be used when
		cards.removeAll();								// the main game card is already done
		cards.setLayout(new CardLayout());
		cards.setOpaque(false);
	}

}