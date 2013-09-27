package restaurant;

import java.util.Hashtable;
import java.util.Map;
import java.util.Random;

public class Menu {
	//Map of Food and Price.
	Map<String, Float> menuDictionary = new Hashtable<String, Float>(); 
	private final int size = 4;
	
	public Menu(){
		menuDictionary.put("Steak", 15.99f);
		menuDictionary.put("Chicken", 10.99f);
		menuDictionary.put("Salad", 5.99f);
		menuDictionary.put("Pizza", 8.99f);
	}
	
	public int getSize(){
		return size;
	}
	
	public String choice(int n){
		switch (n){
			case 0: return "Steak";
			case 1: return "Chicken";
			case 2: return "Salad";
			case 3: return "Pizza";
			default: return null;
		}
		
	}
	
}
