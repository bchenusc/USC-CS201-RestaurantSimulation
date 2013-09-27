package restaurant;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Timer;

//import restaurant.CustomerAgent.CustomerEvent;

public class Order {
	  String choice;
	  WaiterAgent waiter;
	  int tableNumber;
	  Timer timer;
	  int orderTime;
	  public enum OrderState { pending, cooking, cooked, notified;}
	  private OrderState state = OrderState.pending;
	  
	  public Order(String c, WaiterAgent w, int tableNumber){
		 choice = c;
		 waiter = w;
		 this.tableNumber = tableNumber;
	  }
	  public void setTimer(int time){
		  orderTime = time;
		  state =  OrderState.cooking;
		  timer = new Timer(time, new ActionListener() {
			   public void actionPerformed(ActionEvent e){
			      state = OrderState.cooked;
			      
			      timer.stop();
			   }
			});
		  timer.start();
	  }
	  public OrderState getState(){
		  return state;
	  }
	  public void setState(OrderState state){
		  this.state = state;
	  }
	  public String getChoice(){
		  return choice;
	  }
	  
}
