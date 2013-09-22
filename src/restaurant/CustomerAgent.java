package restaurant;

import restaurant.gui.CustomerGui;
import restaurant.gui.RestaurantGui;
import agent.Agent;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.Timer;

/**
 * Restaurant customer agent.
 */
public class CustomerAgent extends Agent {
	
	private final int eatingTime = 10000;
	private final int readingMenuTime = 5000;
	
	private String name;
	//private int hungerLevel = 5;        // determines length of meal
	Timer eatingTimer;
	Timer readMenuTimer;
	private CustomerGui customerGui;

	//Necessary links.
	private HostAgent host;
	private Waiter waiter;
	private String choice;
	private Menu menu;

	public enum CustomerState
	{DoingNothing, WaitingInRestaurant, BeingSeated, Seated, ReadingMenu, Ordering, Eating, DoneEating, Leaving};
	private CustomerState state = CustomerState.DoingNothing;//The start state

	public enum CustomerEvent 
	{none, gotHungry, followWaiter, seated, readyToOrder, doneEating, doneLeaving};
	CustomerEvent event = CustomerEvent.none;

	/**
	 * Constructor for CustomerAgent class
	 *
	 * @param name name of the customer
	 * @param gui  reference to the customergui so the customer can send it messages
	 */
	public CustomerAgent(String name){
		super();
		this.name = name;
		eatingTimer = new Timer(eatingTime, new ActionListener() {
			   public void actionPerformed(ActionEvent e){
			      
			   }
			});
		eatingTimer = new Timer(readingMenuTime, new ActionListener() {
			   public void actionPerformed(ActionEvent e){
			      choice = RandomChoice(menu);
			      event = CustomerEvent.readyToOrder;
			      stateChanged();
			   }
			});	
	}

	/**
	 * hack to establish connection to Host agent.
	 */
	public void setHost(HostAgent host) {
		this.host = host;
	}

	public String getCustomerName() {
		return name;
	}
// ##################### Messages #################
	
	public void IsHungry(){ 
		//DoIsHungry();
		event = CustomerEvent.gotHungry; 
		stateChanged();
    }
	public void FollowMe(Menu m){
		menu = m;
		event = CustomerEvent.followWaiter;
		stateChanged();
	}
//Get a message from customer GUI when we reach the table to handle animation. Once we reach the table set Customer State to seated.
	public void WhatWouldYouLike(){
		TellWaiterMyChoice();
	}
	
	public void HeresYourOrder(String order){
		if (order!= choice){
			System.out.println("Wrong Order!!!");
		}
		state = CustomerState.Eating;
		stateChanged();
		EatFood();
	}

	public void msgAnimationFinishedGoToSeat() {
		//from animation
		event = CustomerEvent.seated;
		stateChanged();
	}
	public void msgAnimationFinishedLeaveRestaurant() {
		//from animation
		event = CustomerEvent.doneLeaving;
		stateChanged();
	}

	/**
	 * Scheduler.  Determine what action is called for, and do it.
	 */
	protected boolean pickAndExecuteAnAction() {
		//	CustomerAgent is a finite state machine

		if (state == CustomerState.DoingNothing && event == CustomerEvent.gotHungry ){
			state = CustomerState .WaitingInRestaurant;
			goToRestaurant();
			return true;
		}
		if (state == CustomerState.WaitingInRestaurant && event == CustomerEvent.followWaiter ){
			state = CustomerState.BeingSeated;
			return true;
		}
		if (state == CustomerState.BeingSeated && event == CustomerEvent.seated){
			state = CustomerState.Eating;
			EatFood();
			return true;
		}
		if (state == CustomerState.ReadingMenu && event == CustomerEvent.readyToOrder){
			state = CustomerState.Ordering;
			CallWaiter();
		}
		if (state == CustomerState.Eating && event == CustomerEvent.doneEating){
			state = CustomerState.Leaving;
			leaveTable();
			return true;
		}
		if (state == CustomerState.Leaving && event == CustomerEvent.doneLeaving){
			state = CustomerState.DoingNothing;
			//no action
			return true;
		}
		return false;
	}

// ################# ACTIONS ####################

	private void goToRestaurant() {
		//Do("Going to restaurant");
		host.IWantToEat(this);//send our instance, so he can respond to us
	}
	
	private void CallWaiter(){
		//DoCallWaiter();
		waiter.ReadyToOrder(this);
	}
	
	private void ChooseFood(){
		readMenuTimer.star
	
	}

	private void SitDown() {
		Do("Being seated. Going to table");
		customerGui.DoGoToSeat(tableToSitAt);//hack; only one table
	}

	private void EatFood() {
		Do("Eating Food");
		//This next complicated line creates and starts a timer thread.
		//We schedule a deadline of getHungerLevel()*1000 milliseconds.
		//When that time elapses, it will call back to the run routine
		//located in the anonymous class created right there inline:
		//TimerTask is an interface that we implement right there inline.
		//Since Java does not all us to pass functions, only objects.
		//So, we use Java syntactic mechanism to create an
		//anonymous inner class that has the public method run() in it.
		timer.schedule(new TimerTask() {
			Object cookie = 1;
			public void run() {
				print("Done eating, cookie=" + cookie);
				event = AgentEvent.doneEating;
				//isHungry = false;
				stateChanged();
			}
		},
		timerLength);//getHungerLevel() * 1000);//how long to wait before running task
	}

	private void leaveTable() {
		Do("Leaving.");
		waiter.msgLeavingTable(this);
		customerGui.DoExitRestaurant();
	}

	// Accessors, etc.

	public String getName() {
		return name;
	}
	
	public int getHungerLevel() {
		return hungerLevel;
	}

	public void setHungerLevel(int hungerLevel) {
		this.hungerLevel = hungerLevel;
		//could be a state change. Maybe you don't
		//need to eat until hunger lever is > 5?
	}

	public String toString() {
		return "customer " + getName();
	}

	public void setGui(CustomerGui g) {
		customerGui = g;
	}

	public CustomerGui getGui() {
		return customerGui;
	}
}

