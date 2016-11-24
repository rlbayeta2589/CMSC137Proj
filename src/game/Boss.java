package src.game;

import java.awt.*;
import javax.swing.*;
import java.awt.image.*;
import java.awt.event.*;
import java.util.*;

public class Boss extends GameObject{

	private final int UP = 1, DOWN = 2, STAY = 3;
	private Random random = new Random();
	private javax.swing.Timer timer;
	private Game game;
	public int width = 150, height = 200, damage = 10;
	public int health, orig_health;
	public boolean moving = false, dead = false;
	public float prevX, prevY;
	public float destination;
	public int direction, animate_count=0, frame_no=1;

	public Boss(float x, float y, ObjectId id, int health,Game game, String utype){
		super(x,y,id);
		this.health = health;
		this.orig_health = health;
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
			game.send("BOSS BOSS "+x+" "+y+" 0");
		}
	}

	public void render(Graphics g){

		ImageIcon ship = new ImageIcon("./src/img/boss.png");
		Image newIMG = Util.resizeImage(ship,width-20,height);

		if(dead){
			ship = new ImageIcon("./src/img/boss_explosion_"+frame_no+".png");
			newIMG = Util.resizeImage(ship,width,height);
			ship = new ImageIcon(newIMG);
			g.drawImage(ship.getImage(),(int)x,(int)y,null);
			return;
		}

		ship = new ImageIcon(newIMG);
		g.drawImage(ship.getImage(),(int)x,(int)y,null);
		

		float percent_health = ((float)health/orig_health)*height;

		g.setColor(Color.GREEN);
		g.fillRect((int)x + (width-20),(int)y+(height-(int)percent_health),width,(int)percent_health);
		g.setColor(Color.BLUE);
		g.drawRect((int)x + (width-20),(int)y,width,height);
	}

	public Thread createMovements(){
        Thread movement = new Thread() {

            public void run(){
            	while(health > 0){

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
            		}else{
            			Handler hand = game.getHandler();
            			float bulletStart = y+(height/2);
						hand.addObject(new BossBullet(x,bulletStart,hand,ObjectId.BossBullet, -2));
						game.send("BOSSBULLET BULLET "+x+" "+bulletStart+" 0");
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
		Boss temp = this;
		health -= damage;
		if(health <= 0){
			dead = true;

			timer = new javax.swing.Timer(200, new ActionListener(){
				public void actionPerformed(ActionEvent ae){
					frame_no++;

					if(frame_no>12 && animate_count<2){
						frame_no = 0;
						animate_count++;
					}else if(frame_no>15 && animate_count==2){
						frame_no = 5;
						animate_count++;
					}else if(animate_count==3){
						timer.setRepeats(false);
						timer.stop();
						game.getHandler().removeObject(temp);
					}
				}
			});
			timer.setRepeats(true);
			timer.start();
		}
	}

}