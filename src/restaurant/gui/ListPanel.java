package restaurant.gui;

import restaurant.CustomerAgent;
import restaurant.HostAgent;

import javax.swing.*;

import java.awt.*;
import java.awt.event.*;
import java.util.List;
import java.util.ArrayList;

/**
 * Subpanel of restaurantPanel.
 * This holds the scroll panes for the customers and, later, for waiters
 */
public class ListPanel extends JPanel implements ActionListener {

    public JScrollPane pane =
            new JScrollPane(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
                    JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
    private JPanel view = new JPanel();
    private List<JButton> list = new ArrayList<JButton>();
    private JButton addPersonB = new JButton("Add");

    private RestaurantPanel restPanel;
    private String type;
    
    final JTextField nameField;
    private JCheckBox hungryBox; //hungry box
    
    private final int nameFieldDimesionWidth = 150;
    private final int nameFieldDimensionHeight = 25;

    /**
     * Constructor for ListPanel.  Sets up all the gui
     *
     * @param rp   reference to the restaurant panel
     * @param type indicates if this is for customers or waiters
     */
    public ListPanel(RestaurantPanel rp, String type) {
        restPanel = rp;
        this.type = type;

        setLayout(new BoxLayout((Container) this, BoxLayout.Y_AXIS));
        
       JLabel nameLabel = new JLabel("<html><pre> <u>" + type + "</u><br></pre></html>");
       nameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        add(nameLabel);
        
        nameField = new JTextField("New Customer");
        nameField.addMouseListener(new MouseAdapter(){
        	@Override
        	public void mouseClicked(MouseEvent e){
        		nameField.setText("");
        	}
        });
        nameField.setAlignmentX(Component.CENTER_ALIGNMENT);
        add(nameField);
        Dimension nameDim = new Dimension(nameFieldDimesionWidth,nameFieldDimensionHeight);
        nameField.setPreferredSize(nameDim);
        nameField.setMinimumSize(nameDim);
        nameField.setMaximumSize(nameDim);
        
        hungryBox = new JCheckBox("Hungry?");
        hungryBox.setAlignmentX(Component.CENTER_ALIGNMENT);
        add(hungryBox);
        

        //addPersonB.addActionListener(this);
        addPersonB.addActionListener(this);
        addPersonB.setAlignmentX(Component.CENTER_ALIGNMENT);
        add(addPersonB);

        view.setLayout(new BoxLayout((Container) view, BoxLayout.Y_AXIS));
        pane.setViewportView(view);
        add(pane);
    }

    /**
     * Method from the ActionListener interface.
     * Handles the event of the add button being pressed
     */
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == addPersonB) {
        	// Chapter 2.19 describes showInputDialog()
        	
            String msg = "Please enter a name:";
            
            //Add the person
           // addPerson(JOptionPane.showInputDialog(msg));
            
            addPerson(nameField.getText(), hungryBox.isSelected()); 
            
        }
        else {
        	// Isn't the second for loop more beautiful?
            //for (int i = 0; i < list.size(); i++) {
                /*JButton temp = list.get(i);*/
        	for (JButton temp:list){
                if (e.getSource() == temp)
                    restPanel.showInfo(type, temp.getText());
            }
        }
    }

    /**
     * If the add button is pressed, this function creates
     * a spot for it in the scroll pane, and tells the restaurant panel
     * to add a new person.
     *
     * @param name name of new person
     */
    public void addPerson(String name) {
        if (name != null) {
        	
            //hungryBox = new JCheckBox();
            //hungryBox.setText("Hungry?");
            //JCheckBox notHungryBox = new JCheckBox();
            //notHungryBox.setText("Not Hungry");
            
            //Object[] objects = {hungryBox, notHungryBox};
            
            //int boolChecked = JOptionPane.showConfirmDialog(null, objects);
            
            //System.out.println(boolChecked);
        	
            JButton button = new JButton(name);
            button.setBackground(Color.white);

            Dimension paneSize = pane.getSize();
            Dimension buttonSize = new Dimension(paneSize.width - 20,
                    (int) (paneSize.height / 7));
            button.setPreferredSize(buttonSize);
            button.setMinimumSize(buttonSize);
            button.setMaximumSize(buttonSize);
            button.addActionListener(this);
            list.add(button);
            view.add(button);
            restPanel.addPerson(type, name);
            //restPanel.addPerson(type, name, boolChecked);//puts customer on list
            restPanel.showInfo(type, name);//puts hungry button on panel
            validate();
        }
    }
    
    public void addPerson(String name, boolean isHungry) {
        if (name != null) {
        	
            //hungryBox = new JCheckBox();
            //hungryBox.setText("Hungry?");
            //JCheckBox notHungryBox = new JCheckBox();
            //notHungryBox.setText("Not Hungry");
            
            //Object[] objects = {hungryBox, notHungryBox};
            
            //int boolChecked = JOptionPane.showConfirmDialog(null, objects);
            
            //System.out.println(boolChecked);
        	
            JButton button = new JButton(name);
            button.setBackground(Color.white);

            Dimension paneSize = pane.getSize();
            Dimension buttonSize = new Dimension(paneSize.width - 20,
                    (int) (paneSize.height / 7));
            button.setPreferredSize(buttonSize);
            button.setMinimumSize(buttonSize);
            button.setMaximumSize(buttonSize);
            button.addActionListener(this);
            list.add(button);
            view.add(button);
            restPanel.addPerson(type, name, isHungry);
            //restPanel.addPerson(type, name, boolChecked);//puts customer on list
            restPanel.showInfo(type, name);//puts hungry button on panel
            validate();
        }
    }
}
