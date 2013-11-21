package chatSystem.view.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

/**
 *
 * @author Jules
 */
public class DialogWindow extends javax.swing.JFrame implements ActionListener{

    
    private final JList conversation;
    private final JButton fileButton;
    private final JMenu jMenu1;
    private final JMenu jMenu2;
    private final JScrollPane jScrollPane1;
    private final JScrollPane jScrollPane2;
    private final JMenuBar menu;
    private final JTextArea message;
    private final JButton sendButton;
    
    private final String contact;
    private final ChatGUI chatGUI;

    /**
     * Creates new form DialogWindow
     * @param contact
     * @param conversationModel
     * @param chatGUI
     */
    public DialogWindow(String contact, DefaultListModel conversationModel, ChatGUI chatGUI) {
        this.contact = contact;
        
        this.jScrollPane1 = new javax.swing.JScrollPane();
        this.message = new javax.swing.JTextArea();
        this.sendButton = new javax.swing.JButton();
        this.fileButton = new javax.swing.JButton();
        this.jScrollPane2 = new javax.swing.JScrollPane();
        this.conversation = new javax.swing.JList(conversationModel);
        this.menu = new javax.swing.JMenuBar();
        this.jMenu1 = new javax.swing.JMenu();
        this.jMenu2 = new javax.swing.JMenu();
        
        this.chatGUI = chatGUI;
        initWindow();
    }
                    
    private void initWindow() {

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        message.setColumns(20);
        message.setRows(5);
        jScrollPane1.setViewportView(message);

        sendButton.setText("Send");
        sendButton.addActionListener(this);

        fileButton.setText("Join File");
        fileButton.addActionListener(this);

        jScrollPane2.setViewportView(conversation);

        jMenu1.setText("File");
        menu.add(jMenu1);

        jMenu2.setText("Edit");
        menu.add(jMenu2);

        setJMenuBar(menu);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(37, 37, 37)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 546, Short.MAX_VALUE)
                    .addComponent(jScrollPane1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 44, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(sendButton, javax.swing.GroupLayout.PREFERRED_SIZE, 159, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(fileButton, javax.swing.GroupLayout.PREFERRED_SIZE, 159, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(28, 28, 28))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(23, Short.MAX_VALUE)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 274, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(sendButton, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(fileButton, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jScrollPane1))
                .addContainerGap())
        );

        pack();
    }                                                                                

    @Override
    public void actionPerformed(ActionEvent ae) {
        System.out.println("Entering actionPerformed DialogWindow");
        if(ae.getSource() == this.sendButton){
            this.chatGUI.sendButtonPressed(this.message.getText(),this.contact);
            this.message.setText("");
        }else if(ae.getSource() == this.fileButton){
            this.chatGUI.fileButtonPressed("",contact);
            //ici demander affichage progressBar
        }
    }
}
