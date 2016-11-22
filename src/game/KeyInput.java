package src.game;

import java.awt.*;
import java.awt.event.*;

public class KeyInput extends KeyAdapter{
	
	Handler handler;
	Game game;

	public KeyInput(Handler handler, Game game){
		this.handler = handler;
		this.game = game;
	}

	public void keyPressed(KeyEvent ke){
		int key = ke.getKeyCode();

		for(int i=0; i<handler.object.size(); i++){
			GameObject tempObject = handler.object.get(i);

			if(tempObject.getId() == ObjectId.SpaceShip){
				SpaceShip tempShip = (SpaceShip) tempObject;
				switch (key) {
					case KeyEvent.VK_S:;
					case KeyEvent.VK_DOWN:
							tempObject.setVelY(3);
							break;
					case KeyEvent.VK_W:;
					case KeyEvent.VK_UP:
							tempObject.setVelY(-3);
							break;
					case KeyEvent.VK_A:;
					case KeyEvent.VK_LEFT:
							tempObject.setVelX(-3);
							break;
					case KeyEvent.VK_D:;
					case KeyEvent.VK_RIGHT:
							tempObject.setVelX(3);
							break;
					case KeyEvent.VK_SPACE:
						try{
							Float tempX = tempObject.getX()+80;
							Float tempY = tempObject.getY()+20;
							//handler.addObject(new Bullet(tempX,tempY,this.handler,ObjectId.Bullet, 5,tempShip.getDamage()));
							game.send("PLAYER BULLET "+tempX+" "+tempY+" "+tempShip.getDamage());
						}catch(Exception e){}
							break;
				}
			}
		}
	}

	public void keyReleased(KeyEvent ke){
		int key = ke.getKeyCode();

		for(GameObject tempObject : handler.object){
			if(tempObject.getId() == ObjectId.SpaceShip){
				switch (ke.getKeyCode()){
					case KeyEvent.VK_S:;
					case KeyEvent.VK_DOWN:
							tempObject.setVelY(0);
							break;
					case KeyEvent.VK_W:;
					case KeyEvent.VK_UP:
							tempObject.setVelY(0);
							break;
					case KeyEvent.VK_A:;
					case KeyEvent.VK_LEFT:
							tempObject.setVelX(0);
							break;
					case KeyEvent.VK_D:;
					case KeyEvent.VK_RIGHT:
							tempObject.setVelX(0);
							break;
				}
			}
		}

	}
}