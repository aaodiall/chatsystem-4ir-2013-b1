package chatsystemg5.ihm;

import chatsystemg5.brain.ChatController;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;

/**
 * Represents the ConnectionWindow frame
 * Implements ActionListener to launch actions when a user clicks on the button
 * 
 */
public class ConnectionWindow extends JFrame implements ActionListener {
    
    private ChatController chat_control;
    
    // Variables declaration - do not modify                     
    private JButton connect_button;
    private JLabel username_label;
    private JTextField username_field;
    // End of variables declaration  
    
    /**
     * Links the ConnectionWindow to the unique Controller<p>
     * Create and display components of the ConnectionWindow
     * 
     * @param chat_control references the unique ChatController
     */
    public ConnectionWindow (ChatController chat_control) {

        this.chat_control = chat_control;
        
        initComponents();
        this.setVisible(true);
        
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if ("Connection".equals(this.connect_button.getText())) {
            String username = this.username_field.getText();
            // On initialise et on met en place la connexion

            this.chat_control.init_controller(username);
            this.chat_control.get_chatGUI().init_list_window();
            this.chat_control.get_listDB().addObserver(chat_control.get_chatGUI().get_list_window());

            this.chat_control.perform_connection();
            
            this.username_field.setEnabled(false);
            this.connect_button.setText("Disconnection");          
        }
        else if ("Disconnection".equals(this.connect_button.getText())) {
            this.chat_control.perform_disconnection();

            
            this.username_field.setEnabled(true);
            this.connect_button.setText("Connection");

        }
        // TO DO : Vider la zone de texte
    }
    
    private void initComponents() {

        username_label = new JLabel("ChatSystem BLT-DFT");
        username_field = new JTextField("Username");
        connect_button = new JButton("Connection");

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        connect_button.addActionListener(this);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(username_label)
                    .addComponent(username_field, javax.swing.GroupLayout.PREFERRED_SIZE, 133, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(connect_button, javax.swing.GroupLayout.PREFERRED_SIZE, 133, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(username_label)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(username_field, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(connect_button)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }
    
}
