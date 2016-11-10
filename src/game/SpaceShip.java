package src.game;

import java.awt.*;
import javax.swing.*;
import java.awt.image.*;
import java.awt.event.*;
import java.util.*;

public class SpaceShip extends GameObject{

	public int width = 70, height =40;

	public SpaceShip(float x, float y, ObjectId id){
		super(x,y,id);
	}

	public void tick(LinkedList<GameObject> object){
		if(x+velX<=5) x = 5;
		if(y+velY<=5) y = 5;
		if(x+width+velX>=Game.WIDTH) x = Game.WIDTH - width - 5;
		if(y+height+velY>=Game.HEIGHT) y = Game.HEIGHT - height - 5;

		x += velX;
		y += velY;
	}

	public void render(Graphics g){
		ImageIcon ship = new ImageIcon("./src/img/spaceship.png");
		Image newIMG = Util.resizeImage(ship,width,height);
		ship = new ImageIcon(newIMG);
		g.drawImage(ship.getImage(),(int)x,(int)y,null);
	}

	public Rectangle getBounds(){
		return new Rectangle((int)x,(int)y,width,height);
	}


}