package grapher;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import javax.swing.JPanel;

/**
 *
 * User interface of graph.
 * 
 * @author Dylan Wheeler
 */
public class Graph extends JPanel implements MouseListener, MouseMotionListener {
    
    private boolean positive = false, trace = true;
    private double scale = 0;
    private int x = 0;
    
    /**
     *
     * Creates new instance of the graph panel.
     * 
     * @param positive boolean indicating if the graph should be focused on only the positive axes or all four quadrants of the coordinate plane
     */
    public Graph(final boolean positive) {
        this.positive = positive;
        addMouseListener(Graph.this);
        addMouseMotionListener(Graph.this);
    }
    
    @Override
    public void paintComponent(final Graphics g) {
        super.paintComponent(g);
        if (g instanceof Graphics2D) {
            final Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        }
        if (scale == 0) {
            calcScale();
        }
        g.setColor(Color.black);
        if (positive) {
            g.drawLine(5, getHeight() - 5, getWidth(), getHeight() - 5); //x
            g.drawLine(5, 0, 5, getHeight() - 5); //y
        } else {
            g.drawLine(0, getHeight() / 2, getWidth(), getHeight() / 2); //x
            g.drawLine(getWidth() / 2, 0, getWidth() / 2, getHeight()); //y
        }
        g.drawString("X = " + (x - (positive ? 5 : getWidth() / 2)), 10, 15);
        int y = 0;
        for (final Grapher.Equation e : Grapher.entries) {
            g.setColor(e.c);
            g.drawString(e.name, 10, ((y += 1) * 13 + 15));
        }
        Grapher.entries.stream().map((e) -> {
            g.setColor(e.c);
            return e;
        }).forEach((e) -> {
            if (positive) {
                final double eval = (e.m * getRealX(x)) + e.b;
                g.drawLine(5, getRealY(e.b, scale), getWidth(), getRealY(e.m * getWidth() + e.b, scale));
                g.fillOval(x - 3, getRealY(eval, scale) - 3, 6, 6);
                g.drawString(format(eval), getRealX(x), getRealY(eval, scale) - 10);
            } else {
                final double eval = e.m * getRealX(x) + e.b;
                g.drawLine(0, getRealY(e.m * (getWidth() / -2) + e.b, scale), getWidth(), getRealY(e.m * getWidth() / 2 + e.b, scale));
                g.fillOval(x - 3, getRealY(eval, scale) - 3, 6, 6);
                g.drawString(format(eval), x - 5, getRealY(eval, scale) - 10);
            }
        });
    }
    
    private void calcScale() {
        if (Grapher.entries.size() < 1) {
            return;
        }
        double yscale = Double.MAX_VALUE;
        for (final Grapher.Equation e : Grapher.entries) {
            final double tmp = scale(e.m, e.b);
            if (tmp < yscale) {
                yscale = tmp;
            }
        }
        scale = yscale;
    }
    
    private double scale(final double m, final double b) {
        if (positive) {
            return getHeight() / (getWidth() * m + b);
        } else {
            return (getHeight() / 2) / (getWidth() / 2 * m + b);
        }
    }
    
    private int getRealX(final double x) {
        if (positive) {
            return (int) x - 5;
        } else {
            return (int) x - (getWidth() / 2);
        }
    }
    
    private int getRealY(final double y, final double scale) {
        if (positive) {
            return (int) Math.round((getHeight() - (y * scale) - 5));
        } else {
            return (int) Math.round((getHeight() - (y * scale) - (getHeight() / 2)));
        }
    }
    
    private String format(final double d) {
        final String[] split = (Math.round(d * 100000) / 100000D + "").split("\\.");
        return comma(split[0]) + (split[1].equals("0") ? "" : "." + split[1]);
    }
    
    private String comma(final String i) {
        if (i.length() < 4) {
            return i;
        }
        return comma(i.substring(0, i.length() - 3)) + "," + i.substring(i.length() - 3, i.length());
    }
    
    @Override
    public void mouseDragged(final MouseEvent e) {}
    
    @Override
    public void mouseMoved(final MouseEvent e) {
        if (trace) {
            x = e.getX();
            repaint();
        }
    }
    
    @Override
    public void mousePressed(final MouseEvent e) {
        if (trace) {
            Grapher.entries.stream().forEach((line) -> {
                final double eval = line.m * getRealX(x) + line.b;
                System.out.println(line.m + "(" + getRealX(x) + ") + " + line.b + " = " + format(eval) + "\t" + line.name);
            });
            System.out.println();
        }
        trace = !trace;
    }
    
    @Override
    public void mouseClicked(final MouseEvent e) {}
    
    @Override
    public void mouseReleased(final MouseEvent e) {}
    
    @Override
    public void mouseEntered(final MouseEvent e) {}
    
    @Override
    public void mouseExited(final MouseEvent e) {}
    
}
