package src;

import src.menu.*;
import src.game.*;
import src.chat.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class GameGUI extends JFrame{
	
	private static ChatField chatfield;
	private static JPanel cards = new JPanel();
	private static JFrame frame;
	private static Game game;
	public static String TITLE;

	public GameGUI(String title) throws Exception{
		super(title);

		frame = this;
		TITLE = title;

		cards.setLayout(new CardLayout());
		cards.add(new MainMenu(), "Menu");
		cards.add(new DataPrompt("SERVER"), "DataPromptServer");
		cards.add(new DataPrompt("CLIENT"), "DataPromptClient");
		cards.add(instantiateGame("","",0,0), "Start");
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

	private static JPanel instantiateGame(String username, String type, int max, int port){
		JPanel gamePanel = new JPanel();
		
		/*gamePanel.setLayout(new BorderLayout());

		game = new Game();
		chatfield = new ChatField();

		gamePanel.add(game,BorderLayout.CENTER);
		gamePanel.add(chatfield, BorderLayout.WEST);
		gamePanel.setOpaque(false);
		
		return gamePanel;*/

		gamePanel.setLayout(null);
		game = new Game(username,type,max,port);
		chatfield = new ChatField();

		game.setBounds(0,0,1200,600);
		chatfield.setBounds(400,370,400,200);

		gamePanel.setOpaque(false);

		gamePanel.add(chatfield);
		gamePanel.add(game);

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

	public static void changeTitle(String title){
		frame.setTitle(title);
	}

	public static void updateCards(String username, String type, int max, int port) throws Exception{  // this will be used when
		cards.removeAll();								// the main game card is already done
		cards.setLayout(new CardLayout());
		cards.add(new MainMenu(), "Menu");
		cards.add(new DataPrompt("SERVER"), "DataPromptServer");
		cards.add(new DataPrompt("CLIENT"), "DataPromptClient");
		cards.add(instantiateGame(username,type,max,port), "Start");
		cards.setOpaque(false);
	}
}