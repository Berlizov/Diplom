import Components.CButton;
import Structs.API;
import Structs.Packet;
import Structs.User;
import Structs.UsersTypes;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

/**
 * Created by Eugene Berlizov on 08.11.2014.
 */
interface ProjectCreateDelegate extends SenderInterface {
    public void projectCreated(String projectName);
}

public class ProjectCreatePanel extends Panel {
    private final JTextField projectTextField = new JTextField();
    private final JComboBox<String> projectOwnerCombobox = new JComboBox<>();

    ProjectCreatePanel(ProjectCreateDelegate parentSender) {
        super(parentSender);
        try {
            User[] productOwner = sendMessage(new Packet(API.GET_USERS_BY_TYPES, UsersTypes.PRODUCT_OWNER)).getArrayOfArgs(User[].class);
            ArrayList<String> poNames = new ArrayList<>();

            if (productOwner != null) {
                for (User aProductOwner : productOwner) {
                    poNames.add(aProductOwner.getLogin());
                }
            }
            open(poNames);
        } catch (Exception e) {
            e.printStackTrace();
            showConnectionError();
        }
    }

    private void open(ArrayList<String> po) {
        JLabel viewName = new JLabel();

        CButton createButton = new CButton();
        JLabel projectNameLabel = new JLabel();
        JLabel productOwnerLabel = new JLabel();


        viewName.setFont(new java.awt.Font("Tahoma", 0, 24));
        viewName.setHorizontalAlignment(SwingConstants.CENTER);
        viewName.setText("Создание проекта");

        createButton.setText("Создать");
        createButton.setBackgroundColor(Constants.MAIN_COLOR);
        createButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                createProject();
            }
        });
        setBackground(Constants.FOREGROUND_COLOR);
        projectNameLabel.setText("Название");

        productOwnerLabel.setText("Product owner");
        po.add(0, "-");
        projectOwnerCombobox.setModel(new DefaultComboBoxModel<>(po.toArray(new String[po.size()])));

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
                                                        .addComponent(projectNameLabel, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                        .addComponent(productOwnerLabel, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING, false)
                                                        .addComponent(projectTextField, GroupLayout.DEFAULT_SIZE, 195, Short.MAX_VALUE)
                                                        .addComponent(projectOwnerCombobox, 0, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                                .addContainerGap())
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(viewName, GroupLayout.PREFERRED_SIZE, 56, GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                        .addComponent(projectTextField, GroupLayout.PREFERRED_SIZE, 33, GroupLayout.PREFERRED_SIZE)
                                        .addComponent(projectNameLabel))
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

    void createProject() {
        if (checkProjectParams()) {
            try {
                if ((Boolean) sendMessage(new Packet(API.ADD_PROJECTS, projectTextField.getText(), projectOwnerCombobox.getSelectedItem())).arguments[0]) {
                    ((ProjectCreateDelegate)getParentSender()).projectCreated(projectTextField.getText());
                } else
                    showError("Неудаеться создать проект с такими параметрами.");
            } catch (Exception e) {
                showConnectionError();
            }
        } else
            showError("Назавние проекта должно содаржать не менее 5 символов.");
    }
    boolean checkProjectParams(){
        return projectTextField.getText().length() >= 5;
    }
}


