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
	JFrame animationFrame = new JFrame("Restaurant Animation");
	AnimationPanel animationPanel = new AnimationPanel();
	
    /* restPanel holds 2 panels
     * 1) the staff listing, menu, and lists of current customers all constructed
     *    in RestaurantPanel()
     * 2) the infoPanel about the clicked Customer (created just below)
     */    
    private RestaurantPanel restPanel = new RestaurantPanel(this);
    
    /* infoPanel holds information about the clicked customer, if there is one*/
    private JPanel infoPanel;
    private JLabel infoLabel; //part of infoPanel
    private JCheckBox stateCB;//part of infoLabel
    
    private JPanel brianPanel;
    private JLabel brianLabel;

    private Object currentPerson;/* Holds the agent that the info is about.
    								Seems like a hack */
    
    private final int boundsPosX = 50;
    private final int boundsPosY = 50;
    

    /**
     * Constructor for RestaurantGui class.
     * Sets up all the gui components.
     */
    public RestaurantGui() {
        int WINDOWX = 450; //450
        int WINDOWY = 350; //350

        animationFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        animationFrame.setBounds(100+WINDOWX, 50 , WINDOWX+100, WINDOWY+100);
        animationFrame.setVisible(true);
    	animationFrame.add(animationPanel); 
    	
    	setBounds(boundsPosX, boundsPosY, WINDOWX, WINDOWY);
    	
        //setLayout(new BoxLayout((Container) getContentPane(), //****
        		//BoxLayout.Y_AXIS)); //******
    	setLayout(new BorderLayout(1,1));

        Dimension restDim = new Dimension(WINDOWX, (int) (WINDOWY * .6));
    	//Dimension restDim = new Dimension(WINDOWX, (int) (WINDOWY));
        restPanel.setPreferredSize(restDim);
        restPanel.setMinimumSize(restDim);
        restPanel.setMaximumSize(restDim);
        //add(restPanel);
        add(restPanel,BorderLayout.PAGE_START);
        
        // Now, setup the info panel
        //Dimension infoDim = new Dimension(WINDOWX, (int) (WINDOWY * .25));
        Dimension infoDim = new Dimension(WINDOWX, (int)(WINDOWY * .1));
        infoPanel = new JPanel();
        infoPanel.setPreferredSize(infoDim);
        infoPanel.setMinimumSize(infoDim);
        infoPanel.setMaximumSize(infoDim);
        infoPanel.setBorder(BorderFactory.createTitledBorder("Information"));

        stateCB = new JCheckBox();
        stateCB.setVisible(false);
        stateCB.addActionListener(this);

        infoPanel.setLayout(new GridLayout(1, 2, 30, 0));
        
        infoLabel = new JLabel(); 
        infoLabel.setText("<html><pre><i>Click Add to make customers</i></pre></html>");
        infoPanel.add(infoLabel);
        infoPanel.add(stateCB);
        //add(infoPanel);
        add(infoPanel, BorderLayout.CENTER);
        
        //My name
        Dimension brianDim = new Dimension(WINDOWX, (int)(WINDOWY * .3));
        brianPanel = new JPanel();
        brianPanel.setPreferredSize(brianDim);
        brianPanel.setMinimumSize(brianDim);
        brianPanel.setMaximumSize(brianDim);
        brianPanel.setBorder(BorderFactory.createTitledBorder("Information"));
        
        brianLabel = new JLabel(); 
        brianLabel.setText("<html><pre><i>Hi I'm Brian!</i></pre></html>");
        
        ImageIcon icon = new ImageIcon("C:/Users/Brian/Documents/School/usc2013Fall/restaurant_brianych/src/Mario-icon.png");
        brianLabel.setIcon(icon);
        add(brianLabel, BorderLayout.PAGE_END);
      
        
        
    }
    private void add(ImageIcon icon, String pageEnd) {
		// TODO Auto-generated method stub
		
	}
	private ImageIcon createImageIcon(String string) {
		// TODO Auto-generated method stub
		return null;
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
