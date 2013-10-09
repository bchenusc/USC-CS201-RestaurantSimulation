package restaurant;

import java.util.Hashtable;
import java.util.Map;
import java.util.Random;
import java.util.Set;

public class Menu {
	//Map of Food and Price.
	Map<String, Float> menuDictionary = new Hashtable<String, Float>(); 
	private int size = 4;
	
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
		String s[] = menuDictionary.keySet().toArray(new String[0]);
		return s[n];
	}
	
	public float getPrice(String food){
		return menuDictionary.get(food);
	}
	
	public boolean HaveEnoughMoneyForAny(float money){
		String s[] = menuDictionary.keySet().toArray(new String[0]);
		for (String st: s){
			if (menuDictionary.get(st) <= money){
				return true;
			}
		}
		return false;
	}
	
	public boolean HaveEnoughMoneyForItem(float money, String choice){
		return money >= menuDictionary.get(choice);
	}
	
	public void remove(String s){
		menuDictionary.remove(s);
		size --;
	}
	
}
