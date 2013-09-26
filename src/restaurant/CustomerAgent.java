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
	
	private final int eatingTime = 15000;
	private final int readingMenuTime = 5000;
	
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
	{DoingNothing, WaitingInRestaurant, BeingSeated, Seated, Ordering, WaitingForFood, Eating, DoneEating, Leaving, ReadingMenu};
	private CustomerState state = CustomerState.DoingNothing;//The start state

	public enum CustomerEvent 
	{none, gotHungry, followWaiter, seated, gotMenu, readyToOrder, ordered, foodArrived, doneEating, doneLeaving};
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
	
	public void msgIsHungry(){ 
		Do("is hungry.");
		event = CustomerEvent.gotHungry; 
		stateChanged();
    }
	public void msgFollowMe(Menu m){
		menu = m;
		event = CustomerEvent.followWaiter;
	
		//**Temporary hack to check if this works. Skips the gui part
		//event = CustomerEvent.seated;
		stateChanged();
	}
//Get a message from customer GUI when we reach the table to handle animation. Once we reach the table set Customer State to seated.
	public void WhatWouldYouLike(){
		event = CustomerEvent.gotMenu;
		stateChanged();
	}
	
	public void HeresYourOrder(String order){
		if (order!= choice){
			Do("Wrong Order!!!");
		}
		event = CustomerEvent.foodArrived;
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
			
			followWaiter();
			return true;
		}
		if (state == CustomerState.BeingSeated && event == CustomerEvent.seated){
			state = CustomerState.Seated;
			return true;
		}
		
		if (state == CustomerState.Seated && event == CustomerEvent.gotMenu){
			state = CustomerState.ReadingMenu;
			ChooseFood();
			return true;
		}
		if (state == CustomerState.ReadingMenu && event == CustomerEvent.readyToOrder){
			state = CustomerState.Ordering;
			CallWaiter();
			return true;
		}
		if (state == CustomerState.Ordering && event == CustomerEvent.ordered){
			state = CustomerState.WaitingForFood;
			TellWaiterMyChoice();
			return true;
		}
		if (state == CustomerState.WaitingForFood && event == CustomerEvent.foodArrived){
			state = CustomerState.Eating;
			EatFood();
			return true;
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
		Do("is going to restaurant.");
		host.msgIWantToEat(this);//send our instance, so he can respond to us
	}
	
	private void followWaiter(){
		DoFollowWaiter ();
		//call the gui to follow the waiter.
	}
	
	private void CallWaiter(){
		Do("is calling Waiter.");
		waiter.msgReadyToOrder(this);
	}
	
	private void ChooseFood(){
		Do("is choosing food.");
		readMenuTimer.restart();
		readMenuTimer.start();
	}

	
	
	private void TellWaiterMyChoice(){
		Do("tells the waiter he wants " + choice + ".");
		waiter.msgHeresMyChoice(this,choice);
		event = CustomerEvent.ordered;
	}

	private void EatFood() {
		Do("is eating food.");
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
		Do("is leaving.");
		waiter.msgImDone(this);
		state = CustomerState.Leaving;
		stateChanged();
		customerGui.DoExitRestaurant(); //set done leaving here.
	}

	// Accessors, etc.

	public String getName() {
		return name;
	}

	public String toString() {
		return "customer " + getName();
	}
	
	// ###### GUI Messaging ########
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
	
	//######### GUI Action###########
	private void DoFollowWaiter() {
		Do("is being seated.");
	}
	
	
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

