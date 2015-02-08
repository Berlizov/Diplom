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

    public static boolean can(TasksSet ts) {
        for (Task task : ts) {
            ArrayList<Task> t = task.getParents();
            for (Task task1 : ts) {
                t.remove(task1);
            }
            if (t.size() > 0)
                return false;
        }
        return true;
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
            s += task.getId() + "\n";
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

    public boolean can() {
        return can(this);
    }

    public boolean can(Task task) {
        TasksSet ts = new TasksSet(this);
        ts.add(task);
        return can(ts);
    }
}
