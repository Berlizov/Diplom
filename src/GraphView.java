import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.util.ArrayList;
import java.util.Comparator;

/**
 * Created by Eugene Berlizov on 20.01.2015.
 */
interface Printer{
    public void print(String string);
}
class GraphView extends JComponent {
    private int maxY = 0;
    private int maxX = 0;
    private int limit = 0;
    private JSpinner spinner;
    private ArrayList<TasksSet> tasksSets;
    private ArrayList<TasksSet> extraTasksSets;
    private Printer printer;

    public GraphView() {
        super();
        setBackground(Color.white);
        setForeground(Color.black);
    }

    void setSpinner(final JSpinner spinner) {
        this.spinner = spinner;
        this.spinner.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                setLimit((Integer) spinner.getValue());
            }
        });
    }

    public void setPrinter(Printer printer) {
        this.printer = printer;
    }

    public void setMaxValue(int maxY, int maxX) {
        this.maxY = maxY;
        this.maxX = maxX;
        limit = maxY;
        this.extraTasksSets = null;
        this.tasksSets = null;
        if (spinner != null) {
            spinner.setModel(new SpinnerNumberModel(limit, 1, maxY, 1));
        }
     //   repaint();
    }

    void setLimit(int limit) {
        this.limit = limit;
        if (spinner != null) {
            spinner.setValue(limit);
        }
   //     repaint();
    }

    public void setTasksSets(ArrayList<TasksSet> tasksSets) {
        this.tasksSets = tasksSets;
        this.extraTasksSets = null;
    //    repaint();
    }

    public void setExtraTasksSets(ArrayList<TasksSet> extraTasksSets) {
        this.extraTasksSets = extraTasksSets;
    //    repaint();
    }

    public void paint(Graphics g) {
        long startTime = System.currentTimeMillis();
        double h = getHeight();
        double w = getWidth();
        Graphics2D g2d = (Graphics2D) g;

        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_LCD_HRGB);

        g2d.setColor(getForeground());
        String s = Integer.toString(maxY);

        int margin = g2d.getFontMetrics().stringWidth(s);
        if (margin < getFont().getSize())
            margin = getFont().getSize();
        s = Integer.toString(maxY);
        g2d.drawString(s, 0, (int)(h-margin));
        s = Integer.toString(maxX);
        int hF=g2d.getFontMetrics().stringWidth(s);
        g2d.drawString(s, (int) (w - hF), (int) h);
        s = "0";
        g2d.drawString(s, margin, (int) h);
        g2d.drawString(s, 0, getFont().getSize());
        s="Важность→";
        g2d.drawString(s, (int) (margin+(w-margin - g2d.getFontMetrics().stringWidth(s))/2), (int) h);
        s="←Сложность";
        g2d.rotate(-Math.PI / 2.0);

        g2d.drawString(s, -(int)(h-margin+g2d.getFontMetrics().stringWidth(s))/2,margin-2);

        g2d.rotate(Math.PI / 2.0);


        h -= margin;
        w -= margin;

        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_LCD_HRGB);
        g2d.setColor(getBackground());
        g2d.fillRect(margin, 0, (int) w, (int) h);
        g2d.setColor(getForeground());
        if (maxY != 0 && maxX != 0) {
            double mY = h / (double) maxY;
            double mX = w / (double) maxX;
            int size = (int) (Math.min(mY,mX) / 3);

            g2d.setColor(Color.red);
            if (extraTasksSets != null) {
                extraTasksSets.sort(new Comparator<TasksSet>() {
                    @Override
                    public int compare(TasksSet o1, TasksSet o2) {
                        return o1.getCost() - o2.getCost();
                    }
                });
                int x=0;
                int y=0;
                for (int i = 0; i < extraTasksSets.size(); i++) {
                    TasksSet set=extraTasksSets.get(i);
                    int x1 = (int) (margin + set.getV() * mX);
                    int y1 = (int) ((set.getCost()) * mY);
                    if(i!=0) {
                        g2d.setStroke(new BasicStroke(size));
                        g2d.drawLine(x, y, x1, y1);
                        g2d.setStroke(new BasicStroke(0));
                    }
                    g2d.fillOval(x1 - size, y1 - size, size * 2, size * 2);
                    x = x1;
                    y = y1;
                }
            }
            g2d.setColor(getForeground());
            if (tasksSets != null) {
                for (TasksSet set : tasksSets) {
                    g2d.drawOval((int) (margin + set.getV() * mX) - size, (int) ((set.getCost()) * mY) - size, size * 2, size * 2);
                }

            }
            g2d.setColor(Color.red);
            if (limit != maxY) {
                g2d.drawLine(0, (int) (limit * mY), getWidth(), (int) (limit * mY));
            }
        }
        long time=System.currentTimeMillis() - startTime;
        if(printer!=null&time>5) printer.print("Отрисовка: " + time + "ms\n");
    }
}