import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Main extends JFrame{

	Game os;
	JPanel window;
	JLabel sample;
	
	Main(String title){
		super(title);

		window = new JPanel();
		sample = new JLabel("CHAT HERE NIGGAS");	
	sample.setOpaque(false);
		os = new Game();

		setLayout(new BorderLayout());
		add(os, BorderLayout.CENTER);
		add(sample,BorderLayout.WEST);

		setPreferredSize(1200,600);
		setResizable(false);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setVisible(true);

		os.start();
	}

	public static void main(String[] args) {
		final Main game = new Main("The Boss Fight");
	}
}