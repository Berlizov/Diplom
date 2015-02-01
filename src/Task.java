import jmetal.core.Variable;

 /**
 * Created by Eugene Berlizov on 16.01.2015.
 */
public class Task extends Variable {
    private final int id;
    private final int Cost;
    private final int V;

    public Task(int id, int cost, int v) {
        this.id = id;
        Cost = cost;
        V = v;
    }

    private Task(Task t) {
        this.id = t.id;
        Cost = t.getCost();
        V = t.getV();
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
