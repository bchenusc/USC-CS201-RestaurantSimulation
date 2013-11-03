package restaurant.test.mock;


import restaurant.Menu;
import restaurant.Table;
import restaurant.interfaces.Cashier;
import restaurant.interfaces.Customer;
import restaurant.interfaces.Waiter;

/**
 * A sample MockCustomer built to unit test a CashierAgent.
 *
 * @author Monroe Ekilah
 *
 */
//public class MockCustomer extends Mock implements Customer 
public class MockWaiter extends Mock implements Waiter {

	/**
	 * Reference to the Cashier under test that can be set by the unit test.
	 */
	public Cashier cashier;
	public EventLog log = new EventLog();

	public MockWaiter(String name) {
		super(name);
	}

	@Override
	public void msgWantABreak() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgCanGoOnBreak() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgSeatAtTable(Customer c, Table t) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgReadyToOrder(Customer c) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgHeresMyChoice(Customer ca, String c) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgOutOfFood(String choice, int table) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgOrderIsReady(String o, int tableNumber) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgImDone(Customer c) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgRequestCheck(Customer c) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgHereIsCheck(double totalCost, Customer c) {
		// TODO Auto-generated method stub
		log.add(new LoggedEvent("Received Check from Cashier"));
		
	}

	@Override
	public void msgCleanUpDeadCustomer(Customer customer) {
		// TODO Auto-generated method stub
		
	}

}
