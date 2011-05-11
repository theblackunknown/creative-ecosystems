package org.blackpanther.ecosystem.gui;

import org.blackpanther.ecosystem.Environment;
import org.blackpanther.ecosystem.Resource;
import org.blackpanther.ecosystem.event.*;

import javax.swing.*;
import javax.swing.border.BevelBorder;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
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
    private static final Integer DEFAULT_DELAY = 400;

    private Environment monitoredEnvironment;
    private MouseListener internalMouseHandler =
            new IconHandler();
    private Scaler internalScaler =
            new Scaler();
    private EnvironmentLineMonitor internalLineMonitor =
            new EnvironmentLineMonitor();
    private EnvironmentEvolutionMonitor internalEvolutionMonitor =
            new EnvironmentEvolutionMonitor();
    private AgentMonitor internalAgentMonitor =
            new AgentMonitor();

    /**
     * Buffer which retains next line to draw at the next repaint() call
     */
    private Stack<Line2D> lineBuffer = new Stack<Line2D>();
    private Stack<Point2D> agentBuffer = new Stack<Point2D>();
    private Timer runEnvironment;
    private boolean panelStructureHasChanged = false;

    public GraphicEnvironment() {
        //panel settings
        setPreferredSize(DEFAULT_DIMENSION);
        setOpaque(true);
        addMouseListener(internalScaler);
        addMouseWheelListener(internalScaler);
        addMouseMotionListener(internalScaler);
        addMouseListener(internalMouseHandler);

        //timer settings
        runEnvironment = new Timer(
                DEFAULT_DELAY, internalEvolutionMonitor);
        runEnvironment.setInitialDelay(1000);

        panelStructureHasChanged = true;

        setBorder(
                BorderFactory.createBevelBorder(BevelBorder.LOWERED)
        );
    }

    @Override
    protected void paintComponent(Graphics g) {
        if (panelStructureHasChanged) {
            logger.fine("Panel structure has changed, "
                    + "all environment history must be drawn again.");
            redrawEnvironment(g);
            panelStructureHasChanged = false;
        } else {
            logger.finest("I'm gonna paint " + lineBuffer.size() + " lines");
            while (!lineBuffer.isEmpty()) {
                Line2D line = lineBuffer.pop();
                //TODO We will need to keep a record of line's color...
                g.setColor(Color.WHITE);
                // HELP Handle environment's coordinate vs panel's coordinates
                Point2D start = internalScaler.environmentToPanel(line.getP1());
                Point2D end = internalScaler.environmentToPanel(line.getP2());
                g.drawLine(
                        (int) start.getX(),
                        (int) start.getY(),
                        (int) end.getX(),
                        (int) end.getY()
                );
            }
            while (!agentBuffer.isEmpty()) {
                Point2D position = agentBuffer.pop();

                g.setColor(Color.RED);

                //TODO Handle diameter scale
                Point2D center = internalScaler.environmentToPanel(position);

                g.fillOval(
                        (int) center.getX(),
                        (int) center.getY(),
                        2,
                        2
                );
            }
        }
    }

    public void setEnvironment(Environment env) {
        //environment settings
        monitoredEnvironment = env;
        env.addLineListener(internalLineMonitor);
        env.addEvolutionListener(internalEvolutionMonitor);
        env.addAgentListener(internalAgentMonitor);
        panelStructureHasChanged = true;
        paintImmediately(0, 0, getWidth(), getHeight());
    }

    public void unsetEnvironment() {
        if (runEnvironment.isRunning())
            runEnvironment.stop();
        if (monitoredEnvironment != null) {
            monitoredEnvironment.removeLineListener(internalLineMonitor);
            monitoredEnvironment.removeEvolutionListener(internalEvolutionMonitor);
            monitoredEnvironment.removeAgentListener(internalAgentMonitor);
            monitoredEnvironment = null;
            panelStructureHasChanged = true;
            paintImmediately(0, 0, getWidth(), getHeight());
        }
    }

    public void runSimulation() {
        if (monitoredEnvironment != null
                && !runEnvironment.isRunning())
            runEnvironment.start();
    }

    public void stopSimulation() {
        if (monitoredEnvironment != null
                && runEnvironment.isRunning())
            runEnvironment.stop();
    }

    /**
     * Send a signal to redraw all environment due to
     * panel's setting modification
     *
     * @param g
     */
    private void redrawEnvironment(Graphics g) {
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, getWidth(), getHeight());
        if (monitoredEnvironment != null) {
            //Draw all environment's lines
            for (Line2D line : monitoredEnvironment.getHistory()) {
                //TODO We will need to keep a record of line's color...
                g.setColor(Color.WHITE);
                // HELP Handle environment's coordinate vs panel's coordinates
                Point2D start = internalScaler.environmentToPanel(line.getP1());
                Point2D end = internalScaler.environmentToPanel(line.getP2());
                g.drawLine(
                        (int) start.getX(),
                        (int) start.getY(),
                        (int) end.getX(),
                        (int) end.getY()
                );
            }
            for (Resource resource : monitoredEnvironment.getResources()) {
                //TODO Resources related to color ?
                Color blueGradient = new Color(0f, 0f,
                        resource.getAmount() / (float) Resource.MAX_AMOUNT
                );
                g.setColor(blueGradient);
                Point2D center = internalScaler.environmentToPanel(resource.getCenter());
                double radius = internalScaler.environmentToPanel(resource.getRadius());
                g.fillOval(
                        (int) center.getX(),
                        (int) center.getY(),
                        (int) radius,
                        (int) radius
                );
            }
        }
    }

    public BufferedImage dumpCurrentImage() {
        if (monitoredEnvironment != null) {
            BufferedImage image = new BufferedImage(
                    getWidth(),
                    getHeight(),
                    BufferedImage.TYPE_INT_RGB
            );
            Graphics2D graphics = image.createGraphics();
            redrawEnvironment(graphics);
            return image;
        } else {
            //no environment set
            return null;
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
                    logger.log(Level.FINEST, "{0} added to line buffer, lineBuffer size : {1}",
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

        /**
         * Handle environment's events to update GUI
         *
         * @param e environment's evolution event
         */
        @Override
        public void update(EvolutionEvent e) {
            switch (e.getType()) {
                case STARTED:
                    Monitor.updateEnvironmentInformation();
                case CYCLE_END:
                    Monitor.updateEnvironmentInformation();
                    repaint();
                    break;
                case ENDED:
                    Monitor.updateEnvironmentInformation();
                    Monitor.environmentFrozen();
                    repaint();
                    runEnvironment.stop();
            }
        }
    }

    class AgentMonitor implements AgentListener {

        @Override
        public void update(AgentEvent e) {
            switch (e.getType()) {
                case BORN:
                    break;
                case DEATH:
                    //TODO Trace dead agent
                    break;
            }
        }
    }

    class Scaler
            extends MouseAdapter {

        private final double UP_SCALE_THRESHOLD = 20.0;
        private final double DOWN_SCALE_THRESHOLD = 0.1;
        private final double SCALE_GROW_STEP = 0.1;

        private final int WHEEL_UP = -1;
        private final int WHEEL_DOWN = 1;

        private double panelScale = 1.0;
        private int panelAbscissaDifferential = 0;
        private int panelOrdinateDifferential = 0;

        /**
         * Adapt environment coordinates to panel ones
         *
         * @param environmentPosition position in environment's landmark
         * @return position in panel's landmark
         */
        public Point2D environmentToPanel(
                Point2D environmentPosition
        ) {
            //get current panel's dimension
            Dimension panelDimension = getBounds().getSize();

            //minus because environment is real mathematics cartesian coordinate
            double panelAbscissa =
                    ((panelDimension.getWidth() / 2.0) + environmentPosition.getX() * panelScale);
            double panelOrdinate =
                    ((panelDimension.getHeight() / 2.0) - environmentPosition.getY() * panelScale);

            logger.finest(String.format(
                    "(%.2f,%.2f) translated to (%.2f,%.2f) [panelDimension=%s]",
                    environmentPosition.getX(),
                    environmentPosition.getY(),
                    panelAbscissa,
                    panelOrdinate,
                    panelDimension
            ));

            return new Point2D.Double(
                    panelAbscissa + panelAbscissaDifferential,
                    panelOrdinate + panelOrdinateDifferential
            );
        }

        public Double environmentToPanel(
                Double distance
        ) {
            //get current panel's dimension
            Dimension panelDimension = getBounds().getSize();

            //minus because environment is real mathematics cartesian coordinate
            double panelDistance = distance * panelScale;

            logger.finest(String.format(
                    "%.4f translated to %.4f [panelDimension=%s]",
                    distance,
                    panelDistance,
                    panelDimension
            ));

            return distance;
        }

        @Override
        public void mouseWheelMoved(MouseWheelEvent e) {
            if (e.getModifiers() == InputEvent.CTRL_MASK) {
                switch (e.getWheelRotation()) {
                    case WHEEL_UP:
                        if (panelScale < UP_SCALE_THRESHOLD) {
                            panelScale += SCALE_GROW_STEP;
                            panelStructureHasChanged = true;
                            logger.finer(String.format(
                                    "Panel zoom scale updated from %.1f to %.1f",
                                    panelScale - 0.1,
                                    panelScale
                            ));
                        }
                        break;
                    case WHEEL_DOWN:
                        if (panelScale > DOWN_SCALE_THRESHOLD) {
                            panelScale -= SCALE_GROW_STEP;
                            panelStructureHasChanged = true;
                            logger.finer(String.format(
                                    "Panel zoom scale updated from %.1f to %.1f",
                                    panelScale + 0.1,
                                    panelScale
                            ));
                        }
                        break;
                }
                repaint();
            }
        }

        private Point oldMouseLocation = null;

        @Override
        public void mouseDragged(MouseEvent e) {
            Point currentMousePoint = e.getPoint();
            if (oldMouseLocation != null) {
                //calculate diff
                panelAbscissaDifferential +=
                        (currentMousePoint.x - oldMouseLocation.x);
                panelOrdinateDifferential +=
                        (currentMousePoint.y - oldMouseLocation.y);
                panelStructureHasChanged = true;
                repaint();
            }
            oldMouseLocation = currentMousePoint;
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            oldMouseLocation = null;
            logger.finer(String.format(
                    "Panel differential updated to (%d,%d)",
                    panelAbscissaDifferential,
                    panelOrdinateDifferential
            ));
            repaint();
        }
    }

    class IconHandler extends MouseAdapter {

        //TODO Closed hand
        @Override
        public void mousePressed(MouseEvent e) {
        }

        @Override
        public void mouseEntered(MouseEvent e) {
            setCursor(
                    Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        }

        @Override
        public void mouseExited(MouseEvent e) {
            setCursor(
                    Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
        }
    }
}
