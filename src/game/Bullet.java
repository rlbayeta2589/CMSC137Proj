package src.game;

import java.awt.*;
import javax.swing.*;
import java.awt.image.*;
import java.awt.event.*;
import java.util.*;

public class Bullet extends GameObject{

	public int width = 10, height = 5;
	private Handler handler;

	public Bullet(float x, float y, Handler handler, ObjectId id, int velX){
		super(x,y,id);
		this.velX = velX;
		this.handler = handler;
	}

	public void tick(LinkedList<GameObject> object){
		if(x+velX>=Game.WIDTH) object.remove(this);
		x += velX;
		
		Collision(object);
	}

	public void render(Graphics g){
		g.setColor(Color.RED);
		g.fillRect((int)x,(int)y,width,height);
	}

	public Rectangle getBounds(){
		return new Rectangle((int)x,(int)y,width-2,height);
	}

	private void Collision(LinkedList<GameObject> object){
		for(int i = 0; i < handler.object.size(); i++){
			GameObject tempObject = handler.object.get(i);

			if(tempObject.getId() == ObjectId.Boss){
				if(getBounds().intersects(tempObject.getBounds())){
					Boss boss = (Boss)tempObject;
					boss.damageBoss(50);
					System.out.println("Boss Health"+boss.getHealth());
					object.remove(this);
				}
			}
		}
	}
}