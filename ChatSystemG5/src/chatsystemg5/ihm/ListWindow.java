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
    
    // Variables declaration - do not modify                     
    private javax.swing.JLabel list_label;
    private javax.swing.JList list_display;
    private javax.swing.JScrollPane jScrollPane1;
    // End of variables declaration  
    
    /**
     *
     * @param chat_control
     */
    public ListWindow(ChatController chat_control){
        this.chat_control = chat_control;
        
        initComponents();
        this.setVisible(true);    
    }

    @Override
    public void update(Observable obs, Object obj) {
        // creation of the list
        // we know that we get an HashMap
        this.list_display.setListData(((ListModel)obs).get_hmap_users().keySet().toArray());
        //this.list_display.updateUI();
        //this.list_display.updateUI();
    }
    
    @Override
    public void valueChanged(ListSelectionEvent lse){
        if(this.list_display.getSelectedValue() != null){
            this.chat_control.get_chatGUI().create_chat_window(this.list_display.getSelectedValue().toString());
        }
    }
    
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        list_display = new javax.swing.JList();
        list_label = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.HIDE_ON_CLOSE);

        list_display.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        list_display.addListSelectionListener(this);
        //list_display.add
        jScrollPane1.setViewportView(list_display);

        list_label.setText("Connected users");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(list_label)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(list_label, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 254, Short.MAX_VALUE)
                .addContainerGap())
        );

        pack();
    }
}
