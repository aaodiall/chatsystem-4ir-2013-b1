/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package chatSystem.controller;
import java.net.InetAddress;
import java.net.UnknownHostException;

import chatSystem.model.*;
/**
 *
 * @author Marjorie
 */
public class Controller {
    protected UserInformation localUser;
    protected RemoteSystems remoteSystems;
    
    public Controller() {
        InetAddress localIP;
        try {
            localIP = InetAddress.getLocalHost();
            this.localUser = new UserInformation(localIP.toString());
            this.remoteSystems = RemoteSystems.getInstance();
        } catch (UnknownHostException ex) {
            System.out.println("local host non existent");
        }
    }
}
