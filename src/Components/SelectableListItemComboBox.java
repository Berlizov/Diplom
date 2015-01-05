package Components;

import javax.swing.*;
import java.awt.event.ActionListener;

/**
 * Created by Eugene Berlizov on 18.11.2014.
 */
public class SelectableListItemComboBox extends SelectableListItem{
    protected final JComboBox<String> combo=new JComboBox<>();
    public SelectableListItemComboBox(String text,String[] model, String selectedV, boolean showCheckBox) {
        this( text, model,showCheckBox);
        selectItem(selectedV);
    }
    private SelectableListItemComboBox(String text, String[] model, boolean showCheckBox) {
        super(text,showCheckBox);
        GroupLayout layout = new GroupLayout(this);
        setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(GroupLayout.Alignment.TRAILING)
                        .addGroup(GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                        .addContainerGap(0, Short.MAX_VALUE)
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
        );
        setModel(model);
    }
    public void setComboBoxEnabled(boolean b){
        combo.setEnabled(b);
    }
    public void selectItem(String item){
        combo.setSelectedItem(item);
    }
    public String getSelectedItem(){
        return (String)combo.getSelectedItem();
    }
    public void setModel(String[] model) {
        combo.setModel(new DefaultComboBoxModel<>(model));
    }
    public void addComboboxActionListener(ActionListener a) {
        combo.addActionListener(a);
    }

}
