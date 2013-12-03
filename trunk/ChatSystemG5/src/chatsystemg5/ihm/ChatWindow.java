/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package chatsystemg5.ihm;

import chatsystemg5.brain.ChatController;
import chatsystemg5.brain.ConversationModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.HashMap;
import java.util.Observable;
import java.util.Observer;
import javax.swing.JFrame;
import javax.swing.text.DefaultCaret;



/**
 * Represents a ChatWindow frame
 * Implements Observer to be aware of the changes in the model
 * Implements ActionListener to launch actions when a user clicks on a button
 * Implements KeyListener to send message when the enter key is pressed
 * 
 */
public class ChatWindow extends JFrame implements Observer, ActionListener, KeyListener {
    
    private ChatController chat_control;
    private String remote_username;

    
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
    
    /**
     * Links the ChatGUI to the unique controller<p>
     * Links the ChatWindow to a specific remote user<p>
     * Links the ChatWindow to its subject, in the model : ConversationModel<p>
     * Create and display components of the ChatWindow
     * 
     * @param chat_control references the unique ChatController
     * @param remote_username references the remote username to which is linked the ChatWindow
     */
    public ChatWindow (ChatController chat_control, String remote_username) {
        this.chat_control = chat_control;
        this.remote_username = remote_username;
        this.chat_control.get_convDB().addObserver(this);
        
        initComponents();
        this.setVisible(true);
    }

    /**
     * Checks what is the action source<p>
     * If its the send_button then perform the send() funtion<p>
     * Else perform the FileActionPerformed(e) function
     * 
     * @param e references the action initializer
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        // it's the send button which is selected
        if (e.getSource() == this.send_button) {
            this.send();
        }
        else if(e.getSource() == this.file_button) {
            this.FileActionPerformed(e);
        }
    }

    /**
     * This function is called each time the ConversationModel linked to this ChatWindow changes<p>
     * Gets the last message put in the ConversationModel and referencing this ChatWindow and writes it in the "received textarea"<p>
     * Displays the ChatWindow
     * 
     * @param obs references the ConversationModel
     * @param obj references the attribute conversation of the ConversationModel
     */
    @Override
    public void update(Observable obs, Object obj){
        this.received_text.append(((ConversationModel)obs).get_last_text_by_user(this.remote_username));
        this.setVisible(true);
    }
    

    @Override
    public void keyTyped(KeyEvent e) {}

    /**
     * Checks which key is pressed by the user<p>
     * If the enter key is pressed then it calls the send() function<p>
     * Else nothing happens
     * @param e 
     */
    @Override
    public void keyPressed(KeyEvent e) {
        if(e.getKeyCode() == KeyEvent.VK_ENTER){
            this.send();
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {}

    private void FileActionPerformed(java.awt.event.ActionEvent evt) {                                     
        // TODO add your handling code here:
    }
    
    /**
     * Gets the text typed by the local user and gives it to the unique chatController<p>
     * Removes the text displayed in the "send textarea"
     */
    public void send () {
        this.chat_control.perform_send(this.remote_username, this.send_text.getText());
        this.send_text.setText(null);
    }

    /**
     * Creates components of the ChatWindow
     */
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
        DefaultCaret caret = (DefaultCaret) received_text.getCaret();
        caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);

        send_text.setColumns(20);
        send_text.setRows(5);
        
        send_text.setLineWrap(true);
        jScrollPane2.setViewportView(send_text);

        send_button.setText("Send");
        send_button.addActionListener(this);

        file_button.setText("File");
        file_button.addActionListener(this);
        
        this.send_text.addKeyListener(this);

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
                            .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 400, Short.MAX_VALUE)
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
                .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 400, javax.swing.GroupLayout.PREFERRED_SIZE)
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
