import jmetal.core.Operator;
import jmetal.core.Problem;
import jmetal.core.Solution;
import jmetal.core.SolutionSet;
import jmetal.metaheuristics.nsgaII.NSGAII;
import jmetal.operators.crossover.CrossoverFactory;
import jmetal.operators.mutation.MutationFactory;
import jmetal.util.JMException;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

/**
 * Created by Eugene Berlizov on 20.01.2015.
 */
class Main extends JFrame implements Printer{
    private Task[] tasks;
    private GraphView graphPanel;
    private JSpinner taskSpinner;
    private JTextArea TextArea;
    private JSpinner limitSpinner;

    public static void main(String[] args) {
        Main m = new Main();
        m.initComponents();
    }

    private JSpinner populationSpinner;
    private void initComponents() {
        JButton genButton = new JButton();
        JLabel taskLabel = new JLabel();
        taskSpinner = new JSpinner();
        JLabel populationLabel = new JLabel();
        populationSpinner = new JSpinner();
        JScrollPane scrollPane = new JScrollPane();
        TextArea = new JTextArea();
        graphPanel = new GraphView();
        limitSpinner = new JSpinner();
        limitSpinner.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                try {
                    graphPanel.setExtraTasksSets(d((Integer) limitSpinner.getValue()));
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });
        graphPanel.setSpinner(limitSpinner);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        genButton.setText("Сгенерировать");
        genButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                print("----------------\n");
                generate((Integer) taskSpinner.getValue());
                for (Task task : tasks) {
                    print(task.toString());
                }
            }
        });

        taskLabel.setText("задач");
        taskSpinner.setModel(new SpinnerNumberModel(10, 1, null, 1));
        populationLabel.setText("Популяция");
        populationSpinner.setModel(new SpinnerNumberModel(1000, 1, null, 100));

        TextArea.setColumns(20);
        TextArea.setRows(5);
        scrollPane.setViewportView(TextArea);

        GroupLayout jPanel1Layout = new GroupLayout(graphPanel);
        graphPanel.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
                jPanel1Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGap(0, 316, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
                jPanel1Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGap(0, 0, Short.MAX_VALUE)
        );

        GroupLayout layout = new GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        graphPanel.setPrinter(this);
        layout.setHorizontalGroup(
                layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.TRAILING)
                                        .addComponent(scrollPane, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, 400)
                                        .addGroup(GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                                                .addComponent(genButton)
                                                .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                                                .addComponent(taskLabel)
                                                .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                                                .addComponent(taskSpinner, GroupLayout.PREFERRED_SIZE, 45, GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(populationLabel)
                                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(populationSpinner, GroupLayout.PREFERRED_SIZE, 73, GroupLayout.PREFERRED_SIZE)))
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)

                                .addComponent(graphPanel, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(limitSpinner, GroupLayout.PREFERRED_SIZE, 45, GroupLayout.PREFERRED_SIZE)
                                .addContainerGap())
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                                .addGroup(layout.createSequentialGroup()
                                                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                                                .addComponent(genButton)
                                                                .addComponent(taskLabel)
                                                                .addComponent(taskSpinner, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                                                .addComponent(populationLabel)
                                                                .addComponent(populationSpinner, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                                                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                                        .addComponent(scrollPane, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))

                                                .addComponent(limitSpinner, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                                .addComponent(graphPanel, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                )
                                .addContainerGap())
        );
        pack();
        setVisible(true);
    }

    void generate(int tasksCount) {
        tasks = new Task[tasksCount];
        Random r = new Random();
        for (int i = 0; i < tasksCount; i++) {
            tasks[i] = new Task(i + 1, r.nextInt(10), r.nextInt(10));
        }
        permutation();
    }

    void permutation() {
        long startTime = System.currentTimeMillis();
        ArrayList<TasksSet> taskPermutation = new ArrayList<TasksSet>();
        TasksSet a = new TasksSet();
        for (int i = 1; i <= tasks.length; i++) {
            permutation(taskPermutation, a, 0, 0, i);
        }
        int maxV = taskPermutation.get(taskPermutation.size() - 1).getV();
        int maxC = taskPermutation.get(taskPermutation.size() - 1).getCost();
        graphPanel.setMaxValue(maxC, maxV);
        graphPanel.setTasksSets(taskPermutation);
        try {
            graphPanel.setExtraTasksSets(d(maxC));
        }
        catch (Exception e){
            e.printStackTrace();
        }
        print("Полный перебор: "+(System.currentTimeMillis() - startTime)+"ms\n");
    }

    void permutation(ArrayList<TasksSet> taskPermutation, TasksSet a, int pos, int maxUsed, int k) {
        if (pos == k) {
            taskPermutation.add(new TasksSet(a));
        } else {
            for (int i = maxUsed; i < tasks.length; i++) {
                a.set(pos, tasks[i]);
                permutation(taskPermutation, a, pos + 1, i + 1, k);
            }
        }
    }

    ArrayList<TasksSet> d(int limit) throws JMException, SecurityException, ClassNotFoundException {
        long startTime = System.currentTimeMillis();
        Object problem;
        problem = new MyProblem(tasks, limit);
        NSGAII algorithm = new NSGAII((Problem) problem);
        algorithm.setInputParameter("populationSize", populationSpinner.getValue());
        algorithm.setInputParameter("maxEvaluations", 1);

        HashMap<String, Double> parameters = new HashMap<String, Double>();
        parameters.put("probability", 0.9);
        parameters.put("distributionIndex", 20.0);
        Operator crossover;
        Operator mutation;
        crossover = CrossoverFactory.getCrossoverOperator("SBXCrossover", parameters);
        parameters = new HashMap<String, Double>();
        parameters.put("probability", 1.0 / limit);
        parameters.put("distributionIndex", 20.0);
        mutation = MutationFactory.getMutationOperator("PolynomialMutation", parameters);
        algorithm.addOperator("crossover", crossover);
        algorithm.addOperator("mutation", mutation);

        SolutionSet population = algorithm.execute();
        //  population.printVariablesToFile("VAR");
        ArrayList<TasksSet> extraTaskSet = new ArrayList<TasksSet>();
        for (int i = 0; i < population.size(); i++) {

            Solution solution = population.get(i);
            TasksSet ts = new TasksSet();
            for (int j = 0; j < solution.getDecisionVariables().length; j++) {
                ts.add((Task) solution.getDecisionVariables()[j]);
            }
            extraTaskSet.add(ts);
        }
        print("Вычисление фронта: "+(System.currentTimeMillis() - startTime)+"ms\n");


        graphPanel.setExtraTasksSets(extraTaskSet);
        graphPanel.repaint();

        //  population.printObjectivesToFile("FUN");

        return extraTaskSet;
    }
    public void print(String str){
        TextArea.append(str);
    }


}
