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
	}
		
//########## Messages  ###############
	public void msgHeresAnOrder(Order o)
	{
		 orders.add(o);
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
				}
				
			}
			
			for (Order o: orders){
				
				if (o.getState() == OrderState.cooked){
					o.waiter.msgOrderIsReady(o);
				}
				
			}
			
			
			return true;
		}
			
		return false;
	}
		
//########## Actions ###############
	public void CookOrder(Order o){
		  DoCookOrder(o); //GUI
		  o.setTimer(foodDictionary.get(o.choice));
	}
	
//################    GUI     ##################
	public void DoCookOrder(Order o){
		System.out.println("Cook " + name + " is cooking " + o.choice + ".");
	}

//######################## End of Class #############################
}



