package src.menu;

import src.GameGUI;
import src.menu.*;
import src.game.*;
import src.chat.*;

import java.util.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.text.*; 

public class DataPrompt extends JPanel implements MouseListener{

	private JPanel banner = new JPanel();
	private JPanel play = new JPanel();
	private JPanel prompt = new JPanel();
	private JPanel askUsername = new JPanel();
	private JPanel askIPaddress = new JPanel();
	private JPanel askMaxPlayers = new JPanel();

	private JTextField usernameField;
	private JTextField ipField;
	private JTextField maxField;

	private String type;

	public DataPrompt(String utype){
		setLayout(null);

		type = utype;

		usernameField = new JTextField(30);
		ipField = new JTextField(30);

		usernameField.setFont(new Font("Serif", Font.BOLD, 20));
		ipField.setFont(new Font("Serif", Font.BOLD, 20));
		
		usernameField.setHorizontalAlignment(JTextField.CENTER);
		ipField.setHorizontalAlignment(JTextField.CENTER);

        usernameField.setBounds(250,200,700,40);
        ipField.setBounds(250,350,700,40);

		ImageIcon image = new ImageIcon("./src/img/gameBackground.jpg");
		Image img = resizeImage(image, 1200, 600);
		JLabel background = new JLabel();
		ImageIcon icon = new ImageIcon(img);
		
		background.setIcon(icon);
		background.setBounds(0, 0, 1200, 600);
		background.setOpaque(false);

		image = new ImageIcon("./src/img/buttonPlay.png");
		img = resizeImage(image, 180, 70);
		play.add(new JLabel(new ImageIcon(img)));

		image = new ImageIcon("./src/img/incompleteData.png");
		img = resizeImage(image, 250, 60);
		prompt.add(new JLabel(new ImageIcon(img)));

		image = new ImageIcon("./src/img/askusername.png");
		img = resizeImage(image, 800, 100);
		askUsername.add(new JLabel(new ImageIcon(img)));

		image = new ImageIcon("./src/img/askipaddress.png");
		img = resizeImage(image, 800, 100);
		askIPaddress.add(new JLabel(new ImageIcon(img)));

		play.setOpaque(false);
		prompt.setOpaque(false);
		askUsername.setOpaque(false);
		askIPaddress.setOpaque(false);

		play.setBounds(1000,10,180,70);
		prompt.setBounds(920,100,250,60);
		askUsername.setBounds(200,150,800,100);
		askIPaddress.setBounds(200,300,800,100);

		if(utype=="SERVER"){
			image = new ImageIcon("./src/img/askmaxplayers.png");
			img = resizeImage(image, 800, 100);
			askMaxPlayers.add(new JLabel(new ImageIcon(img)));

			askMaxPlayers.setOpaque(false);
			askMaxPlayers.setBounds(200,450,800,100);

			maxField = new JTextField(30);
			maxField.setFont(new Font("Serif", Font.BOLD, 20));
			maxField.setHorizontalAlignment(JTextField.CENTER);
			maxField.setBounds(250,500,700,40);
	        add(maxField);

			image = new ImageIcon("./src/img/createserver.png");
			img = resizeImage(image, 400, 150);
			banner.add(new JLabel(new ImageIcon(img)));

			banner.setOpaque(false);
			banner.setBounds(80,10,400,150);			
		}else{
			image = new ImageIcon("./src/img/createclient.png");
			img = resizeImage(image, 400, 150);
			banner.add(new JLabel(new ImageIcon(img)));

			banner.setOpaque(false);
			banner.setBounds(80,10,400,150);
		}

		play.addMouseListener(this);
		prompt.setVisible(false);

        add(banner);
        add(play);
        add(prompt);
        add(usernameField);
        add(ipField);
        add(askUsername);
        add(askIPaddress);
        add(askMaxPlayers);
        add(background);
	}

	public Image resizeImage(ImageIcon img, int width, int height){
		return (img.getImage().getScaledInstance(width, height,  java.awt.Image.SCALE_SMOOTH));
	}

	public void mouseClicked(MouseEvent me){
		if(me.getSource() == play){
			String uname = usernameField.getText().trim();
			String ip = ipField.getText().trim();
			String max = "";
			if(type.equals("SERVER")){
				max = maxField.getText().trim();
			}

			if((uname.equals("") || ip.equals("")) || (type.equals("SERVER") && max.equals(""))){
				prompt.setVisible(true);

				new java.util.Timer().schedule(new TimerTask(){
					public void run(){
						prompt.setVisible(false);
					}
				},1500);
			}else{
				if(max.equals("")) max = "0";

				MainMenu.setUsername(uname);
				MainMenu.setClientVars(ip,Integer.parseInt(max));
				MainMenu.gameStart(type);
			}
		}
	}

	public void mousePressed(MouseEvent me){ }

	public void mouseReleased(MouseEvent me){ }

	public void mouseEntered(MouseEvent me){
		if(me.getSource() == play){
			ImageIcon image = new ImageIcon("./src/img/buttonPlayHover.png");
			Image img = resizeImage(image, 180, 70);
			((JLabel)(play.getComponent(0))).setIcon(new ImageIcon(img));
		}
	}

	public void mouseExited(MouseEvent me){
		if(me.getSource() == play){
			ImageIcon image = new ImageIcon("./src/img/buttonPlay.png");
			Image img = resizeImage(image, 180, 70);
			((JLabel)(play.getComponent(0))).setIcon(new ImageIcon(img));
		}
	}

}