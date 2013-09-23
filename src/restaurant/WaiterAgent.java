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
	public enum WaiterState {idle, busy;}
	private WaiterState state = WaiterState.idle;
		public WaiterState getState(){
			return state;
		}
		public void setState(WaiterState state){
			this.state = state;
		}
	enum MyCustomerState {waiting, seated, readyToOrder, ordered;}
	
	//Animation stuff - To implement in 2c
	private Semaphore atTargetLocation = new Semaphore(0,true);
	
	public WaiterAgent(String name) {
		this.name = name;
		
	}

// ######## Messages ################
	public void msgSeatAtTable(CustomerAgent c, Table t) {
		//WaiterState = busy;
		// myCustomers.add(new MyCustomer(c, t));
		
		state = WaiterState.busy;
		myCustomers.add(new MyCustomer(c,t));
		
		stateChanged();

	};	
	
	public void msgReadyToOrder(CustomerAgent c){  
		/*foreach MyCustomer mc in myCustomer{
		      if (mc == c){
		           WaiterState = busy;
		          mc.CustomerState = readyToOrder;
		      } 
		 }
		 */
		
		for (MyCustomer mc : myCustomers){
			if (mc.customer == c){
				state = WaiterState.busy;
				mc.state = MyCustomerState.readyToOrder;
				stateChanged();
			}
		}
	}
	public void msgHeresMyChoice(String c){ 
		/*foreach MyCustomer mc in myCustomer{
		      if (mc == c){
		           WaiterState = busy;
		           mc.order = new Order(c, this. mc.table.tableNumber);
		           mc.CustomerState = ordered;
		      } 
		   }
		   */
		for (MyCustomer mc: myCustomers){
			if (mc.order.choice == c){
				state = WaiterState.busy;
				stateChanged();
			}
		}
	}
	public void msgOrderIsReady(Order o){ 
		/*foreach MyCustomer mc in myCustomer{
		      if (mc == c){
		           WaiterState = busy;
		           GiveFoodToCustomer(mc);
		      } 
		  }
		  */
		for (MyCustomer mc : myCustomers){
			if (mc.order == o){
				state = WaiterState.busy;
				GiveFoodToCustomer(mc);
				stateChanged();
			}
		}
	}
	public void msgImDone(CustomerAgent c){ 
		/*foreach MyCustomer mc in myCustomer{
		     if (mc == c){
		           WaiterState = busy;
		           CustomerLeaving(mc);
		      } 
	    }
	    */
		for (MyCustomer mc: myCustomers){
			if (mc.customer == c){
				state = WaiterState.busy;
				stateChanged();
				CustomerLeaving(mc);
			}
		}
	}

	
	
//##########  Scheduler  ##############
	protected boolean pickAndExecuteAnAction(){
		
		/*
		 * if  there exists a MyCustomer mc such that mc.CustomerState == waiting
   				then SeatCustomer(mc);


			if  there exists a MyCustomer mc such that mc.CustomerState == readyToOrder
   				then TakeOrder(mc);

		 * 
		 */
		if (!myCustomers.isEmpty()){
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
			return true;
		}
		
		return false;
	}
	
//############ Action ################
	public void SeatCustomer(Table t, MyCustomer mc) {
		/*
		    mc.customer.FollowMe(new Menu());
		    DoSeatCustomer(); //GUI
		    mc.CustomerState = seated;
		    WaiterState = idle;*/
		Do(name + " is seating " + mc.customer.getName());
		mc.customer.FollowMe(new Menu());
		mc.state = MyCustomerState.seated;
		state = WaiterState.idle;
		stateChanged();
	}
	
	public void TakeOrder(MyCustomer mc){
	   /*DoTakeOrder();
	   mc.customer.WhatWouldYouLike();  
	   WaiterState = idle;
	   */
		Do(name + " is taking " + mc.customer.getName()+ "'s order.");
		mc.customer.WhatWouldYouLike();
		state = WaiterState.idle;
		stateChanged();
	}
	 
	public void GiveOrderToCook(MyCustomer mc){
	   /*DoGiveOrderToCook();
	   cook.HeresAnOrder(mc.order);
	   WaiterState = idle;
	   */
		Do(name + " gives an order to the cook.");
		cook.msgHeresAnOrder(mc.order);
		state = WaiterState.idle;
		stateChanged();
	}

	public void GiveFoodToCustomer(MyCustomer mc){
	   /*DoGiveFoodToCustomer();
	   mc.customer.HeresYourOrder(mc.order.choice);
	   WaiterState = idle;
	   */
		Do(name + " is giving food to " + mc.customer.getName());
		mc.customer.HeresYourOrder(mc.order.choice);
		state = WaiterState.idle;
		stateChanged();
	}

	public void CustomerLeaving(MyCustomer c){
	   /*DoCustomerLeaving();
	   host.TableIsClear(c.table);
	   WaiterState = idle;
	   */
		Do(c.customer.getName() + " is leaving the restaurant.");
		host.msgTableIsClear(c.table);
		state = WaiterState.idle;
		stateChanged();
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
