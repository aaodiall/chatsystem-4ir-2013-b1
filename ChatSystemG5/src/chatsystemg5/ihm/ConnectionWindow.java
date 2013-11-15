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
    
    public ConnectionWindow (ChatController chat_control) {

        this.chat_control = chat_control;
        // create the components
        // a new label with the "Username" as value
        name = new JLabel("Username : ");
        // a new text field with 20 columns
        text = new JTextField(20);
        // a new button identified as Connection
        connect_button = new JButton("Connection");
        // On écoute le bouton
        connect_button.addActionListener(this) ;
        // configures the JFrame layout using a border layout
        this.setLayout(new BorderLayout());
        // places the components in the layout
        this.add("West",name);
        this.add("Center",text);
        this.add("East",connect_button);
        
        // packs the fenetre: size is calculated
        // regarding the added components
        this.pack();
        // the JFrame is visible now
        this.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String username = text.getText();
        
        // On initialise et on met en place la connexion
        chat_control.init_controller(username);
        chat_control.perform_connection(username);
        
        // On lance la fenetre de chat
        ChatWindow chat_wdw = new ChatWindow(chat_control, this);
        
        // On ferme la fenetre immédiatement et on vide la zone de texte
        this.setVisible(false);
        // Vider la zone de texte
 
        
        // La fenetre se ferme totalement
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE) ; 
     
    }
    
    
}
