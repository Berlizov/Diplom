import Components.CButton;
import Structs.API;
import Structs.Packet;
import Structs.User;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by Eugene Berlizov on 19.10.2014.
 */
interface SettingsDelegate extends SenderInterface{
    public void exit();
}
public class SettingsPanel extends Panel {
    private JPasswordField passField;
    public SettingsPanel(SettingsDelegate parentSender) {
        super(parentSender);
        showPanel();
    }

    private void showPanel() {
        JPanel panel = new JPanel();
        panel.setBackground(Color.WHITE);
        GroupLayout layout = new GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, 10, Short.MAX_VALUE)
                                .addComponent(panel, GroupLayout.PREFERRED_SIZE, 400, GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, 10, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addComponent(panel, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        JLabel helloLabel = new JLabel();
        CButton exitButton = new CButton();
        JLabel loginLabel = new JLabel();
        passField = new JPasswordField();
        CButton changePassButton = new CButton();


        helloLabel.setFont(new java.awt.Font("Tahoma", 0, 24));
        helloLabel.setForeground(Color.BLACK);
        helloLabel.setHorizontalAlignment(SwingConstants.CENTER);
        helloLabel.setText("Настройки");
        changePassButton.setText("Изменить пароль");
        changePassButton.setBackgroundColor(Constants.MAIN_COLOR);
        changePassButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                changePass();
            }
        });
        exitButton.setText("Сменить пользователя");
        exitButton.setBackgroundColor(Constants.MAIN_COLOR);
        exitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ((SettingsDelegate)getParentSender()).exit();
            }
        });
        layout = new GroupLayout(panel);
        panel.setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                                        .addComponent(helloLabel, GroupLayout.DEFAULT_SIZE, 249, Short.MAX_VALUE)
                                                        .addGroup(GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                                                                .addComponent(exitButton, GroupLayout.DEFAULT_SIZE, 500, Short.MAX_VALUE))
                                                        .addGroup(GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                                                .addComponent(changePassButton))
                                                        .addGroup(GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                                                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                                                                        .addComponent(loginLabel, GroupLayout.PREFERRED_SIZE, 100, Short.MAX_VALUE)
                                                                                        .addComponent(passField, GroupLayout.Alignment.TRAILING)
                                                                        )
                                                        )
                                        )
                        )
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addComponent(helloLabel, GroupLayout.PREFERRED_SIZE, 66, GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                        .addComponent(exitButton, GroupLayout.DEFAULT_SIZE, 35, Short.MAX_VALUE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                        .addComponent(passField, GroupLayout.PREFERRED_SIZE, 33, GroupLayout.PREFERRED_SIZE)
                                        .addComponent(loginLabel))
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING, false)
                                                .addComponent(changePassButton, GroupLayout.DEFAULT_SIZE, 35, Short.MAX_VALUE))
                                .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
    }
    private void changePass(){
        if(User.checkPass(String.valueOf(passField.getPassword()))) {
            try {
                if ((Boolean) sendMessage(new Packet(API.CHANGE_USER_PASS, String.valueOf(passField.getPassword()))).arguments[0]) {
                    showSuccess();
                } else {
                    showError("Неполучилось изменить пароль");
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                showConnectionError();
            }
        }else{
            showError("Пароль не должен быть меньше 5 символов.");
        }
    }
}
