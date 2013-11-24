/**
 * First window to appear, where the user is to enter a username to get connected
 */

package chatSystem.view.gui;


import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

public final class ConnectWindow extends JFrame implements ActionListener {

    private static final long serialVersionUID = 1L;

    private final JButton connectButton;
    private final JLabel bienvenueLabel;
    private final JTextField usernameTextField;
    private final JLabel usernameLabel;
    private final ChatGUI chatGUI;

    /**
     * Class' constructor
     * @param chatGUI instance of chatGUI which is responsible for the instance of ConnectWindow
     */
    public ConnectWindow(ChatGUI chatGUI) {

        this.bienvenueLabel = new JLabel("Bienvenue sur le chat System");
        this.usernameLabel = new JLabel("username  ");
        this.usernameTextField = new JTextField(30);
        this.connectButton = new JButton("Connect");   
        this.chatGUI = chatGUI;

        this.initWindow();
    }

    /**
     * Initialization of all the window's components
     */
    public void initWindow() {

        this.setTitle("Connect Window");
        this.setSize(300, 400);

        JPanel panel = new JPanel();
        panel.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();

        //initialization of the constraints for the message label
        c.gridx = 0;
        c.gridy = 0;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridwidth = 3;
        c.ipady = 50;
        c.ipadx = 100;
        c.weightx = 0.0;
        panel.add(this.bienvenueLabel, c);

        //initialization of the constraints for usernameL label 
        c.gridx = 0;
        c.gridy = 1;
        c.ipady = 20;
        c.ipadx = 20;
        c.gridwidth = 1;
        c.fill = GridBagConstraints.HORIZONTAL;
        panel.add(this.usernameLabel, c);

         //initialization of the constraints for username textField
        c.gridx = 1;
        c.gridy = 1;
        c.ipady = 10;
        c.ipadx = 40;
        c.gridwidth = 2;
        c.fill = GridBagConstraints.HORIZONTAL;
        panel.add(this.usernameTextField, c);

         //initialization of the constraints for the connect button
        c.gridx = 1;
        c.gridy = 2;
        c.ipady = 10;
        c.ipadx = 10;
        c.fill = GridBagConstraints.HORIZONTAL;
        panel.add(connectButton, c);

        this.add(panel, BorderLayout.PAGE_START);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setVisible(true);
        
        this.connectButton.addActionListener(this);
    }

    /**
     * Action performed when an event is detected
     * @param ae actionEvent detected
     */
    @Override 
    public void actionPerformed(ActionEvent ae) {
        System.out.println("Entering actionPerformed");
        if(ae.getSource() == this.connectButton){
            if(!usernameTextField.getText().equals("")){
                chatGUI.connect(usernameTextField.getText());
            }
        }
    }

}
