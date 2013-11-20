/**
 * tasks' file, the user can add tasks like when using a normal file but also
 * insert urgent tasks which will be treated first.
 */
package chatSystem.view.ni;

import java.util.List;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Marjorie
 * @param <E> : type of object
 */
public class FileWithPriority<E> {

    private final List<E> fileTask;

    public FileWithPriority() {
        this.fileTask = new LinkedList<E>();
    }

    public boolean isEmpty() {
        return this.fileTask.isEmpty();
    }

    public synchronized void addTask(E task) {
        this.fileTask.add(task);
        notify();
    }

    public synchronized void addUrgentTask(E task) {
        this.fileTask.add(0, task);
        notify();
    }

    public int size() {
        return this.fileTask.size();
    }

    public synchronized E getNextTask() {
        if (this.fileTask.isEmpty()) {
            try {
                wait();
            } catch (InterruptedException ex) {
                Logger.getLogger(FileWithPriority.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        E task = this.fileTask.get(0);
        this.fileTask.remove(0);
        return task;
    }

    /**
     *
     * @return
     */
    @Override
    public String toString() {
        String result = "";
        for (E elt : this.fileTask) {
            result = result + elt.toString() + " ";
        }
        return result;
    }

}
