/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package chatsystemg5.ihm;

import chatsystemg5.brain.ChatController;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class ChatWindow extends JFrame implements ActionListener {
    
    private ChatController chat_control;
    private String remote_username;
    
    private GridLayout layout;

    private JButton send_button;
    
    private JLabel label_send_text ;
    private JLabel label_received_text;
    
    private JTextArea received_text;
    private JTextArea send_text;
    
    //private BufferedWriter writer;
    //private BufferedReader reader;
    
    public ChatWindow (ChatController chat_control) {
        this.chat_control = chat_control;
        this.remote_username = null;

        // create the components
        setLayout(new GridLayout(4,2,2,2));
        
        // the send button and his eventListenner
        this.send_button = new JButton("Send");
        this.send_button.addActionListener(this);
        
        // not used yet
        this.label_received_text = new JLabel("Received text");
        this.label_send_text = new JLabel("Send text");
        
        
        this.send_text = new JTextArea();
        this.received_text = new JTextArea();
        this.received_text.setEditable(false);

        // dispose component in the GridLayout
		 
        this.add("1",this.label_received_text);
        this.add("3",this.received_text);
        this.add("5",this.label_send_text);
        this.add("7",this.send_text);
        this.add("8",this.send_button);
        
        // packs the fenetre: size is calculated
        // regarding the added components
        this.pack();
        // the JFrame is visible now
        this.setVisible(true);

        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE) ; 
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        // it's the send button which is selected
        if (e.getSource() == this.send_button) {
            this.chat_control.perform_send(this.remote_username, this.send_text.getText());
        }
    }
    
    public void update_chat_window(String remote_user){
        this.remote_username = remote_user;
        this.setVisible(true);
    }
    
}
