package src.game;

import java.awt.*;
import javax.swing.*;
import java.awt.image.*;
import java.awt.event.*;
import java.util.*;

public class PowerUp extends GameObject{

	public int width = 30, height = 30;
	private Handler handler;
	private String type;
	private int dmg = 0, health = 0, shield = 0, speed = 0;
	private Random random = new Random();

	public PowerUp(float xpos, float ypos, float velX, Handler handler, ObjectId id, String type){
		super(xpos,ypos,id);

		this.y = ypos;
		this.x = xpos;
		this.velX = velX;

		this.handler = handler;
		this.type = type;


		if(type.equals("HEALTH")) health = 100;
		else if(type.equals("DMG")) dmg = 10;
		else if(type.equals("ARMOR")) shield = 1;
	}

	public PowerUp(Handler handler, Game game, ObjectId id, String type){
		super(0,0,id);

		speed = random.nextInt(5) + 1;
		this.y = random.nextInt(Game.HEIGHT-20) + 21;
		this.x = Game.WIDTH - 10;

		this.handler = handler;
		this.type = type;

		this.velX = -1 * speed;

		if(type.equals("HEALTH")) health = 100;
		else if(type.equals("DMG")) dmg = 10;
		else if(type.equals("ARMOR")) shield = 1;

		game.send("POWERUP "+type+" "+x+" "+y+" "+velX);
	}

	public void tick(LinkedList<GameObject> object){
		if(x+velX<=5) handler.removeObject(this);
		x += velX;
		
		collision(object);
	}

	public void render(Graphics g){

		ImageIcon power_up = new ImageIcon("./src/img/power_up_hp.png");

		if(type.equals("HEALTH")) power_up = new ImageIcon("./src/img/power_up_hp.png");
		else if(type.equals("DMG")) power_up = new ImageIcon("./src/img/power_up_dmg.png");
		else if(type.equals("ARMOR")) power_up = new ImageIcon("./src/img/power_up_armor.png");

		g.drawImage(power_up.getImage(),(int)(x+width/2),(int)y,null);

	}

	public Rectangle getBounds(){
		return new Rectangle((int)x,(int)y,width,height);
	}

	public void collision(LinkedList<GameObject> object){
		for(int i = 0; i < handler.object.size(); i++){
			GameObject tempObject = handler.object.get(i);

			if(tempObject.getId() == ObjectId.SpaceShip){
				if(getBounds().intersects(tempObject.getBounds())){
					if(type.equals("HEALTH")) ((SpaceShip)tempObject).heal(health);
					else if(type.equals("DMG")) ((SpaceShip)tempObject).damageUP(dmg);
					else if(type.equals("ARMOR")) ((SpaceShip)tempObject).shield(shield);
					
					handler.removeObject(this);
				}
			}
		}
	}
}