package chatSystem.view.gui;

import java.util.Observer;
import chatSystem.controller.Controller;

/*
 * VIEW of the system, following the MVC pattern
 */
public abstract class View implements Observer {

    protected Controller controller;

    /**
     * Class' constructor
     *
     * @param controller controller the view has to report to
     */
    public View(Controller controller) {
        this.controller = controller;
    }
}
