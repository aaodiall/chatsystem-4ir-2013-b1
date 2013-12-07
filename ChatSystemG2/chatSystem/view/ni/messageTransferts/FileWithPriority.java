package chatSystem.view.ni.messageTransferts;

import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Tasks' file, the user can add tasks like when using a normal file but also
 * insert urgent tasks which will be treated first.
 *
 * @param <E> : type of object
 */
public class FileWithPriority<E> {

    private final List<E> fileTask;

    /**
     * Class' constructor
     */
    public FileWithPriority() {
        this.fileTask = new LinkedList<>();
    }

    /**
     * Determining if the file is empty
     *
     * @return true if the file is empty, or false if it's not
     */
    public boolean isEmpty() {
        return this.fileTask.isEmpty();
    }

    /**
     * Add a task to the end of the pile
     *
     * @param task the task which is to be added
     */
    public synchronized void addTask(E task) {
        this.fileTask.add(task);
        notify();
    }

    /**
     * Add a task at the beginning of the pile
     *
     * @param task the task which is to be added
     */
    public synchronized void addUrgentTask(E task) {
        this.fileTask.add(0, task);
        notify();
    }

    /**
     * Determine the file's size
     *
     * @return file's size
     */
    public int size() {
        return this.fileTask.size();
    }

    /**
     * Return and erase the next task in the file
     *
     * @return first element in the file
     */
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
     * Return the String representation of the file
     *
     * @return String representation of the file
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
