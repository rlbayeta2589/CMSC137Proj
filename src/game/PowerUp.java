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
	private int dmg = 0, health = 0, shield = 0, speed = 0, value = 0;
	private Random random = new Random();

	public PowerUp(float xpos, float ypos, float velX, Handler handler, ObjectId id, String type, int val){
		super(xpos,ypos,id);

		dmg = 0;
		health = 0;
		shield = 0;
		speed = 0;
		value = 0;

		this.y = ypos;
		this.x = xpos;
		this.velX = velX;

		this.handler = handler;
		this.type = type;


		if(type.equals("HEALTH")) health = val;	//max 300
		else if(type.equals("DMG")) dmg = val; //max 15
		else if(type.equals("ARMOR")) shield = val; //max 3

		if(health<=100 || dmg<=8 || shield==1){
			width = 30;
			height = 30;
		}else if(health<=200 || dmg<=12 || shield==2){
			width = 35;
			height = 35;
		}else{
			width = 40;
			height = 40;
		}
	}

	public PowerUp(Handler handler, Game game, ObjectId id, String type){
		super(0,0,id);

		speed = random.nextInt(5) + 1;
		this.y = random.nextInt(Game.HEIGHT-120) + 61;
		this.x = Game.WIDTH - 10;

		this.handler = handler;
		this.type = type;

		this.velX = -1 * speed;

		if(type.equals("HEALTH")){
			health = ((random.nextInt(4)+1)*50) + 50;	//max 300
			value = health;
		}else if(type.equals("DMG")){
			dmg = ((random.nextInt(5)+1)*2) + 5; //max 15
			value = dmg;
		}else if(type.equals("ARMOR")){
			shield = (random.nextInt(3)+1); //max 3
			value = shield;
		}


		if(health<=100 || dmg<=8 || shield==1){
			width = 30;
			height = 30;
		}else if(health<=200 || dmg<=12 || shield==2){
			width = 35;
			height = 35;
		}else{
			width = 40;
			height = 40;
		}

		game.send("POWERUP "+type+" "+x+" "+y+" "+velX+" "+value);
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
			}else if(tempObject.getId() == null){
				if(getBounds().intersects(tempObject.getBounds())){
					handler.removeObject(this);
				}
			}
		}
	}
}