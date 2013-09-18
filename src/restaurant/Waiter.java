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
	
	
	
//##########  Scheduler  ##############
	protected boolean pickAndExecuteAnAction(){
		
		return false;
	}

	
	
	
//#### Inner Class ####	
	private class MyCustomer {
		   CustomerAgent customer;
		   Table table;
		   Order order;
		   MyCustomerState state = MyCustomerState.waiting;
	};	
}
