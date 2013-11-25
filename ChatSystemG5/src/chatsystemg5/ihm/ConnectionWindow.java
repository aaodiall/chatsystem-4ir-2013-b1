package chatsystemg5.ihm;

import chatsystemg5.brain.ChatController;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;

public class ConnectionWindow extends JFrame implements ActionListener {
    
    private ChatController chat_control;
    
    // Variables declaration - do not modify                     
    private JButton connect_button;
    private JLabel username_label;
    private JTextField username_field;
    // End of variables declaration  
    
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
            this.chat_control.get_chatGUI().get_list_window().dispose();

        }
        // TO DO : Vider la zone de texte
     
    }
    
    private void initComponents() {

        username_label = new JLabel("ChatSystem BLT-DFT");
        username_field = new JTextField("Username");
        connect_button = new JButton("Connection");

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        connect_button.addActionListener(this);

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(username_label)
                    .add(username_field, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 133, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(connect_button, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 133, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(username_label)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(username_field, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(connect_button)
                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }
    
}
