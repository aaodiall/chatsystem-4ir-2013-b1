package chatSystem.controller;

/*
 * Interface modelizing the messages that can be sent from the network interface to the controller
 */
public interface NiToCont {
 
    /**
     * React after the reception of a hello message
     * @param username contact who sent the hello message
     * @param ip contact's ip
     * @param isAck whether or not the hello message is a answer
     */
    public void performHelloReceived(String username, String ip, boolean isAck);

    /**
     * React after the reception of a goodbye message
     * @param idRemoteSystem id of the remote system which sent the goodbye message
     */
    public void performGoodbyeReceived(String idRemoteSystem);

    /**
     * React after the reception of a text message
     * @param msg text message's content
     * @param idRemoteSystem id of the remote system which sent the text message
     */
    public void performMessageReceived(String msg, String idRemoteSystem);

    /**
     * React after having sent a text message to a given remote system
     * @param message text message' content
     * @param idRemoteSystem id of the remote system the text message has been sent to
     */
    public void performMessageSent(String message, String idRemoteSystem);

    /**
     * React after the reception of a file transfert demand
     * @param name file's name
     * @param size file's size
     * @param idRemoteSystem id of the remote system which sent the request
     * @param idTransfert transfert's id
     * @param portServer port which is going to be used for the transfert
     */
    public void performSuggestionReceived(String name, long size, String idRemoteSystem, int idTransfert, int portServer);

    /**
     * React after the reception of file transfert's confirmation
     * @param idRemoteSystem id of the remote system which confirmed the file's transfert
     * @param idTransfert file transfert's id
     * @param accepted whether the transfert was accepted or declined
     */
    public void performConfirmationReceived(String idRemoteSystem, int idTransfert, boolean accepted);

    /**
     * React after the reception of part of a file during a transfert
     * @param idTransfert file transfert's id
     * @param filePart file's part that was received
     * @param isLast whether or not it is the file's last part
     */
    public void performFilePartReceived(int idTransfert, byte[] filePart, boolean isLast);

    /**
     * React after having terminated a file's transfert
     * @param idTransfert file transfert's id
     * @param idRemoteSystem id of the remote system the file has been sent to
     */
    public void performFileSended(int idTransfert, String idRemoteSystem);

    /** Display a notification to inform the user that a problem occurs during the connection */
    public void performConnectionError();

    /**
     * Display a notification to inform the user that a problem occurs during a file Transfert
     * @param idTransfert the id of the transfert where error occurs
     */
    public void performFileTransfertError(int idTransfert);
}
