package restaurant;

import agent.Agent;

import java.util.*;

import restaurant.WaiterAgent.MyCustomerState;

/**
 * Restaurant Host Agent
 */
//We only have 2 types of agents in this prototype. A customer and an agent that
//does all the rest. Rather than calling the other agent a waiter, we called him
//the HostAgent. A Host is the manager of a restaurant who sees that all
//is proceeded as he wishes.
public class HostAgent extends Agent {
	
	private final int WINDOWX = 450;
	private final int WINDOWY = 350;
	
	static final int NTABLES = 4;//a global for the number of tables.
	//Notice that we implement waitingCustomers using ArrayList, but type it
	//with List semantics.
	public List<CustomerAgent> waitingCustomers = new ArrayList<CustomerAgent>();
	
	//List of waiters
	public List<MyWaiter> waiters = new ArrayList<MyWaiter>();
	
	public Collection<Table> tables;
	//note that tables is typed with Collection semantics.
	//Later we will see how it is implemented

	private String name;
	
	private enum MyWaiterState {none, wantBreak, allowedBreak, onBreak};
	int workingWaiters = 0;

	public HostAgent(String name) {
		super();

		this.name = name;
		// make some tables
		tables = new ArrayList<Table>(NTABLES);
		for (int ix = 1; ix <= NTABLES; ix++) {
			tables.add(new Table(ix, WINDOWX/NTABLES * ix, WINDOWY/2)); // animation for later
		}
	}

	public String getName() {
		return name;
	}

//########### Messages #####################
	
	//Waiter wants a break.
	public void msgWaiterWantsABreak(WaiterAgent waiter){
		for(MyWaiter w: waiters){
			if (w.waiter == waiter){
				w.state = MyWaiterState.wantBreak;
				stateChanged();
				return;
			}
		}
	}
	
	public void msgWaiterOffBreak(WaiterAgent waiter){
		for(MyWaiter w: waiters){
			if (w.waiter == waiter){
				w.state = MyWaiterState.none;
				workingWaiters++;
				stateChanged();
				return;
			}
		}
	}

	public void msgIWantToEat(CustomerAgent c){
		waitingCustomers.add(c);
		stateChanged();
	}
	
	public void msgTableIsClear(Table t){
		t.occupiedBy = null;
		stateChanged();
	}

//########### Scheduler ##############
	/**
	 * Scheduler.  Determine what action is called for, and do it.
	 */
	protected boolean pickAndExecuteAnAction() {
		/*if !waitingCustomer.empty() there exists a Table t in tables such that t.occupiedBy == null 
		 * 
		 * 			then notifyWaiter(t, w);*/
		if (!waitingCustomers.isEmpty()){
			if (waiters.size() > 0){
				for (Table t : tables){
					if (t.occupiedBy == null){
						
						MyWaiter w = findWaiterWithLowestCust();
						notifyWaiter(t, w);
						return true;
					}
				}
			}
			//return true;
		}
		
		//Waiter break code/
		for(MyWaiter w: waiters){
			if (w.state == MyWaiterState.wantBreak && workingWaiters> 1){
				workingWaiters--;
				w.state = MyWaiterState.allowedBreak;
				WaiterOnBreak(w);
				return true;
			}
		}
		
		for (MyWaiter w: waiters){
			if (w.state == MyWaiterState.wantBreak){
				return true;
			}
		}
		
		return false;
		//we have tried all our rules and found
		//nothing to do. So return false to main loop of abstract agent
		//and wait.
	}

// ######################   Actions  ##################
	private void notifyWaiter(Table t, MyWaiter w){
		  Do("is notifying waiter "+ w.waiter.name);
		   w.numberOfCustomers++;
		   w.waiter.msgSeatAtTable(waitingCustomers.remove(0), t);
		}

	private void WaiterOnBreak(MyWaiter w){
		Do("Allowed "+ w.waiter.name + " to go on break.");
		w.state = MyWaiterState.onBreak;
		w.waiter.msgCanGoOnBreak();
	}

	//utilities
	public MyWaiter findWaiterWithLowestCust(){
		
		int index = 0;
		int lowest = waiters.get(0).numberOfCustomers;
		//First find the first waiter who is not on break as initial waiter to find lowest customers assigned to waiters.
		//This is to prevent if waiter #1 decides to go on break but he has the lowest customer assigned.
		for (int i=0; i<waiters.size(); i++){
			if (waiters.get(i).state != MyWaiterState.allowedBreak){
				index = i;
				lowest = waiters.get(i).numberOfCustomers;
				break;
			}
		}
		
		for (int i=0; i<waiters.size(); i++){
			//If the waiter has not been approved to take a break and the waiter has the lowest number of customers.
			if (waiters.get(i).state != MyWaiterState.allowedBreak && waiters.get(i).numberOfCustomers < lowest){
				lowest = waiters.get(i).numberOfCustomers;
				index = i;
			}
		}
		return waiters.get(index);
		
	}
	
	public void addWaiter(WaiterAgent w){
		waiters.add(new MyWaiter(w));
		workingWaiters++;
		stateChanged();
	}
	
	private class MyWaiter {
		MyWaiterState state = MyWaiterState.none;
		WaiterAgent waiter;
		int numberOfCustomers;
		
		public MyWaiter(WaiterAgent w){
			waiter = w;
			numberOfCustomers = 0;
		}
	}

}


