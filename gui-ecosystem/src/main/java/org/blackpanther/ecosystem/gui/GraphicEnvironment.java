package org.blackpanther.ecosystem.gui;

import org.blackpanther.ecosystem.Environment;
import org.blackpanther.ecosystem.event.LineEvent;
import org.blackpanther.ecosystem.event.LineListener;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Line2D;
import java.util.Stack;
import java.util.logging.Logger;

/**
 * @author MACHIZAUD Andréa
 * @version
 */
public class GraphicEnvironment
    extends JPanel {

    private static final Logger logger =
        Logger.getLogger(
            GraphicEnvironment.class.getCanonicalName()
        );

    /**
     * Buffer which retains next line to draw at the next repaint() call
     */
    private Stack<Line2D> lineBuffer = new Stack<Line2D>();

    public GraphicEnvironment(Environment env) {
        env.addLineListener(
                new EnvironmentLineMonitor()
        );
    }

    @Override
    protected void paintComponent(Graphics g) {
        while (!lineBuffer.isEmpty()) {
            Line2D line = lineBuffer.pop();
            //TODO Keep in mine that we will need to keep a record of line's color...
            g.setColor(Color.BLACK);
            //FIXME Handle environment's coordinates versus panel's coordinates
            g.drawLine(
                    (int)line.getX1(),
                    (int)line.getX2(),
                    (int)line.getY1(),
                    (int)line.getY2()
            );
        }
    }

    /**
     * Record every line added to environment history
     */
    class EnvironmentLineMonitor implements LineListener {

        @Override
        public void update(LineEvent e) {
            switch (e.getType()) {
                case ADDED:
                    //if a line has been added to monitored environment,
                    // add it to lineBuffer
                    lineBuffer.add(e.getValue());
                    break;
                default:
                    logger.warning(String.format("Unhandled line event : %s",e));
            }

        }
    }
}
