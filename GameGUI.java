package src;

import src.menu.MainMenu;
import src.game.*;
import src.chat.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class GameGUI extends JFrame{
	
	private static ChatField chatfield;
	private static JPanel cards = new JPanel();
	private static Game game;

	public GameGUI(String title) throws Exception{
		super(title);

		cards.setLayout(new CardLayout());
		cards.add(new MainMenu(), "Menu");
		cards.add(instantiateGame(), "Start");
		/*
			Insert more display here
			(e.g.) the panel that will be displayed when create
						game is clicked
				   the panel that will be displayed when join
				   		game is clicked
		*/
		cards.setOpaque(false);
		
		getContentPane().add(cards);

		setSize(1200,600);
		setResizable(false);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setVisible(true);
	}

	private static JPanel instantiateGame(){
		JPanel gamePanel = new JPanel();
		
		gamePanel.setLayout(new BorderLayout());

		game = new Game();
		chatfield = new ChatField();

		gamePanel.add(game,BorderLayout.CENTER);
		gamePanel.add(chatfield, BorderLayout.WEST);
		gamePanel.setOpaque(false);
		
		return gamePanel;
	}

	public static Game getGame(){
		return game;
	}

	public static JPanel getCards(){
		return cards;
	}

	public static void attachedChatClient(ChatClient cc){
		chatfield.setChatClient(cc);
	}

	public static void updateCards() throws Exception{  // this will be used when
		cards.removeAll();								// the main game card is already done
		cards.setLayout(new CardLayout());
		cards.add(new MainMenu(), "Menu");
		cards.add(instantiateGame(), "Start");
		cards.setOpaque(false);
	}
}