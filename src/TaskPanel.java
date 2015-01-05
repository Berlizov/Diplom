import Components.*;
import Structs.*;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

/**
 * Created by  Eugene Berlizov on 19.11.2014.
 */
interface TaskPanelDelegate extends SenderInterface {
    public void openTaskPanel(String task, Panel panel);
}

public class TaskPanel extends Panel {
    private final Task task;
    private final UsersTypes type;
    JTextArea description;
    private Selector workersPanel;
    private JPanel subtasksPanel;
    private JTextField subtaskField;

    public TaskPanel(String project, String task, UsersTypes type, TaskPanelDelegate parentSender) {
        super(parentSender);
        this.task = new Task(task, project);
        this.type = type;
        if (type == UsersTypes.PRODUCT_OWNER) {
            openControlPanel();
        } else {
            openSimplePanel();
        }
    }

    public void openSimplePanel() {
        JLabel label = new JLabel();
        label.setFont(new Font("Tahoma", 0, 24));
        label.setHorizontalAlignment(SwingConstants.CENTER);
        label.setText(task.getProject() + "/" + task.getName());
        JLabel descriptionLabel = new JLabel();
        JLabel tasksLabel = new JLabel();
        subtasksPanel = new JPanel();

        Border border = BorderFactory.createLineBorder(Constants.MINOR_TEXT_COLOR);
        descriptionLabel.setText("Описание");
        description = new JTextArea();
        description.setEditable(false);
        tasksLabel.setText("Подзадачи");
        subtasksPanel.setBorder(border);
        subtasksPanel.setBackground(Constants.FOREGROUND_COLOR);


        JPanel p = new JPanel();
        JScrollPane menuS = new JScrollPane(p);
        menuS.getVerticalScrollBar().setUnitIncrement(16);
        menuS.setBorder(null);
        menuS.setOpaque(false);
        menuS.getViewport().setOpaque(false);
        p.setBackground(Constants.FOREGROUND_COLOR);
        menuS.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);


        JScrollPane tasksPanelS = new JScrollPane(subtasksPanel);
        tasksPanelS.getVerticalScrollBar().setUnitIncrement(16);
        tasksPanelS.setBorder(null);
        tasksPanelS.setOpaque(false);
        tasksPanelS.getViewport().setOpaque(false);
        tasksPanelS.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        subtasksPanel.setLayout(new BoxLayout(subtasksPanel, BoxLayout.PAGE_AXIS));

        GroupLayout layout = new GroupLayout(this);
        setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addComponent(menuS, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addComponent(menuS, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        layout = new GroupLayout(p);
        p.setLayout(layout);


        layout.setHorizontalGroup(
                layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                                .addComponent(tasksLabel, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                .addComponent(label, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                .addComponent(tasksPanelS, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                .addComponent(descriptionLabel, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                .addComponent(description, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                )
                                .addContainerGap()));
        layout.setVerticalGroup(
                layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(label, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(tasksLabel, GroupLayout.PREFERRED_SIZE, 27, GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(tasksPanelS, GroupLayout.PREFERRED_SIZE, 300, GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(descriptionLabel, GroupLayout.PREFERRED_SIZE, 27, GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(description, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)
                                .addContainerGap()));
        updateSubtasks();
        updateDescription();
    }

    public void openControlPanel() {
        Border border = BorderFactory.createLineBorder(Constants.MINOR_TEXT_COLOR);
        JLabel label = new JLabel();
        label.setFont(new java.awt.Font("Tahoma", 0, 24));
        label.setHorizontalAlignment(SwingConstants.CENTER);
        label.setText(task.getProject() + "/" + task.getName());
        JLabel workersLabel = new JLabel();
        workersPanel = new Selector("Разработчики задачи", "Разработчики проекта");
        CButton saveButton = new CButton();
        JLabel tasksLabel = new JLabel();
        subtasksPanel = new JPanel();
        subtaskField = new JTextField();
        CButton addTaskButton = new CButton();
        description = new JTextArea();
        JLabel descriptionLabel = new JLabel();
        CButton descriptionsaveButton = new CButton();
        descriptionsaveButton.setText("Сохранить");
        descriptionsaveButton.setBackgroundColor(Constants.MAIN_COLOR);
        descriptionsaveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    sendMessage(new Packet(API.ADD_TASK_DESCRIPTION, task, description.getText()));
                } catch (Exception ex) {
                    ex.printStackTrace();
                    showConnectionError();
                }
            }
        });
        descriptionLabel.setText("Описание");
        description.setBorder(border);

        workersLabel.setText("Работники");
        workersPanel.setSelectColor(Constants.MAIN_COLOR);
        workersPanel.setBackground(Constants.FOREGROUND_COLOR);
        workersPanel.setBordersOfView(border);
        saveButton.setText("Сохранить");
        saveButton.setBackgroundColor(Constants.MAIN_COLOR);
        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    ArrayList<SelectableListItem> sli = workersPanel.getSelectedItems();
                    Object[] str = new Object[sli.size() + 1];
                    str[0] = task;
                    for (int i = 1; i < str.length; i++) {
                        str[i] = sli.get(i - 1).getText();
                    }
                    Packet p = new Packet(API.CHANGE_TASK_USERS, str);
                    p.setArguments(str);
                    if ((Boolean) sendMessage(p).arguments[0]) {
                        showSuccess();
                    } else {
                        showError("Не удалось обновить пользователей проекта");
                    }
                    updateTaskUsers();
                } catch (Exception ex) {
                    ex.printStackTrace();
                    showConnectionError();
                }
            }
        });

