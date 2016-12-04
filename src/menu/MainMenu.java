package src.menu;

import src.GameGUI;
import src.game.*;
import src.chat.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class MainMenu extends JPanel implements MouseListener{
	private JPanel createGame = new JPanel();
	private JPanel joinGame = new JPanel();
	private JPanel quitGame = new JPanel();
	private JPanel menu = new JPanel();	

	private static ChatServer server;
	private static ChatClient client;
	private static String servername;
	private static String username;
	private static int maxPlayers;
	private static final int PORT = 8000;

	public MainMenu(){
		setLayout(null);
		setPreferredSize(new Dimension(1200,600));

		ImageIcon image = new ImageIcon("./src/img/mainBackground.jpg");
		Image img = resizeImage(image, 1200, 600);
		JLabel background = new JLabel();
		ImageIcon icon = new ImageIcon(img);
		
		background.setIcon(icon);
		background.setBounds(0, 0, 1200, 600);
		background.setOpaque(false);
		
		image = new ImageIcon("./src/img/buttonCreate.png");
		img = resizeImage(image, 300, 90);
		createGame.add(new JLabel(new ImageIcon(img)));
		
		image = new ImageIcon("./src/img/buttonJoin.png");
		img = resizeImage(image, 300, 90);
		joinGame.add(new JLabel(new ImageIcon(img)));
		
		image = new ImageIcon("./src/img/buttonQuit.png");
		img = resizeImage(image, 100, 50);
		quitGame.add(new JLabel(new ImageIcon(img)));
	
		createGame.setOpaque(false);
		joinGame.setOpaque(false);
		quitGame.setOpaque(false);
	
		createGame.addMouseListener(this);
		joinGame.addMouseListener(this);
		quitGame.addMouseListener(this);

		createGame.setBounds(250,70,300,90);
		joinGame.setBounds(250,170,300,90);
		quitGame.setBounds(680,300,100,100);

		menu.setLayout(null);
		menu.add(createGame);
		menu.add(joinGame);
		menu.add(quitGame);
		menu.setOpaque(false);
		menu.setBounds(200, 200, 800, 400);

		image = new ImageIcon("./src/img/sample.png");
		img = resizeImage(image, 600, 220);
		JLabel logo = new JLabel(new ImageIcon(img));
		logo.setBounds(300, 35, 600, 220);
	
		add(logo);
		add(menu);
		add(background);
	}

	//////////////////////////////////
	public static void setClientVars(String server, int max){
		servername = server;
		maxPlayers = max;
	}////////////////////////////////

	public static void setMaxPlayers(int max){
		maxPlayers = max;
	}

	public static void setUsername(String uname){
		GameGUI.TITLE = GameGUI.TITLE + "<"+uname+"> ";
		username = uname;
	}

	public Image resizeImage(ImageIcon img, int width, int height){
		return (img.getImage().getScaledInstance(width, height,  java.awt.Image.SCALE_SMOOTH));
	}
	
	public static void gameStart(String utype){
		CardLayout cardLayout = (CardLayout)GameGUI.getCards().getLayout();
		
		try{
			GameGUI.updateCards(username,utype,maxPlayers,servername,PORT);
			if(utype.equals("SERVER")){
				server = new ChatServer(PORT);
				server.start();
			}
			client = new ChatClient(servername,username,PORT);
			client.start();
			GameGUI.attachedChatClient(client);
		}catch(Exception e){}

		cardLayout = (CardLayout)GameGUI.getCards().getLayout();
		cardLayout.show(GameGUI.getCards(), "Start");
		GameGUI.focusOnGame();
		GameGUI.getGame().start();
	}

	public void mouseClicked(MouseEvent me){
		CardLayout cardLayout = (CardLayout)GameGUI.getCards().getLayout();
			if(me.getSource() == createGame) {
				cardLayout = (CardLayout)GameGUI.getCards().getLayout();
				cardLayout.show(GameGUI.getCards(), "DataPromptServer");
			}
			if(me.getSource() == joinGame){
				cardLayout = (CardLayout)GameGUI.getCards().getLayout();
				cardLayout.show(GameGUI.getCards(), "DataPromptClient");
			}
			if(me.getSource() == quitGame){
				System.exit(0);
			}
	}

	public void mousePressed(MouseEvent me){ }

	public void mouseReleased(MouseEvent me){ }

	public void mouseEntered(MouseEvent me){
		if(me.getSource() == createGame){
			//play sound here
			ImageIcon image = new ImageIcon("./src/img/buttonCreateHover.png");
			Image img = resizeImage(image, 300, 90);
			((JLabel)(createGame.getComponent(0))).setIcon(new ImageIcon(img));
		}
		if(me.getSource() == joinGame){
			ImageIcon image = new ImageIcon("./src/img/buttonJoinHover.png");
			Image img = resizeImage(image, 300, 90);
			((JLabel)(joinGame.getComponent(0))).setIcon(new ImageIcon(img));
		}
		if(me.getSource() == quitGame){
			ImageIcon image = new ImageIcon("./src/img/buttonQuitHover.png");
			Image img = resizeImage(image, 100, 50);
			((JLabel)(quitGame.getComponent(0))).setIcon(new ImageIcon(img));
		}
	}

	public void mouseExited(MouseEvent me){
		if(me.getSource() == createGame){
			//play sound here
			ImageIcon image = new ImageIcon("./src/img/buttonCreate.png");
			Image img = resizeImage(image, 300, 90);
			((JLabel)(createGame.getComponent(0))).setIcon(new ImageIcon(img));
		}
		if(me.getSource() == joinGame){
			ImageIcon image = new ImageIcon("./src/img/buttonJoin.png");
			Image img = resizeImage(image, 300, 90);
			((JLabel)(joinGame.getComponent(0))).setIcon(new ImageIcon(img));
		}
		if(me.getSource() == quitGame){
			ImageIcon image = new ImageIcon("./src/img/buttonQuit.png");
			Image img = resizeImage(image, 100, 50);
			((JLabel)(quitGame.getComponent(0))).setIcon(new ImageIcon(img));
		}
	}
}