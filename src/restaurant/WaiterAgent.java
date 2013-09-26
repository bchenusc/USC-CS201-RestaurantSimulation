package restaurant;

import agent.Agent;
import restaurant.gui.WaiterGui;

import java.util.*;
import java.util.concurrent.Semaphore;

public class WaiterAgent extends Agent {
	List<MyCustomer> myCustomers = new ArrayList<MyCustomer>();
	CookAgent cook;
	HostAgent host;
		public HostAgent getHost(){
			return host;
		}
	String name;
		public String getName(){
			return name;
		}

	// This is to distribute the waiting customers evenly among waiters.
	public int numberOfCustomers;
	enum MyCustomerState {waiting, seated, readyToOrder, ordering, ordered, orderCooking, orderReady, eating, doneEating;}
	
	//Animation stuff - To implement in 2c
	private Semaphore atTargetLocation = new Semaphore(0,true);
	
	public WaiterAgent(String name, HostAgent h, CookAgent c) {
		this.name = name;
		host = h;
		cook = c;
	}

// ######## Messages ################
	public void msgSeatAtTable(CustomerAgent c, Table t) {
		c.waiter = this;
		MyCustomer mc = new MyCustomer(c,t);
		mc.state = MyCustomerState.waiting;
		myCustomers.add(mc);
		numberOfCustomers++;
		stateChanged();
	};	
	
	public void msgReadyToOrder(CustomerAgent c){  		
		for (MyCustomer mc : myCustomers){
			if (mc.customer == c){
				mc.state = MyCustomerState.readyToOrder;
				stateChanged();
			}
		}
	}
	public void msgHeresMyChoice(CustomerAgent ca, String c){ 
		for (MyCustomer mc : myCustomers){
			if (mc.customer == ca){
				mc.order = new Order(c, this, mc.table.tableNumber);
				mc.state = MyCustomerState.ordering;
				stateChanged();
			    
			    return;
			}
		}
	}
	public void msgOrderIsReady(Order o){ 		
		for (MyCustomer mc : myCustomers){
			if (mc.order == o){
				mc.state = MyCustomerState.orderReady;
				stateChanged();
			}
		}
	}
	public void msgImDone(CustomerAgent c){ 
		for (MyCustomer mc: myCustomers){
			if (mc.customer == c){
				mc.state = MyCustomerState.doneEating;
				stateChanged();
			}
		}
	}

	
	
//##########  Scheduler  ##############
	protected boolean pickAndExecuteAnAction(){
		if (!myCustomers.isEmpty()){
			try{
			for (MyCustomer mc : myCustomers){
				if (mc.state == MyCustomerState.waiting){
					SeatCustomer(mc.table, mc);
					return true;
				}
			}
			
			for (MyCustomer mc: myCustomers){
				if (mc.state == MyCustomerState.readyToOrder){
					TakeOrder(mc);
					return true;
				}
			}
			
			for (MyCustomer mc: myCustomers){
				if (mc.state == MyCustomerState.ordering){
					GiveOrderToCook(mc);
					return true;
				}
			}
			
			for (MyCustomer mc: myCustomers){
				if (mc.state == MyCustomerState.orderReady){
					GiveFoodToCustomer(mc);
					return true;
				}
			}
			
			for (MyCustomer mc: myCustomers){
				if (mc.state == MyCustomerState.doneEating){
					CustomerLeaving(mc);
					return true;
				}
			}

			return true;
			}
			catch(Exception e){}
		}
		
		return false;
	}
	
//############ Action ################
	public void SeatCustomer(Table t, MyCustomer mc) {
		Do("is seating " + mc.customer.getName());
		DoSeatCustomer(t.getTableNumber(), mc);
		mc.state = MyCustomerState.seated;
		mc.customer.msgFollowMe(new Menu());
	}
	
	public void TakeOrder(MyCustomer mc){
		Do("is taking " + mc.customer.getName()+ "'s order.");
		mc.customer.WhatWouldYouLike();
	}
	 
	public void GiveOrderToCook(MyCustomer mc){
		Do("gives an order to the cook.");
		mc.state = MyCustomerState.orderCooking;
		cook.msgHeresAnOrder(mc.order);
	}

	public void GiveFoodToCustomer(MyCustomer mc){
		Do("is giving food to " + mc.customer.getName());	
		mc.state = MyCustomerState.eating;
		mc.customer.HeresYourOrder(mc.order.choice);
	}

	public void CustomerLeaving(MyCustomer c){
		Do(c.customer.getName() + "is leaving the restaurant.");
		host.msgTableIsClear(c.table);
		myCustomers.remove(c);
	}

	//##GUI ACTIONS###
	private void DoSeatCustomer(int tableNum, MyCustomer mc){
		mc.customer.getGui().DoGoToSeat(tableNum);
	}

//#####    GUI STUFF DEAL WITH LATER   ####
	/*public void msgAtTable() {//from animation
			//print("msgAtTable() called");
			atTargetLocation.release();// = true;
			stateChanged();
	}
	*/
	
//#### Inner Class ####	
	private class MyCustomer {
		CustomerAgent customer;
		   Table table;
		   Order order;
		   MyCustomerState state = MyCustomerState.waiting;
		   
		   public MyCustomer(CustomerAgent c, Table t) {
				customer = c;
				table = t;
			}
	}

}
