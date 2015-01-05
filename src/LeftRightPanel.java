import javax.swing.*;
import java.awt.*;

/**
 * Created by Eugene Berlizov on 12.11.2014.
 */
class LeftRightPanel extends Panel {
    protected final JPanel leftPanel;
    protected final JPanel rightPanel;

    public LeftRightPanel(SenderInterface parentSender, int w, Color leftColor) {
        super(parentSender);
        leftPanel = new JPanel();
        leftPanel.setBackground(leftColor);
        rightPanel = new JPanel();
        rightPanel.setBackground(Constants.FOREGROUND_COLOR);
        rightPanel.setLayout(new java.awt.CardLayout());
        JLabel tempLabel = new JLabel("Ничего не выбрано.");
        tempLabel.setForeground(Constants.MINOR_TEXT_COLOR);
        tempLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        rightPanel.add(tempLabel);
        GroupLayout layout = new GroupLayout(this);
        setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addComponent(leftPanel, GroupLayout.PREFERRED_SIZE, w, GroupLayout.PREFERRED_SIZE)
                                .addComponent(rightPanel, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addComponent(leftPanel, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE, Short.MAX_VALUE)
                        .addComponent(rightPanel, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
    }
    public void openPanel(Panel p) {
        rightPanel.removeAll();
        rightPanel.add(p);
        rightPanel.revalidate();
        setChildSender(p);
    }
    protected void updatePanels() {
        leftPanel.revalidate();
        leftPanel.repaint();
        rightPanel.revalidate();
        rightPanel.repaint();
    }
}
