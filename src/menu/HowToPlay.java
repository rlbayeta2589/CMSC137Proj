package src.menu;

import src.GameGUI;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.text.*;

public class HowToPlay extends JPanel implements MouseListener{

	private JPanel back = new JPanel();

	public HowToPlay(){
		super();
		setLayout(null);

		ImageIcon image = new ImageIcon("./src/img/howToPlay.png");
		Image img = resizeImage(image, 1200, 600);
		JLabel background = new JLabel();
		ImageIcon icon = new ImageIcon(img);

		background.setIcon(icon);
		background.setBounds(0, 0, 1200, 600);
		background.setOpaque(false);

		image = new ImageIcon("./src/img/buttonBack.png");
		img = resizeImage(image, 150, 90);
		back.add(new JLabel(new ImageIcon(img)));

		back.setOpaque(false);
		back.setBounds(5,480,150,90);
		back.addMouseListener(this);


		setPreferredSize(new Dimension(1200,600));
        add(back);
		add(background);
	}

	public Image resizeImage(ImageIcon img, int width, int height){
		return (img.getImage().getScaledInstance(width, height,  java.awt.Image.SCALE_SMOOTH));
	}

	public void mouseClicked(MouseEvent me){
		if(me.getSource()==back){
			CardLayout cardLayout = (CardLayout)GameGUI.getCards().getLayout();
			cardLayout.first(GameGUI.getCards());
		}
	}

	public void mousePressed(MouseEvent me){ }

	public void mouseReleased(MouseEvent me){ }

	public void mouseEntered(MouseEvent me){ }

	public void mouseExited(MouseEvent me){ }

}