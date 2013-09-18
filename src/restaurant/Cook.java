package restaurant;

import agent.Agent;

public class Cook extends Agent {
	
	List<Order> pendingOrders;

	Timer cookTimer = new Timer(0, new ActionListener() {
	   Public void actionPerformed(ActionEvent e){
	       if and  an Order o in pendingOrder  o.OrderState == cooking
		then o.OrderState = cooked;
	              o.waiter.OrderIsReady(o);
	   }

	});
	Map<String, Int> foodDictionary = TreeMap<String, Int>();

	Enum State {idle, cooking}


	@Override
	protected boolean pickAndExecuteAnAction() {
		// TODO Auto-generated method stub
		return false;
	}

}
