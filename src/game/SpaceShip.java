package src.game;

import java.awt.*;
import javax.swing.*;
import java.awt.image.*;
import java.awt.event.*;
import java.util.*;

public class SpaceShip extends GameObject{

	private Game game;
	private String name;
	public int width = 70, height =40, damage=10, health=250, shield=0;
	public float prevX, prevY;
	public boolean dead = false, cooldown = false, shielded = false;
	private int bullet = 30, checkpoint = 3000;
	private int points = 0, reward = 1;

	public SpaceShip(float x, float y, ObjectId id, Game game, String name){
		super(x,y,id);
		this.game = game;
		this.name = name;

		damage=10;
		health=250;
		shield=0;

		dead = false;
		cooldown = false;
		shielded = false;

		bullet = 30;
		checkpoint = 3000;

		points = 0;
		reward = 1;		

		prevX = x;
		prevY = y;
	}

	public void tick(LinkedList<GameObject> object){

		prevX = x;
		prevY = y;

		x += velX;
		y += velY;

		if(x<=5) x = 5;
		if(y<=5) y = 5;
		if(x+width>= Game.WIDTH ) x = Game.WIDTH - width;
		if(y+height>=Game.HEIGHT-35) y = Game.HEIGHT - height - 35;

		if(game!=null && (prevX!=x || prevY!=y)){
			game.send("PLAYER "+name+" "+x+" "+y+" "+damage);
		}

		collision(object);
	}

	public void render(Graphics g){
		ImageIcon ship = new ImageIcon("./src/img/spaceship.png");

		if(shielded){
			ImageIcon shield = new ImageIcon("./src/img/spaceship_barrier.png");
			Image newIMG = Util.resizeImage(shield,width+5,height+5);
			shield = new ImageIcon(newIMG);
			g.drawImage(shield.getImage(),(int)x-2,(int)y-2,null);
		}

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

	public void collision(LinkedList<GameObject> object){
		Handler handler = game.getHandler();
		for(int i = 0; i < handler.object.size(); i++){
			GameObject tempObject = handler.object.get(i);

			if(tempObject.getId() == ObjectId.Boss){
				if(getBounds().intersects(tempObject.getBounds())){
					damageSpaceShip(1000);
				}
			}else if(tempObject.getId() == ObjectId.PowerUp && this.id == null){
				if(getBounds().intersects(tempObject.getBounds())){
					handler.removeObject(tempObject);
				}
			}
		}
	}

	public int getDamage(){
		return this.damage;
	}

	public void setShielded(boolean shielded){
		this.shielded = shielded;
	}

	public void explode(){
		dead = true;
	}

	public boolean isDead(){
		return this.dead;
	}

	public void damageSpaceShip(int indmg){
		if(dead) return;

		SpaceShip temp = this;

		if(shielded){
			shield--;
			if(shield<=0){
				shield = 0;
				shielded = false;
				game.send("BARRIER "+name+" 0 0 0");
			}
			StatField.setArmor(shield);
			return;
		}

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

	public boolean isBulletCoolDown(){
		return cooldown;
	}

	public void decrementBullet(){
		if(dead) return;
		bullet-=1;

		if(bullet <= 0){
			cooldown = true;

			new java.util.Timer().schedule(new TimerTask(){
				public void run(){
					cooldown = false;
					replenishBullet();
				}
			},2500);
		}

		StatField.setBullet(bullet);
	}

	public void replenishBullet(){
		this.bullet = 30;
		StatField.setBullet(bullet);
	}

	public void damageUP(int dmg){
		if(dead) return;
		this.damage+=dmg;
		StatField.setDamage(damage);
	}

	public void heal(int hp){
		if(dead) return;
		this.health+=hp;
		StatField.setHealth(health);
	}

	public void shield(int amount){
		if(dead) return;
		this.shield+=amount;
		shielded = true;
		StatField.setArmor(shield);

		game.send("BARRIER "+name+" 0 0 1");
	}

	public void addScore(int score){
		int dmg, hp;
		points+=score; 

		if(points>checkpoint){

			if(checkpoint<10000){
				checkpoint+=5000;
				dmg=2;
				hp=15*reward;
			}else if(checkpoint<30000){
				checkpoint+=8500;
				dmg=5;
				hp=30*reward;
			}else{
				checkpoint+=12000;
				dmg=7;
				hp=50*reward;
			}

			heal(hp);
			damageUP(dmg);


			if(reward%2==0){
				shield(reward);
			}

			reward++;
		}


		StatField.setScore(points);
	}

	public int getScore(){
		return this.points;
	}
}