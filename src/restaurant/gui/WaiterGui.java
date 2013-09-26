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
    
    
    private static final int movementOffset = 20;

    public WaiterGui(WaiterAgent agent) {
        this.agent = agent;
    }

    public void updatePosition() {
        if (xPos < xDestination)
            xPos++;
        else if (xPos > xDestination)
            xPos--;

        if (yPos < yDestination)
            yPos++;
        else if (yPos > yDestination)
            yPos--;

        if (xPos == xDestination && yPos == yDestination
        		& (xDestination == xTable + movementOffset) & (yDestination == yTable - movementOffset)) {
           //agent.msgAtTable();
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
    	
        
    }

    public void DoLeaveCustomer() {
        xDestination = -movementOffset;
        yDestination = -movementOffset;
    }

    public int getXPos() {
        return xPos;
    }

    public int getYPos() {
        return yPos;
    }
}
