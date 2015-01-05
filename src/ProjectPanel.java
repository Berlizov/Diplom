import Components.CButton;
import Structs.UsersTypes;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

/**
 * Created by Eugene Berlizov on 16.09.2014.
 */
class ProjectPanel extends LeftRightPanel implements TaskPanelDelegate {
    private final String projectName;
    private final ArrayList<CButton> leftButtons = new ArrayList<>();
    private final UsersTypes type;


    public ProjectPanel(UsersTypes type, String projectName, SenderInterface parentSender) {
        super(parentSender, 60, Constants.MAIN_COLOR2);
        this.projectName = projectName;
        this.type=type;


        leftPanel.setLayout(new javax.swing.BoxLayout(leftPanel, BoxLayout.Y_AXIS));
        if ((type == UsersTypes.PRODUCT_OWNER)||(type == UsersTypes.ADMIN)) {
            addLeftButton(new ImageIcon("img/Settings.png"), new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            selectButton((CButton) e.getSource());
                            openProjectSettings();
                        }
                    }
            );
        }
        if (type != UsersTypes.PRODUCT_OWNER) {
            addLeftButton(new ImageIcon("img/Tasks.png"), new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            selectButton((CButton) e.getSource());
                            openProjectTasksPanel();
                        }
                    }
            );
        }
       /* addLeftButton(new ImageIcon("img/Star.png"), new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        selectButton((CButton) e.getSource());
                    }
                }
        );*/
        addLeftButton(new ImageIcon("img/Stats.png"), new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        selectButton((CButton) e.getSource());
                        showSprintSettings();
                    }
                }
        );
        showDefButtons();
        leftButtons.get(0).doClick();
    }

    private void addLeftButton(ImageIcon icon, ActionListener al) {
        CButton button = new CButton();
        button.setIcon(icon);
        button.setMaximumSize(new Dimension(Integer.MAX_VALUE, 60));
        button.addActionListener(al);
        leftButtons.add(button);
    }

    private void  showDefButtons(){
        leftPanel.removeAll();
        for (CButton leftButton : leftButtons) {
            leftPanel.add(leftButton);
        }
        updatePanels();
    }

    private void selectButton(CButton b) {
        for (CButton leftButton : leftButtons) {
            leftButton.setSelected(false);
        }
        b.setSelected(true);
    }

    private void openProjectTasksPanel()
    {
        openPanel(new ProjectTasksPanel(type,projectName,this));
    }

    private void openProjectSettings()
    {
        openPanel(new ProjectSettingsPanel(type,projectName,this));
    }

    private void showSprintSettings(){
        openPanel(new ProjectSprintSettings(projectName,this));
    }

    @Override
    public void openTaskPanel(String task,final Panel p) {
        rightPanel.removeAll();
        TaskPanel taskPanel = new TaskPanel(projectName,task,type,this);
        taskPanel.setBackground(rightPanel.getBackground());

        CButton button = new CButton();
        button.setIcon(new ImageIcon("img/Back.png"));
        button.setMaximumSize(new Dimension(Integer.MAX_VALUE, 60));
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showDefButtons();
                openPanel(p);
            }
        });
        openPanel(taskPanel);
        leftPanel.removeAll();
        leftPanel.add(button);
        updatePanels();
    }
}
