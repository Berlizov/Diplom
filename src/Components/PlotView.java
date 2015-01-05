package Components;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.GeneralPath;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;

/**
 * Created by Eugene Berlizov on 26.11.2014.
 */
public class PlotView extends JComponent implements MouseMotionListener {
    private int maxSprintValues = 0;
    private int points = 10;//todo
    private int[] values;

    public PlotView() {
        addMouseMotionListener(this);
    }

    public int[] getValues() {
        return values;
    }

    public void setValues(int[] values) {
        this.values = values;
    }

    public int getMaxSprintValues() {
        return maxSprintValues;
    }

    public void setMaxSprintValues(int maxSprintValues) {
        this.maxSprintValues = maxSprintValues;
    }

    private Color graphColor= Color.black;

    public Color getGraphColor() {
        return graphColor;
    }

    public void setGraphColor(Color graphColor) {
        this.graphColor = graphColor;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
        margin=Integer.toString(points).length()*7+3;
        if(margin<20)
            margin=20;
    }

    private int mouseY=-1;
    private int mouseX=-1;
    private int margin = 30;
    @Override
    public void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        double x1 = margin;
        double y1 = 0;
        ArrayList<Integer> xArray=new ArrayList<>();
        xArray.add((int)x1);
        ArrayList<Integer> yArray=new ArrayList<>();
        yArray.add((int)y1);
        if (values != null) {
            for (int value : values) {
                x1 += (getWidth() - margin-1) / (double) maxSprintValues;
                y1 += value * ((getHeight() - margin-1) / (double) points);
                xArray.add((int) x1);
                yArray.add((int) y1);
            }
            xArray.add((int)x1);
            yArray.add(getHeight()- margin);
            xArray.add(margin);
            yArray.add(getHeight()- margin);
            int[]xa=new int[xArray.size()];
            int[]ya=new int[xArray.size()];
            for (int i = 0; i < xa.length; i++) {
                xa[i]=xArray.get(i);
                ya[i]=yArray.get(i);
            }
            g2d.setColor(graphColor);
            g2d.drawPolyline(xa, ya, xa.length - 2);
            GradientPaint gp = new GradientPaint(0, 0, new Color(graphColor.getRed(),graphColor.getGreen(),graphColor.getBlue(),(int)(graphColor.getAlpha()*0.5)),
                    0, getHeight(), new Color(graphColor.getRed(),graphColor.getGreen(),graphColor.getBlue(),0), true);
            g2d.setPaint(gp);
            g2d.fillPolygon(xa,ya,xa.length);
        }
        g2d.setColor(getForeground());
        g2d.drawRect(margin, 0, getWidth() - margin -1, getHeight() - margin - 1);
        g2d.drawLine(margin, 0, getWidth() - 1, getHeight() - margin - 1);
        int countOfLines=5;
        double r=(getHeight() - margin-1) / (double) countOfLines;
        for (int i = 0; i < countOfLines; i++) {
            drawInfo(g2d,(int)(i*r),getForeground());
        }
        if(mouseX>margin)
        {

            g2d.setColor(graphColor);
            int tt=(getWidth()-margin-1)/maxSprintValues;
            int index=(mouseX-margin)/tt;
            if(index<values.length) {
                int temp=0;
                int i = 0;
                for (; i <= index; i++) {
                    temp+=values[i];
                }
                mouseY = temp*(getHeight()-margin)/points;
                g2d.drawLine(mouseX, 0, mouseX, getHeight() - margin);
                g2d.drawLine(margin, mouseY, margin+(i)*(getWidth()-margin)/maxSprintValues, mouseY);
                drawInfo(g2d, mouseY,graphColor);

                String str="Спринт "+(index+1);
                Rectangle2D rect=calrStringLen(str);
                int d = (int)rect.getHeight()+5;

                int dd = (int)rect.getWidth();
                g2d.drawString(str, mouseX-dd, getHeight()-margin+d);
                int[] xa=new int[]{mouseX-dd-2,mouseX-3,mouseX,mouseX+3,mouseX+3,mouseX-dd-2};
                int[] ya=new int[]{getHeight()-margin+3,getHeight()-margin+3,getHeight()-margin,getHeight()-margin+3,getHeight()-margin+3+d,getHeight()-margin+3+d};
                g2d.drawPolygon(xa,ya,xa.length);
            }
        }
    }
    private void drawInfo(Graphics2D g2d,int h,Color c){
        String str=(int)(points-points*h/(double)(getHeight()-margin))+"";
        int d = (int)calrStringLen(str).getHeight()+5;
        g2d.setColor(new Color(255,255,255,200));
        g2d.fillRect(0,h,margin,d+4);
        g2d.setColor(c);
        g2d.drawLine(0,h,margin,h);
        g2d.drawString(str, 0, h+d);
    }

    @Override
        public void mouseMoved(MouseEvent me)
        {
            mouseX=me.getX();
            mouseY=me.getY();
            repaint();
        }
    @Override
    public void mouseDragged(MouseEvent e) {

    }
    private Rectangle2D calrStringLen(String str){
        return getGraphics().getFont().createGlyphVector(getGraphics().getFontMetrics().getFontRenderContext(), str).getVisualBounds();
    }




    /*public void mouseDragged(MouseEvent e) {
        //System.out.println("Canvas.mouseDragged()");
        Graphics2D g2d = (Graphics2D) backBuffer.getGraphics();
        int x = e.getX(), y = e.getY();
        if(lastCoord == null) {
            g2d.drawRect(x, y, 0, 0);
        } else {
            g2d.drawLine(lastCoord[0], lastCoord[1], x, y);
        }
        lastCoord = new Integer[]{x, y};
        repaint();
    }*/
}
