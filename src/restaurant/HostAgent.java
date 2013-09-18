package restaurant;

import agent.Agent;
import restaurant.CustomerAgent.AgentEvent;
import restaurant.CustomerAgent.AgentState;
import restaurant.gui.HostGui;

import java.util.*;
import java.util.concurrent.Semaphore;

/**
 * Restaurant Host Agent
 */
//We only have 2 types of agents in this prototype. A customer and an agent that
//does all the rest. Rather than calling the other agent a waiter, we called him
//the HostAgent. A Host is the manager of a restaurant who sees that all
//is proceeded as he wishes.
public class HostAgent extends Agent {
	static final int NTABLES = 4;//a global for the number of tables.
	//Notice that we implement waitingCustomers using ArrayList, but type it
	//with List semantics.
	public List<CustomerAgent> waitingCustomers = new ArrayList<CustomerAgent>();
	
	//List of waiters
	public List<Waiter> waiters = new ArrayList<Waiter>();
	
	public Collection<Table> tables;
	//note that tables is typed with Collection semantics.
	//Later we will see how it is implemented

	private String name;

	public HostGui hostGui = null;

	public HostAgent(String name) {
		super();

		this.name = name;
		// make some tables
		tables = new ArrayList<Table>(NTABLES);
		for (int ix = 0; ix < NTABLES; ix++) {
			//tables.add(new Table(ix, tableLocX+ix*tableWidth, tableLocY));// animation for later
		}
	}

	public String getName() {
		return name;
	}

	public List getWaitingCustomers() {
		return waitingCustomers;
	}

//########### Messages #####################

	/*public void msgIWantFood(CustomerAgent cust) {
		waitingCustomers.add(cust);
		stateChanged();
	}
	*/
	public void msgIWantToEat(CustomerAgent c){
		waitingCustomers.add(c);
		stateChanged();
	}
	
	public void msgTableIsClear(Table t){
		t.occupiedBy = null;
		stateChanged();
	}

	/*public void msgLeavingTable(CustomerAgent cust) {
		for (Table table : tables) {
			if (table.getOccupant() == cust) {
				print(cust + " leaving " + table);
				table.setUnoccupied();
				stateChanged();
			}
		}
	}
	*/
	



//########### Scheduler ##############
	/**
	 * Scheduler.  Determine what action is called for, and do it.
	 */
	protected boolean pickAndExecuteAnAction() {
		/*if !waitingCustomer.empty() there exists a Table t in tables such that t.occupiedBy == null 
		 * and  there exists a Waiter w in waiters such that w.State == idle
		 * 			then notifyWaiter(t, w);*/


		return false;
		//we have tried all our rules and found
		//nothing to do. So return false to main loop of abstract agent
		//and wait.
	}

// ######################   Actions  ##################
	private void notifyWaiter(Table t, Waiter w){
		  // DoNotifyWaiter();
		   w.SeatAtTable(waitingCustomers.remove(0), t);
		}


	/*private void seatCustomer(CustomerAgent customer, Table table) {
		//State change
		state = HostState.Seating;
		stateChanged();
		
		customer.msgSitAtTable(table.getTableNumber());
		DoSeatCustomer(customer, table);
		try {
			atTable.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		table.setOccupant(customer);
		waitingCustomers.remove(customer);
		hostGui.DoLeaveCustomer();
		
	}*/
	
	/*public void setDoNothing(){
		state = HostState.DoingNothing;
		stateChanged();
	
	}*/
	
	/*public HostState getState(){
		
		return state;
	}
	*/

	// The animation DoXYZ() routines
	/*private void DoSeatCustomer(CustomerAgent customer, Table table) {
		//Notice how we print "customer" directly. It's toString method will do it.
		//Same with "table"
		print("Seating " + customer + " at " + table);
		hostGui.DoBringToTable(customer , table.getTableNumber());
	}
	*/

	//utilities

	public void setGui(HostGui gui) {
		hostGui = gui;
	}

	public HostGui getGui() {
		return hostGui;
	}

}


