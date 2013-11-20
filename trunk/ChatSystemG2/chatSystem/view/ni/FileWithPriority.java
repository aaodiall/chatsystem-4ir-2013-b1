/**
 * tasks' file, the user can add tasks like when using a normal file
 * but also insert urgent tasks which will be treated first.
 */

package chatSystem.view.ni;

import java.util.List;
import java.util.LinkedList;
      

/**
 *
 * @author Marjorie
 */
public class FileWithPriority<E> {
    
        private List<E> fileTask; 
        
        public FileWithPriority() {
            this.fileTask = new LinkedList<E>();
        }
        
        public boolean isEmpty() {
            return this.fileTask.isEmpty();
        }
        
        public void addTask(E task) {
            this.fileTask.add(task);
        }
        
        public void addUrgentTask(E task) {
            this.fileTask.add(0, task);
        }
        
        public int size() {
            return this.fileTask.size();
        }
        
        public E getNextTask() {
            E task = this.fileTask.get(0);
            this.fileTask.remove(0);
            return task;
        }
        
        public String toString() {
            String result = "";
            for (E elt: this.fileTask) {
                result = result + elt.toString() + " ";
            }
            return result;              
        }
    
}
