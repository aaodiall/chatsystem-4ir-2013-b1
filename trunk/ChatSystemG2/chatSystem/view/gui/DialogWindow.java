/**
 * Window corresponding to the communication between the local user and a given remote system
 */

package chatSystem.view.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.HashMap;
import javax.swing.*;


public class DialogWindow extends JFrame implements ActionListener {

    private final JList conversation;
    private final DefaultListModel conversationMessages;
    private final JButton fileButton;
    private final JScrollPane messageJScrollPane;
    private final JScrollPane conversationJScrollPane;
    private final JTextArea message;
    private final JLabel uploadLabel;
    private final JLabel downloadLabel;
    private final JProgressBar uploadJProgressBar;
    private final JProgressBar downloadJProgressBar;
    private final JButton sendButton;
    private JFileChooser fileChooser;
    
    private final HashMap<Integer,Long> gestionDownloadProgression;
    private final HashMap<Integer,Long> gestionUploadProgression;

    private final String contact;
    private final ChatGUI chatGUI;

    /**
     * Creates new form DialogWindow
     * @param contact username of the contact the local user can communicate using this instance of DialogWindow
     * @param chatGUI instance of chatGUI which is responsible for this instance of DialogWindow
     */
    public DialogWindow(String contact, ChatGUI chatGUI) {
        this.contact = contact;
        this.gestionDownloadProgression = new HashMap<Integer,Long>();
        this.gestionUploadProgression = new HashMap<Integer,Long>();

        this.messageJScrollPane = new JScrollPane();
        this.message = new JTextArea();
        this.sendButton = new JButton();
        this.fileButton = new JButton();
        this.conversationJScrollPane = new JScrollPane();
        this.conversationMessages = new DefaultListModel();
        this.conversation = new JList(this.conversationMessages);
        this.uploadJProgressBar = new JProgressBar();
        this.uploadLabel = new JLabel();
        this.downloadJProgressBar = new JProgressBar();
        this.downloadLabel = new JLabel();

        this.chatGUI = chatGUI;
        initWindow();
    }

