import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.GeneralPath;
import java.util.ArrayList;

interface DrawingObject {
    public Point getPosition();

    public void onMouseEntered();

    public void onMouseExited();

    public void onMouseClicked(Point point);

    public void movedTo(Point point);
}

interface ButtonDelegate {
    public void repaint();

    public void buttonClicked(Button button, Point point);

    public void buttonDragged(Button button, Point point);
}

/**
 * Created by Eugene Berlizov on 04.02.2015.
 */
interface OrderViewDelegate {
    public void orderChanged();
}

class OrderView extends JComponent implements MouseMotionListener, MouseListener {
    private ArrayList<TaskComponent> tasks = new ArrayList<TaskComponent>();
    private ArrayList<TasksRelation> tasksRelations = new ArrayList<TasksRelation>();
    private final ArrayList<OrderViewDelegate> delegate = new ArrayList<OrderViewDelegate>();
    private DrawingObject last = null;
    private DrawingObject pressed = null;
    private Point pressedPoint;

    public OrderView() {
        addMouseMotionListener(this);
        addMouseListener(this);
    }

    public void addDelegate(OrderViewDelegate delegate) {
        this.delegate.add(delegate);
    }

    public void clear() {
        tasks = new ArrayList<TaskComponent>();
        tasksRelations = new ArrayList<TasksRelation>();
    }

    public void generateRelations() {
        for (OrderViewDelegate o : delegate) {
            o.orderChanged();
        }
        tasksRelations.clear();
        tasksRelations = new ArrayList<TasksRelation>();
        for (TaskComponent taskC2 : tasks) {
            ArrayList<Task> parents = taskC2.getTask().getParents();
            for (TaskComponent taskC1 : tasks) {
                if (parents.contains(taskC1.getTask())) {
                    tasksRelations.add(new TasksRelation(taskC1, taskC2, this));
                }
            }
        }
    }

    void autoLayout() {
        int tempX = 0;
        for (TaskComponent task : tasks)
            task.movedTo(new Point(0, 0));
        repaint();
        for (TaskComponent task : tasks) {
            if (task.getTask().getParents().size() == 0) {
                tempX += autoLayout(task, 0, tempX) + 1;
            }
        }
    }

    private TaskComponent findTaskComponentByTask(Task task) {
        for (TaskComponent taskC : tasks) {
            if (taskC.getTask() == task)
                return taskC;
        }
        return null;
    }

    private int autoLayout(TaskComponent tc, int i, int tempX) {
        int temp = 0;
        TaskComponent tempC;
        int count = 0;
        int xStep = 100;
        for (Task task : tc.getTask().getChilds()) {
            tempC = findTaskComponentByTask(task);
            if (tempC.getPosition().getY() < (i + 1) * xStep) {
                if (count > 0)
                    temp++;
                temp += autoLayout(tempC, i + 1, tempX + temp);
                count++;
            }

        }
        int yStep = 50;
        int margin = 10;
        tc.movedTo(new Point((int) ((tempX + temp * 0.5f) * yStep - (tc.w * 0.5f)) + margin, i * xStep + margin));
        return temp;
    }

    public void addTask(Task task) {
        tasks.add(new TaskComponent(task, this));

    }


    public void paint(Graphics g) {
        double h = getHeight();
        double w = getWidth();
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setColor(getBackground());
        g2d.fillRect(0, 0, (int) w, (int) h);
        for (TaskComponent task : tasks) {
            task.calcNextPosition(g2d);
        }
        for (TasksRelation tr : tasksRelations) {
            tr.draw(g2d);
        }
        for (TaskComponent task : tasks) {
            task.draw(g2d);
        }
    }

