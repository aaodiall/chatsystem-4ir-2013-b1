/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package chatsystemg5.ihm;

import chatsystemg5.brain.ChatController;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;

public class ChatWindow extends JFrame implements ActionListener {
    
    private ChatController chat_control;
    private JButton disconnect_button;
    
    public ChatWindow (ChatController chat_control) {

        this.chat_control = chat_control;
        // create the components
        // a new button identified as Connection
        disconnect_button = new JButton("Disonnection");
        // On écoute le bouton
        disconnect_button.addActionListener(this) ;
        // configures the JFrame layout using a border layout
        this.setLayout(new BorderLayout());
        // places the components in the layout
        this.add("East",disconnect_button);
        
        // packs the fenetre: size is calculated
        // regarding the added components
        this.pack();
        // the JFrame is visible now
        this.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        
        // On lance la déconnection
        chat_control.perform_disconnection();
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
