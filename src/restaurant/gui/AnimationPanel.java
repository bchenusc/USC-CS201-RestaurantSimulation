package restaurant.gui;

import javax.swing.*;

import restaurant.HostAgent;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.ArrayList;

public class AnimationPanel extends JPanel implements ActionListener {

    private final int WINDOWX = 600;
    private final int WINDOWY = 450;
    
    
    private final int TABLEWIDTH = 50;
    private final int TABLEHEIGHT = 50;
    
    private final int timerint = 3;

    private List<Gui> guis = new ArrayList<Gui>();
    
    private HostAgent host;
    public void setHost(HostAgent host){
    	this.host = host;
    }

    public AnimationPanel() {
    	setSize(WINDOWX, WINDOWY);
        setVisible(true);
 
    	Timer timer = new Timer(timerint, this );
    	timer.start();
    }

	public void actionPerformed(ActionEvent e) {
		repaint();  //Will have paintComponent called
	}

    public void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D)g;

        //Clear the screen by painting a rectangle the size of the frame
        g2.setColor(getBackground());
        g2.fillRect(0, 0, WINDOWX, WINDOWY );
        
        for (restaurant.Table t : host.tables){
        	//Here is the table
            g2.setColor(Color.ORANGE);
            g2.fillRect(t.getPosX(), t.getPosY(), TABLEWIDTH, TABLEHEIGHT);//200 and 250 need to be table params
        }

        for(Gui gui : guis) {
            if (gui.isPresent()) {
                gui.updatePosition();
            }
        }

        for(Gui gui : guis) {
            if (gui.isPresent()) {
                gui.draw(g2);
            }
        }
    }

    public void addGui(CustomerGui gui) {
        guis.add(gui);
    }

    public void addGui(WaiterGui gui) {
        guis.add(gui);
    }
}
