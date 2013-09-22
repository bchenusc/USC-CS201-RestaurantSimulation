package restaurant;

import agent.Agent;
import restaurant.gui.HostGui;

import java.util.*;
import java.util.concurrent.Semaphore;

public class Waiter extends Agent {
	List<MyCustomer> myCustomers;
	Cook cook;
	HostAgent host;

	// This is to distribute the waiting customers evenly among waiters.
	enum WaiterState {idle, busy;}
	private WaiterState state = WaiterState.idle;
	enum MyCustomerState {waiting, seated, readyToOrder, ordered;}
	
	//Animation stuff - To implement in 2c
	private Semaphore atTargetLocation = new Semaphore(0,true);

// ######## Messages ################
	public void SeatAtTable(CustomerAgent remove, Table t) {
		//WaiterState = busy;
		// myCustomers.add(new MyCustomer(c, t));
	};	
	
	public void ReadyToOrder(CustomerAgent c){  
		/*foreach MyCustomer mc in myCustomer{
		      if (mc == c){
		           WaiterState = busy;
		          mc.CustomerState = readyToOrder;
		      } 
		 }
		 */
	}
	public void HeresMyChoice(String c){ 
		/*foreach MyCustomer mc in myCustomer{
		      if (mc == c){
		           WaiterState = busy;
		           mc.order = new Order(c);
		           mc.CustomerState = ordered;
		      } 
		   }
		   */
	}
	public void OrderIsReady(Order o){ 
		/*foreach MyCustomer mc in myCustomer{
		      if (mc == c){
		           WaiterState = busy;
		           GiveFoodToCustomer(mc);
		      } 
		  }
		  */
	}
	public void ImDone(CustomerAgent c){ 
		/*foreach MyCustomer mc in myCustomer{
		     if (mc == c){
		           WaiterState = busy;
		           CustomerLeaving(mc);
		      } 
	    }
	    */
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
		
		return false;
	}
	
//############ Action ################
	public void SeatCustomer(Table t, MyCustomer mc) {
		/*
		    mc.customer.FollowMe(new Menu());
		    DoSeatCustomer(); //GUI
		    mc.CustomerState = seated;
		    WaiterState = idle;*/
	}
	
	public void TakeOrder(MyCustomer mc){
	   /*DoTakeOrder();
	   mc.customer.WhatWouldYouLike();  
	   WaiterState = idle;
	   */
	}
	 
	public void GiveOrderToCook(MyCustomer mc){
	   /*DoGiveOrderToCook();
	   cook.HeresAnOrder(mc.order);
	   WaiterState = idle;
	   */
	}

	public void GiveFoodToCustomer(MyCustomer mc){
	   /*DoGiveFoodToCustomer();
	   mc.customer.HeresYourOrder(mc.order.choice);
	   WaiterState = idle;
	   */
	}

	public void CustomerLeaving(MyCustomer c){
	   /*DoCustomerLeaving();
	   host.TableIsClear(c.table);
	   WaiterState = idle;
	   */
	}


//#####    GUI STUFF DEAL WITH LATER   ####
	public void msgAtTable() {//from animation
			//print("msgAtTable() called");
			atTargetLocation.release();// = true;
			stateChanged();
	}
	
//#### Inner Class ####	
	private class MyCustomer {
		   CustomerAgent customer;
		   Table table;
		   Order order;
		   MyCustomerState state = MyCustomerState.waiting;
	}

}
