package src.game;

import java.awt.*;
import javax.swing.*;
import java.awt.image.*;
import java.awt.event.*;
import java.util.*;

public class BossBullet extends GameObject{

	public int width = 10, height = 5, damage;
	private Handler handler;

	public BossBullet(float x, float y, Handler handler, ObjectId id, int velX){
		super(x,y,id);
		this.velX = velX;
		this.damage = damage;
		this.handler = handler;
	}

	public void tick(LinkedList<GameObject> object){
		if(x+velX<=-1*width) object.remove(this);
		x += velX;
		
		collision(object);
	}

	public void render(Graphics g){
		g.setColor(new Color(66,133,255));
		g.fillRect((int)x,(int)y,width,height);
	}

	public Rectangle getBounds(){
		return new Rectangle((int)x,(int)y,width,height);
	}

	public void collision(LinkedList<GameObject> object){
		for(int i = 0; i < handler.object.size(); i++){
			GameObject tempObject = handler.object.get(i);

			if(tempObject.getId() == ObjectId.SpaceShip){
				if(getBounds().intersects(tempObject.getBounds())){
					object.remove(this);
				}
			}
		}
	}
}