package org.blackpanther.ecosystem.gui;

import org.blackpanther.ecosystem.Agent;
import org.blackpanther.ecosystem.Environment;
import org.blackpanther.ecosystem.event.EvolutionEvent;
import org.blackpanther.ecosystem.event.EvolutionListener;
import org.blackpanther.ecosystem.event.LineEvent;
import org.blackpanther.ecosystem.event.LineListener;
import org.blackpanther.ecosystem.gui.helper.Scaler;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.util.Stack;
import java.util.logging.Level;
import java.util.logging.Logger;

import static org.blackpanther.ecosystem.gui.GUIMonitor.Monitor;

/**
 * @author MACHIZAUD Andréa
 */
public class GraphicEnvironment
        extends JPanel {

    private static final Logger logger =
            Logger.getLogger(
                    GraphicEnvironment.class.getCanonicalName()
            );

    private static final Dimension DEFAULT_DIMENSION = new Dimension(800, 600);
    private static final Integer DEFAULT_DELAY = 500;

    private Environment monitoredEnvironment;

    /**
     * Buffer which retains next line to draw at the next repaint() call
     */
    private Stack<Line2D> lineBuffer = new Stack<Line2D>();
    private Stack<Point2D> agentBuffer = new Stack<Point2D>();
    private Timer runEnvironment;
    private boolean environmentIsRunning = false;

    public GraphicEnvironment(Environment env) {
        setPreferredSize(DEFAULT_DIMENSION);
        setOpaque(true);
        monitoredEnvironment = env;
        LineListener lineListener = new EnvironmentLineMonitor();
        EnvironmentEvolutionMonitor evolutionMonitor = new EnvironmentEvolutionMonitor();
        env.addLineListener(
                lineListener
        );
        env.addEvolutionListener(
                evolutionMonitor
        );
        runEnvironment = new Timer(
                DEFAULT_DELAY, evolutionMonitor);
        runEnvironment.start();
    }

    /**
     * FIXME If global window is repainted, image is lost
     * TODO listen for window event and save a backup image
     * @param g
     *      component graphics
     */
    @Override
    protected void paintComponent(Graphics g) {
        logger.fine("I'm gonna paint " + lineBuffer.size() + " lines");
        while (!lineBuffer.isEmpty()) {
            Line2D line = lineBuffer.pop();
            //TODO Keep in mine that we will need to keep a record of line's color...
            g.setColor(Color.BLACK);
            //FIXME Handle environment's coordinates versus panel's coordinates
            Point2D start = Scaler.environmentToPanel(
                    this,
                    monitoredEnvironment,
                    line.getP1());
            Point2D end   = Scaler.environmentToPanel(
                    this,
                    monitoredEnvironment,
                    line.getP2());
            g.drawLine(
                    (int) start.getX(),
                    (int) start.getY(),
                    (int) end.getX(),
                    (int) end.getY()
            );
        }
    }

    public void runSimulation() {
        runEnvironment.start();
    }

    public void stopSimulation() {
        runEnvironment.stop();
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
                    logger.log(Level.FINE, "{0} added to line buffer, lineBuffer size : {1}",
                            new Object[]{e.getValue(), lineBuffer.size()});
                    break;
                default:
                    logger.warning(String.format("Unhandled line event : %s", e));
            }

        }
    }

    class EnvironmentEvolutionMonitor implements ActionListener, EvolutionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            monitoredEnvironment.runNextCycle();
        }

        @Override
        public void update(EvolutionEvent e) {
            switch (e.getType()) {
                case STARTED:
                    environmentIsRunning = true;
                    Monitor.updateEnvironmentRunningId(
                            e.getSource().getId()
                    );
                case CYCLE_END:
                    Monitor.updateEnvironmentAgentNumber(
                            e.getSource().getPool().size()
                    );
                    for (Agent agent : e.getSource().getPool()) {
                        agentBuffer.push(agent.getLocation());
                    }
                    invalidate();
                    repaint();
                    break;
                case ENDED:
                    invalidate();
                    repaint();
                    runEnvironment.stop();
            }
        }
    }
}
