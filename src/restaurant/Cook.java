package restaurant;

import javax.swing.*;

import agent.Agent;
import restaurant.Order;

//import restaurant.HostAgent.HostState;
//import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
//import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;

public class Cook extends Agent {
	
	//A list of ALL orders that the cook is attending to.
	List<Order> pendingOrders;

	//Timer for the cook to know when he's busy or not busy.
	Timer cookTimer; 
	
	//A map containing all the foods and their cook times. Implement in Constructor pls!
	Map<String, Integer> foodDictionary = new TreeMap<String, Integer>(); 
	
	//The state of the cook
	private enum CookState {idle, cooking;}
	private CookState state = CookState.idle; //The start state

	//Constructor
	public Cook(){
	  cookTimer = 	new Timer(0, new ActionListener() {
		   public void actionPerformed(ActionEvent e){
		      /* if and  an Order o in pendingOrder  o.OrderState == cooking
			then o.OrderState = cooked;
		              o.waiter.OrderIsReady(o);
			     */
		   }
	
		});	
	}
		
	//########## Messages  ###############
	public void msgHeresAnOrder(Order o)
	{
		 pendingOrders.add(o);
	}
	
	
	//##########  Scheduler  ##############
		@Override
	protected boolean pickAndExecuteAnAction() {
		// if State == idle and there exists an Order o in pendingOrder such that o.OrderState == pending
		//then CookOrder(o);
		return false;
	}
		
		//########## Actions ###############
	public void CookOrder(Order o){
		  //DoCookOrder(); //GUI
		  state = CookState.cooking;
		  cookTimer.setDelay(foodDictionary.get(o.choice));
		  cookTimer.start();
	}

//######################## End of Class #############################
}



