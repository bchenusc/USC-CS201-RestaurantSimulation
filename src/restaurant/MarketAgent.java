package restaurant;

//import javax.swing.*;

import agent.Agent;

import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.HashMap;
import java.util.List;


public class MarketAgent extends Agent {
	
	String name;
	
	//Hashmap of all food types and quantity available.
	HashMap<String, Integer> inventory = new HashMap<String, Integer>();
	List<MarketOrder> marketOrders = new ArrayList<MarketOrder>();
	
	public enum OrderState { pending, cooking, cooked, notified;}

	//Constructor
	public MarketAgent(String name){
	  this.name = name;
	  
	  //Tree map
	  inventory.put("Steak", 2);
	  inventory.put("Chicken", 2);
	  inventory.put("Salad", 2);
	  inventory.put("Pizza", 2);
	  
	}
		
//########## Messages  ###############
	public void msgINeedFood(String choice, int amount, CookAgent c){
		MarketOrder mo = new MarketOrder(choice, amount, c);
		marketOrders.add(mo);
		stateChanged();
	}
	
	
//##########  Scheduler  ##############
@Override
	protected boolean pickAndExecuteAnAction() {
	try{
		if (marketOrders.size() > 0){
			fillOrder();
		}
		
	}
	catch(ConcurrentModificationException e){
			return false;
	}
		
		return false;
	}
		
//########## Actions ###############
	public void fillOrder(){
		Do("Filling order");
		MarketOrder mo = marketOrders.remove(0);
		if(inventory.get(mo.choice)>0){
			if (mo.amount > inventory.get(mo.choice)){
				mo.cook.msgFillOrder(mo.choice, inventory.get(mo.choice), false);
				inventory.put(mo.choice, 0);
			}
			else{
				mo.cook.msgFillOrder(mo.choice, mo.amount, true);
				inventory.put(mo.choice, inventory.get(mo.choice) - mo.amount);
			}
		}
	}
	
//################    Utility     ##################
	public String toString(){
		return "Cook " + name;
	}

//######################## End of Class Cook#############################
	
	//#### Inner Class ####	
	private class MarketOrder {
		  CookAgent cook;
		  String choice;
		  int amount;
		  
		  public MarketOrder(String ch, int amt, CookAgent c){
			  cook = c;
			  choice = ch;
			  amount = amt;
		  }
	}
}



