package restaurant.gui;


import restaurant.CookAgent;
import restaurant.HostAgent;
import restaurant.interfaces.Customer;

import java.awt.*;
import java.util.ArrayList;

public class CookGui implements Gui {

    private CookAgent agent = null;
    
    RestaurantGui gui;

    private int xPos = -20, yPos = -20;//default waiter position
    private int xDestination = -20, yDestination = -20;//default start position

    private int xTable = 200;
    private int yTable = 250;
    
    private int hostWidth = 20;
    private int hostHeight = 20;
    
    private boolean receivedAction;
    private boolean doingIdle;
    private static final int movementOffset = 20;

    private String displayText = "";
    private List<String> foods = new ArrayList<String>();

    public CookGui(CookAgent agent, RestaurantGui r) {
        this.agent = agent;
        receivedAction = false;
        doingIdle = false;
        gui = r;
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
        		displayText = "";
        		receivedAction = false;
        		return;
	        }
    	}
    }

    public void draw(Graphics2D g) {
        g.setColor(Color.MAGENTA);
        g.fillRect(xPos, yPos, hostWidth, hostHeight);
        if (displayText.trim().length() > 0){
        	if (xPos > 0 && xPos < 600 && yPos>0 && yPos<450){
        		g.drawString(displayText, xPos, yPos);
        	}
        }
    }
    
    public void DoGoToGrills(){
    	
    }
    

    public boolean isPresent() {
        return true;
    }
    
    public void setText(String text){
    	displayText = text;
    }
    

    public int getXPos() {
        return xPos;
    }

    public int getYPos() {
        return yPos;
    }
   
}
