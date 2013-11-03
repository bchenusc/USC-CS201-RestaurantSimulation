package restaurant.test.mock;


import restaurant.interfaces.Cashier;
import restaurant.interfaces.Cook;
import restaurant.interfaces.Market;

/**
 *
 */
//public class MockCustomer extends Mock implements Customer 
public class MockMarket extends Mock implements Market {

	/**
	 * Reference to the Cashier under test that can be set by the unit test.
	 */
	public String name;
	public Cashier cashier;
	public double money = 0;

	public MockMarket(String name) {
		super(name);
		this.name = name;
	}
	
	public EventLog log = new EventLog();

	@Override
	public void msgINeedFood(String choice, int amount, Cook c) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgPayMarket(double money) {
		// TODO Auto-generated method stub
		this.money += money;
		log.add(new LoggedEvent("money added is " + money));
		
	}
	
	
}
