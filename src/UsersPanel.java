import Components.CButton;
import Components.SelectableListItemComboBox;
import Structs.API;
import Structs.Packet;
import Structs.User;
import Structs.UsersTypes;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

/**
 * Created by  Eugene Berlizov on 18.10.2014.
 */
class UsersPanel extends LeftRightPanel implements UserAddDelegate {
    private final ArrayList<SelectableListItemComboBox> uli = new ArrayList<>();
    private final JPanel userList;

    public UsersPanel(SenderInterface parentSender) {
        super(parentSender, 60, Constants.MAIN_COLOR2);
        userList = new JPanel();
        userList.setBackground(Constants.FOREGROUND_COLOR);
        updatePanels();
    }

    public void openUsersMain() {
        setChildSender(null);
        clearPanels();
        leftPanel.setLayout(new javax.swing.BoxLayout(leftPanel, BoxLayout.Y_AXIS));
        CButton addButton = new CButton();
        addButton.setIcon(new ImageIcon("img/Add.png"));
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                openUserAddPanel();
            }
        });
        addButton.setMaximumSize(new Dimension(Integer.MAX_VALUE, 60));
        leftPanel.add(addButton);
        CButton delButton = new CButton();
        delButton.setIcon(new ImageIcon("img/Del.png"));
        delButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deleteUsers();
            }
        });
        delButton.setMaximumSize(new Dimension(Integer.MAX_VALUE, 60));
        leftPanel.add(delButton);

        rightPanel.setLayout(new java.awt.GridLayout());
        JScrollPane menuS = new JScrollPane(userList);
        menuS.getVerticalScrollBar().setUnitIncrement(16);
        menuS.setBorder(null);
        menuS.setOpaque(false);
        menuS.getViewport().setOpaque(false);
        menuS.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        userList.setLayout(new BoxLayout(userList, BoxLayout.PAGE_AXIS));
        rightPanel.add(menuS);
        updateUsers();
    }

    public void deleteUsers() {
        if(uli.size()>0) {
            for (SelectableListItemComboBox anUli : uli) {
                changeType(anUli.getText(), UsersTypes.NO);//TODO
            }
            updateUsers();
        }
        else{
            showError("Нужно выделить пользователей, которых вы хотите удалить");
        }
    }

    public synchronized void updateUsers() {
            uli.clear();
            userList.removeAll();
            try {
                User[] a = sendMessage(new Packet(API.GET_ALL_USERS_AND_TYPES)).getArrayOfArgs(User[].class);
                for (User anA : a) {
                    final SelectableListItemComboBox l = new SelectableListItemComboBox(anA.getLogin(),UsersTypes.getModel(), anA.getType().toString(),true);
                    l.setMaximumSize(new Dimension(Integer.MAX_VALUE, 60));
                    l.setMinimumSize(new Dimension(Integer.MIN_VALUE, 60));
                    l.setBackground(Constants.FOREGROUND_COLOR);
                    l.setSelectColor(Constants.MAIN_COLOR);
                    l.addComboboxActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            changeType(l.getText(), UsersTypes.valueOf(l.getSelectedItem()));
                        }
                    });
                    l.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            if (l.isSelected())
                                uli.add(l);
                            else
                                uli.remove(l);
                        }
                    });
                    userList.add(l);
                }
            } catch (Exception e) {
                showConnectionError();
            }
            updatePanels();
    }

    private void changeType(String login, UsersTypes type) {
        try {
            sendMessage(new Packet(API.CHANGE_USER_TYPE, new User(login, "", type)));
            showSuccess();
        } catch (Exception e) {
            showConnectionError();
        }
    }

    private void clearPanels() {
        leftPanel.removeAll();
        rightPanel.removeAll();
    }

    private void openUserAddPanel() {
        clearPanels();
        UserAddPanel uap = new UserAddPanel(this);
        uap.setBackground(rightPanel.getBackground());
        GroupLayout layout = new GroupLayout(rightPanel);
        rightPanel.setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, 10, Short.MAX_VALUE)
                                .addComponent(uap, GroupLayout.PREFERRED_SIZE, 400, GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, 10, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addComponent(uap, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        CButton backButton = new CButton();
        backButton.setIcon(new ImageIcon("img/Back.png"));
        backButton.setMinimumSize(new Dimension(60, 200));
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                openUsersMain();
            }
        });
        leftPanel.setLayout(new javax.swing.BoxLayout(leftPanel, BoxLayout.Y_AXIS));
        leftPanel.add(backButton);
        updatePanels();
        setChildSender(uap);
    }
    @Override
    public void userAdded() {
        openUsersMain();
    }

    @Override
    public synchronized void update() {
        super.update();
        updateUsers();
    }
}
