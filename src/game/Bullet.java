package src.game;

import java.awt.*;
import javax.swing.*;
import java.awt.image.*;
import java.awt.event.*;
import java.util.*;

public class Bullet extends GameObject{

	public int width = 8, height = 10, damage;
	private Handler handler;
	private Game game;
	private String name;

	public Bullet(String name, float x, float y, Handler handler, Game game, ObjectId id, int velX, int dmg){
		super(x,y,id);
		this.name = name;
		this.velX = velX;
		this.damage = dmg;
		this.game = game;
		this.handler = handler;

		if(damage>=25){
			height += 5;
			width += 5;
		}

		if(damage>=50){
			height += 10;
			width += 10;
		}

		if(damage>=100){
			height += 15;
			width += 15;
		}
	}

	public void tick(LinkedList<GameObject> object){
		if(x+velX>=Game.WIDTH) handler.removeObject(this);
		x += velX;
		
		collision(object);
	}

	public void render(Graphics g){

		ImageIcon bullet = new ImageIcon("./src/img/bullet.png");
		Image newIMG = Util.resizeImage(bullet,width,height);
		bullet = new ImageIcon(newIMG);
		g.drawImage(bullet.getImage(),(int)x,(int)(y-(height/2)),null);

	}

	public Rectangle getBounds(){
		return new Rectangle((int)x,(int)(y-(height/2)),width,height);
	}

	public void collision(LinkedList<GameObject> object){
		for(int i = 0; i < handler.object.size(); i++){
			GameObject tempObject = handler.object.get(i);

			if(tempObject.getId() == ObjectId.Boss){
				if(getBounds().intersects(tempObject.getBounds())){
					handler.removeObject(this);
					Boss boss = (Boss)tempObject;
					int point = damage*2;
					boss.damageBoss(damage);
					
					if(!boss.isDead()) game.send("SCORE "+name+" 0 0 "+point);
				}
			}else if(tempObject.getId() == ObjectId.BossBullet){
				if(getBounds().intersects(tempObject.getBounds())){
					handler.removeObject(this);
					handler.removeObject(tempObject);
					game.send("SCORE "+name+" 0 0 10");
				}
			}
		}
	}
}