package Components;

import javax.swing.*;
import java.awt.*;

/**
 * Created by Eugene Berlizov on 19.11.2014.
 */
public class SelectableListItemLabelComboBox extends SelectableListItemComboBox{
    private final JLabel label=new JLabel();
    public SelectableListItemLabelComboBox(String text, String secondText, String[] model, String selectedV, boolean showCheckBox) {
        super(text, model, selectedV, showCheckBox);
        GroupLayout layout = new GroupLayout(this);
        label.setText(secondText);
        label.setHorizontalAlignment(SwingConstants.RIGHT);
        setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(GroupLayout.Alignment.TRAILING)
                        .addGroup(GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                        .addContainerGap(150, Short.MAX_VALUE)
                                        .addComponent(label, GroupLayout.PREFERRED_SIZE, 0, 100)
                                        .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(combo, GroupLayout.PREFERRED_SIZE, 50, 200)
                                        .addContainerGap()
                        )
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addContainerGap(0, Short.MAX_VALUE)
                                .addComponent(combo, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                .addContainerGap(0, Short.MAX_VALUE))
                        .addGroup(layout.createSequentialGroup()
                                .addContainerGap(0, Short.MAX_VALUE)
                                .addComponent(label, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                .addContainerGap(0, Short.MAX_VALUE))
        );
    }
    public void setSecondLabelVisible(Boolean t)
    {
        label.setVisible(t);
    }
    public void setSecondLabelColor(Color c){
        label.setForeground(c);
    }
}
