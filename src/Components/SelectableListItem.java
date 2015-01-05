package Components;

import javax.swing.*;
import java.awt.*;
import java.util.EventListener;

/**
 * Created by Eugene Berlizov on 17.11.2014.
 */
public class SelectableListItem extends JCheckBox implements EventListener {
    private Color selectColor = Color.BLUE;
    private boolean showCheckBox;
    private int padding;
    private boolean selectable = true;

    public SelectableListItem(String text, boolean showCheckBox) {
        super(text);
        this.showCheckBox = showCheckBox;
        setShowCheckBox(showCheckBox);
        setOpaque(false);
    }

    public void setShowCheckBox(boolean showCheckBox) {
        this.showCheckBox = showCheckBox;
        if (showCheckBox) {
            padding = 37;
        } else {
            padding = 20;
        }
    }

    public Color getSelectColor() {
        return selectColor;
    }

    public void setSelectColor(Color selectColor) {
        this.selectColor = selectColor;
    }

    public void setSelectable(boolean selectable) {
        this.selectable = selectable;
    }

    @Override
    public void paintComponent(Graphics g) {
        g.setColor(getBackground());
        g.fillRect(0, 0, getWidth(), getHeight());
        if (isSelected() && !selectable)
            setSelected(false);

        if (isSelected()) {
            g.setColor(selectColor);
            g.fillRect(0, 0, getWidth(), getHeight());
        }
        if (getModel().isPressed()) {
            g.setColor(new Color(0, 0, 0, 100));
            g.fillRect(0, 0, getWidth(), getHeight());
        } else if (getModel().isRollover()) {
            Color c = new Color(selectColor.getRed(), selectColor.getGreen(), selectColor.getBlue(), 20);
            g.setColor(c);
            g.fillRect(0, 0, getWidth(), getHeight());
        }
        g.setColor(Color.LIGHT_GRAY);
        g.drawLine(padding, getHeight() - 1, getWidth(), getHeight() - 1);

        Graphics2D g2d = (Graphics2D) g;
        if (isSelected() || getModel().isPressed())
            g2d.setColor(Color.WHITE);
        else
            g2d.setColor(Color.BLACK);
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_LCD_HRGB);
        double d = g2d.getFont().createGlyphVector(g2d.getFontMetrics().getFontRenderContext(), getText()).getVisualBounds().getHeight();
        g2d.drawString(getText(), padding, (int) ((getHeight() + d) / 2));
        if (showCheckBox) {
            int dd = 12;
            g.setColor(Color.WHITE);
            int h = (getHeight() - dd) / 2;
            int w = (padding - dd) / 2;
            g.fillRect(w, h, dd, dd);
            g.setColor(Color.GRAY);
            g.drawRect(w, h, dd, dd);
            if (isSelected()) {
                g2d.setColor(Color.BLACK);
                g2d.drawString("✓", w + dd / 8, h + dd);
            }
        }
    }
}
