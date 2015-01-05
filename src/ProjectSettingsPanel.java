import Components.*;
import Structs.*;

import javax.swing.*;
import javax.swing.border.Border;
import javax.xml.bind.JAXBException;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by Eugene Berlizov on 15.11.2014.
 */
class ProjectSettingsPanel extends Panel {
    private final JComboBox<String> projectOwnerCombobox = new JComboBox<>();
    private final String project;
    private final UsersTypes type;
    private String nameOfSelectedPO = null;
    private Selector workersPanel;
    private JPanel tasksPanel;
    private JTextField taskField;

    public ProjectSettingsPanel(UsersTypes type, String project, TaskPanelDelegate parentSender) {
        super(parentSender);
        this.project = project;
        this.type = type;
        showPanelByType();
    }

    private synchronized void showPanelByType() {
        removeAll();
        try {
            if (type == UsersTypes.ADMIN)
                openAdmin();
            if (type == UsersTypes.PRODUCT_OWNER)
                openProductOwner();
        } catch (Exception e) {
            e.printStackTrace();
            showConnectionError();
        }
    }

    private void changeProject() {
        nameOfSelectedPO = null;
        try {
            if ((Boolean) sendMessage(new Packet(API.CHANGE_PROJECT_PRODUCT_OWNER, project, projectOwnerCombobox.getSelectedItem())).arguments[0]) {
                showSuccess();
            } else {
                showError("Неудаеться создать проект с такими параметрами.");
            }
        } catch (Exception e) {
            showConnectionError();
        }
    }

