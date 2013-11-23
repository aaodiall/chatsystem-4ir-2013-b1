package chatSystem.view.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;

/**
 *
 * @author Jules
 */
public class DialogWindow extends JFrame implements ActionListener {

    private final JList conversation;
    private final JButton fileButton;
    //private final JMenu jMenu1;
    //private final JMenu jMenu2;
    private final JScrollPane messageJScrollPane;
    private final JScrollPane conversationJScrollPane;
    //private final JMenuBar menu;
    private final JTextArea message;
    private final JButton sendButton;
    private JFileChooser fileChooser;

    private final String contact;
    private final ChatGUI chatGUI;

    /**
     * Creates new form DialogWindow
     *
     * @param contact
     * @param conversationModel
     * @param chatGUI
     */
    public DialogWindow(String contact, DefaultListModel conversationModel, ChatGUI chatGUI) {
        this.contact = contact;

        this.messageJScrollPane = new JScrollPane();
        this.message = new JTextArea();
        this.sendButton = new JButton();
        this.fileButton = new JButton();
        this.conversationJScrollPane = new JScrollPane();
        this.conversation = new JList(conversationModel);

        //this.fileChooser = new JFileChooser();
        //this.menu = new javax.swing.JMenuBar();
        //this.jMenu1 = new javax.swing.JMenu();
        //this.jMenu2 = new javax.swing.JMenu();
        this.chatGUI = chatGUI;
        initWindow();
    }

    private void initWindow() {

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        message.setColumns(20);
        message.setRows(5);
        messageJScrollPane.setViewportView(message);

        sendButton.setText("Send");
        sendButton.addActionListener(this);

        fileButton.setText("Join File");
        fileButton.addActionListener(this);

        conversationJScrollPane.setViewportView(conversation);

        /*jMenu1.setText("File");
         menu.add(jMenu1);

         jMenu2.setText("Edit");
         menu.add(jMenu2);

         setJMenuBar(menu);*/
        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                        .addGap(37, 37, 37)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(conversationJScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 546, Short.MAX_VALUE)
                                .addComponent(messageJScrollPane))
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
                        .addComponent(conversationJScrollPane, javax.swing.GroupLayout.PREFERRED_SIZE, 274, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addGroup(layout.createSequentialGroup()
                                        .addComponent(sendButton, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(fileButton, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addComponent(messageJScrollPane))
                        .addContainerGap())
        );

        pack();
    }

    public void displaySuggestion(final String name, final int idTransfert) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                // JOptionPane suggestion = new JOptionPane();
                int choix = JOptionPane.showConfirmDialog(null, "Souhaitez-vous recevoir le fichier " + name + " proposé par " + contact + " ?",
                        "Réception d'un fichier",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.QUESTION_MESSAGE);
                switch (choix) {
                    case JOptionPane.YES_OPTION:
                        saveFile(name, idTransfert);
                        chatGUI.acceptSuggestion(idTransfert);
                        break;
                    case JOptionPane.NO_OPTION:
                        chatGUI.declineSuggestion(idTransfert);
                        break;
                }
            }
        });
    }

    public void saveFile(final String name, final int idTransfert) {
        final JFrame parent = this;
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                if (fileChooser == null) {
                    fileChooser = new JFileChooser();
                }

                int choix = fileChooser.showSaveDialog(parent);
                if (choix == JFileChooser.APPROVE_OPTION) {
                    File fileSelected = fileChooser.getSelectedFile();
                    chatGUI.saveFile(fileSelected, idTransfert);
                }
            }
        });
    }

    @Override
    public void actionPerformed(ActionEvent ae) {
        System.out.println("Entering actionPerformed DialogWindow");
        if (ae.getSource() == this.sendButton) {
            this.chatGUI.sendMessageRequest(this.message.getText(), this.contact);
            this.message.setText("");
        } else if (ae.getSource() == this.fileButton) {

            if (fileChooser == null) {
                fileChooser = new JFileChooser();
            }

            int choix = fileChooser.showOpenDialog(this);
            if (choix == JFileChooser.APPROVE_OPTION) {
                File fileSelected = fileChooser.getSelectedFile();
                this.chatGUI.sendFileRequest(fileSelected, contact);
            }

            //
            //ici demander affichage progressBar
        }
    }
}
