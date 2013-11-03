package restaurant.test;

import restaurant.CashierAgent;
import restaurant.CashierAgent.CheckStatus;
import restaurant.interfaces.Customer;
import restaurant.interfaces.Waiter;
import restaurant.test.mock.MockCustomer;
import restaurant.test.mock.MockWaiter;
import junit.framework.*;

/**
 * 
 * This class is a JUnit test class to unit test the CashierAgent's basic interaction
 * with waiters, customers, and the host.
 * It is provided as an example to students in CS201 for their unit testing lab.
 *
 * @author Monroe Ekilah
 */
public class CashierTest extends TestCase
{
	//these are instantiated for each test separately via the setUp() method.
	CashierAgent cashier;
	MockWaiter waiter;
	MockCustomer customer;
	
	
	/**
	 * This method is run before each test. You can use it to instantiate the class variables
	 * for your agent and mocks, etc.
	 */
	public void setUp() throws Exception{
		super.setUp();		
		cashier = new CashierAgent("cashier");		
		customer = new MockCustomer("mockcustomer");		
		waiter = new MockWaiter("mockwaiter");
	}	
	/**
	 * This tests the cashier under very simple terms: one customer is ready to pay the exact bill.
	 */
	public void testCashierGettingCheck()
	{
		//setUp() runs first before this test!
		
		customer.cashier = cashier;//You can do almost anything in a unit test.			
		
		//check preconditions
		assertEquals("Cashier should have 0 pending checks in it. It doesn't.",cashier.checks.size(), 0);		

		
		//step 1 of the test
		//public Bill(Cashier, Customer, int tableNum, double price) {
		cashier.msgHereIsCheck("Steak", customer, waiter);
		
		//check postconditions for step 1 and preconditions for step 2
		assertEquals("The number of checks should be 0. This statement is  ",
						 cashier.checks.size(),1);
		assertTrue("The state of the check should be pending. This statement is ", cashier.checks.get(0).state == CheckStatus.pending);
		
		//step 2 call scheduler
		assertTrue("Cashier's scheduler should have returned true (needs to react to customer's ReadyToPay), but didn't.", 
				cashier.pickAndExecuteAnAction());
		
		//check postconditions for step 3 / preconditions for step 4
		assertTrue("Cashier's check should now be calculated: ", cashier.checks.get(0).state == CheckStatus.calculated);
	
	
	}//end one normal customer scenario
	
	
}
