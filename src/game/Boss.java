package src.game;

import java.awt.*;
import javax.swing.*;
import java.awt.image.*;
import java.awt.event.*;
import java.util.*;

public class Boss extends GameObject{

	private final int UP = 1, DOWN = 2, STAY = 3;
	private Random random = new Random();
	private Game game;
	public int width = 150, height = 200, health;
	public boolean moving = false;
	public float prevX, prevY;
	public float destination;
	public int direction;

	public Boss(float x, float y, ObjectId id, int health,Game game, String utype){
		super(x,y,id);
		this.health = health;
		this.game = game;

		if(utype=="SERVER"){
			Thread movement = createMovements();
			movement.start();
		}
	}

	public synchronized void tick(LinkedList<GameObject> object){

		if(direction == UP && y+velY<destination){
			y = destination;
			setVelY(0);
			moving = false;
		}else if(direction == DOWN && y+velY>destination){
			y = destination;
			setVelY(0);
			moving = false;
		}

		prevY = y;

		if(moving) y += velY;

		if(game!=null && prevY!=y){
			game.send("PLAYER ==BOSS== "+x+" "+y+" 0");
		}
	}

	public void render(Graphics g){
		ImageIcon ship = new ImageIcon("./src/img/boss.png");
		Image newIMG = Util.resizeImage(ship,width,height);
		ship = new ImageIcon(newIMG);
		g.drawImage(ship.getImage(),(int)x,(int)y,null);
	}

	public Thread createMovements(){
        Thread movement = new Thread() {

            public void run(){
            	while(health != 0){

            		try{
            			Thread.sleep(800);
            		}catch(Exception e){}

            		int move = random.nextInt(100) + 1;
            	
            		if(!moving && move>70){
            			moving = true;
						destination = (float)random.nextInt(Game.HEIGHT-height-5) + 5;

						if(y < destination){
							setVelY(2);
							direction = DOWN;
						}else{
							setVelY(-2);
							direction = UP;
						}
            		}
            	}
            }
        };

        return movement;
	}

	public Rectangle getBounds(){
		return new Rectangle((int)x,(int)y,1,height);
	}
	
	public int getHealth(){
		return health;
	}

	public void damageBoss(int damage){
		health -= damage;
	}

}