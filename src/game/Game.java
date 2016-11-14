package src.game;

import src.GameGUI;

import java.awt.*;
import javax.swing.*;
import java.awt.image.*;
import java.awt.event.*;


public class Game extends Canvas implements Runnable {

	private ImageIcon background;
	private boolean running = false;
	private Thread thread;
	public static int WIDTH, HEIGHT;

	Handler handler;

	public Game(){
		super();
		setPreferredSize(new Dimension(1200, 600));
	}

	public void init(){
		handler = new Handler();

		WIDTH = getWidth();
		HEIGHT = getHeight();

		System.out.println(HEIGHT);
		System.out.println(WIDTH);

		handler.addObject(new SpaceShip(100,100,ObjectId.SpaceShip));

		this.addKeyListener(new KeyInput(handler));
	}

	public synchronized void start(){
		if(running)
			return;

		running = true;
		thread = new Thread(this);
		thread.start();
	}

	public void run(){
		init();
		this.requestFocus();
		long lastTime = System.nanoTime();
		double amountOfTicks = 60.0;
		double ns = 1000000000 / amountOfTicks;
		double delta = 0;
		long timer = System.currentTimeMillis();
		int updates = 0;
		int frames = 0;

		while(running){
			long now = System.nanoTime();
			delta += (now - lastTime) / ns;
			lastTime = now;
			while(delta >= 1){
				tick();
				updates++;
				delta--;
			}
			render();
			frames++;
					
			if(System.currentTimeMillis() - timer > 1000){
				timer += 1000;
				GameGUI.changeTitle(GameGUI.TITLE + " (FPS:" + frames + " TICKS:" + updates + ")");
				// System.out.println("FPS: " + frames + " TICKS: " + updates);
				frames = 0;
				updates = 0;
			}
		}
	}

	public void tick(){
		handler.tick();
	}

	public void render(){
		BufferStrategy bs = this.getBufferStrategy();
		if(bs == null){
			this.createBufferStrategy(3);
			return;
		}

		Graphics g = bs.getDrawGraphics();

		background = new ImageIcon("./src/img/gameBackground.jpg");
		Image newIMG = Util.resizeImage(background,WIDTH,HEIGHT);
		background = new ImageIcon(newIMG);
		g.drawImage(background.getImage(),0,0,null);

		handler.render(g);

		g.dispose();
		bs.show();
	}
}