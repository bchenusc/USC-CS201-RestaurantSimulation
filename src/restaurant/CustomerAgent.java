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
	private WaiterAgent waiter;
	private String choice;
	private Menu menu;

	public enum CustomerState
	{DoingNothing, WaitingInRestaurant, BeingSeated, Seated, Ordering, Eating, DoneEating, Leaving};
	private CustomerState state = CustomerState.DoingNothing;//The start state

	public enum CustomerEvent 
	{none, gotHungry, followWaiter, seated, readyToOrder, ordered, doneEating, doneLeaving};
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
			      event = CustomerEvent.doneEating;
			      stateChanged();
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
			stateChanged();
			goToRestaurant();
			return true;
		}
		if (state == CustomerState.WaitingInRestaurant && event == CustomerEvent.followWaiter ){
			state = CustomerState.BeingSeated;
			stateChanged();
			return true;
		}
		if (state == CustomerState.BeingSeated && event == CustomerEvent.seated){
			state = CustomerState.Eating;
			stateChanged();
			EatFood();
			return true;
		}
		if (state == CustomerState.Seated && event == CustomerEvent.readyToOrder){
			state = CustomerState.Ordering;
			stateChanged();
			CallWaiter();
			return true;
		}
		if (state == CustomerState.Eating && event == CustomerEvent.doneEating){
			state = CustomerState.Leaving;
			stateChanged();
			leaveTable();
			return true;
		}
		if (state == CustomerState.Leaving && event == CustomerEvent.doneLeaving){
			state = CustomerState.DoingNothing;
			stateChanged();
			//no action
			return true;
		}
		return false;
	}

// ################# ACTIONS ####################

	private void goToRestaurant() {
		//Do("Going to restaurant");
		host.msgIWantToEat(this);//send our instance, so he can respond to us
	}
	
	private void CallWaiter(){
		//DoCallWaiter();
		waiter.msgReadyToOrder(this);
	}
	
	private void ChooseFood(){
		readMenuTimer.restart();
		readMenuTimer.start();
	}

	private void SitDown() {
		Do("Being seated. Going to table");
		//customerGui.DoGoToSeat(tableToSitAt);//hack; only one table
	}
	
	private void TellWaiterMyChoice(){
		waiter.msgHeresMyChoice(choice);
		event = CustomerEvent.ordered;
		stateChanged();
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
		eatingTimer.restart();
		eatingTimer.start();
	}

	private void leaveTable() {
		//Do("Leaving.");
		waiter.msgImDone(this);
		state = CustomerState.Leaving;
		//customerGui.DoExitRestaurant(); //set done leaving here.
	}

	// Accessors, etc.

	public String getName() {
		return name;
	}

	public String toString() {
		return "customer " + getName();
	}
	
	
	//########## UTILITIES ###########
	private void RandomChoice(Menu menu){
		menu.RandomChoice();
	}
	

	public void setGui(CustomerGui g) {
		customerGui = g;
	}

	public CustomerGui getGui() {
		return customerGui;
	}
}

