import Components.SelectableListItemComboBox;
import Structs.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by Eugene Berlizov on 19.11.2014.
 */
class ProjectTasksPanel extends Panel {
    private final String project;
    private final UsersTypes type;
    private final JPanel subtasksPanel;
    public ProjectTasksPanel(UsersTypes type, String project, TaskPanelDelegate parentSender) {
        super(parentSender);
        this.project = project;
        this.type = type;

        subtasksPanel = new JPanel();
        JScrollPane menuS = new JScrollPane(subtasksPanel);
        menuS.getVerticalScrollBar().setUnitIncrement(16);
        menuS.setBorder(null);
        menuS.setOpaque(false);
        menuS.getViewport().setOpaque(false);
        subtasksPanel.setBackground(Constants.FOREGROUND_COLOR);
        subtasksPanel.setLayout(new BoxLayout(subtasksPanel, BoxLayout.PAGE_AXIS));
        menuS.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        updateSubtasks();
        GroupLayout layout = new GroupLayout(this);
        setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addComponent(menuS, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addComponent(menuS, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
    }
    private void updateSubtasks() {
        subtasksPanel.removeAll();
        try {
            Task[] tasks = sendMessage(new Packet(API.GET_PROJECT_USER_TASKS_COMPLEXITY, project)).getArrayOfArgs(Task[].class);
            if (tasks != null) {
                for (Task task : tasks) {
                    String complexity;
                    if(task.getComplexity()!= PokerCardDeck.NOTSET) {
                        complexity = task.getComplexity().toString();
                    }else {
                        complexity = task.getUserComplexity().toString();
                    }
                    final SelectableListItemComboBox l = new SelectableListItemComboBox(task.getName(), PokerCardDeck.getModel(),complexity, false);
                    l.setMaximumSize(new Dimension(Integer.MAX_VALUE, 60));
                    l.setMinimumSize(new Dimension(Integer.MIN_VALUE, 60));
                    if(task.getComplexity()!=PokerCardDeck.NOTSET) {
                        l.setComboBoxEnabled(false);
                    }
                    l.setSelectColor(Constants.MAIN_COLOR2);
                    l.setBackground(Constants.FOREGROUND_COLOR);
                    l.setSelectable(false);
                    l.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            openTaskPanel(l.getText());
                        }
                    });
                    l.addComboboxActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                    setUserComplexityTask(new Task(l.getText(), project, null, PokerCardDeck.valueOf(l.getSelectedItem())));
                        }
                    });
                    subtasksPanel.add(l);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            showConnectionError();
        }
        this.revalidate();
        this.repaint();
    }
    private void openTaskPanel(String taskName){
        ((TaskPanelDelegate)getParentSender()).openTaskPanel(taskName,this);
    }

    private void setUserComplexityTask(Task task){
        try {
            if((Boolean)sendMessage(new Packet(API.SET_PROJECT_TASK_COMPLEXITY, task)).arguments[0])
                showSuccess();
            else
                showError("Неудалось установить сложность задачи.");
        }catch (Exception e){
            showConnectionError();
        }
    }
    @Override
    public synchronized void update() {
        super.update();
        updateSubtasks();
    }
}
