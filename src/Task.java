import jmetal.core.Variable;

import java.util.ArrayList;

/**
 * Created by Eugene Berlizov on 16.01.2015.
 */
public class Task extends Variable {
    private final int id;
    private final int Cost;
    private final int V;
    private final ArrayList<Task> parents = new ArrayList<Task>();
    private final ArrayList<Task> childs = new ArrayList<Task>();

    public Task(int id, int cost, int v) {
        this.id = id;
        Cost = cost;
        V = v;
    }

    private Task(Task t) {
        this.id = t.id;
        this.Cost = t.getCost();
        this.V = t.getV();
    }

    public ArrayList<Task> getChilds() {
        return new ArrayList<Task>(childs);
    }

    public boolean addParentTask(Task task) {
        if (find(task)) {
            parents.add(task);
            task.childs.add(this);
            return true;
        }
        return false;
    }

    private boolean find(Task task) {
        if (task == this) {
            return false;
        }
        for (int i = 0; i < task.getParents().size(); i++) {
            if (!find(task.getParents().get(i))) {
                return false;
            }
        }
        return true;
    }

    public void removeParent(Task task) {
        parents.remove(task);
        task.childs.remove(this);
    }

    public ArrayList<Task> getParents() {
        return new ArrayList<Task>(parents);
    }

    public Variable deepCopy() {
        return new Task(this);

    }

    public int getId() {
        return id;
    }

    public int getCost() {
        return Cost;
    }

    public int getV() {
        return V;
    }

    @Override
    public String toString() {
        return "id=" + id +
                ", Cost=" + Cost +
                ", V=" + V + "\n";
    }
}
