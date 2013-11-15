/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package chatsystemg5.ihm;

import chatsystemg5.brain.ChatController;
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
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

/**
 *
 * @author joffreydufant
 */
public class ListWindow extends JFrame implements Observer, ListSelectionListener {
    private ChatController chat_control;
    private JList listDisplay;
    private HashMap listUser;
    
    public ListWindow(ChatController chat_control){
        this.chat_control = chat_control;
        this.listUser = new HashMap<String, String>();
    }

    @Override
    public void update(Observable obs, Object obj) {
        // creation of the list
        // we know that we get an HashMap
        this.listUser = (HashMap)obj;
        
        this.listDisplay = new JList(this.listUser.keySet().toArray());
        this.listDisplay.addListSelectionListener(this);
        this.listDisplay.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        
        this.setLayout(new BorderLayout());
        this.add("West",listDisplay);
        this.pack();
        this.setVisible(true);      }

    @Override
    public void valueChanged(ListSelectionEvent lse) {
              this.chat_control.get_chatGUI().get_chat_window().update_chat_window(lse.toString());
    }
}
