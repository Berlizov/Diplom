package Components;

import javax.swing.*;
import java.awt.*;

/**
 * Created by Eugene Berlizov on 16.09.2014.
 */
public class CButton extends JButton {
    private Color backgroundColor = new Color(0, 0, 0, 0);

    public CButton() {
        super();
        setOpaque(false);

    }

    public void setBackgroundColor(Color backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    @Override
    public void paint(Graphics g) {

        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_LCD_HRGB);
        if (getModel().isEnabled()) {
            g2d.setColor(backgroundColor);
            g2d.fillRect(0, 0, getWidth(), getHeight());

            if (getModel().isRollover()) {
                g2d.setColor(new Color(255, 255, 255, 25));
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
            if (getModel().isSelected()) {
                g2d.setColor(new Color(255, 255, 255, 50));
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
            g2d.setColor(new Color(255, 255, 255));
            if (getModel().isPressed()) {
                g2d.setColor(new Color(0, 0, 0, 100));
                g2d.fillRect(0, 0, getWidth(), getHeight());
                g2d.setColor(new Color(255, 255, 255, 100));
            }

        } else {
            g2d.setColor(new Color(255, 255, 255, 100));
        }
        String s = getText();
        if (s.length() > 0) {
            g2d.drawString(s, (getWidth() - (g2d.getFontMetrics().stringWidth(s))) / 2, (getHeight() + getFont().getSize()) / 2 - 1);
        } else {
            Icon icon = getIcon();
            icon.paintIcon(this, g, (getWidth() - icon.getIconWidth()) / 2, (getHeight() - icon.getIconHeight()) / 2);
        }
    }

}
