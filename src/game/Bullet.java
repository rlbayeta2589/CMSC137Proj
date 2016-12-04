package src.game;

import java.awt.*;
import javax.swing.*;
import java.awt.image.*;
import java.awt.event.*;
import java.util.*;

public class Bullet extends GameObject{

	public int width = 8, height = 10, damage;
	private Handler handler;

	public Bullet(float x, float y, Handler handler, ObjectId id, int velX, int damage){
		super(x,y,id);
		this.velX = velX;
		this.damage = damage;
		this.handler = handler;

		if(damage>=25){
			height += 5;
			width += 5;
		}

		if(damage>=50){
			height += 10;
			width += 10;
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

		// g.setColor(Color.RED);
		// g.fillRect((int)x,(int)y,width,height);
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
					boss.damageBoss(damage);

					// System.out.println("Boss Health "+boss.getHealth());
				}
			}else if(tempObject.getId() == ObjectId.BossBullet){
				if(getBounds().intersects(tempObject.getBounds())){
					handler.removeObject(this);
					handler.removeObject(tempObject);
					// System.out.println("Boss Health "+boss.getHealth());
				}
			}
		}
	}
}