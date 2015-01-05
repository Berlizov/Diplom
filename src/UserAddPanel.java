import Components.CButton;
import Structs.API;
import Structs.Packet;
import Structs.User;
import Structs.UsersTypes;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by  Eugene Berlizov on 18.10.2014.
 */
interface UserAddDelegate extends SenderInterface {
    public void userAdded();
}

public class UserAddPanel extends Panel {

    private JTextField loginField;
    private JTextField passField;
    private JComboBox<UsersTypes> typeField;

    public UserAddPanel(UserAddDelegate parentSender) {
        super(parentSender);
        showPanel();
    }

    void showPanel() {
        JLabel CreateHello = new JLabel();
        loginField = new JTextField();
        passField = new JTextField();
        typeField = new JComboBox<>();
        CButton createButton = new CButton();
        JLabel loginLabel = new JLabel();
        JLabel passLabel = new JLabel();
        JLabel typeLabel = new JLabel();
        typeField.setModel(new DefaultComboBoxModel<>(UsersTypes.values()));
        CreateHello.setFont(new Font("Tahoma", 0, 24));
        CreateHello.setHorizontalAlignment(SwingConstants.CENTER);
        CreateHello.setText("Создание пользователя");

        createButton.setText("Создать");
        createButton.setBackgroundColor(Constants.MAIN_COLOR);
        createButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                addUser();
            }
        });

        loginLabel.setText("Login");
        passLabel.setText("Пароль");
        typeLabel.setText("Тип");

        GroupLayout layout = new GroupLayout(this);
        setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                        .addComponent(CreateHello, GroupLayout.DEFAULT_SIZE, 249, Short.MAX_VALUE)
                                        .addGroup(GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                .addComponent(createButton))
                                        .addGroup(GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                                                .addComponent(loginLabel, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                                .addComponent(passLabel, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                                .addComponent(typeLabel, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                )
                                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING, false)
                                                                .addComponent(loginField, GroupLayout.DEFAULT_SIZE, 195, Short.MAX_VALUE)
                                                                .addComponent(passField, GroupLayout.DEFAULT_SIZE, 195, Short.MAX_VALUE)
                                                                .addComponent(typeField, GroupLayout.DEFAULT_SIZE, 195, Short.MAX_VALUE)
                                                )))
                                .addContainerGap())
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(CreateHello, GroupLayout.PREFERRED_SIZE, 56, GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                        .addComponent(loginField, GroupLayout.PREFERRED_SIZE, 33, GroupLayout.PREFERRED_SIZE)
                                        .addComponent(loginLabel))
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                        .addComponent(passLabel)
                                        .addComponent(passField, GroupLayout.PREFERRED_SIZE, 33, GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                        .addComponent(typeLabel)
                                        .addComponent(typeField, GroupLayout.PREFERRED_SIZE, 33, GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING, false)
                                        .addComponent(createButton, GroupLayout.DEFAULT_SIZE, 35, Short.MAX_VALUE))
                                .addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
    }

    private void addUser() {
        if (User.checkLoginParameters(loginField.getText(), passField.getText())) {
            try {
                if ((Boolean) sendMessage(new Packet(API.ADD_USER,
                        new User(loginField.getText(),
                                passField.getText(),
                                (UsersTypes) typeField.getSelectedItem()))).arguments[0]) {
                    ((UserAddDelegate)getParentSender()).userAdded();
                } else {
                    showError("Не удалось создать пользователя с такими параметрами.");
                }
            } catch (Exception e) {
                e.printStackTrace();
                showConnectionError();
            }
        } else {
            showError("Введите данные пользователя в поля.");
        }

    }
}
