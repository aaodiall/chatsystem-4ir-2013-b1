package chatsystemg5.ihm;

import chatsystemg5.brain.ChatController;
import chatsystemg5.brain.UserModel;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;

public class ConnectionWindow /*extends ChatGUI*/ extends JFrame implements ActionListener {
    
    private ChatController chat_control;
    private JLabel name;
    private JTextField text;
    private JButton connect_button;
    private JButton disconnect_button;
    
    public ConnectionWindow (ChatController chat_control) {

        this.chat_control = chat_control;
        // create the components
        // a new label with the "Username" as value
        this.name = new JLabel("Username : ");
        // a new text field with 20 columns
        this.text = new JTextField(20);
        // the connect button and his eventListenner
        this.connect_button = new JButton("Connection");
        this.connect_button.addActionListener(this) ;

        // the disconnect button and his eventListenner
        this.disconnect_button = new JButton("Disonnection");
        this.disconnect_button.addActionListener(this);
        
        // configures the JFrame layout using a border layout
        this.setLayout(new BorderLayout());
        // places the components in the layout
        this.add("West",this.name);
        this.add("Center",this.text);
        this.add("East",this.connect_button);
        
        // packs the fenetre: size is calculated
        // regarding the added components
        this.pack();
        // the JFrame is visible now
        this.setVisible(true);
        
        // the way the window closes
        //this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == this.connect_button) {
            String username = this.text.getText();
            // On initialise et on met en place la connexion
            chat_control.init_controller(username);
            chat_control.perform_connection(username);
            this.getContentPane().removeAll();
            this.getContentPane().invalidate();
            this.remove(this.text);
            this.remove(this.name);
            this.remove(this.connect_button);
            this.add("East", this.disconnect_button);
            this.repaint();
            this.setVisible(true);
        }
        else if (e.getSource() == this.disconnect_button) {
            this.chat_control.perform_disconnection();
            this.getContentPane().removeAll();
            this.getContentPane().invalidate();
            this.remove(this.disconnect_button);
            this.add("West",this.name);
            this.add("Center",this.text);
            this.add("East",this.connect_button);
            this.repaint();
            this.setVisible(true);
        }
        // TO DO : Vider la zone de texte
     
    }
    
    
}
