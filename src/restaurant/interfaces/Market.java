package restaurant.interfaces;


public interface Market {

	String name = null;

	//########## Messages  ###############
	public abstract void msgINeedFood(String choice, int amount, Cook c);
	
	public void msgPayMarket(double money);

}