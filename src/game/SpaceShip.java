package src.game;

import java.awt.*;
import javax.swing.*;
import java.awt.image.*;
import java.awt.event.*;
import java.util.*;

public class SpaceShip extends GameObject{

	private Game game;
	private String name;
	public int width = 70, height =40, damage=10, health=250;
	public float prevX, prevY;
	public boolean dead = false;

	public SpaceShip(float x, float y, ObjectId id, Game game, String name){
		super(x,y,id);
		this.game = game;
		this.name = name;

		prevX = x;
		prevY = y;
	}

	public void tick(LinkedList<GameObject> object){
		if(x+velX<=5) x = 5;
		if(y+velY<=5) y = 5;
		if(x+width+velX>= Game.WIDTH 	) x = Game.WIDTH - width - 5;
		if(y+height+velY>=Game.HEIGHT-35) y = Game.HEIGHT - height - velY - 35;

		prevX = x;
		prevY = y;

		x += velX;
		y += velY;

		if(game!=null && (prevX!=x || prevY!=y)){
			game.send("PLAYER "+name+" "+x+" "+y+" "+damage);
		}
	}

	public void render(Graphics g){
		ImageIcon ship = new ImageIcon("./src/img/spaceship.png");

		if(dead){
			ship = new ImageIcon("./src/img/explosion.png");
			Image newIMG = Util.resizeImage(ship,width,height);
			ship = new ImageIcon(newIMG);
			g.drawImage(ship.getImage(),(int)x,(int)y,null);
			return;

		}else if(this.id!=null){
			ship = new ImageIcon("./src/img/spaceship_p1.png");
		}else{
			ship = new ImageIcon("./src/img/spaceship_pX.png");
		}
		
		Image newIMG = Util.resizeImage(ship,width,height);
		ship = new ImageIcon(newIMG);
		g.drawImage(ship.getImage(),(int)x,(int)y,null);	
	}

	public Rectangle getBounds(){
		return new Rectangle((int)x,(int)y,width,height);
	}

	public int getDamage(){
		return this.damage;
	}

	public void explode(){
		dead = true;
	}

	public boolean isDead(){
		return this.dead;
	}

	public void damageSpaceShip(int indmg){
		SpaceShip temp = this;
		health = health - indmg;
		if(health<=0){
			health = 0;
			dead = true;

			game.send("DEAD "+name);

			new java.util.Timer().schedule(new TimerTask(){
				public void run(){
					game.getHandler().removeObject(temp);
				}
			},3000);
		}
		StatField.setHealth(health);
	}
}