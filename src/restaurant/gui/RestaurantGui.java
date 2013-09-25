package restaurant.gui;

import restaurant.CustomerAgent;
import javax.swing.*;

import java.awt.*;
import java.awt.event.*;
/**
 * Main GUI class.
 * Contains the main frame and subsequent panels
 */
public class RestaurantGui extends JFrame implements ActionListener {
    /* The GUI has two frames, the control frame (in variable gui) 
     * and the animation frame, (in variable animationFrame within gui)
     */
	//JFrame animationFrame = new JFrame("Restaurant Animation");
	AnimationPanel animationPanel = new AnimationPanel();
	
    /* restPanel holds 2 panels
     * 1) the staff listing, menu, and lists of current customers all constructed
     *    in RestaurantPanel()
     * 2) the infoPanel about the clicked Customer (created just below)
     */    
    private RestaurantPanel restPanel = new RestaurantPanel(this);
    
    private final int WINDOWX = 1200; 
    private final int WINDOWY = 450; 
    private final int bigBoundX = 50;
    private final int bigBoundY = 50;
  
    private JPanel bigRestaurantPanel = new JPanel();
    private JPanel bigAnimationPanel = new JPanel();
    
    /* infoPanel holds information about the clicked customer, if there is one*/
    private JPanel infoPanel;
    private JLabel infoLabel; //part of infoPanel
    private JCheckBox stateCB;//part of infoLabel
    
    private JPanel bottomPanel = new JPanel();
    private JLabel brianLabel;
    private JButton pauseButton = new JButton("Pause");

    private Object currentPerson;/* Holds the agent that the info is about.
    								Seems like a hack */
   
    /**
     * Constructor for RestaurantGui class.
     * Sets up all the gui components.
     */
    public RestaurantGui() {
        
        //----------------------- *Finds the host. ------------------ Important step: Caching the host in the AnimationPanel
        animationPanel.setHost(restPanel.getHost());
   
        //-------------------------------
        setBounds(bigBoundX,bigBoundY,WINDOWX , WINDOWY);
        setLayout(new BoxLayout((Container) getContentPane(), BoxLayout.X_AXIS));
        bigRestaurantPanel.setLayout(new BoxLayout(bigRestaurantPanel, BoxLayout.Y_AXIS));
        bigAnimationPanel.setLayout(new BoxLayout(bigAnimationPanel, BoxLayout.Y_AXIS));
        
        //Sets all the dimensions of the panels.
        initializePanels();
        
        //All adds below
        addTo(animationPanel, bigAnimationPanel);
        addTo(restPanel, bigRestaurantPanel);
        addTo(infoLabel, infoPanel);
        addTo(stateCB, infoPanel);
        addTo (infoPanel, bigRestaurantPanel);
        addTo(brianLabel, bottomPanel);
        addTo(pauseButton, bottomPanel);
        addTo(bottomPanel, bigRestaurantPanel);
        add(bigRestaurantPanel);
        add(bigAnimationPanel);
    }
   
	/**
     * updateInfoPanel() takes the given customer (or, for v3, Host) object and
     * changes the information panel to hold that person's info.
     *
     * @param person customer (or waiter) object
     */
    public void updateInfoPanel(Object person) {
        stateCB.setVisible(true);
        currentPerson = person;

        if (person instanceof CustomerAgent) {
            CustomerAgent customer = (CustomerAgent) person;
            stateCB.setText("Hungry?");
          //Should checkmark be there? 
            stateCB.setSelected(customer.getGui().isHungry());
          //Is customer hungry? Hack. Should ask customerGui
            stateCB.setEnabled(!customer.getGui().isHungry());
          // Hack. Should ask customerGui
            infoLabel.setText(
               "<html><pre>     Name: " + customer.getName() + " </pre></html>");
        }
        infoPanel.validate();
    }
    
    private void addTo(JComponent c, JComponent d){
    	d.add(c);
    }
    
