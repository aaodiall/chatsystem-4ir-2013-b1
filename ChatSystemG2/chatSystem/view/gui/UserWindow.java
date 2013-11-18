package chatSystem.view.gui;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Collections;
import java.util.List;

import javax.swing.*;

public class UserWindow extends JFrame implements ActionListener, MouseListener {

    /**
     * numéro de version
     */
    private static final long serialVersionUID = 1L;

    private final JButton deconnectButton;
    private final JLabel username;
    private final JList contactsList;
    private final DefaultListModel contactsName;

    private final ChatGUI chatGUI;

    public UserWindow(String username, ChatGUI chatGUI) {

        this.username = new JLabel(username);
        this.deconnectButton = new JButton("Deconnect");
        this.contactsName = new DefaultListModel();

        this.contactsList = new JList(this.contactsName);

        this.chatGUI = chatGUI;
        this.initWindow();
    }

    private void initWindow() {

        this.setTitle("User Window");
        this.setSize(300, 400);
        JPanel panel = new JPanel();
        panel.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();

        //contraintes pour le label username
        c.gridx = 0;
        c.gridy = 0;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridwidth = 1;
        c.ipady = 30;
        c.ipadx = 20;
        c.weightx = 0.0;
        panel.add(this.username, c);

        //contraintes pour le bouton déconnecter
        c.gridx = 3;
        c.gridy = 0;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridwidth = 1;
        c.ipady = 0;
        c.ipadx = 0;
        c.weightx = 0.0;
        panel.add(this.deconnectButton, c);

        //initialisation de la liste de contacts
        contactsList.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
        contactsList.setLayoutOrientation(JList.VERTICAL);
        contactsList.setVisibleRowCount(-1);
        JScrollPane listScroller = new JScrollPane(contactsList);
        listScroller.setVerticalScrollBar(new JScrollBar(JScrollBar.VERTICAL));
        listScroller.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        //contraintes pour la liste de contacts
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

    List<String> toUp;// = new List<String>();

    public void updateContacts(List<String> newContacts) {
        this.toUp = newContacts;
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                updateContactsSwing();
            }
        });

    }

    public void updateContactsSwing() {
        (this.contactsName).removeAllElements();
        Collections.sort(this.toUp);
        for (String name : this.toUp) {
            this.contactsName.addElement(name);
        }
    }

    public void setUsername(String username) {
        this.username.setText(username);
    }

    @Override
    public void actionPerformed(ActionEvent ae) {
        System.out.println("Entering actionPerformed");
        if (ae.getSource() == this.deconnectButton) {
            chatGUI.deconnectButtonPressed();
        }
    }

    @Override
    public void mouseClicked(MouseEvent me) {
        if (me.getClickCount() == 2) {
            //localisation index du clic
            int index = this.contactsList.locationToIndex(me.getPoint());
            //demande d'ouverture de la dialog Window correspondante
            String idRemoteSystem = (String)this.contactsList.getModel().getElementAt(index);
            this.chatGUI.displayDialogWindow(idRemoteSystem);
        }
    }

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
