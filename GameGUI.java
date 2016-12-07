package src;

import src.menu.*;
import src.game.*;
import src.chat.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class GameGUI extends JFrame{
	
	private static StatField statField;
	private static ChatField chatfield;
	private static ScoreBoard board;
	private static GameOverBanner banner_win;
	private static GameOverBanner banner_lose;

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
		cards.add(instantiateGame("","",0,"",0), "Start");
		cards.setOpaque(false);
		
		getContentPane().add(cards);

		setSize(1200,600);
		setResizable(false);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setVisible(true);
	}

	private static JPanel instantiateGame(String username, String type, int max, String servername, int port){
		JPanel mainPanel = new JPanel();
		JPanel gamePanel = new JPanel();
		JPanel westPanel = new JPanel();

		mainPanel.setLayout(new BorderLayout());
		gamePanel.setLayout(null);
		westPanel.setLayout(new BorderLayout());

		game = new Game(username,type,max,servername,port);
		board = new ScoreBoard();
		banner_win = new GameOverBanner("WIN");
		banner_lose = new GameOverBanner("LOSE");
		chatfield = new ChatField();
		statField = new StatField(username);

		westPanel.add(statField, BorderLayout.NORTH);
		westPanel.add(chatfield, BorderLayout.CENTER);

		game.setBounds(-5,0,1000,600);
		board.setBounds(300,175,400,250);
		banner_win.setBounds(300,50,400,130);
		banner_lose.setBounds(300,50,400,130);
		board.setVisible(false);
		banner_win.setVisible(false);
		banner_lose.setVisible(false);

		gamePanel.setOpaque(false);
		gamePanel.add(board);
		gamePanel.add(banner_win);
		gamePanel.add(banner_lose);
		gamePanel.add(game);

		mainPanel.add(gamePanel,BorderLayout.CENTER);
		mainPanel.add(westPanel, BorderLayout.WEST);
		mainPanel.setOpaque(false);
		
		return mainPanel;

	}

	public static void showBanner(String type){
		if(type.equals("WIN")) banner_win.setVisible(true);
		else if(type.equals("LOSE")) banner_lose.setVisible(true);
	}

	public static void showBoard(){
		board.setVisible(true);
	}

	public static void hideBoard(){
		board.setVisible(false);
	}

	public static void updateScores(String data){
		ScoreBoard.updateData(data);
	}

	public static void focusOnGame(){
		game.requestFocusInWindow();
	}

	public static boolean isGameOver(){
		return Game.GAME_OVER;
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

	public static void killAll(){
		MainMenu.killAll();
		Game.killAll();
	}

	public static void updateCards(String username, String type, int max, String servername,int port) throws Exception{  // this will be used when
		cards.removeAll();								// the main game card is already done
		cards.setLayout(new CardLayout());
		cards.add(new MainMenu(), "Menu");
		cards.add(new DataPrompt("SERVER"), "DataPromptServer");
		cards.add(new DataPrompt("CLIENT"), "DataPromptClient");
		cards.add(instantiateGame(username,type,max,servername,port), "Start");
		cards.setOpaque(false);
	}
}