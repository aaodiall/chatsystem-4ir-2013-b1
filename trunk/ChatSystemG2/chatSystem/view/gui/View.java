/*
 * VIEW of the system, following the MVC pattern
 */

package chatSystem.view.gui;

import java.util.Observer;
import chatSystem.controller.Controller;

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
