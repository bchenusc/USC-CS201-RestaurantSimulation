package restaurant;

import java.util.Hashtable;
import java.util.Map;
import java.util.Random;

public class Menu {
	//Map of Food and Price.
	Map<String, Float> menuDictionary = new Hashtable<String, Float>(); 
	private final int size = 4;
	
	public Menu(){
		menuDictionary.put("Filet", 24.00f);
		menuDictionary.put("Hamburger", 5.00f);
		menuDictionary.put("Salmon", 15.00f);
		menuDictionary.put("Jellyfish", 16.00f);
	}
	
	public int getSize(){
		return size;
	}
	
	public String choice(int n){
		switch (n){
			case 0: return "Filet";
			case 1: return "Hamburger";
			case 2: return "Salmon";
			case 3: return "JellyFish";
			default: return null;
		}
		
	}
	
}
