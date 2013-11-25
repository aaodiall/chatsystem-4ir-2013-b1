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

public class ChatWindow extends JFrame implements Observer, ActionListener, WindowClosingListener {
    
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
    
    //private BufferedWriter writer;
    //private BufferedReader reader;
    
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
        this.received_text.setCaretColor(Color.blue);
        this.received_text.append(((ConversationModel)obs).get_last_text_by_user(this.remote_username));
        //this.received_text.updateUI();
    }

    @Override
    public RuntimeException windowClosingNotify(WindowEvent we) {
        String exception_message = "GUI : The chat window with " + this.remote_username + " has been closed";
        RuntimeException e = new RuntimeException(exception_message);
        return e;
    }

    @Override
    public RuntimeException windowClosingDelivered(WindowEvent we) {
        this.chat_control.get_chatGUI().delete_chat_window(this.remote_username);
        this.chat_control.get_convDB().deleteObserver(this);
        RuntimeException e = new RuntimeException();
        return e;
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
        

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        label.setText(this.remote_username);

        received_text.setColumns(20);
        received_text.setRows(5);
        send_text.setLineWrap(true);
        received_text.setEditable(false);
        jScrollPane1.setViewportView(received_text);

        send_text.setColumns(20);
        send_text.setRows(5);
        send_text.setLineWrap(true);
        jScrollPane2.setViewportView(send_text);

        send_button.setText("Send");
        send_button.addActionListener(this);

        file_button.setText("File");
        file_button.addActionListener(this);

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(org.jdesktop.layout.GroupLayout.TRAILING, jSeparator1)
                    .add(layout.createSequentialGroup()
                        .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(jScrollPane2, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 388, Short.MAX_VALUE)
                            .add(jScrollPane1)
                            .add(layout.createSequentialGroup()
                                .add(label, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                .add(file_button, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 59, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                            .add(org.jdesktop.layout.GroupLayout.TRAILING, jSeparator2))
                        .addContainerGap())))
            .add(layout.createSequentialGroup()
                .add(send_button, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING, false)
                    .add(file_button, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .add(label, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jSeparator2, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 10, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jScrollPane1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 226, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jSeparator1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jScrollPane2, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(send_button)
                .addContainerGap())
        );

        pack();
    }
}
