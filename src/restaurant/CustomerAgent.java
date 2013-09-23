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
	
	private final int eatingTime = 1000;
	private final int readingMenuTime = 1000;
	
	private String name;
	//private int hungerLevel = 5;        // determines length of meal
	Timer eatingTimer;
	Timer readMenuTimer;
	private CustomerGui customerGui;

	//Necessary links.
	public HostAgent host;
	public WaiterAgent waiter;
	private String choice;
	private Menu menu;

	public enum CustomerState
	{DoingNothing, WaitingInRestaurant, BeingSeated, Seated, Ordering, Eating, DoneEating, Leaving, ReadingMenu};
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
			      eatingTimer.stop();
			   }
			});
		readMenuTimer = new Timer(readingMenuTime, new ActionListener() {
			   public void actionPerformed(ActionEvent e){
			      choice = RandomChoice(menu);
			      event = CustomerEvent.readyToOrder;
			      
			    //**Hack for gui to make this code work
			      state = CustomerState.Seated;
			      
			      stateChanged();
			      
			      readMenuTimer.stop();
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
	
		//**Temporary hack to check if this works. Skips the gui part
		event = CustomerEvent.seated;
		state = CustomerState.BeingSeated;
		stateChanged();
	}
//Get a message from customer GUI when we reach the table to handle animation. Once we reach the table set Customer State to seated.
	public void WhatWouldYouLike(){
		TellWaiterMyChoice();
	}
	
	public void HeresYourOrder(String order){
		if (order!= choice){
			Do("Wrong Order!!!");
		}
		state = CustomerState.Eating;
		EatFood();
		stateChanged();
	}

	/*public void msgAnimationFinishedGoToSeat() {
		//from animation
		event = CustomerEvent.seated;
		stateChanged();
	}
	public void msgAnimationFinishedLeaveRestaurant() {
		//from animation
		event = CustomerEvent.doneLeaving;
		stateChanged();
	}
	*/

	/**
	 * Scheduler.  Determine what action is called for, and do it.
	 */
	protected boolean pickAndExecuteAnAction() {
		//	CustomerAgent is a finite state machine

		if (state == CustomerState.DoingNothing && event == CustomerEvent.gotHungry ){
			state = CustomerState .WaitingInRestaurant;
			goToRestaurant();
			stateChanged();
			return true;
		}
		if (state == CustomerState.WaitingInRestaurant && event == CustomerEvent.followWaiter ){
			state = CustomerState.BeingSeated;
			followWaiter();
			stateChanged();
			return true;
		}
		
		if (state == CustomerState.BeingSeated && event == CustomerEvent.seated){
			state = CustomerState.ReadingMenu;
			ChooseFood();
			stateChanged();
			return true;
		}
		if (state == CustomerState.Seated && event == CustomerEvent.readyToOrder){
			state = CustomerState.Ordering;
			CallWaiter();
			stateChanged();
			return true;
		}
		if (state == CustomerState.Eating && event == CustomerEvent.doneEating){
			state = CustomerState.Leaving;
			leaveTable();
			stateChanged();
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
		Do(name + " is going to restaurant.");
		host.msgIWantToEat(this);//send our instance, so he can respond to us
	}
	
	private void followWaiter(){
		Do (name + " is following waiter.");
		//call the gui to follow the waiter.
	}
	
	private void CallWaiter(){
		Do(name + " is calling Waiter.");
		waiter.msgReadyToOrder(this);
	}
	
	private void ChooseFood(){
		Do(name + " is choosing food.");
		readMenuTimer.restart();
		readMenuTimer.start();
	}

	private void SitDown() {
		Do(name + " is being seated.");
		//customerGui.DoGoToSeat(tableToSitAt);//hack; only one table
	}
	
	private void TellWaiterMyChoice(){
		Do(name + " tells the waiter he wants " + choice + ".");
		waiter.msgHeresMyChoice(this,choice);
		event = CustomerEvent.ordered;
		stateChanged();
	}

	private void EatFood() {
		Do(name + "is eating food.");
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
		Do(name + "is leaving.");
		waiter.msgImDone(this);
		state = CustomerState.Leaving;
		stateChanged();
		//customerGui.DoExitRestaurant(); //set done leaving here.
	}

	// Accessors, etc.

	public String getName() {
		return name;
	}

	public String toString() {
		return "customer " + getName();
	}
	
	//######### GUI ###########
	
	
	
	//########## UTILITIES ###########
	private String RandomChoice(Menu menu){
		int random = (int)(Math.random() * ((menu.getSize())));
		return menu.choice(random);
	}
	

	public void setGui(CustomerGui g) {
		customerGui = g;
	}

	public CustomerGui getGui() {
		return customerGui;
	}
}

