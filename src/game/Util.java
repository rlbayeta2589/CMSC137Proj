package src.game;

import java.awt.*;
import javax.swing.*;
import java.awt.image.*;

class Util {
	public static Image resizeImage(ImageIcon img, int width, int height){
		return (img.getImage().getScaledInstance(width, height,  java.awt.Image.SCALE_SMOOTH));
	}
}