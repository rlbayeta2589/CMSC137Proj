package src.menu;

import src.GameGUI;
import src.game.*;
import src.chat.*;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.text.*; 

public class UsernamePrompt extends JPanel{
	private String username;
	private JTextField usernameField;

	public UsernamePrompt(){
		setLayout(null);

		ImageIcon image = new ImageIcon("./src/img/mainBackground.jpg");
		Image img = resizeImage(image, 800, 600);
		JLabel background = new JLabel();
		ImageIcon icon = new ImageIcon(img);
		
		background.setIcon(icon);
		background.setBounds(0, 0, 800, 600);
		background.setOpaque(false);

		JLabel label = new JLabel("Enter username:");
		label.setForeground(Color.white);

		usernameField = new JTextField(30);
		usernameField.addActionListener(acceptUsername());
		
		DefaultCaret caret = (DefaultCaret) usernameField.getCaret();
        caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);

        label.setBounds(250,320,300,30);
        usernameField.setBounds(250,350,300,30);
		
		add(label);
        add(usernameField);
        add(background);
	

	}

	public Image resizeImage(ImageIcon img, int width, int height){
		return (img.getImage().getScaledInstance(width, height,  java.awt.Image.SCALE_SMOOTH));
	}

	private ActionListener acceptUsername(){
		ActionListener temp = new ActionListener(){
			public void actionPerformed(ActionEvent ae){
				MainMenu.setUsername(usernameField.getText());
				CardLayout cardLayout = (CardLayout)GameGUI.getCards().getLayout();
				cardLayout = (CardLayout)GameGUI.getCards().getLayout();
				cardLayout.show(GameGUI.getCards(), "Menu");
			}
		};

		return temp;
	}

}