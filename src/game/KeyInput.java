package src.game;

import java.awt.*;
import java.awt.event.*;

public class KeyInput extends KeyAdapter{
	
	Handler handler;

	public KeyInput(Handler handler){
		this.handler = handler;
	}

	public void keyPressed(KeyEvent ke){
		int key = ke.getKeyCode();

		for(int i=0; i<handler.object.size(); i++){
			GameObject tempObject = handler.object.get(i);

			if(tempObject.getId() == ObjectId.SpaceShip){
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
							handler.addObject(new Bullet(tempObject.getX()+80,tempObject.getY()+20, ObjectId.Bullet, 5));
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