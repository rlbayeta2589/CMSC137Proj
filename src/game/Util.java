package src.game;

import java.awt.*;
import javax.swing.*;
import java.awt.image.*;
import java.util.*;
import java.util.Map.Entry; 

class Util {
	public static Image resizeImage(ImageIcon img, int width, int height){
		return (img.getImage().getScaledInstance(width, height,  java.awt.Image.SCALE_SMOOTH));
	}

	public static <K,V extends Comparable<? super V>> 
		java.util.List<Entry<K, V>> entriesSortedByValues(Map<K,V> map) { //http://stackoverflow.com/a/11648106

		java.util.List<Entry<K,V>> sortedEntries = new ArrayList<Entry<K,V>>(map.entrySet());

		Collections.sort(sortedEntries, 
			new Comparator<Entry<K,V>>() {
				@Override
				public int compare(Entry<K,V> e1, Entry<K,V> e2) {
					return e2.getValue().compareTo(e1.getValue());
				}
			}
		);

		return sortedEntries;
	}

	public static void printStringArray(String[] arr){
		for(int i=0;i<arr.length;i++){
			System.out.print(arr[i] + " === ");
		}
		System.out.println();
	}
}