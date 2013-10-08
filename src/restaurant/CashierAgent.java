package restaurant;

//import javax.swing.*;

import agent.Agent;


import restaurant.WaiterAgent.MyCustomerState;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.HashMap;
//import restaurant.HostAgent.HostState;
//import java.awt.*;
//import java.awt.event.ActionEvent;
//import java.awt.event.ActionListener;
import java.util.List;
//import java.util.ArrayList;
import java.util.Map;

import javax.swing.Timer;

public class CashierAgent extends Agent {
	
	String name;
	
	private List<Check> checks;
	private Menu menu;
	private enum CheckStatus {pending, calculated, paid};

	//Constructor
	public CashierAgent(String name){
		checks = new ArrayList<Check>();
		menu = new Menu();
		this.name = name;
	}
		
//########## Messages  ###############
	public void msgHereIsCheck(String choice, CustomerAgent c, WaiterAgent wa){
		Check ch = new Check(choice, c, wa);
		checks.add(ch);
		stateChanged();
	}
	
	public void msgHeresIsMyMoney(CustomerAgent c, int amount){
		for (Check ch: checks){
			if (ch.customer == c){
				ch.state = CheckStatus.paid;
				stateChanged();
			}
		}
	}
	
//##########  Scheduler  ##############
@Override
	protected boolean pickAndExecuteAnAction() {
		// if there exists an Order o in pendingOrder such that o.OrderState == pending
		//then CookOrder(o);
	try{
		for(Check ch: checks){
			if (ch.state == CheckStatus.pending){
				CalculateCheck(ch);
				return true;
			}
		}
		
		for (Check ch: checks){
			if (ch.state == CheckStatus.paid){
				CheckisPaid(ch);
				return true;
			}
		
		}
	}
	catch(ConcurrentModificationException e){
			return false;
	}
		
		return false;
	}
		
//########## Actions ###############
	
	
//################    Utility     ##################
	public String toString(){
		return "Cashier " + name;
	}

//######################## End of Class Cook#############################
	
	//#### Inner Class ####	
	private class Check {
		  String choice;
		  CustomerAgent customer;
		  WaiterAgent waiter;
		  CheckStatus state = CheckStatus.pending;
		  
		  public Check(String choice, CustomerAgent c, WaiterAgent w){
			  this.choice = choice;
			  customer = c;
			  waiter = w;
		  }
	}

}



