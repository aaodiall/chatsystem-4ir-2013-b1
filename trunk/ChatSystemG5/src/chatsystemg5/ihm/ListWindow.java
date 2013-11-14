/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package chatsystemg5.ihm;

import chatsystemg5.brain.ListModel;
import java.awt.BorderLayout;
import java.util.Observable;
import java.util.Observer;
import java.util.Vector;
import javax.swing.JFrame;

import javax.swing.JList;

/**
 *
 * @author joffreydufant
 */
public class ListWindow extends JFrame implements Observer{
    private JList list;
    private ListModel listDB;
    
    public ListWindow(ListModel listDB){
        System.out.println("test");
        this.listDB = listDB;
        
    }

    public void update(Observable o, Object o1) {
        //this.listDB = o;
        // create vector because of JList argument 
        Vector v;
        // juste for test (get only the IP)
        v = new Vector(this.listDB.get_hashmap_users().values());
        
        // create the list
        this.list = new JList(v);     
        this.setLayout(new BorderLayout());
        this.add("West",list);
        this.pack();
        this.setVisible(true);        
    }
}
