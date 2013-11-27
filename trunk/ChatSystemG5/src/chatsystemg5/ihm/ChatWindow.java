/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package chatsystemg5.ihm;

import chatsystemg5.brain.ChatController;
import chatsystemg5.brain.ConversationModel;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.util.HashMap;
import java.util.Observable;
import java.util.Observer;
import javax.swing.JFrame;
import sun.awt.WindowClosingListener;

public class ChatWindow extends JFrame implements Observer, ActionListener {
    
    private ChatController chat_control;
    private String remote_username;
    private HashMap conversation_list;
    
    // Variables declaration - do not modify                     
    private javax.swing.JButton file_button;
    private javax.swing.JButton send_button;
    private javax.swing.JTextArea received_text;
    private javax.swing.JTextArea send_text;
    private javax.swing.JLabel label;
    
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    // End of variables declaration  
    
    public ChatWindow (ChatController chat_control, String remote_username) {
        this.chat_control = chat_control;
        this.remote_username = remote_username;
        this.chat_control.get_convDB().addObserver(this);
        
        initComponents();
        this.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        // it's the send button which is selected
        if (e.getSource() == this.send_button) {
            this.chat_control.get_convDB().add_conversation(this.remote_username, "YOU : " + this.send_text.getText());
            this.chat_control.perform_send(this.remote_username, this.send_text.getText());
            this.send_text.setText(null);
        }
        else if(e.getSource() == this.file_button) {
            FileActionPerformed(e);
        }
    }

    @Override
    public void update(Observable obs, Object obj){
        this.received_text.append(((ConversationModel)obs).get_last_text_by_user(this.remote_username));
        this.received_text.setAutoscrolls(rootPaneCheckingEnabled);
        this.setVisible(true);
    }

    private void FileActionPerformed(java.awt.event.ActionEvent evt) {                                     
        // TODO add your handling code here:
    }
                        
    private void initComponents() {

        label = new javax.swing.JLabel();
        received_text = new javax.swing.JTextArea();
        send_text = new javax.swing.JTextArea();
        send_button = new javax.swing.JButton();
        file_button = new javax.swing.JButton();
        
        jScrollPane1 = new javax.swing.JScrollPane();        
        jScrollPane2 = new javax.swing.JScrollPane();
        jSeparator1 = new javax.swing.JSeparator();
        jSeparator2 = new javax.swing.JSeparator();
        

        setDefaultCloseOperation(javax.swing.WindowConstants.HIDE_ON_CLOSE);

        label.setText(this.remote_username);

        received_text.setColumns(20);
        received_text.setRows(5);
        received_text.setLineWrap(true);
        received_text.setEditable(false);
        jScrollPane1.setViewportView(received_text);
        jScrollPane1.setAutoscrolls(rootPaneCheckingEnabled);
        received_text.setAutoscrolls(rootPaneCheckingEnabled);

        send_text.setColumns(20);
        send_text.setRows(5);
        send_text.setLineWrap(true);
        jScrollPane2.setViewportView(send_text);

        send_button.setText("Send");
        send_button.addActionListener(this);

        file_button.setText("File");
        file_button.addActionListener(this);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jSeparator1, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 388, Short.MAX_VALUE)
                            .addComponent(jScrollPane1)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(label, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(file_button, javax.swing.GroupLayout.PREFERRED_SIZE, 59, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jSeparator2, javax.swing.GroupLayout.Alignment.TRAILING))
                        .addContainerGap())))
            .addGroup(layout.createSequentialGroup()
                .addComponent(send_button, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(file_button, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(label, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 226, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(send_button)
                .addContainerGap())
        );

        pack();
    }
}
