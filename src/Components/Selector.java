package Components;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

/**
 * Created by Eugene Berlizov on 17.11.2014.
 */
public class Selector extends JPanel {
    private final CButton toLeftButton = new CButton();
    private final CButton toRightButton = new CButton();
    private final JPanel leftPanel = new JPanel();
    private final JPanel rightPanel = new JPanel();
    private final JScrollPane leftScrollPanel = setPanelScrollable(leftPanel);
    private final JScrollPane rightScrollPanel = setPanelScrollable(rightPanel);
    private Color selectedColor = Color.BLACK;
    private ArrayList<SelectableListItem> selectedItems = new ArrayList<>();
    private ArrayList<SelectableListItem> notSelectedItems = new ArrayList<>();

    public Selector(String selectedCol,String selectCol) {
        JLabel selectedColLabel=new JLabel(selectedCol);
        JLabel selectColLabel=new JLabel(selectCol);
        selectColLabel.setHorizontalAlignment(SwingConstants.CENTER);
        selectedColLabel.setHorizontalAlignment(SwingConstants.CENTER);
        JPanel panel = new JPanel();
        leftPanel.setBackground(Color.WHITE);
        panel.setBackground(Color.WHITE);
        rightPanel.setBackground(Color.WHITE);
        toLeftButton.setText("<<");
        toLeftButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                move(notSelectedItems, selectedItems);
            }
        });
        toRightButton.setText(">>");
        toRightButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                move(selectedItems, notSelectedItems);
            }
        });
        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(panel);
        panel.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
                jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel2Layout.createParallelGroup()
                                .addComponent(toLeftButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(toRightButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
                jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel2Layout.createSequentialGroup()
                                .addContainerGap(5, Short.MAX_VALUE)
                                .addComponent(toLeftButton, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(toRightButton, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addContainerGap(5, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                        .addComponent(selectedColLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(leftScrollPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        )
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(panel, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addComponent(selectColLabel, GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(rightScrollPanel, GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        )
                        )
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                        .addComponent(selectedColLabel, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE)
                                        .addComponent(selectColLabel, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE))
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(leftScrollPanel, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(panel, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(rightScrollPanel, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
        );
    }

    private JScrollPane setPanelScrollable(JPanel panel) {
        JScrollPane panelS = new JScrollPane(panel);
        panelS.getVerticalScrollBar().setUnitIncrement(16);
        panelS.setBorder(null);
        panelS.setOpaque(false);
        panelS.getViewport().setOpaque(false);
        panelS.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        panel.setLayout(new BoxLayout(panel, BoxLayout.PAGE_AXIS));
        return panelS;
    }

    public void setSelectColor(Color color) {
        selectedColor = color;
        toLeftButton.setBackgroundColor(selectedColor);
        toRightButton.setBackgroundColor(selectedColor);
        //todo
    }

    public void setBordersOfView(Border border) {
        leftScrollPanel.setBorder(border);
        rightScrollPanel.setBorder(border);
    }

    public void setSelectableElements(ArrayList<SelectableListItem> items) {
        notSelectedItems = setParams(items);
        redistribute(notSelectedItems, selectedItems);
    }

    public void setSelectedElements(ArrayList<SelectableListItem> items) {
        selectedItems = setParams(items);
        redistribute(selectedItems, notSelectedItems);
    }

    private ArrayList<SelectableListItem> setParams(ArrayList<SelectableListItem> items) {
        for (SelectableListItem sli : items) {
            sli.setMaximumSize(new Dimension(Integer.MAX_VALUE, 60));
            sli.setMinimumSize(new Dimension(Integer.MIN_VALUE, 60));
            sli.setSelectColor(selectedColor);
        }
        return items;
    }

    private void redistribute(ArrayList<SelectableListItem> Fitems,
                              ArrayList<SelectableListItem> Sitems) {
        for (SelectableListItem item : Fitems) {
            for (int i = 0; i < Sitems.size(); i++) {
                if (item.getText().equals(Sitems.get(i).getText()))
                    Sitems.remove(i);
            }
        }
        update();
    }


    void update() {
        leftPanel.removeAll();
        rightPanel.removeAll();
        for (SelectableListItem sli : notSelectedItems) {
            if (sli != null) {
                rightPanel.add(sli);
            }
        }
        for (SelectableListItem sli : selectedItems) {
            if (sli != null) {
                leftPanel.add(sli);
            }
        }
        revalidate();
        repaint();
    }

    private void move(ArrayList<SelectableListItem> from,
                      ArrayList<SelectableListItem> to) {
        for (int i = 0; i < from.size(); i++) {
            if (from.get(i).isSelected()) {
                move(from.get(i), from, to);
                i--;//не трогать
            }
        }
        update();
    }

    private void move(SelectableListItem sli,
                      ArrayList<SelectableListItem> from,
                      ArrayList<SelectableListItem> to) {
        sli.setSelected(false);
        from.remove(sli);
        to.add(sli);
    }

    public ArrayList<SelectableListItem> getSelectedItems() {
        return selectedItems;
    }
}
