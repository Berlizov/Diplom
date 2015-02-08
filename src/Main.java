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
class Main extends JFrame implements Printer, OrderViewDelegate {
    private JFrame frame;
    private OrderView ov;
    private Task[] tasks;
    private GraphView graphPanel;
    private JSpinner taskSpinner;
    private JTextArea TextArea;
    private JSpinner limitSpinner;
    private JSpinner populationSpinner;

    public static void main(String[] args) {
        Main m = new Main();
        m.initComponents();
    }

    @Override
    public void orderChanged() {
        graphPanel.repaint();
        calcParetoFront();
    }
    public void calcParetoFront(){
        try {
            graphPanel.setExtraTasksSets(d((Integer) limitSpinner.getValue()));
        } catch (Exception ex) {
            print(ex.getMessage() + "\n");
        }
    }

    private void initComponents() {
        JButton genButton = new JButton();
        JButton openTreeButton = new JButton();
        JLabel taskLabel = new JLabel();
        JTextArea TextArea1 = new JTextArea();
        taskSpinner = new JSpinner();
        JLabel populationLabel = new JLabel();
        populationSpinner = new JSpinner();
        JScrollPane scrollPane = new JScrollPane();
        JScrollPane scrollPane1 = new JScrollPane();
        TextArea = new JTextArea();
        graphPanel = new GraphView();
        limitSpinner = new JSpinner();
        limitSpinner.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                calcParetoFront();
            }
        });
        graphPanel.setSpinner(limitSpinner);
        graphPanel.setTextArea(TextArea1);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        genButton.setText("Сгенерировать");
        genButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    print("----------------\n");
                    generate((Integer) taskSpinner.getValue());
                    for (Task task : tasks) {
                        print(task.toString());
                    }
                    permutation();
                    graphPanel.repaint();
                    orderInit();
                } catch (Exception ex) {
                    print(ex.getMessage() + "\n");
                }
            }
        });
        ov = new OrderView();
        ov.addDelegate(this);
        openTreeButton.setText("открыть дерево");
        openTreeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (tasks != null) {
                    if (frame != null) {
                        frame.setVisible(false);
                        frame = null;
                    }
                    frame = new JFrame();
                    JButton button=new JButton("Раставить");
                    button.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            ov.autoLayout();
                        }
                    });
                    frame.add(ov, java.awt.BorderLayout.CENTER);
                    frame.add(button, java.awt.BorderLayout.PAGE_END);
                    frame.setSize(400, 400);
                    frame.setVisible(true);
                    orderInit();
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
        scrollPane1.setViewportView(TextArea1);

        GroupLayout layout = new GroupLayout(getContentPane());
        setLayout(layout);
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

                                .addComponent(graphPanel, GroupLayout.DEFAULT_SIZE, 200, Short.MAX_VALUE)
                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.TRAILING)
                                        .addComponent(scrollPane1, GroupLayout.PREFERRED_SIZE, 45, GroupLayout.PREFERRED_SIZE)
                                        .addComponent(limitSpinner, GroupLayout.PREFERRED_SIZE, 45, GroupLayout.PREFERRED_SIZE)
                                        .addComponent(openTreeButton, GroupLayout.PREFERRED_SIZE, 45, GroupLayout.PREFERRED_SIZE))
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
                                                .addGroup(layout.createSequentialGroup()
                                                        .addComponent(limitSpinner, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                                        .addComponent(openTreeButton, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                                        .addComponent(scrollPane1, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                                .addComponent(graphPanel, GroupLayout.DEFAULT_SIZE, 300, Short.MAX_VALUE)
                                )
                                .addContainerGap())
        );
        pack();
        setVisible(true);
    }

    private void orderInit() {
        ov.clear();
        for (Task task : tasks) {
            ov.addTask(task);
        }
        ov.generateRelations();
        ov.autoLayout();
    }

    void generate(int tasksCount) {
        tasks = new Task[tasksCount];
        Random r = new Random();
        for (int i = 0; i < tasksCount; i++) {
            tasks[i] = new Task(i + 1, r.nextInt(10), r.nextInt(10));
        }
        for (int i = 0; i < tasksCount; i++) {
            int relCount = r.nextInt(tasksCount / 2);
            for (int j = 0; j < relCount; j++) {
                tasks[i].addParentTask(tasks[r.nextInt(tasksCount)]);
            }
        }
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
        calcParetoFront();
        print("Полный перебор: " + (System.currentTimeMillis() - startTime) + "ms Кол-во сочетаний - " + taskPermutation.size() + "\n");
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
        //population.printVariablesToFile("VAR");
        ArrayList<TasksSet> extraTaskSet = new ArrayList<TasksSet>();
        for (int i = 0; i < population.size(); i++) {

            Solution solution = population.get(i);
            TasksSet ts = new TasksSet();
            for (int j = 0; j < solution.getDecisionVariables().length; j++) {
                ts.add((Task) solution.getDecisionVariables()[j]);
            }
            extraTaskSet.add(ts);
        }
        print("Вычисление фронта: " + (System.currentTimeMillis() - startTime) + "ms\n");

        graphPanel.setExtraTasksSets(extraTaskSet);

        // population.printObjectivesToFile("FUN");

        return extraTaskSet;
    }

    public void print(String str) {
        TextArea.append(str);
    }


}
