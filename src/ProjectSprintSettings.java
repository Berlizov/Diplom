import Components.CButton;
import Components.PlotView;

import javax.swing.*;
import javax.swing.border.Border;

import Structs.API;
import Structs.Packet;
import Structs.Sprint;
import org.jdatepicker.*;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.GregorianCalendar;

/**
 * Created by Eugene Berlizov on 26.11.2014.
 */
class ProjectSprintSettings extends Panel {
    private final PlotView pv;
    private final JDatePicker startdata;
    private final JDatePicker enddata;
    private final String project;

    public ProjectSprintSettings(String project, TaskPanelDelegate parentSender) {
        super(parentSender);
        this.project=project;
        JLabel newSprintLabel = new JLabel("Создание нового спринта");
        final JLabel startdataLabel = new JLabel("Начало");
        startdata = new JDateComponentFactory().createJDatePicker();
        JLabel enddataLabel = new JLabel("Конец");
        enddata = new JDateComponentFactory().createJDatePicker();
        CButton addSprintButton = new CButton();
        addSprintButton.setText("Добавить");
        addSprintButton.setBackgroundColor(Constants.MAIN_COLOR);
        addSprintButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    GregorianCalendar selectedStartDate =  (GregorianCalendar)startdata.getModel().getValue();
                    GregorianCalendar selectedEndDate =  (GregorianCalendar)enddata.getModel().getValue();
                    if(selectedStartDate.before(selectedEndDate)) {
                        showSuccess();
                        sendMessage(new Packet(API.ADD_SPRINT, new Sprint(selectedStartDate.getTime(),selectedEndDate.getTime())));
                    }
                    else {
                        showError("Начальная дата должна быть раньше, чем конечная дата.");
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                    showConnectionError();
                }
            }
        });
        JLabel sprintLabel = new JLabel("Список спринтов");
        JPanel sprintPanel = new JPanel();
        Border border = BorderFactory.createLineBorder(Constants.MINOR_TEXT_COLOR);
        sprintPanel.setBorder(border);
        sprintPanel.setBackground(Constants.FOREGROUND_COLOR);


        pv = new PlotView();
        pv.setMaxSprintValues(5);
        int[] t = new int[]{18, 30};
        pv.setValues(t);
        pv.setPoints(100);
        pv.setGraphColor(Constants.MAIN_COLOR);
        GroupLayout layout = new GroupLayout(this);
        setLayout(layout);
        JPanel panel = new JPanel();
        panel.setBackground(getBackground());
        JScrollPane menuS = new JScrollPane(panel);
        menuS.getVerticalScrollBar().setUnitIncrement(16);
        menuS.setBorder(null);
        menuS.setOpaque(false);
        menuS.getViewport().setOpaque(false);
        menuS.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);


        layout.setHorizontalGroup(
                layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                        .addComponent(menuS, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        ));
        layout.setVerticalGroup(
                layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addComponent(menuS, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout = new GroupLayout(panel);
        panel.setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                                .addComponent(newSprintLabel, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                .addGroup(layout.createSequentialGroup()

                                                                .addComponent(startdataLabel, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                                .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                                                                .addComponent((JComponent) startdata, GroupLayout.DEFAULT_SIZE, 40, Short.MAX_VALUE)
                                                                .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                                                                .addComponent(enddataLabel, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                                .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                                                                .addComponent((JComponent) enddata, GroupLayout.DEFAULT_SIZE, 40, Short.MAX_VALUE)

                                                )
                                                .addGroup(GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                                        .addComponent(addSprintButton))
                                                .addComponent(sprintLabel, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                .addComponent(sprintPanel, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                .addComponent(pv, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)

                                )
                                .addContainerGap())
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addComponent(newSprintLabel, GroupLayout.PREFERRED_SIZE, 27, GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING, false)
                                                .addComponent(startdataLabel, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE)
                                                .addComponent((JComponent) startdata, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE)
                                                .addComponent(enddataLabel, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE)
                                                .addComponent((JComponent) enddata, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE)
                                )
                                .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(addSprintButton, GroupLayout.PREFERRED_SIZE, 35, GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(sprintLabel, GroupLayout.PREFERRED_SIZE, 27, GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(sprintPanel, GroupLayout.PREFERRED_SIZE, 300, GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(pv, GroupLayout.PREFERRED_SIZE, 300, GroupLayout.PREFERRED_SIZE))
        );
    }

    @Override
    public synchronized void update() {
        super.update();
    }
}