package restaurant;

import agent.Agent;
import restaurant.WaiterAgent.WaiterState;
import restaurant.gui.WaiterGui;

import java.util.*;

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
	public List<WaiterAgent> waiters = new ArrayList<WaiterAgent>();
	
	public Collection<Table> tables;
	//note that tables is typed with Collection semantics.
	//Later we will see how it is implemented

	private String name;

	public WaiterGui hostGui = null;

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

//########### Messages #####################

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
		 * and  there exists a Waiter w in waiters such that w.State == idle
		 * 			then notifyWaiter(t, w);*/
		if (!waitingCustomers.isEmpty()){
			for (Table t : tables){
				if (t.occupiedBy == null){
					for (WaiterAgent w: waiters){
						if (w.state == WaiterState.idle){
							notifyWaiter(t, w);
							return true;
						}
					}
					return true;
				}
			}
			return true;
		}


		return false;
		//we have tried all our rules and found
		//nothing to do. So return false to main loop of abstract agent
		//and wait.
	}

// ######################   Actions  ##################
	private void notifyWaiter(Table t, WaiterAgent w){
		  // DoNotifyWaiter();
		   w.msgSeatAtTable(waitingCustomers.remove(0), t);
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
	
	// The animation DoXYZ() routines
	/*private void DoSeatCustomer(CustomerAgent customer, Table table) {
		//Notice how we print "customer" directly. It's toString method will do it.
		//Same with "table"
		print("Seating " + customer + " at " + table);
		hostGui.DoBringToTable(customer , table.getTableNumber());
	}
	*/

	//utilities

	public void setGui(WaiterGui gui) {
		hostGui = gui;
	}

	public WaiterGui getGui() {
		return hostGui;
	}

}