    private void openAdmin() throws IOException, JAXBException {
        JLabel viewName = new JLabel();

        CButton createButton = new CButton();
        JLabel productOwnerLabel = new JLabel();


        viewName.setFont(new java.awt.Font("Tahoma", 0, 24));
        viewName.setHorizontalAlignment(SwingConstants.CENTER);
        viewName.setText(project);

        createButton.setText("Изменить");
        createButton.setBackgroundColor(Constants.MAIN_COLOR);
        createButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                changeProject();
            }
        });
        setBackground(Constants.FOREGROUND_COLOR);


        productOwnerLabel.setText("Product owner");
        User[] productOwners = sendMessage(new Packet(API.GET_USERS_BY_TYPES, UsersTypes.PRODUCT_OWNER)).getArrayOfArgs(User[].class);
        String productOwner = (String) sendMessage(new Packet(API.GET_PROJECT_PRODUCT_OWNER, project)).arguments[0];
        ArrayList<String> po = new ArrayList<>();
        if (productOwners != null) {
            for (User aProductOwner : productOwners) {
                po.add(aProductOwner.getLogin());
            }
        }
        po.add(0, "-");
        projectOwnerCombobox.setModel(new DefaultComboBoxModel<>(po.toArray(new String[po.size()])));
        if (nameOfSelectedPO != null) {
            projectOwnerCombobox.setSelectedItem(nameOfSelectedPO);
        } else {
            projectOwnerCombobox.setSelectedItem(productOwner);
        }
        projectOwnerCombobox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                nameOfSelectedPO = (String) projectOwnerCombobox.getSelectedItem();
            }
        });
        JPanel panel = new JPanel();
        panel.setBackground(getBackground());
        GroupLayout layout = new GroupLayout(this);
        setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 10, Short.MAX_VALUE)
                                .addComponent(panel, javax.swing.GroupLayout.PREFERRED_SIZE, 400, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 10, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(panel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        layout = new GroupLayout(panel);
        panel.setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                        .addComponent(viewName, GroupLayout.DEFAULT_SIZE, 249, Short.MAX_VALUE)
                                        .addGroup(GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                .addComponent(createButton))
                                        .addGroup(GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                                        .addComponent(productOwnerLabel, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING, false)
                                                        .addComponent(projectOwnerCombobox, 0, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                                .addContainerGap())
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(viewName, GroupLayout.PREFERRED_SIZE, 56, GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                        .addComponent(productOwnerLabel)
                                        .addComponent(projectOwnerCombobox, GroupLayout.PREFERRED_SIZE, 28, GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING, false)
                                        .addComponent(createButton, GroupLayout.DEFAULT_SIZE, 35, Short.MAX_VALUE))
                                .addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
    }

    private void openProductOwner() {
        JLabel label = new JLabel();
        JLabel workersLabel = new JLabel();
        workersPanel = new Selector("Пользователи проекта","Пользователи системы");
        CButton saveButton = new CButton();
        JLabel tasksLabel = new JLabel();
        tasksPanel = new JPanel();
        taskField = new JTextField();
        CButton addTaskButton = new CButton();


        label.setFont(new Font("Tahoma", 0, 24));
        label.setHorizontalAlignment(SwingConstants.CENTER);
        label.setText(project);

        Border border = BorderFactory.createLineBorder(Constants.MINOR_TEXT_COLOR);
        workersLabel.setText("Работники");
        workersPanel.setSelectColor(Constants.MAIN_COLOR2);
        workersPanel.setBackground(Constants.FOREGROUND_COLOR);
        workersPanel.setBordersOfView(border);
        saveButton.setText("Сохранить");
        saveButton.setBackgroundColor(Constants.MAIN_COLOR);
        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                saveProjectUsers();
            }
        });

        tasksLabel.setText("Задачи");
        tasksPanel.setBorder(border);
        tasksPanel.setBackground(Constants.FOREGROUND_COLOR);


        JPanel p = new JPanel();
        JScrollPane menuS = new JScrollPane(p);
        menuS.getVerticalScrollBar().setUnitIncrement(16);
        menuS.setBorder(null);
        menuS.setOpaque(false);
        menuS.getViewport().setOpaque(false);
        p.setBackground(Constants.FOREGROUND_COLOR);
        menuS.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);


        JScrollPane tasksPanelS = new JScrollPane(tasksPanel);
        tasksPanelS.getVerticalScrollBar().setUnitIncrement(16);
        tasksPanelS.setBorder(null);
        tasksPanelS.setOpaque(false);
        tasksPanelS.getViewport().setOpaque(false);
        tasksPanelS.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        tasksPanel.setLayout(new BoxLayout(tasksPanel, BoxLayout.PAGE_AXIS));

        addTaskButton.setText("Добавить");
        addTaskButton.setBackgroundColor(Constants.MAIN_COLOR);
        addTaskButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addTask();
            }
        });
        GroupLayout layout = new GroupLayout(this);
        this.setLayout(layout);
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
                                        .addComponent(workersLabel, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(workersPanel, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addGroup(GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                                .addComponent(saveButton))
                                        .addComponent(tasksLabel, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(label, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addGroup(GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                                .addComponent(taskField, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                                                .addComponent(addTaskButton))
                                        .addComponent(tasksPanelS, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addContainerGap())
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(label, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(workersLabel, GroupLayout.PREFERRED_SIZE, 27, GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(workersPanel, GroupLayout.PREFERRED_SIZE, 300, GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(saveButton, GroupLayout.PREFERRED_SIZE, 35, GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(tasksLabel, GroupLayout.PREFERRED_SIZE, 27, GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING, false)
                                        .addComponent(taskField, GroupLayout.DEFAULT_SIZE, 35, Short.MAX_VALUE)
                                        .addComponent(addTaskButton, GroupLayout.DEFAULT_SIZE, 35, Short.MAX_VALUE))
                                .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(tasksPanelS, GroupLayout.PREFERRED_SIZE, 300, GroupLayout.PREFERRED_SIZE)
                                .addContainerGap())
        );

        updateTasks();
        updateUsers();
    }

    private void updateUsers() {
        try {
            User[] a = sendMessage(new Packet(API.GET_USERS_BY_TYPES, UsersTypes.DEVELOPER)).getArrayOfArgs(User[].class);
            ArrayList<SelectableListItem> l = new ArrayList<>();
            if (a != null) {
                for (User anA : a) {
                    SelectableListItem sli = new SelectableListItem(anA.getLogin(), false);
                    sli.setBackground(new Color(255, 255, 0, 20));
                    l.add(sli);
                }
            }
            a = sendMessage(new Packet(API.GET_USERS_BY_TYPES, UsersTypes.STAKEHOLDERS)).getArrayOfArgs(User[].class);
            if (a != null) {
                for (User anA : a) {
                    SelectableListItem sli = new SelectableListItem(anA.getLogin(), false);
                    sli.setBackground(new Color(0, 255, 0, 20));
                    l.add(sli);
                }
            }
            workersPanel.setSelectableElements(l);
            a = sendMessage(new Packet(API.GET_PROJECT_USERS, project)).getArrayOfArgs(User[].class);
            l = new ArrayList<>();
            if (a != null) {
                for (User anA : a) {
                    SelectableListItem sli = new SelectableListItem(anA.getLogin(), false);
                    if (anA.getType() == UsersTypes.DEVELOPER)
                        sli.setBackground(new Color(255, 255, 0, 20));
                    else
                        sli.setBackground(new Color(0, 255, 0, 20));
                    l.add(sli);
                }
            }
            workersPanel.setSelectedElements(l);
        } catch (Exception e) {
            e.printStackTrace();
            showConnectionError();
        }
    }

    private void updateTasks() {
        tasksPanel.removeAll();
        try {
            Task[] tasks = sendMessage(new Packet(API.GET_PROJECT_TASKS, project)).getArrayOfArgs(Task[].class);
            if (tasks != null) {
                for (Task task : tasks) {
                    final SelectableListItemLabelComboBox l = new SelectableListItemLabelComboBox(task.getName(),task.getUserComplexity().toString(), PokerCardDeck.getModel(),task.getComplexity().toString(), false);
                    if((task.getComplexity()!=PokerCardDeck.NOTSET)||(task.getUserComplexity()==PokerCardDeck.NOTSET))
                    {
                        l.setSecondLabelVisible(false);

                    }
                    l.setSelectable(false);
                    l.setSecondLabelColor(Constants.MINOR_TEXT_COLOR);
                    l.setMaximumSize(new Dimension(Integer.MAX_VALUE, 60));
                    l.setMinimumSize(new Dimension(Integer.MIN_VALUE, 60));
                    l.setSelectColor(Constants.MAIN_COLOR2);
                    l.setBackground(Constants.FOREGROUND_COLOR);
                    l.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            openTaskPanel(l.getText());
                        }
                    });
                    l.addComboboxActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            setComplexityTask(new Task(l.getText(), project, PokerCardDeck.valueOf(l.getSelectedItem()), null));
                        }
                    });
                    tasksPanel.add(l);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            showConnectionError();
        }
        updatePanels();
    }

    private void setComplexityTask(Task task){
        try {
            if((Boolean)sendMessage(new Packet(API.SET_PROJECT_TASK_COMPLEXITY, task)).arguments[0])
                showSuccess();
            else
                showError("Неудалось установить сложность задачи.");
        }catch (Exception e){
            showConnectionError();
        }
    }

    private void updatePanels() {
        this.revalidate();
        this.repaint();
    }

    private void saveProjectUsers() {
        try {
            ArrayList<SelectableListItem> sli = workersPanel.getSelectedItems();
            String[] str = new String[sli.size() + 1];
            str[0] = project;
            for (int i = 1; i < str.length; i++) {
                str[i] = sli.get(i - 1).getText();
            }
            Packet p = new Packet(API.CHANGE_PROJECT_USERS, str);
            p.setArguments(str);
            if ((Boolean) sendMessage(p).arguments[0]) {
                showSuccess();
            } else {
                showError("Не удалось обновить пользователей проекта");
            }
            updateUsers();
        } catch (Exception e) {
            e.printStackTrace();
            showConnectionError();
        }
    }

    private void addTask() {
        try {
            if (Task.checkName(taskField.getText())) {
                if ((Boolean) sendMessage(new Packet(API.ADD_PROJECT_TASK, new Task(taskField.getText(), project))).arguments[0]) {
                    showSuccess();
                    taskField.setText("");
                } else {
                    showError("Не удалось создать новую задачу в проекте");
                }
                updateTasks();
            } else {
                showError("Название задачи должно быть не меньше 5 символов");
            }
        } catch (Exception e) {
            e.printStackTrace();
            showConnectionError();
        }
    }

    private void openTaskPanel(String taskName){
        ((TaskPanelDelegate)getParentSender()).openTaskPanel(taskName,this);
    }

    @Override
    public synchronized void update() {
        super.update();
        showPanelByType();
    }

}