    /**
     * Initialization of all the window's components
     */
    private void initWindow() {

        setTitle("Dialog Window : "+contact);
        //seems not to exist on the gei's computers
        this.setAutoRequestFocus(false);
        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        message.setColumns(20);
        message.setRows(5);
        messageJScrollPane.setViewportView(message);

        sendButton.setText("Send");
        sendButton.addActionListener(this);
        
        uploadLabel.setText("Upload");
        uploadLabel.setVisible(false);
        
        uploadJProgressBar.setVisible(false);
        uploadJProgressBar.setStringPainted(true);

        downloadLabel.setText("Download");
        downloadLabel.setVisible(false);
        
        downloadJProgressBar.setVisible(false);
        downloadJProgressBar.setStringPainted(true);

        fileButton.setText("Join File");
        fileButton.addActionListener(this);

        conversationJScrollPane.setViewportView(conversation);
        this.conversationJScrollPane.setVerticalScrollBar(new JScrollBar());
        this.conversationJScrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        
        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(37, 37, 37)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(conversationJScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 546, Short.MAX_VALUE)
                    .addComponent(messageJScrollPane))
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 44, Short.MAX_VALUE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(sendButton, javax.swing.GroupLayout.PREFERRED_SIZE, 159, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(fileButton, javax.swing.GroupLayout.PREFERRED_SIZE, 159, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(uploadJProgressBar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(28, 28, 28))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(downloadJProgressBar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addContainerGap())))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(92, 92, 92)
                                .addComponent(downloadLabel))
                            .addGroup(layout.createSequentialGroup()
                                .addGap(96, 96, 96)
                                .addComponent(uploadLabel)))
                        .addGap(0, 0, Short.MAX_VALUE))))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap(23, Short.MAX_VALUE)
                        .addComponent(conversationJScrollPane, javax.swing.GroupLayout.PREFERRED_SIZE, 274, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(39, 39, 39)
                        .addComponent(uploadLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(uploadJProgressBar, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(20, 20, 20)
                        .addComponent(downloadLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(downloadJProgressBar, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
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

    /**
     * Opens a window to show the user a file transfert's suggestion, with the possibility for him to answer yes or no
     * The user's answer is recovered and performed
     * @param name name of the file
     * @param idTransfert id of the file's transfert
     */
    public void displaySuggestion(final String name, final int idTransfert) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                int choix = JOptionPane.showConfirmDialog(null, "Souhaitez-vous recevoir le fichier " + name + " proposé par " + contact + " ?",
                        "Réception d'un fichier",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.QUESTION_MESSAGE);
                switch (choix) {
                    case JOptionPane.YES_OPTION:
                        saveFile(name, idTransfert);
                        break;
                    case JOptionPane.NO_OPTION:
                        chatGUI.declineSuggestion(idTransfert);
                        break;
                }
            }
        });
    }

    /**
     * Open a window to ask the user where to save the file he is going to download
     * The answer is collected and performed
     * @param name file's name
     * @param idTransfert id of the file's transfert
     */
    public void saveFile(final String name, final int idTransfert) {
        final JFrame parent = this;
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                if (fileChooser == null) {
                    fileChooser = new JFileChooser();
                }
                
                fileChooser.setSelectedFile(new File(name));
                int choix = fileChooser.showSaveDialog(parent);
                if (choix == JFileChooser.APPROVE_OPTION) {
                    File fileSelected = fileChooser.getSelectedFile();
                    chatGUI.saveFile(fileSelected, idTransfert);
                    chatGUI.acceptSuggestion(idTransfert);
                }else{
                    chatGUI.declineSuggestion(idTransfert);
                }
            }
        });
    }
    
    /**
     * Show the user the progression of the file he is sending
     * @param idTransfert id of the file's transfert
     * @param size file's size
     * @param sizeTransfered size of the information that has already been sent
     */
    public void displayFileSendingProgression(final int idTransfert,final long size,final long sizeTransfered) {
         SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                int updateValue = gestionProgressionBar(idTransfert, size, sizeTransfered, uploadJProgressBar, gestionUploadProgression);
                uploadJProgressBar.setValue(updateValue);
                
                if(!uploadJProgressBar.isVisible()){
                    uploadJProgressBar.setVisible(true);
                    uploadLabel.setVisible(true);
                }
            }
        });
    }
    
    /**
     * Show the user the progression of the file he is receiving
     * @param idTransfert id of the file's transfert
     * @param size file's size
     * @param sizeTransfered size of the information that has already been received
     */
    public void displayFileReceivingProgression(final int idTransfert,final long size,final long sizeTransfered){
         SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                int updateValue = gestionProgressionBar(idTransfert, size, sizeTransfered, downloadJProgressBar, gestionDownloadProgression);
                downloadJProgressBar.setValue(updateValue);
                
                if(!downloadJProgressBar.isVisible()){
                    downloadJProgressBar.setVisible(true);
                    downloadLabel.setVisible(true);
                }
            }
        });
    }
    
    /**
     * Reset the progression of a file (sending side)
     * @param idTransfert id of the file's transfert
     * @param size file's size
     */
    public void resetFileSendingProgression(final int idTransfert, final long size){
         SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                gestionUploadProgression.clear();
                uploadJProgressBar.setValue(0);
                uploadJProgressBar.setVisible(false);
                uploadLabel.setVisible(false);
            }
        });
    }
    
    /**
     * Reset the progression of a file (receiving side)
     * @param idTransfert id of the file's transfert
     * @param size file's size
     */
    public void resetFileReceivingProgression(final int idTransfert, final long size){
         SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                gestionDownloadProgression.clear();
                downloadJProgressBar.setValue(0);
                downloadJProgressBar.setVisible(false);
                downloadLabel.setVisible(false);
            }
        });
    }
    
    void displayFileTransfertError(final String fileName) {
            SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                JOptionPane.showConfirmDialog(null, "Problems occurs during the transfert of " + fileName + " please try again",
                        "Connection Error",
                        JOptionPane.CLOSED_OPTION,
                        JOptionPane.WARNING_MESSAGE);
            }
        });
    }
    
    /**
     * Gestion of the progression bar in the window
     * We decided to only show one progress bar in reception and one in sending, even when there are several files
     * The progress bar show the progression all the sendings (or receptions) seen as a whole
     * @param idTransfert id of the file's transfert
     * @param size file's size
     * @param sizeTransfered size of the information that has already been transfered (sent or received)
     * @param pb ProgressBar corresponding to this file's transfert
     * @param gesPb map of progressBar which is concerned
     * @return whole progression of all the transferts (sending or receiving)
     */

    private int gestionProgressionBar(int idTransfert,long size,long sizeTransfered, JProgressBar pb, HashMap<Integer, Long> gesPb) {
       // If the transfert has terminated we stop counting it
        if(size <= sizeTransfered){
            int oldMax = pb.getMaximum();
            int newMax = oldMax - (int) size;
            pb.setString("Done");
            gesPb.remove(idTransfert); 
        } else {
            //on affiche le pourcentage
            pb.setString(null);
            if(!gesPb.containsKey(idTransfert)){
                int oldMax = pb.getMaximum();
                pb.setMaximum(oldMax + (int) size);
            }
            else {
                gesPb.remove(idTransfert);
            }
            gesPb.put((Integer)idTransfert, (Long)sizeTransfered);
                
        }

        //Check where are all the transferts and send back the value
        int totalTrans = 0;
        for(Integer key : gesPb.keySet()){
            totalTrans += gesPb.get(key).intValue();
        }
        return totalTrans;
    }

    /**
     * Update the conversation that is happening with the contact
     * @param newMessage message to add to update the conversation with the new messages
     */
    public void updateConversation(final Object newMessage) {   
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                conversationMessages.addElement(newMessage);
                conversation.ensureIndexIsVisible(conversationMessages.getSize()-1);
            }
        });
    }

    /**
     * Action performed when an event is detected
     * @param ae actionEvent detected
     */
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
        }
    }
    
    /**
     * Show the local user the remote system got disconnected
     * We decided to do it by printing a message in the window if this one is opened
     */
    public void displayUserDisconnected() {
        if (this.isVisible())
            this.updateConversation("THE USER GOT DISCONNECTED");
    }
}
