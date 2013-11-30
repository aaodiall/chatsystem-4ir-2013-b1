/**
 * Display to the local user the list of contacts he can communicate with
 * The user has only to click on the name of the remote system he wishes to communicate with
 * to see displayed the corresponding DialogWindow
 */

package chatSystem.view.gui;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.List;

import javax.swing.*;

public class UserWindow extends JFrame implements ActionListener, MouseListener {

    private static final long serialVersionUID = 1L;

    private final JButton deconnectButton;
    private final JLabel username;
    private final JList contactsList;
    private final DefaultListModel contactsName;

    private final ChatGUI chatGUI;

    /**
     * Class' constructor
     * @param username local user's username
     * @param chatGUI instance of chatGUI which is responsible for this instance of UserWindow
     */
    public UserWindow(String username, ChatGUI chatGUI) {

        this.username = new JLabel(username);
        this.deconnectButton = new JButton("Deconnect");

        this.contactsName = new DefaultListModel();
        this.contactsList = new JList(contactsName);

        this.chatGUI = chatGUI;
        this.initWindow();
    }

    /**
     * Initialization of all the window's components
     */
    private void initWindow() {

        this.setTitle("User Window");
        this.setSize(300, 400);
        JPanel panel = new JPanel();
        panel.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();

        //constraints for the username label
        c.gridx = 0;
        c.gridy = 0;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridwidth = 1;
        c.ipady = 30;
        c.ipadx = 20;
        c.weightx = 0.0;
        panel.add(this.username, c);

        //constraints for the  disconnect button
        c.gridx = 3;
        c.gridy = 0;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridwidth = 1;
        c.ipady = 0;
        c.ipadx = 0;
        c.weightx = 0.0;
        panel.add(this.deconnectButton, c);

        //initialization of the contacts' list
        contactsList.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
        contactsList.setLayoutOrientation(JList.VERTICAL);
        contactsList.setVisibleRowCount(-1);
        JScrollPane listScroller = new JScrollPane(contactsList);
        listScroller.setVerticalScrollBar(new JScrollBar(JScrollBar.VERTICAL));
        listScroller.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        //constraints for the contacts' list
        c.gridx = 0;
        c.gridy = 5;
        c.fill = GridBagConstraints.BOTH;
        c.gridwidth = 3;
        c.gridheight = 5;
        c.ipady = 300;
        c.ipadx = 100;
        c.weightx = 0.0;
        panel.add(listScroller, c);

        this.add(panel, BorderLayout.PAGE_START);
        this.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        this.deconnectButton.addActionListener(this);
        this.contactsList.addMouseListener(this);
    }

    /**
     * Update the contacts' list
     * @param newContacts new contacts' list
     */
    public void updateContacts(final List<String> newContacts) {   
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                contactsName.removeAllElements();
                for (String name : newContacts){
                    contactsName.addElement(name);
                }   
            }
        });
    }

    /**
     * Set the local user's username
     * @param usrname username the local user wants to have 
     */
    public void setUsername(final String usrname) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                username.setText(usrname);
            }
        });
    }

     /**
     * Action performed when an event is detected
     * @param ae actionEvent detected
     */
    @Override
    public void actionPerformed(ActionEvent ae) {
        System.out.println("Entering actionPerformed");
        if (ae.getSource() == this.deconnectButton) {
            chatGUI.disconnect();
        }
    }

    /**
     * Action performed when the user clicks on the contacts' list
     * to select a remote system he wants to communicate with
     * @param me mouse event
     */
    @Override
    public void mouseClicked(MouseEvent me) {
        if (me.getClickCount() == 2) {
            //localisation index du clic
            int index = this.contactsList.locationToIndex(me.getPoint());
            //demande d'ouverture de la dialog Window correspondante
            if (index != -1) { //cas de la liste non vide
                String idRemoteSystem = (String) this.contactsList.getModel().getElementAt(index);
                this.chatGUI.openDialogWindow(idRemoteSystem);
            }
        }
    }

    /**
     * Fonctions we had to overwrite but will not use for this application
     * @param me event
     */
    @Override
    public void mousePressed(MouseEvent me) {
        // throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void mouseReleased(MouseEvent me) {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void mouseEntered(MouseEvent me) {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void mouseExited(MouseEvent me) {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
