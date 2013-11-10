/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package chatSystem.view.gui;

import java.util.Observer;
import chatSystem.controller.Controller;
/**
 * 
 * @author clero
 */
public abstract class View implements Observer{
    protected Controller controller;
    
    /**
     * Class' constructor
     * @param controller controller the view has to report to
     */
    public View(Controller controller) {
        this.controller = controller;
    }
}
