package Components;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by Eugene Berlizov on 16.10.2014.
 */
public class NotifyPanel extends JPanel implements ActionListener {
    private final Timer clock = new Timer(5000, this);
    private final Timer clockA = new Timer(20, this);
    private JLabel errorLabel;
    private boolean open = false;
    private boolean close = false;

    public NotifyPanel() {
        setBounds(0, 0, 280, 60);
        showPanel();
    }

    public void setX(int w) {
        setBounds((w - getWidth()) / 2, getY(), getWidth(), getHeight());
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == clock)
            close();
        else {
            int speed = 4;
            int h = getHeight();
            int w = getWidth();
            int x = getX();
            int y = getY();
            if (close) {
                if (y >= -h) {
                    clockA.restart();
                } else {
                    setVisible(false);
                }
                setBounds(x, y - speed, w, h);
                invalidate();
            }
            if (open) {
                if (y < 0) {
                    clockA.restart();
                }
                int t = speed;
                if (y + t > 0)
                    t = -y;
                setBounds(x, y + t, w, h);
                invalidate();
            }
        }

    }

    void showPanel() {
        CButton closeButton = new CButton();
        errorLabel = new JLabel();
        setBackground(new Color(0, 0, 0));
        errorLabel.setForeground(new Color(255, 255, 255));
        closeButton.setIcon(new ImageIcon("img/Close.png"));
        closeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                close();
            }
        });
        GroupLayout layout = new GroupLayout(this);
        setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(errorLabel, GroupLayout.DEFAULT_SIZE, 300, Short.MAX_VALUE)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(closeButton, GroupLayout.PREFERRED_SIZE, 60, GroupLayout.PREFERRED_SIZE))
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addComponent(closeButton, GroupLayout.PREFERRED_SIZE, 60, GroupLayout.PREFERRED_SIZE)
                        .addComponent(errorLabel, GroupLayout.DEFAULT_SIZE, 300, Short.MAX_VALUE)
        );
    }

    public void setText(String error) {
        errorLabel.setText("<html>" + error + "</html>");
    }


    void close() {
        open = false;
        close = true;
        clockA.restart();
    }

    public void open() {
        open = true;
        close = false;
        setBounds(getX(), -getHeight(), getWidth(), getHeight());
        setVisible(true);
        clockA.restart();
        clock.restart();
    }


}
