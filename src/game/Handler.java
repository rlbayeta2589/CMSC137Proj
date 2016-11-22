package src.game;

import java.awt.*;
import javax.swing.*;
import java.awt.image.*;
import java.awt.event.*;
import java.util.*;

public class Handler{
	public LinkedList<GameObject> object = new LinkedList<GameObject>();

	private GameObject tempObject;

	public void tick(){
		for (int i=0; i<object.size(); i++) {
			tempObject = object.get(i);
			tempObject.tick(object);
		}
	}

	public void render(Graphics g){
		for (int i=0; i<object.size(); i++) {
			tempObject = object.get(i);
			tempObject.render(g);
		}
	}

	public void collision(){
		for (int i=0; i<object.size(); i++){
			tempObject = object.get(i);
			if(tempObject.getId() == ObjectId.Bullet){
				Bullet temp = (Bullet) tempObject; 
				temp.collision(object);
			}
			if(tempObject.getId() == ObjectId.BossBullet){
				BossBullet temp = (BossBullet) tempObject;
				temp.collision(object);
			}
		}
	}

	public void addObject(GameObject object){
		this.object.add(object);
	}

	public void removeObject(GameObject object){
		this.object.remove(object);
	}
}