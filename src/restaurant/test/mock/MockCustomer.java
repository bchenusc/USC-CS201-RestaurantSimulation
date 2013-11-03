package restaurant.test.mock;


import restaurant.Menu;
import restaurant.CustomerAgent.CustomerEvent;
import restaurant.CustomerAgent.CustomerState;
import restaurant.interfaces.Cashier;
import restaurant.interfaces.Customer;

/**
 * A sample MockCustomer built to unit test a CashierAgent.
 *
 * @author Monroe Ekilah
 *
 */
//public class MockCustomer extends Mock implements Customer 
public class MockCustomer extends Mock implements Customer {

	/**
	 * Reference to the Cashier under test that can be set by the unit test.
	 */
	public Cashier cashier;

	public MockCustomer(String name) {
		super(name);

	}
	
	EventLog log = new EventLog();
	
	// ##################### Messages #################
	
		@Override
		public void msgIsHungry(){ 
	    }
		@Override
		public void msgFollowMe(Menu m){
		}
		
		@Override
		public void msgFullHouse(){
		}
		
	//Get a message from customer GUI when we reach the table to handle animation. Once we reach the table set Customer State to seated.
		@Override
		public void msgWhatWouldYouLike(){
		}
		
		@Override
		public void msgHeresYourOrder(String order){
			
		}
		
		@Override
		public void msgOutOfFood(Menu m){
		}
		
		//Paying
		@Override
		public void msgHereIsTotal(double totalCost2){
			
			log.add(new LoggedEvent("Received HereIsYourTotal from cashier. Total = "+ totalCost2));
		}
		
		@Override
		public void msgHeresYourChange(double d){
		}
		
		@Override
		public void msgDie(){
		}
		@Override
		public void DoGoToDeadLocation() {
			// TODO Auto-generated method stub
			
		}

/*
	@Override
	public void HereIsYourTotal(double total) {
		log.add(new LoggedEvent("Received HereIsYourTotal from cashier. Total = "+ total));

		if(this.name.toLowerCase().contains("thief")){
			//test the non-normative scenario where the customer has no money if their name contains the string "theif"
			cashier.IAmShort(this, 0);

		}else if (this.name.toLowerCase().contains("rich")){
			//test the non-normative scenario where the customer overpays if their name contains the string "rich"
			cashier.HereIsMyPayment(this, Math.ceil(total));

		}else{
			//test the normative scenario
			cashier.HereIsMyPayment(this, total);
		}
	}

	@Override
	public void HereIsYourChange(double total) {
		log.add(new LoggedEvent("Received HereIsYourChange from cashier. Change = "+ total));
	}

	@Override
	public void YouOweUs(double remaining_cost) {
		log.add(new LoggedEvent("Received YouOweUs from cashier. Debt = "+ remaining_cost));
	}
*/
}
