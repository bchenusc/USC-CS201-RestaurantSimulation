package restaurant;

import restaurant.gui.CustomerGui;
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
	Timer eatingTimer;
	Timer readMenuTimer;
	private CustomerGui customerGui;

	//Necessary links.
	public HostAgent host;
	public WaiterAgent waiter;
	public CashierAgent cashier;
	private String choice;
	private Menu menu;
	double totalMoney;
	double totalCost;
	
	private int numberOfTimesOrdering;

	public enum CustomerState
	{DoingNothing, WaitingInRestaurant, BeingSeated, Seated, Ordering, WaitingForFood, Eating, DoneEating, Leaving, ReadingMenu, NotEnoughmoney, RequestingCheck, Dead};
	private CustomerState state = CustomerState.DoingNothing;//The start state

	public enum CustomerEvent 
	{none, gotHungry, followWaiter, seated, gotMenu, readyToOrder, ordered, foodArrived, doneEating, doneLeaving, ReceivedCheck};
	CustomerEvent event = CustomerEvent.none;
	

	/**
	 * Constructor for CustomerAgent class
	 *
	 * @param name name of the customer
	 * @param gui  reference to the customergui so the customer can send it messages
	 */
	public CustomerAgent(String n, CashierAgent cashier){
		super();
		
		
		this.name = n;
		this.cashier = cashier;
		totalMoney = 5.99; //can change in future;
		numberOfTimesOrdering = 0;
		
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
			      
			      //Super hack. Allows you to order a specific food off of the menu.
			      if (name.equals("Steak") || name.equals("Pizza") || name.equals("Salad")|| name.equals("Chicken")){
			    	  choice = name;
			      }
			      
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
		stateChanged();
	}
//Get a message from customer GUI when we reach the table to handle animation. Once we reach the table set Customer State to seated.
	public void msgWhatWouldYouLike(){
		event = CustomerEvent.ordered;
		stateChanged();
	}
	
	public void msgHeresYourOrder(String order){
		if (order!= choice){
			Do("Wrong Order!!!");
		}
		event = CustomerEvent.foodArrived;
		stateChanged();
	}
	
	public void msgOutOfFood(Menu m){
		menu = m;
		event = CustomerEvent.gotMenu;
		state = CustomerState.Seated;
		stateChanged();
	}
	
	//Paying
	public void msgHereIsTotal(double totalCost2){
		totalCost = totalCost2;
		event = CustomerEvent.ReceivedCheck;
		stateChanged();
	}
	
	public void msgHeresYourChange(double d){
		Do("Received: $"+ d);
		totalMoney = d;
	}
	
	public void msgDie(){
		state = CustomerState.Dead;
		stateChanged();
	}

	/**
	 * Scheduler.  Determine what action is called for, and do it.
	 */
	protected boolean pickAndExecuteAnAction() {
		//	CustomerAgent is a finite state machine
		if (state == CustomerState.DoingNothing && event == CustomerEvent.gotHungry ){
			state = CustomerState .WaitingInRestaurant;
			Do(state.toString());
			goToRestaurant();
			return true;
		}
		if (state == CustomerState.WaitingInRestaurant && event == CustomerEvent.followWaiter ){
			state = CustomerState.Seated;
			Do(state.toString());
			followWaiter();
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
			state = CustomerState.RequestingCheck;
			RequestCheck();
			return true;
		}
		if (state==CustomerState.RequestingCheck && event == CustomerEvent.ReceivedCheck){
			state = CustomerState.Leaving;
			leaveTable();
			return true;
		}
		if (state == CustomerState.Leaving && event == CustomerEvent.doneLeaving){
			state = CustomerState.DoingNothing;
			Paying();
			return true;
		}
		if (state==CustomerState.NotEnoughmoney && event == CustomerEvent.ReceivedCheck){
			giveAllMoneyToCashier();
			leaveTable();
			state = CustomerState.DoingNothing;
		}
		
		if (state == CustomerState.Dead){
			Dead();
		}
		
		return false;
	}

// ################# ACTIONS ####################

	private void giveAllMoneyToCashier() {
		totalMoney = 0;
	}

	private void goToRestaurant() {
		Do("is going to restaurant with " + totalMoney);
		customerGui.setText("Hungry");
		host.msgIWantToEat(this);//send our instance, so he can respond to us
	}
	
	private void followWaiter(){
		customerGui.setText("Walking");
		DoFollowWaiter ();
		//call the gui to follow the waiter.
	}
	
	private void CallWaiter(){
		Do("is calling Waiter.");
		customerGui.setText("Call Waiter");
		waiter.msgReadyToOrder(this);
	}
	
	private void ChooseFood(){
		Do("is choosing food.");
		customerGui.setText("Choosing");
			if (!menu.HaveEnoughMoneyForAny(totalMoney) || numberOfTimesOrdering > 2){
				if (!menu.HaveEnoughMoneyForAny(totalMoney))
				Do("I don't have enough money!");
				else Do("This restaurant is always out of stock!");
				state = CustomerState.NotEnoughmoney;
				event = CustomerEvent.ReceivedCheck;
				stateChanged();
				return;
			}
		numberOfTimesOrdering ++;
		readMenuTimer.restart();
		readMenuTimer.start();
	}

	private void TellWaiterMyChoice(){
		Do("tells the waiter he wants " + choice + ".");
		waiter.msgHeresMyChoice(this,choice);
		event = CustomerEvent.ordered;
		customerGui.setText("?");
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
		customerGui.setText("Eating " + choice);
		eatingTimer.start();
	}
	
	private void RequestCheck(){
		Do("Requesting a check");
		customerGui.setText("Check Please");
		waiter.msgRequestCheck(this);
	}
	
	private void Paying(){
		Do("I'm paying");
		cashier.msgHeresIsMyMoney(this, totalMoney);
	}

	private void leaveTable() {
		Do("is leaving.");
		customerGui.setText("Leaving");
		DoLeavingTable();
		waiter.msgImDone(this);
		state = CustomerState.Leaving;
		stateChanged();
		customerGui.DoExitRestaurant(); //set done leaving here.
	}
	
	private void Dead(){
		Do("has been terminated for lack of payment.");
		customerGui.setText("Dead");
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
		Do("is seated.");
		event = CustomerEvent.gotMenu;
		stateChanged();
	}
	public void msgAnimationFinishedLeaveRestaurant() {
		//from animation
		event = CustomerEvent.doneLeaving;
		stateChanged();
	}
	
	public void DoGoToDeadLocation(){
		customerGui.setText("Dead");
		customerGui.DoGoToDeadLocation();
	}
	
	//######### GUI Action###########
	private void DoFollowWaiter() {
		Do("is following the waiter.");
	}
	private void DoLeavingTable(){
		customerGui.DoLeavingTable();
	}
	
	//########## UTILITIES ###########
	private String RandomChoice(Menu menu){
		int random = (int)(Math.random() * ((menu.getSize())));
		return menu.choice(random);
	}
	
	public double getMoney(){
		return totalMoney;
	
	}
	
	public void setGui(CustomerGui g) {
		customerGui = g;
	}

	public CustomerGui getGui() {
		return customerGui;
	}
}

