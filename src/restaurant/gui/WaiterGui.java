package restaurant.gui;


import restaurant.CustomerAgent;
import restaurant.HostAgent;
import restaurant.WaiterAgent;

import java.awt.*;

public class WaiterGui implements Gui {

    private WaiterAgent agent = null;

    private int xPos = -20, yPos = -20;//default waiter position
    private int xDestination = -20, yDestination = -20;//default start position

    private int xTable = 200;
    private int yTable = 250;
    
    private  int hostWidth = 20;
    private  int hostHeight = 20;
    
    private boolean receivedAction;
    private boolean doingIdle;
    private static final int movementOffset = 20;

    public WaiterGui(WaiterAgent agent) {
        this.agent = agent;
        receivedAction = false;
        doingIdle = false;
    }

    public void updatePosition() {
    	if (receivedAction){
	        if (xPos < xDestination)
	            xPos++;
	        else if (xPos > xDestination)
	            xPos--;
	        if (yPos < yDestination)
	            yPos++;
	        else if (yPos > yDestination)
	            yPos--;
	        else
	        if (xPos == xDestination && yPos == yDestination){
	        		if(!doingIdle)
	        			agent.atLocation();     
	        		receivedAction = false;
	        		return;
	        }
    	}
    }

    public void draw(Graphics2D g) {
        g.setColor(Color.MAGENTA);
        g.fillRect(xPos, yPos, hostWidth, hostHeight);
    }

    public boolean isPresent() {
        return true;
    }

    public void DoBringToTable(CustomerAgent customer, int tableNumber) {
    	for (restaurant.Table myTable : agent.getHost().tables){
    		if (myTable.getTableNumber() == tableNumber){
    			xTable = myTable.getPosX();
    			yTable = myTable.getPosY();
    			
    			xDestination = xTable + movementOffset;
    			yDestination = yTable - movementOffset;
    		}
    	}
    	receivedAction = true;
    }
    
    public void DoIdle(){
    	xDestination = 300; //Idle destination
    	yDestination = 100; //Idle destination
    	receivedAction = true;
    	doingIdle = true;
    }
    
    public void DoGetCustomer(){
    	xDestination = -20; //Host destination
    	yDestination = -20; // Host Destination
    	receivedAction = true;
    	doingIdle = false;
    }
    
    public void DoGiveOrderToCook(){
    	xDestination = 601; //Destination of cook
    	yDestination = 100; //Destination of cook
    	receivedAction = true;
    	doingIdle = false;
    }
    
    public void DoWalkToCustomer(restaurant.Table table){
    	xDestination = table.getPosX() + movementOffset;
    	yDestination = table.getPosY() - movementOffset;
    	receivedAction = true;
    	doingIdle = false;
    }

    public void DoLeaveCustomer() {
        xDestination = -movementOffset;
        yDestination = -movementOffset;
        receivedAction = true;
        doingIdle = false;
    }

    public int getXPos() {
        return xPos;
    }

    public int getYPos() {
        return yPos;
    }
   
}
