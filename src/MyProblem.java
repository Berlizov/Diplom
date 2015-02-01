import jmetal.core.Problem;
import jmetal.core.Solution;
import jmetal.util.JMException;

import java.util.Arrays;
/**
 * Created by Eugene Berlizov on 20.01.2015.
 */
class MyProblem extends Problem {
    public MyProblem(Task[] tasks, int limit) {
        numberOfVariables_ = limit;
        numberOfObjectives_ = 2;
        problemName_ = "MyProblem";
        solutionType_ = new MySolutionType(this, tasks);
    }

    public void evaluate(Solution solution) throws JMException {
        Task[] tasks = Arrays.copyOf(solution.getDecisionVariables(), solution.getDecisionVariables().length, Task[].class);
        solution.setObjective(0, -TasksSet.getV(tasks));
        solution.setObjective(1, TasksSet.getCost(tasks));
    }
}
