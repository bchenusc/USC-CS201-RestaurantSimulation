package restaurant.gui;

import restaurant.CustomerAgent;
import restaurant.HostAgent;
import restaurant.Table;

import java.awt.*;

public class CustomerGui implements Gui{

	private CustomerAgent agent = null;
	private boolean isPresent = false;
	private boolean isHungry = false;

	//private HostAgent host;
	RestaurantGui gui;
	
	//GUI Customer Stats
	private final int sizeX = 20;
	private final int sizeY = 20;

	private int xPos, yPos;
	private int xDestination, yDestination;
	private enum Command {noCommand, GoToSeat, LeaveRestaurant};
	private Command command = Command.noCommand;
	
	private boolean receivedCoordinates;
	
	String displayText = "";
	
	//Cache the host so we have access to table locations.
	HostAgent host; //We only cache the host so that we can ask for the table location.

	public CustomerGui(CustomerAgent c, RestaurantGui gui, HostAgent host){ //HostAgent m) {
		agent = c;
		xPos = -40;
		yPos = -40;
		xDestination = -40;
		yDestination = -40;
		this.gui = gui;
		this.host = host;
		
		receivedCoordinates = false;
	}

	public void updatePosition() {
		if (receivedCoordinates){
			if (xPos < xDestination)
				xPos++;
			else if (xPos > xDestination)
				xPos--;
	
			if (yPos < yDestination)
				yPos++;
			else if (yPos > yDestination)
				yPos--;
	
			if (xPos == xDestination && yPos == yDestination) {
				if (command==Command.GoToSeat) {
					agent.msgAnimationFinishedGoToSeat();
				}
				else if (command==Command.LeaveRestaurant) {
					agent.msgAnimationFinishedLeaveRestaurant();
					//System.out.println("about to call gui.setCustomerEnabled(agent);");
					isHungry = false;
					gui.setCustomerEnabled(agent);
				}
				receivedCoordinates = false;
				displayText = "";
				command=Command.noCommand;
			}
		}
	}
	
	public void DoLeavingTable(){
		xDestination = -20; //home base
		yDestination = -20; //home base
		receivedCoordinates = true;
	}
	
	public void DoGoToSeat(int tableNumber){
		for(Table t : host.tables){
			if (t.getTableNumber() == tableNumber){
				xDestination = t.getPosX();
				yDestination = t.getPosY();
			}
		}
		command = Command.GoToSeat;
		receivedCoordinates = true;
	}

	public void draw(Graphics2D g) {
		g.setColor(Color.GREEN);
		g.fillRect(xPos, yPos, sizeX, sizeY);
		
		if (displayText.trim().length() >0)
		g.drawString(displayText, xPos, yPos);
	}
	
	public void setText(String text){
		displayText = text;
	}

	public boolean isPresent() {
		return isPresent;
	}
	public void setHungry() {
		isHungry = true;
		agent.msgIsHungry();
		setPresent(true);
	}
	public boolean isHungry() {
		return isHungry;
	}

	public void setPresent(boolean p) {
		isPresent = p;
	}

	public void DoExitRestaurant() {
		xDestination = -40;
		yDestination = -40;
		command = Command.LeaveRestaurant;
	}
}