    //Initialization helper functions
    private void initializePanels(){
    	//Big panel sizes
        Dimension bigDim = new Dimension(WINDOWX/2, (int) (WINDOWY-20));
        bigRestaurantPanel.setPreferredSize(bigDim);
        bigRestaurantPanel.setMinimumSize(bigDim);
        bigRestaurantPanel.setMaximumSize(bigDim);
        bigAnimationPanel.setPreferredSize(bigDim);
        bigAnimationPanel.setMinimumSize(bigDim);
        bigAnimationPanel.setMaximumSize(bigDim);
        
        //Animation Panel
        animationPanel.setPreferredSize(bigDim);
        animationPanel.setMinimumSize(bigDim);
        animationPanel.setMaximumSize(bigDim);
        animationPanel.setBounds(WINDOWX/2, 50, WINDOWX/2, WINDOWY);
        
        //Restaurant panel size
        Dimension restDim = new Dimension(WINDOWX/2, (int) (WINDOWY*.5));
        restPanel.setPreferredSize(restDim);
        restPanel.setMinimumSize(restDim);
        restPanel.setMaximumSize(restDim);
        
        //Information panel size
        Dimension infoDim = new Dimension(WINDOWX, (int)(WINDOWY * .3));
        infoPanel = new JPanel();
        infoPanel.setPreferredSize(infoDim);
        infoPanel.setMinimumSize(infoDim);
        infoPanel.setMaximumSize(infoDim);
        infoPanel.setLayout(new GridLayout(1, 2, 30, 0));
        
        //Bottom panel dimensions.
        Dimension bottomDim = new Dimension(WINDOWX/2, (int)(WINDOWY * .1));
        bottomPanel.setPreferredSize(bottomDim);
        bottomPanel.setMinimumSize(bottomDim);
        bottomPanel.setMaximumSize(bottomDim);
        
        //Initialize checkbox.
        stateCB = new JCheckBox();
        stateCB.setVisible(false);
        stateCB.addActionListener(this);
        //Initialize information label "Add to make customers"
        infoLabel = new JLabel(); 
        infoLabel.setText("<html><pre><i>Click Add to make customers</i></pre></html>");
        
        //My name panel (belongs in the bottom panel
        brianLabel = new JLabel("<html><pre><i>Hi I'm Brian!</i></pre></html>");   
        ImageIcon icon = new ImageIcon("C:/Users/Brian/Documents/School/usc2013Fall/restaurant_brianych/src/Mario-icon.png");
        brianLabel.setIcon(icon);
        bottomPanel.setLayout(new BoxLayout(bottomPanel, BoxLayout.X_AXIS));
        
        //Pause Button
        initializePauseButton();
        
      //Make all the borders.
        bigRestaurantPanel.setBorder(BorderFactory.createTitledBorder("Restaurant"));
        bigAnimationPanel.setBorder(BorderFactory.createTitledBorder("Animation"));
        infoPanel.setBorder(BorderFactory.createTitledBorder("Information"));
    }
    
    private void initializePauseButton(){
    	pauseButton.addActionListener(new ActionListener() 
    	{ 
    		public void actionPerformed(ActionEvent e) {
    			if (e.getSource() == pauseButton){
    	    		if (pauseButton.getText() == "Pause"){
    	    			pauseButton.setText("Resume");
    	    			
    	    		}
    	    		else
    	    		{
    	    			pauseButton.setText("Pause");
    	    			
    	    		}
    	    		restPanel.Pause();
    	    		
    	    	}
    		}
    	
    	}
    );
    }
    
    /**
     * Action listener method that reacts to the checkbox being clicked;
     * If it's the customer's checkbox, it will make him hungry
     * For v3, it will propose a break for the waiter.
     */
    public void actionPerformed(ActionEvent e) {
    	
        if (e.getSource() == stateCB) {
            if (currentPerson instanceof CustomerAgent) {
                CustomerAgent c = (CustomerAgent) currentPerson;
                c.getGui().setHungry();
                stateCB.setEnabled(false);
            }
        }
    }
    /**
     * Message sent from a customer gui to enable that customer's
     * "I'm hungry" checkbox.
     *
     * @param c reference to the customer
     */
    public void setCustomerEnabled(CustomerAgent c) {
        if (currentPerson instanceof CustomerAgent) {
            CustomerAgent cust = (CustomerAgent) currentPerson;
            if (c.equals(cust)) {
                stateCB.setEnabled(true);
                stateCB.setSelected(false);
            }
        }
    }
    /**
     * Main routine to get gui started
     */
    public static void main(String[] args) {
        RestaurantGui gui = new RestaurantGui();
        gui.setTitle("csci201 Restaurant");
        gui.setVisible(true);
        gui.setResizable(false);
        gui.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
}
