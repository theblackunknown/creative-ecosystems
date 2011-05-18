package org.blackpanther.ecosystem.gui;

import org.blackpanther.ecosystem.ColorfulTrace;
import org.blackpanther.ecosystem.Environment;
import org.blackpanther.ecosystem.Resource;
import org.blackpanther.ecosystem.event.*;
import org.blackpanther.ecosystem.gui.lightweight.EnvironmentInformation;

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

import static org.blackpanther.ecosystem.Configuration.*;
import static org.blackpanther.ecosystem.gui.GUIMonitor.Monitor;
import static org.blackpanther.ecosystem.helper.Helper.normalizeProbability;

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
    private static final Integer DEFAULT_DELAY = 100;

    public static final int NO_OPTION = 0;
    public static final int BOUNDS_OPTION = 1;
    public static final int RESOURCE_OPTION = 1 << 1;
    public static final int SCROLL_OPTION = 1 << 2;
    public static final int ZOOM_OPTION = 1 << 3;

    private int options = SCROLL_OPTION | ZOOM_OPTION;

    private Environment monitoredEnvironment;
    private MouseListener internalMouseHandler =
            new IconHandler();
    private Scaler internalScaler =
            new Scaler();
    private EnvironmentLineMonitor internalLineMonitor =
            new EnvironmentLineMonitor();
    private EnvironmentEvolutionMonitor internalEvolutionMonitor =
            new EnvironmentEvolutionMonitor();
    private ResourceMonitor internalResourceMonitor =
            new ResourceMonitor();

    /**
     * Buffer which retains next line to draw at the next repaint() call
     */
    private Stack<ColorfulTrace> lineBuffer = new Stack<ColorfulTrace>();
    private Timer runEnvironment;
    private Timer drawEnvironment;
    private boolean panelStructureHasChanged = false;
    private Color currentBackground;
    private Color originalBackground;

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
        drawEnvironment = new Timer(50, new GraphicMonitor());
        currentBackground = originalBackground = getBackground();

        setBorder(
                BorderFactory.createBevelBorder(BevelBorder.LOWERED)
        );
    }

    private boolean isActivated(int option) {
        return (options & option) == option;
    }

    @Override
    public void paintComponent(Graphics g) {
        if (panelStructureHasChanged) {
            redrawEnvironment(g);
            panelStructureHasChanged = false;
        } else {
            while (!lineBuffer.isEmpty()) {
                ColorfulTrace graphicalLine = lineBuffer.pop();

                g.setColor(graphicalLine.getColor());

                Point2D start = internalScaler.environmentToPanel(graphicalLine.getP1());
                Point2D end = internalScaler.environmentToPanel(graphicalLine.getP2());
                g.drawLine(
                        (int) start.getX(),
                        (int) start.getY(),
                        (int) end.getX(),
                        (int) end.getY()
                );
            }
        }
    }

    /**
     * Send a signal to redraw all environment due to
     * panel's setting modification
     *
     * @param g
     */
    private void redrawEnvironment(Graphics g) {
        g.setColor(originalBackground);
        g.fillRect(0, 0, getWidth(), getHeight());

        //repaint environment structure if it has been erased
        if (monitoredEnvironment != null) {
            //paint background
            g.setColor(currentBackground);
            Point2D top = internalScaler.environmentToPanel(new Point2D.Double(
                    monitoredEnvironment.getBounds().getX(),
                    -monitoredEnvironment.getBounds().getY()));
            double width = internalScaler.environmentToPanel(
                    monitoredEnvironment.getBounds().getWidth());
            double height = internalScaler.environmentToPanel(
                    monitoredEnvironment.getBounds().getHeight());
            g.fillRect((int) top.getX(), (int) top.getY(), (int) width, (int) height);

            //environment bounds painting activated
            if ((options & BOUNDS_OPTION) == BOUNDS_OPTION) {
                g.setColor(Color.BLACK);
                //north line
                g.drawRect((int) top.getX(), (int) top.getY(), (int) width, (int) height);
            }

            //resource painting activated
            if ((options & RESOURCE_OPTION) == RESOURCE_OPTION)
                for (Resource resource : monitoredEnvironment.getResources()) {
                    Double bluePercentage = normalizeProbability(
                            resource.getAmount()
                                    / Configuration.getParameter(RESOURCE_AMOUNT_THRESHOLD, Double.class));
                    Color blueGradient = new Color(0f,
                            0f,
                            bluePercentage.floatValue()
                    );
                    g.setColor(blueGradient);
                    Point2D center = internalScaler.environmentToPanel(resource);
                    double radius = internalScaler.environmentToPanel(
                            Configuration.getParameter(CONSUMMATION_RADIUS, Double.class));
                    g.fillOval(
                            (int) (center.getX() - radius),
                            (int) (center.getY() - radius),
                            (int) (radius * 2.0),
                            (int) (radius * 2.0)
                    );
                }


            //Draw all environment's lines
            for (Line2D line : monitoredEnvironment.getHistory()) {
                ColorfulTrace graphicalLine = (ColorfulTrace) line;

                g.setColor(graphicalLine.getColor());

                Point2D start = internalScaler.environmentToPanel(graphicalLine.getP1());
                Point2D end = internalScaler.environmentToPanel(graphicalLine.getP2());
                g.drawLine(
                        (int) start.getX(),
                        (int) start.getY(),
                        (int) end.getX(),
                        (int) end.getY()
                );
            }
        } else {
            g.fillRect(0, 0, getWidth(), getHeight());
        }
    }

    public void setEnvironment(Environment env) {
        drawEnvironment.stop();
        runEnvironment.stop();
        //environment settings
        monitoredEnvironment = env;
        env.addLineListener(internalLineMonitor);
        env.addEvolutionListener(internalEvolutionMonitor);
        env.addResourceListener(internalResourceMonitor);
        panelStructureHasChanged = true;
        repaint();
    }


    public void unsetEnvironment() {
        if (monitoredEnvironment != null) {
            drawEnvironment.stop();
            runEnvironment.stop();
            monitoredEnvironment.removeLineListener(internalLineMonitor);
            monitoredEnvironment.removeEvolutionListener(internalEvolutionMonitor);
            monitoredEnvironment.removeResourceListener(internalResourceMonitor);
            monitoredEnvironment = null;
            panelStructureHasChanged = true;
            paintImmediately(getBounds());
        }
    }

    public void runSimulation() {
        if (monitoredEnvironment != null) {
            drawEnvironment.start();
            runEnvironment.start();
        }
    }

    public void stopSimulation() {
        if (monitoredEnvironment != null) {
            drawEnvironment.stop();
            runEnvironment.stop();
        }
    }

    //fetch only the image generated
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

    public void setOption(int boundsOption, boolean shouldBePainted) {
        if (shouldBePainted)
            options |= boundsOption;
        else
            options &= ~boundsOption;
        switch (boundsOption) {
            case BOUNDS_OPTION:
            case RESOURCE_OPTION:
                panelStructureHasChanged = true;
                repaint();
        }
    }

    public Environment dumpCurrentEnvironment() {
        try {
            Environment env = monitoredEnvironment != null
                    ? monitoredEnvironment.clone()
                    : null;
            if (env != null)
                env.clearAllExternalsListeners();
            return env;
        } catch (CloneNotSupportedException e1) {
            throw new Error("Model is not cloneable");
        }
    }

    public void applyBackgroundColor(Color color) {
        currentBackground = color;
        panelStructureHasChanged = true;
        repaint();
    }

    /**
     * Record every line added to environment history
     */
    class GraphicMonitor implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (runEnvironment.isRunning())
                repaint();
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
                    lineBuffer.add((ColorfulTrace) e.getValue());
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
                    drawEnvironment.start();
                case CYCLE_END:
                    Monitor.updateEnvironmentInformation(
                            monitoredEnvironment, EnvironmentInformation.State.RUNNING);
                    break;
                case ENDED:
                    Monitor.updateEnvironmentInformation(
                            monitoredEnvironment, EnvironmentInformation.State.FROZEN);
                    repaint();
                    drawEnvironment.stop();
                    runEnvironment.stop();
            }
        }
    }

    class ResourceMonitor implements ResourceListener {

        @Override
        public void update(ResourceEvent e) {
            switch (e.getType()) {
                case DEPLETED:
                    if ((options & RESOURCE_OPTION) == RESOURCE_OPTION)
                        panelStructureHasChanged = true;
                    break;
            }
        }
    }

    class Scaler
            extends MouseAdapter {

        private final double UP_SCALE_THRESHOLD = 20.0;
        private final double DOWN_SCALE_THRESHOLD = 0.1;
        private final double SCALE_GROW_STEP = 0.2;

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
                    (panelDimension.getWidth() / 2.0) + environmentPosition.getX() * panelScale;
            double panelOrdinate =
                    (panelDimension.getHeight() / 2.0) - environmentPosition.getY() * panelScale;

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

            return panelDistance;
        }

        @Override
        public void mouseWheelMoved(MouseWheelEvent e) {
            if (isActivated(ZOOM_OPTION)
                    && e.getModifiers() == InputEvent.CTRL_MASK) {
                switch (e.getWheelRotation()) {
                    case WHEEL_UP:
                        if (panelScale < UP_SCALE_THRESHOLD) {
                            panelScale += SCALE_GROW_STEP;
                            panelStructureHasChanged = true;
                            logger.finer(String.format(
                                    "Panel zoom scale updated from %.1f to %.1f",
                                    panelScale - SCALE_GROW_STEP,
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
                                    panelScale + SCALE_GROW_STEP,
                                    panelScale
                            ));
                        }
                        break;
                }
                paintImmediately(0, 0, getWidth(), getHeight());
            }
        }

        private Point oldMouseLocation = null;

        @Override
        public void mouseDragged(MouseEvent e) {
            if (isActivated(SCROLL_OPTION)) {
                Point currentMousePoint = e.getPoint();
                if (oldMouseLocation != null) {
                    //calculate diff
                    panelAbscissaDifferential +=
                            (currentMousePoint.x - oldMouseLocation.x);
                    panelOrdinateDifferential +=
                            (currentMousePoint.y - oldMouseLocation.y);
                    panelStructureHasChanged = true;

                    paintImmediately(0, 0, getWidth(), getHeight());
                }
                oldMouseLocation = currentMousePoint;
            }
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            if (isActivated(SCROLL_OPTION)) {
                oldMouseLocation = null;
                logger.finer(String.format(
                        "Panel differential updated to (%d,%d)",
                        panelAbscissaDifferential,
                        panelOrdinateDifferential
                ));

                paintImmediately(0, 0, getWidth(), getHeight());
            }
        }
    }

    class IconHandler extends MouseAdapter {
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