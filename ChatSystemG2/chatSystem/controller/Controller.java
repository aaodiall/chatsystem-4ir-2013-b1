/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package chatSystem.controller;

import chatSystem.model.*;
/**
 *
 * @author Marjorie
 */
public class Controller {
    protected UserInformation localUser;
    protected RemoteSystems remoteSystems;
    
    public Controller() {
        this.localUser = new UserInformation();
        this.remoteSystems = RemoteSystems.getInstance();
    }
}
