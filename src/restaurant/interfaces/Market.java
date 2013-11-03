package restaurant.interfaces;


public interface Market {

	//########## Messages  ###############
	public abstract void msgINeedFood(String choice, int amount, Cook c);

}