        tasksLabel.setText("Подзадачи");
        subtasksPanel.setBorder(border);
        subtasksPanel.setBackground(Constants.FOREGROUND_COLOR);


        JPanel p = new JPanel();
        JScrollPane menuS = new JScrollPane(p);
        menuS.getVerticalScrollBar().setUnitIncrement(16);
        menuS.setBorder(null);
        menuS.setOpaque(false);
        menuS.getViewport().setOpaque(false);
        p.setBackground(Constants.FOREGROUND_COLOR);
        menuS.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);


        JScrollPane tasksPanelS = new JScrollPane(subtasksPanel);
        tasksPanelS.getVerticalScrollBar().setUnitIncrement(16);
        tasksPanelS.setBorder(null);
        tasksPanelS.setOpaque(false);
        tasksPanelS.getViewport().setOpaque(false);
        tasksPanelS.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        subtasksPanel.setLayout(new BoxLayout(subtasksPanel, BoxLayout.PAGE_AXIS));

        addTaskButton.setText("Добавить");
        addTaskButton.setBackgroundColor(Constants.MAIN_COLOR);
        addTaskButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addSubtask();
            }
        });
        GroupLayout layout = new GroupLayout(this);
        setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(menuS, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(menuS, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        layout = new GroupLayout(p);
        p.setLayout(layout);


        layout.setHorizontalGroup(
                layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                        .addComponent(descriptionLabel, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(description, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addGroup(GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                                .addComponent(descriptionsaveButton))
                                        .addComponent(tasksLabel, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(label, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addGroup(GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                                .addComponent(subtaskField, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                                                .addComponent(addTaskButton))
                                        .addComponent(tasksPanelS, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(workersLabel, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(workersPanel, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addGroup(GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                                .addComponent(saveButton)))
                                .addContainerGap()));
        layout.setVerticalGroup(
                layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(label, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(descriptionLabel, GroupLayout.PREFERRED_SIZE, 27, GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(description, GroupLayout.PREFERRED_SIZE, 200, GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(descriptionsaveButton, GroupLayout.PREFERRED_SIZE, 35, GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(tasksLabel, GroupLayout.PREFERRED_SIZE, 27, GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING, false)
                                        .addComponent(subtaskField, GroupLayout.DEFAULT_SIZE, 35, Short.MAX_VALUE)
                                        .addComponent(addTaskButton, GroupLayout.DEFAULT_SIZE, 35, Short.MAX_VALUE))
                                .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(tasksPanelS, GroupLayout.PREFERRED_SIZE, 300, GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(workersLabel, GroupLayout.PREFERRED_SIZE, 27, GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(workersPanel, GroupLayout.PREFERRED_SIZE, 300, GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(saveButton, GroupLayout.PREFERRED_SIZE, 35, GroupLayout.PREFERRED_SIZE)
                                .addContainerGap()));
        updateTaskUsers();
        updateSubtasks();
        updateDescription();
    }

    private void updateDescription() {
        try {
            description.setText((String) (sendMessage(new Packet(API.GET_TASK_DESCRIPTION, task)).arguments[0]));
        } catch (Exception e) {
            showConnectionError();
        }
    }

    private void updateSubtasks() {
        subtasksPanel.removeAll();
        try {
            Subtask[] subtasks = sendMessage(new Packet(API.GET_TASK_SUBTASKS, task)).getArrayOfArgs(Subtask[].class);
            if (subtasks != null) {
                for (final Subtask subtask : subtasks) {
                    System.out.println(subtask.getFinish().toString());
                    final SelectableListItemComboBox l = new SelectableListItemComboBox(subtask.getName(),
                            new String[]{"false", "true"},
                            subtask.getFinish().toString(),
                            false);
                    if (type != UsersTypes.DEVELOPER) {
                        l.setComboBoxEnabled(false);
                    }
                    l.setSelectable(false);
                    l.setMaximumSize(new Dimension(Integer.MAX_VALUE, 60));
                    l.setMinimumSize(new Dimension(Integer.MIN_VALUE, 60));
                    l.setSelectColor(Constants.MAIN_COLOR2);
                    l.setBackground(Constants.FOREGROUND_COLOR);

                    l.addComboboxActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            try {

                                sendMessage(new Packet(API.CHANGE_SUBTASKS_COMPLETENESS,
                                                new Subtask(task.getName(),
                                                        task.getProject(),
                                                        l.getText(),
                                                        Boolean.parseBoolean(l.getSelectedItem())
                                                )
                                        )
                                );
                                showSuccess();
                            } catch (Exception ex) {
                                ex.printStackTrace();
                                showConnectionError();
                            }
                        }
                    });
                    subtasksPanel.add(l);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            showConnectionError();
        }
    }

    private void updateTaskUsers() {
        try {
            User[] a = sendMessage(new Packet(API.GET_PROJECT_USERS_BY_TYPE, task.getProject(), UsersTypes.DEVELOPER)).getArrayOfArgs(User[].class);
            ArrayList<SelectableListItem> l = new ArrayList<>();
            if (a != null) {
                for (User anA : a) {
                    SelectableListItem sli = new SelectableListItem(anA.getLogin(), false);
                    sli.setBackground(Constants.FOREGROUND_COLOR);
                    l.add(sli);
                }
            }

            if (workersPanel != null) {
                workersPanel.setSelectableElements(l);
                l = new ArrayList<>();
                a = sendMessage(new Packet(API.GET_TASK_USERS, task)).getArrayOfArgs(User[].class);
                if (a != null) {
                    for (User anA : a) {
                        SelectableListItem sli = new SelectableListItem(anA.getLogin(), false);
                        sli.setBackground(Constants.FOREGROUND_COLOR);
                        l.add(sli);
                    }
                }
                workersPanel.setSelectedElements(l);
            }
        } catch (Exception e) {
            e.printStackTrace();
            showConnectionError();
        }
    }

    public void addSubtask() {
        try {
            if (Task.checkName(subtaskField.getText())) {
                if ((Boolean) sendMessage(new Packet(API.ADD_TASK_SUBTASK,
                        new Subtask(subtaskField.getText(), task.getName(), task.getProject()))).arguments[0]) {
                    showSuccess();
                    subtaskField.setText("");
                } else {
                    showError("Не удалось создать новую задачу в проекте");
                }
                updateSubtasks();
            } else {
                showError("Название задачи должно быть не меньше 5 символов");
            }
        } catch (Exception e) {
            e.printStackTrace();
            showConnectionError();
        }
    }

    @Override
    public synchronized void update() {
        super.update();
        updateTaskUsers();
        updateSubtasks();
        updateDescription();
    }
}
