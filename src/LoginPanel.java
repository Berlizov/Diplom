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
 * Created by Eugene Berlizov on 16.10.2014.
 */
class LoginPanel extends Panel {
    private boolean settingsOpened = false;
    private JTextField loginField;
    private JTextField passwordField;
    private JTextField ipField;
    private JTextField portField;

    public LoginPanel(SenderInterface parentSender, String login, String ip, int port) {
        super(parentSender);
        showPanel();
        loginField.setText(login);
        ipField.setText(ip);
        portField.setText(Integer.toString(port));
    }

    public void focusPassword() {
        passwordField.grabFocus();
    }

    void login() {
        String login = loginField.getText();
        String password = passwordField.getText();
        if (!User.checkLoginParameters(login, password)) {
            showError("Проверте логин и пароль.");
            return;
        }
        try {
            UsersTypes type = (UsersTypes) sendMessage(new Packet(API.LOGIN, login, password, ipField.getText(), Integer.parseInt(portField.getText()))).arguments[0];
            if (type == UsersTypes.NO) {
                showError("Логин и пароль неверны.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            showConnectionError();
        }

    }

    void openSettings() {
        JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(this);
        int t = frame.getHeight();
        int d = 90;
        if (settingsOpened)
            t -= d;
        else
            t += d;
        settingsOpened = !settingsOpened;

        ipField.setEnabled(settingsOpened);
        portField.setEnabled(settingsOpened);
        frame.setSize(frame.getWidth(), t);
    }


    void showPanel() {
        JLabel helloLabel = new JLabel();
        loginField = new JTextField();
        CButton loginButton = new CButton();
        CButton settingsButton = new CButton();
        passwordField = new JPasswordField();
        JLabel loginLabel = new JLabel();
        JLabel passLabel = new JLabel();
        JLabel ipLabel = new JLabel();
        ipField = new JTextField();
        JLabel portLabel = new JLabel();
        portField = new JTextField();

        helloLabel.setFont(new java.awt.Font("Tahoma", 0, 24));
        helloLabel.setForeground(Color.WHITE);
        helloLabel.setBackground(Constants.MAIN_COLOR);
        helloLabel.setOpaque(true);
        helloLabel.setHorizontalAlignment(SwingConstants.CENTER);
        helloLabel.setText("Добро пожаловать");
        loginButton.setText("Вход");
        loginButton.setBackgroundColor(Constants.MAIN_COLOR);
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                login();
            }
        });

        settingsButton.setText("Настройки");
        settingsButton.setBackgroundColor(Constants.MAIN_COLOR);
        settingsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                openSettings();
            }
        });

        loginLabel.setText("Логин");
        passLabel.setText("Пароль");
        ipLabel.setText("IP");
        ipField.setText("121.0.0.0");
        portLabel.setText("Port");
        portField.setText("1234");
        ipField.setEnabled(settingsOpened);
        portField.setEnabled(settingsOpened);

        GroupLayout layout = new GroupLayout(this);
        setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                                        .addComponent(helloLabel, GroupLayout.DEFAULT_SIZE, 249, Short.MAX_VALUE)
                                                        .addGroup(GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                                                .addContainerGap()
                                                                .addComponent(settingsButton)
                                                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                                .addComponent(loginButton)
                                                                .addContainerGap())
                                                        .addGroup(GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                                                        .addContainerGap()
                                                                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                                                                .addComponent(loginLabel, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                                                .addComponent(passLabel, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                                                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                                                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING, false)
                                                                                        .addComponent(loginField, GroupLayout.Alignment.TRAILING)
                                                                                        .addComponent(passwordField, GroupLayout.Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, 195, Short.MAX_VALUE)
                                                                        )
                                                                        .addContainerGap()
                                                        )
                                                        .addGroup(GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                                                        .addContainerGap()
                                                                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                                                                        .addComponent(ipLabel, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                                                        .addComponent(portLabel, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                                        )
                                                                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                                                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING, false)
                                                                                        .addComponent(ipField, GroupLayout.Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, 195, Short.MAX_VALUE)
                                                                                        .addComponent(portField, GroupLayout.Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, 195, Short.MAX_VALUE)
                                                                        )
                                                                        .addContainerGap()
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
                                        .addComponent(loginField, GroupLayout.PREFERRED_SIZE, 33, GroupLayout.PREFERRED_SIZE)
                                        .addComponent(loginLabel))
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                        .addComponent(passwordField, GroupLayout.PREFERRED_SIZE, 33, GroupLayout.PREFERRED_SIZE)
                                        .addComponent(passLabel))
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING, false)
                                        .addComponent(loginButton, GroupLayout.DEFAULT_SIZE, 35, Short.MAX_VALUE)
                                        .addComponent(settingsButton, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                        .addComponent(ipField, GroupLayout.PREFERRED_SIZE, 33, GroupLayout.PREFERRED_SIZE)
                                        .addComponent(ipLabel)).addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                        .addComponent(portField, GroupLayout.PREFERRED_SIZE, 33, GroupLayout.PREFERRED_SIZE)
                                        .addComponent(portLabel))
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
    }

}
