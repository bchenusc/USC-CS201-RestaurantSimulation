package restaurant.test;

import java.util.ArrayList;
import java.util.List;

import restaurant.CashierAgent;
import restaurant.CashierAgent.CheckStatus;
import restaurant.CashierAgent.CheckType;
import restaurant.interfaces.Customer;
import restaurant.interfaces.Waiter;
import restaurant.test.mock.MockCustomer;
import restaurant.test.mock.MockWaiter;
import restaurant.test.mock.MockMarket;
import junit.framework.*;

/**
 * 
 * This class is a JUnit test class to unit test the CashierAgent's basic interaction
 * with waiters, customers, and the host.
 * It is provided as an example to students in CS201 for their unit testing lab.
 *
 */
public class CashierTest extends TestCase
{
	//these are instantiated for each test separately via the setUp() method.
	CashierAgent cashier;
	MockWaiter waiter;
	MockCustomer customer;
	
	List<MockMarket> markets = new ArrayList<MockMarket>();
	
	
	/**
	 * This method is run before each test. You can use it to instantiate the class variables
	 * for your agent and mocks, etc.
	 */
	public void setUp() throws Exception{
		super.setUp();		
		cashier = new CashierAgent("cashier");		
		customer = new MockCustomer("mockcustomer");		
		waiter = new MockWaiter("mockwaiter");
		
		markets.add(new MockMarket("Market 1"));
		markets.add(new MockMarket("Market 2"));
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
	
	public void testcashierpaysmarketfullorder()
	{
		//setUp() runs first before this test!
		
		markets.get(0).cashier = cashier;//Sets the cashier of the first market
		
		//check preconditions
		assertEquals("Cashier should have 0 pending checks in it. It doesn't.", cashier.checks.size(), 0);
		assertTrue(markets.get(0).log.size() == 0); // the market has no logs.

		
		//step 1 of the test
		//public Bill(Cashier, Customer, int tableNum, double price) {
		cashier.msgHereIsMarketCost(2.00, markets.get(0));
		
		//check postconditions for step 1 and preconditions for step 2
		assertEquals("The number of checks should be 0. This statement is  ",
						 cashier.checks.size(),1);
		assertTrue("The state of the check should be pending. This statement is ", cashier.checks.get(0).state == CheckStatus.pending);
		assertTrue("The type of check should be market", cashier.checks.get(0).type == CheckType.market);
		assertTrue("The value of the check should be 2.00", cashier.checks.get(0).totalCost == 2.00);
		
		//step 2 call scheduler
		assertTrue("Cashier's scheduler should have returned true (needs to react to customer's ReadyToPay), but didn't.", 
				cashier.pickAndExecuteAnAction());
		
		//check postconditions for step 3 / preconditions for step 4
		assertTrue("Cashier should have no checks", cashier.checks.size() == 0);
	}//end one normal customer scenario
	
	public void testtwoMarketPayments()
	{
		//setUp() runs first before this test!
		
		markets.get(0).cashier = cashier;//Sets the cashier of the first market
		markets.get(1).cashier = cashier;//Sets the cashier of the first market
		
		//check preconditions
		assertEquals("Cashier should have 0 pending checks in it. It doesn't.", cashier.checks.size(), 0);
		assertTrue(markets.get(0).log.size() == 0); // the market has no logs.

		
		//step 1 of the test
		//public Bill(Cashier, Customer, int tableNum, double price) {
		cashier.msgHereIsMarketCost(2.00, markets.get(0));
		cashier.msgHereIsMarketCost(3.00, markets.get(1));
		
		//check postconditions for step 1 and preconditions for step 2
		assertEquals("The number of checks should be 0. This statement is  ",
						 cashier.checks.size(),2);
		assertTrue("The state of the check should be pending. This statement is ", cashier.checks.get(0).state == CheckStatus.pending);
		assertTrue("The state of the check should be pending. This statement is ", cashier.checks.get(1).state == CheckStatus.pending);
		assertTrue("The type of check should be market", cashier.checks.get(0).type == CheckType.market);
		assertTrue("The type of check should be market", cashier.checks.get(1).type == CheckType.market);
		assertTrue("The value of the check should be 2.00", cashier.checks.get(0).totalCost == 2.00);
		assertTrue("The value of the check should be 3.00", cashier.checks.get(1).totalCost == 3.00);
		
		//step 2 call scheduler
		assertTrue("Cashier's scheduler should have returned true (needs to react to customer's ReadyToPay), but didn't.", 
				cashier.pickAndExecuteAnAction());
		assertTrue("Cashier should have no checks", cashier.checks.size() == 1);
		assertTrue("Cashier scheduler should return true.", 
				cashier.pickAndExecuteAnAction());
		
		//check postconditions for step 3 / preconditions for step 4
		assertTrue("Cashier should have no checks", cashier.checks.size() == 0);
		assertTrue("Cashier should have no checks", markets.get(0).money == 2);
		assertTrue("Cashier should have no checks", markets.get(1).money == 3);
	}//end one normal customer scenario
	
	
}
