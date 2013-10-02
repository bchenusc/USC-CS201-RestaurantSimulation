package restaurant;

//import javax.swing.*;

import agent.Agent;
import restaurant.Order;
import restaurant.Order.OrderState;


import restaurant.WaiterAgent.MyCustomerState;

import java.util.ArrayList;
import java.util.HashMap;
//import restaurant.HostAgent.HostState;
//import java.awt.*;
//import java.awt.event.ActionEvent;
//import java.awt.event.ActionListener;
import java.util.List;
//import java.util.ArrayList;
import java.util.Map;

public class CookAgent extends Agent {
	
	String name;
	
	//A list of ALL orders that the cook is attending to.
	List<Order> orders;

	//A map containing all the foods and their cook times. Implement in Constructor pls!
	Map<String, Food> foodDictionary = new HashMap<String, Food>(); 

	//Constructor
	public CookAgent(String name){
	  this.name = name;
	  orders = new ArrayList<Order>();
	  
	  //Tree map
	  foodDictionary.put("Steak", new Food("Steak", 5000, 1));
	  foodDictionary.put("Chicken", new Food("Chicken", 4500, 1));
	  foodDictionary.put("Salad", new Food("Salad", 6000, 1));
	  foodDictionary.put("Pizza", new Food("Pizza", 7000, 1));
	  
	}
		
//########## Messages  ###############
	public void msgHeresAnOrder(String o, WaiterAgent w, int tableNumber)
	{
		Order order = new Order(o, w, tableNumber);
		 orders.add(order);
		 stateChanged();
	}
	
	
//##########  Scheduler  ##############
@Override
	protected boolean pickAndExecuteAnAction() {
		// if there exists an Order o in pendingOrder such that o.OrderState == pending
		//then CookOrder(o);
		if (orders.size() > 0){
				//Look for all pending orders.
				for(Order o : orders){
					
					if (o.getState() == OrderState.pending){
						CookOrder(o);
						return true;
					}
					
				}
				
				for (int i=0; i<orders.size();i++){
					
					if (orders.get(i).getState() == OrderState.cooked){
						tellWaiterOrderIsReady(orders.get(i));
						orders.remove(i);
						i--;
						return true;
					}
				}
			return true;
		}
		return false;
	}
		
//########## Actions ###############
	private void CookOrder(Order o){
		Food temp = foodDictionary.get(o.choice);
		if (temp.amount == 0){
			orders.remove(o);
			o.waiter.msgOutOfFood(o.choice, o.tableNumber);
			return;
		}
		if (temp.amount == 1){
			//order more from the restaurant;
		}
		
		temp.amount --;
		
		  Do("is cooking " + o.choice + ".");
		  o.setTimer(foodDictionary.get(o.choice).cookTime);
	}
	
	private void tellWaiterOrderIsReady(Order o){
		o.waiter.msgOrderIsReady(o.choice, o.tableNumber);
		o.setState(OrderState.notified);
	}
	
//################    Utility     ##################
	public String toString(){
		return "Cook " + name;
	}

//######################## End of Class Cook#############################
	
	//#### Inner Class ####	
	private class Food {
		   private String choice;
		   private int cookTime;
		   private int amount;
		   
		   private Food(String c, int ct, int amt){
			   choice = c;
			   cookTime = ct;
			   amount = amt;
		   }
		   
		   
	}
}