    public DrawingObject getTaskComponentUnder(Point point) {
        for (int i = tasks.size() - 1; i >= 0; i--) {
            if (tasks.get(i).has(point)) {
                return tasks.get(i);
            } else {
                if (tasks.get(i).getBotButton().has(point)) {
                    return tasks.get(i).getBotButton();
                }
                if (tasks.get(i).getTopButton().has(point)) {
                    return tasks.get(i).getTopButton();
                }
            }
        }
        for (int i = tasksRelations.size() - 1; i >= 0; i--) {
            if (tasksRelations.get(i).getCloseButton().has(point)) {
                return tasksRelations.get(i).getCloseButton();
            }
        }
        return null;
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        DrawingObject obj = getTaskComponentUnder(e.getPoint());
        if (obj != null) {
            obj.onMouseEntered();
        }
        if (last != null && last != obj) {
            last.onMouseExited();
            last = null;
        }
        if (obj != null) {
            last = obj;
        }
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        if (pressed != null) {
            if (pressed.getClass() == TaskComponent.class) {
                pressed.movedTo(new Point((int) (e.getX() + pressedPoint.getX()),
                        (int) (e.getY() + pressedPoint.getY())));
            } else {
                pressed.movedTo(new Point(e.getX(), e.getY()));
            }
            repaint();
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
    }

    @Override
    public void mousePressed(MouseEvent e) {
        pressed = getTaskComponentUnder(e.getPoint());
        if (pressed != null) {

            pressedPoint = new Point((int) (pressed.getPosition().getX() - e.getX()),
                    (int) (pressed.getPosition().getY() - e.getY()));
            if (pressed.getClass() == TaskComponent.class) {
                tasks.add((TaskComponent) pressed);
                tasks.remove(pressed);
            }
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if (pressed != null) {
            pressed.onMouseClicked(e.getPoint());
            pressed = null;
        }

    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }
}

class TaskComponent implements DrawingObject, ButtonDelegate {
    private final Task task;
    private final Button t = new Button("⇣", this);
    private final Button b = new Button("↓", this);
    public int w = 0;
    public int h = 0;
    private final Color background = Color.white;
    private final Color foreground = Color.black;
    private boolean selected = false;
    private TasksRelation tr;
    private int sq = 0;
    private final int margin = 10;
    private OrderView parent;
    private int x = 0;
    private int y = 0;

    public TaskComponent(Task task, int x, int y, OrderView parent) {
        this.task = task;
        this.x = x;
        this.y = y;
        init(parent);
    }

    public TaskComponent(Task task, OrderView parent) {
        this.task = task;
        init(parent);
    }

    private void init(OrderView parent) {
        this.parent = parent;
    }

    public void onMouseEntered() {
        parent.setCursor(new Cursor(Cursor.MOVE_CURSOR));
    }

    @Override
    public void onMouseExited() {
        parent.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
    }

    @Override
    public void onMouseClicked(Point point) {

    }

    public Task getTask() {
        return task;
    }

    public void movedTo(Point point) {
        x = (int) point.getX();
        if (x < 0)
            x = 0;
        y = (int) point.getY();
        if (y < 0)
            y = 0;

    }

    public Point getPosition() {
        return new Point(x, y);
    }

    public void calcNextPosition(Graphics2D g2d) {
        String s = Integer.toString(task.getId());
        w = g2d.getFontMetrics().stringWidth(s) + margin * 2;
        sq = g2d.getFont().getSize();
        h = sq + margin * 2;
        h += sq + sq;
    }

    public void draw(Graphics2D g2d) {
        String s = Integer.toString(task.getId());
        h = sq + margin * 2;
        if (selected) {
            g2d.setStroke(new BasicStroke(1.0f,
                    BasicStroke.CAP_BUTT,
                    BasicStroke.JOIN_MITER,
                    10.0f, new float[]{2.0f}, 0.0f));
        }
        g2d.setColor(background);
        g2d.fillRect(x, y + sq, w, h);
        g2d.setColor(foreground);
        g2d.drawRect(x, y + sq, w, h);

        g2d.drawString(s, x + margin, y + sq + h - margin);
        t.draw(x + (w - sq) / 2, y, sq, g2d);
        b.draw(x + (w - sq) / 2, y + h + sq, sq, g2d);
        h += sq + sq;
        if (tr != null) {
            tr.draw(g2d);
        }
        if (selected) {
            g2d.setStroke(new BasicStroke(1));
        }
        selected = false;
    }

    public boolean has(Point point) {
        if (point.getX() >= x && point.getX() <= x + w) {
            if (point.getY() >= y + sq && point.getY() <= y + h - sq) {
                return true;
            }
        }
        return false;
    }

    public Button getTopButton() {
        return t;
    }

    public Button getBotButton() {
        return b;
    }

    @Override
    public void repaint() {
        parent.repaint();
    }

    @Override
    public void buttonClicked(Button button, Point point) {
        tr = null;
        Object obj = parent.getTaskComponentUnder(point);
        if (obj != null && obj.getClass() == TaskComponent.class && obj != this) {
            if (button == t) {
                task.addParentTask(((TaskComponent) obj).getTask());
            }
            if (button == b) {
                ((TaskComponent) obj).getTask().addParentTask(task);
            }
            parent.generateRelations();
        }
        repaint();
    }

    @Override
    public void buttonDragged(Button button, Point point) {
        if (tr == null) {
            if (button == t) {
                tr = new TasksRelation(null, this, parent);
            }
            if (button == b) {
                tr = new TasksRelation(this, null, parent);
            }
        }
        if (button == t || button == b) {
            tr.setP1(point, point);
            repaint();
            Object obj = parent.getTaskComponentUnder(point);
            if (obj != null && obj.getClass() == TaskComponent.class && obj != this) {
                ((TaskComponent) obj).setSelected();
            }
        }
    }

    void setSelected() {
        selected = true;
    }
}

class Button implements DrawingObject {
    private final String title;
    private Color background = Color.white;
    private Color foreground = Color.black;
    private boolean selected = false;
    private int x = 0;
    private int y = 0;
    private int sq = 0;
    private final ButtonDelegate parent;

    public Button(String title, ButtonDelegate parent) {
        this.title = title;
        this.parent = parent;
    }

    public void setColors(Color foregroundColor, Color backgroundColor) {
        this.foreground = foregroundColor;
        this.background = backgroundColor;
    }

    public void draw(int x, int y, int sq, Graphics2D g2d) {
        g2d.setColor(selected ? foreground : background);
        this.x = x;
        this.y = y;
        this.sq = sq;
        g2d.fillRect(x, y, sq, sq);
        g2d.setColor(selected ? background : foreground);
        if (!selected) {
            g2d.drawRect(x, y, sq, sq);
        }
        g2d.drawString(title, x + (sq - g2d.getFontMetrics().stringWidth(title) + 1) / 2, y + sq - 1);
    }

    @Override
    public void onMouseEntered() {
        selected = true;
        parent.repaint();
    }

    @Override
    public void onMouseExited() {
        selected = false;
        parent.repaint();
    }

    @Override
    public void onMouseClicked(Point point) {
        parent.buttonClicked(this, point);
    }

    public Point getPosition() {
        return new Point(x, y);
    }

    public boolean has(Point point) {
        if (point.getX() >= x && point.getX() <= x + sq) {
            if (point.getY() >= y && point.getY() <= y + sq) {
                return true;
            }
        }
        return false;
    }

    public void movedTo(Point point) {
        parent.buttonDragged(this, point);
    }
}

class TasksRelation implements ButtonDelegate {
    private final TaskComponent task1;
    private final TaskComponent task2;
    private Button close;
    private final Color foreground = Color.black;
    private OrderView parent;
    private Point p1 = new Point(0, 0);
    private Point p2 = new Point(0, 0);

    public TasksRelation(TaskComponent task1, TaskComponent task2, OrderView parent) {
        this.task1 = task1;
        this.task2 = task2;
        init(parent);
    }

    public void setP1(Point p1, Point p2) {
        this.p1 = p1;
        this.p2 = p2;
        repaint();
    }

    private void init(OrderView parent) {
        this.parent = parent;
        close = new Button("×", this);
        close.setColors(Color.red, Color.white);
    }

    public void draw(Graphics2D g2d) {
        if (task1 != null) {
            p1 = task1.getPosition();
            p1.x = p1.x + task1.w / 2;
            p1.y = p1.y + task1.h;
        }
        if (task2 != null) {
            p2 = task2.getPosition();
            p2.x = p2.x + task2.w / 2;
        }
        GeneralPath path = new GeneralPath();
        path.moveTo(p1.x, p1.y);
        path.curveTo(p1.x, p1.y + 50, p2.x, p2.y - 50, p2.x, p2.y);
        g2d.setColor(foreground);
        g2d.draw(path);
        if (task1 != null && task2 != null) {
            int sq = 11;
            close.draw((p1.x + p2.x - sq) / 2, (p1.y + p2.y - sq) / 2, sq, g2d);
        }
    }

    public Button getCloseButton() {
        return close;
    }

    @Override
    public void repaint() {
        parent.repaint();
    }

    @Override
    public void buttonClicked(Button button, Point point) {
        if (task1 != null && task2 != null) {
            task2.getTask().removeParent(task1.getTask());
            parent.generateRelations();
            repaint();
        }
    }

    @Override
    public void buttonDragged(Button button, Point point) {

    }
}