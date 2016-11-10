package src.game;

import java.awt.*;
import javax.swing.*;
import java.awt.image.*;
import java.awt.event.*;
import java.util.*;

public class Bullet extends GameObject{

	public int width = 10, height = 5;

	public Bullet(float x, float y, ObjectId id, int velX){
		super(x,y,id);
		this.velX = velX;
	}

	public void tick(LinkedList<GameObject> object){
		if(x+velX>=Game.WIDTH) object.remove(this);
		x += velX;
	}

	public void render(Graphics g){
		g.setColor(Color.RED);
		g.fillRect((int)x,(int)y,width,height);
	}

	public Rectangle getBounds(){
		return new Rectangle((int)x,(int)y,width,height);
	}


}