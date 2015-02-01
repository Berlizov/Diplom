import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by Eugene Berlizov on 20.01.2015.
 */
class TasksSet extends ArrayList<Task> {
    public TasksSet(Collection<? extends Task> c) {
        super(c);
    }

    public TasksSet() {
    }

    public static int getCost(Task[] tasks) {
        int Cost = 0;
        for (Task task : tasks) {
            Cost += task.getCost();
        }
        return Cost;
    }

    public static int getV(Task[] tasks) {
        int V = 0;
        for (Task task : tasks) {
            V += task.getV();
        }
        return V;
    }

    public int getCost() {
        int Cost = 0;
        for (Task task : this) {
            Cost += task.getCost();
        }
        return Cost;
    }

    public int getV() {
        int V = 0;
        for (Task task : this) {
            V += task.getV();
        }
        return V;
    }

    @Override
    public String toString() {
        String s = "";
        for (Task task : this) {
            s += task.getId() + " ";
        }
        return s;
    }

    @Override
    public Task set(int index, Task task) {
        if (size() <= index) {
            add(task);
            return null;
        } else {
            return super.set(index, task);
        }
    }
}
