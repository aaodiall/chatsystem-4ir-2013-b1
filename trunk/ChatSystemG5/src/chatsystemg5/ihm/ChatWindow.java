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
    private ConnectionWindow connect_window;
    private JButton disconnect_button;
    
    public ChatWindow (ChatController chat_control, ConnectionWindow connect_window) {

        this.chat_control = chat_control;
        this.connect_window = connect_window;
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
        
        // On ferme la fenetre
        this.setVisible(false);
        
        // On réouvre la fenetre de connection
        connect_window.setVisible(true);
        
    }
    
}
