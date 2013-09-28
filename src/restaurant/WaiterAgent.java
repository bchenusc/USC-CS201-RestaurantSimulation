package restaurant;

import agent.Agent;
import restaurant.gui.WaiterGui;

import java.util.*;
import java.util.concurrent.Semaphore;

public class WaiterAgent extends Agent {
	WaiterGui gui;
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
	boolean idle;
	// This is to distribute the waiting customers evenly among waiters.
	public int numberOfCustomers;
	enum MyCustomerState {waiting, seated, readyToOrder, ordering, ordered, orderCooking, orderReady, eating, doneEating;}
	
	//Animation stuff - To implement in 2c
	private Semaphore atTargetLocation = new Semaphore(0, true);
	
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
		t.occupiedBy = c;
		idle = true;
		myCustomers.add(mc);
		numberOfCustomers++;
		stateChanged();
	};	
	
	public void msgReadyToOrder(CustomerAgent c){  		
		for (MyCustomer mc : myCustomers){
			if (mc.customer == c){
				Do("Received customer call");
				mc.state = MyCustomerState.readyToOrder;
				stateChanged();
			}
		}
	}
	public void msgHeresMyChoice(CustomerAgent ca, String c){ 
		for (MyCustomer mc : myCustomers){
			if (mc.customer == ca){
				mc.order = new Order(c, this, mc.table.tableNumber);
				mc.state = MyCustomerState.ordered;
				stateChanged();
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
						idle = false;
						SeatCustomer(mc.table, mc);
						return true;
					}
				}
			
			for (MyCustomer mc: myCustomers){
				if (mc.state == MyCustomerState.readyToOrder){
					idle = false;
					TakeOrder(mc);
					return true;
				}
			}
			
			for (MyCustomer mc: myCustomers){
				if (mc.state == MyCustomerState.ordered){
					idle = false;
					GiveOrderToCook(mc, true);
					return true;
				}
			}
			
			for (MyCustomer mc: myCustomers){
				if (mc.state == MyCustomerState.orderReady){
					idle = false;
					GiveFoodToCustomer(mc);
					return true;
				}
			}
			
			for (MyCustomer mc: myCustomers){
				if (mc.state == MyCustomerState.doneEating){
					idle = false;
					CustomerLeaving(mc);
					return true;
				}
			}
			
			DoIdle();
			}
			catch(ConcurrentModificationException e){
				
			}
			return true;
			}
		
		
		return false;
		
		
	}
	
//############ Action ################
	public void SeatCustomer(Table t, MyCustomer mc) {
		DoGetCustomer();
		Do("is seating " + mc.customer.getName());
		mc.customer.msgFollowMe(new Menu());
		mc.state = MyCustomerState.seated;
		DoSeatCustomer(t.getTableNumber(), mc);
	}
	
	public void TakeOrder(MyCustomer mc){
		Do("is taking " + mc.customer.getName() + "'s order.");
		DoWalkToCustomer(mc, false);
		mc.customer.WhatWouldYouLike();
		mc.state = MyCustomerState.ordering;
	}
	 
	public void GiveOrderToCook(MyCustomer mc, boolean displayText){
		DoGiveOrderToCook(mc.order);
		mc.state = MyCustomerState.orderCooking;
		cook.msgHeresAnOrder(mc.order);
	}

	public void GiveFoodToCustomer(MyCustomer mc){
		DoGiveOrderToCook(mc.order);
		DoWalkToCustomer(mc, true);
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
		gui.setText("Seating Customere");
		gui.DoBringToTable(mc.customer, tableNum);
		mc.customer.getGui().DoGoToSeat(tableNum);
		atLocAcquire();
	}
	
	public void DoGetCustomer(){
		Do("is getting customer.");
		gui.setText("Getting Customer");
		gui.DoGetCustomer();
		atLocAcquire();
	}
	
	public void DoWalkToCustomer(MyCustomer mc, boolean displayText){
		//Do("is taking " + mc.customer.getName()+ "'s order.");
		if (displayText)
			gui.DoWalkToCustomer(mc.table, mc.order.choice);
		else
			gui.DoWalkToCustomer(mc.table, "");
		atLocAcquire();
	}
	
	public void DoGiveOrderToCook(Order o){
		Do("gives an order to the cook.");
		gui.DoGiveOrderToCook(o);
		atLocAcquire();
	}
	
	private void atLocAcquire(){
		try {
			atTargetLocation.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	private void DoIdle(){
		gui.DoIdle();
		idle = false;
	}

//#####    GUI Utilities  ####
	public void atLocation() {//from animation
				atTargetLocation.release();// = true;
				idle = true;
	}
	

	
	public void setGUI(WaiterGui wg){
	    	gui = wg;
	}
	
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
