package chatSystem.view.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.HashMap;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JProgressBar;
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
     *
     * @param contact
     * @param conversationModel
     * @param chatGUI
     */
    public DialogWindow(String contact, DefaultListModel conversationModel, ChatGUI chatGUI) {
        this.contact = contact;
        this.gestionDownloadProgression = new HashMap<Integer,Long>();
        this.gestionUploadProgression = new HashMap<Integer,Long>();

        this.messageJScrollPane = new JScrollPane();
        this.message = new JTextArea();
        this.sendButton = new JButton();
        this.fileButton = new JButton();
        this.conversationJScrollPane = new JScrollPane();
        this.conversation = new JList(conversationModel);
        this.uploadJProgressBar = new JProgressBar();
        this.uploadLabel = new JLabel();
        this.downloadJProgressBar = new JProgressBar();
        this.downloadLabel = new JLabel();

        this.chatGUI = chatGUI;
        initWindow();
    }

    private void initWindow() {

        setTitle("Dialog Window : "+contact);
        
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
    
    public int gestionProgressionBar(int idTransfert,long size,long sizeTransfered, JProgressBar pb, HashMap<Integer, Long> gesPb) {
        
        // On regarde si on connait le transfert, si on le connait pas on l'ajoute
        if(!gesPb.containsKey((Integer)idTransfert)){
            gesPb.put((Integer)idTransfert, (Long)sizeTransfered);
            int oldMax = pb.getMaximum();
            pb.setMaximum(oldMax + (int) size);
        }
        
        // Si le transfert est fini on ne le compte plus
        if(size <= sizeTransfered){
            gesPb.remove(idTransfert);
        }

        //on regarde ou en sont l'ensemble des transfert et on renvoi la valeur
        int totalTrans = 0;
        for(Integer key : gesPb.keySet()){
            totalTrans += gesPb.get(key).intValue();
        }
        
        return totalTrans;
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

            //uploadJProgressBar.setVisible(true);
            //uploadLabel.setVisible(true);
        }
    }
}
