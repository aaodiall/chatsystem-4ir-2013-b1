/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package chatsystemg5.ihm;

import chatsystemg5.brain.ListModel;
import java.awt.BorderLayout;
import java.util.Collection;
import java.util.HashMap;
import java.util.Observable;
import java.util.Observer;
import java.util.Vector;
import javax.swing.AbstractListModel;
import javax.swing.JFrame;
import javax.swing.*;

/**
 *
 * @author joffreydufant
 */
public class ListWindow extends JFrame implements Observer{
    private JList listDisplay;
    private HashMap listUser;
    
    public ListWindow(){
        this.listUser = new HashMap<String, String>();
        this.listDisplay = new JList();
    }

    @Override
    public void update(Observable obs, Object obj) {
        // creation of the list
        // we know that we get an HashMap
        this.listUser = (HashMap)obj;
        
        this.listDisplay.setListData(this.listUser.keySet().toArray());
        
        this.setLayout(new BorderLayout());
        this.add("West",listDisplay);
        this.pack();
        this.setVisible(true);      }

}
