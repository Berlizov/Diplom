import jmetal.core.SolutionType;
import jmetal.core.Variable;

import java.util.Random;

/**
 * Created by Eugene Berlizov on 21.01.2015.
 */
class MySolutionType extends SolutionType {
    private final Task[] tasks;

    public MySolutionType(MyProblem problem, Task[] tasks) {
        super(problem);
        this.tasks = tasks;
    }

    public Variable[] createVariables() {
        TasksSet v = new TasksSet();
        int totalCost = 0;
        Random r = new Random();
        Task task;
        int margin = 0;
        int maxCost = r.nextInt(problem_.getNumberOfVariables()) + 1;
        for (int j = 0; j < problem_.getNumberOfVariables() * 2 && totalCost <= problem_.getNumberOfVariables() - margin; j++) {
            task = tasks[r.nextInt(tasks.length)];
            if (v.contains(task)) {
                continue;
            }

            if (totalCost + task.getCost() <= maxCost) {
                if(v.can(task)) {
                    v.add(task);
                    totalCost += task.getCost();
                }
            }
        }

        return v.toArray(new Variable[v.size()]);
    }
}
