package restaurant;

//import javax.swing.*;

import agent.Agent;
import restaurant.Order;
import restaurant.Order.OrderState;


import java.util.ArrayList;
//import restaurant.HostAgent.HostState;
//import java.awt.*;
//import java.awt.event.ActionEvent;
//import java.awt.event.ActionListener;
import java.util.List;
//import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;

public class CookAgent extends Agent {
	
	String name;
	
	//A list of ALL orders that the cook is attending to.
	List<Order> orders;

	//A map containing all the foods and their cook times. Implement in Constructor pls!
	Map<String, Integer> foodDictionary = new TreeMap<String, Integer>(); 

	//Constructor
	public CookAgent(String name){
	  this.name = name;
	  orders = new ArrayList<Order>();
	  
	  //Tree map
	  foodDictionary.put("Steak", 5000);
	  foodDictionary.put("Chicken", 6000);
	  foodDictionary.put("Salad", 4000);
	  foodDictionary.put("Pizza", 8000);
	  
	}
		
//########## Messages  ###############
	public void msgHeresAnOrder(Order o)
	{
		 orders.add(o);
		 stateChanged();
	}
	
	
//##########  Scheduler  ##############
@Override
	protected boolean pickAndExecuteAnAction() {
		// if State == idle and there exists an Order o in pendingOrder such that o.OrderState == pending
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
		  Do("is cooking " + o.choice + ".");
		  o.setTimer(foodDictionary.get(o.choice));
	}
	
	private void tellWaiterOrderIsReady(Order o){
		o.waiter.msgOrderIsReady(o);
		o.setState(OrderState.notified);
	}
	
//################    Utility     ##################
	public String toString(){
		return "Cook " + name;
	}

//######################## End of Class #############################
}



