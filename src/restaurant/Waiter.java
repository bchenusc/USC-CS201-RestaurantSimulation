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
	enum MyCustomerState {waiting, seated, readyToOrder, ordered;}
	
	//Animation stuff - To implement in 2c
	private Semaphore atTable = new Semaphore(0,true);

// ######## Messages ################
	public void SeatAtTable(CustomerAgent remove, Table t) {
		//WaiterState = busy;
		// myCustomers.add(new MyCustomer(c, t));
	};	
	
	
//##########  Scheduler  ##############
	protected boolean pickAndExecuteAnAction(){
		
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

//#####    GUI STUFF DEAL WITH LATER   ####
	public void msgAtTable() {//from animation
			//print("msgAtTable() called");
			atTable.release();// = true;
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
