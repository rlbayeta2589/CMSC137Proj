package src.game;

import java.awt.*;
import javax.swing.*;
import java.awt.image.*;
import java.awt.event.*;
import java.util.*;

public class Boss extends GameObject{


	public int width = 150, height = 200, health;
	public float prevX, prevY;

	public Boss(float x, float y, ObjectId id, int health){
		super(x,y,id);
		this.health = health;
	}

	public void tick(LinkedList<GameObject> object){
		
	}

	public void render(Graphics g){
		g.setColor(Color.blue);
		g.fillRect((int)Game.WIDTH-150,200,width,height);
	
		Graphics2D g2d = (Graphics2D)g;
		g2d.setColor(Color.red);
		g2d.draw(getBounds()); 
	}

	public Rectangle getBounds(){
		return new Rectangle((int)x,(int)y,5,height);
	}
	
	public int getHealth(){
		return health;
	}

	public void damageBoss(int damage){
		health -= damage;
	}
}