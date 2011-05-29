package org.blackpanther.ecosystem.gui;

import org.blackpanther.ecosystem.Environment;
import org.blackpanther.ecosystem.agent.Creature;
import org.blackpanther.ecosystem.agent.Resource;
import org.blackpanther.ecosystem.event.*;
import org.blackpanther.ecosystem.factory.EnvironmentAbstractFactory;
import org.blackpanther.ecosystem.factory.fields.FieldsConfiguration;
import org.blackpanther.ecosystem.factory.fields.StateFieldMould;
import org.blackpanther.ecosystem.gui.lightweight.EnvironmentInformation;
import org.blackpanther.ecosystem.line.AgentLine;

import javax.swing.*;
import javax.swing.border.BevelBorder;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.util.Collection;
import java.util.Random;
import java.util.Stack;
import java.util.logging.Level;
import java.util.logging.Logger;

import static org.blackpanther.ecosystem.Configuration.Configuration;
import static org.blackpanther.ecosystem.Configuration.ENERGY_AMOUNT_THRESHOLD;
import static org.blackpanther.ecosystem.agent.AgentConstants.AGENT_LOCATION;
import static org.blackpanther.ecosystem.agent.ResourceConstants.RESOURCE_NATURAL_COLOR;
import static org.blackpanther.ecosystem.factory.generator.StandardProvider.StandardProvider;
import static org.blackpanther.ecosystem.gui.GUIMonitor.Monitor;
import static org.blackpanther.ecosystem.helper.Helper.require;

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
    private static final Integer EVOLUTION_DELAY = 200;
    private static final Integer PAINTER_DELAY = 160;

    public static final int BOUNDS_OPTION = 1;
    public static final int RESOURCE_OPTION = 1 << 1;
    public static final int SCROLL_OPTION = 1 << 2;
    public static final int ZOOM_OPTION = 1 << 3;
    public static final int CREATURE_OPTION = 1 << 4;
    public static final int FANCY_LINE_OPTION = 1 << 5;

    public static Double CREATURE_RADIUS = 10.0;
    public static Double LINE_WIDTH = 3.0;

    private int options =
            BOUNDS_OPTION
                    | RESOURCE_OPTION
                    | SCROLL_OPTION
                    | ZOOM_OPTION
                    | CREATURE_OPTION
                    | FANCY_LINE_OPTION;

    private Environment monitoredEnvironment;
    private MouseMonitor internalMouseMonitor =
            new MouseMonitor();
    private EnvironmentLineMonitor internalLineMonitor =
            new EnvironmentLineMonitor();
    private EnvironmentEvolutionMonitor internalEvolutionMonitor =
            new EnvironmentEvolutionMonitor();
    private AgentMonitor internalAgentMonitor =
            new AgentMonitor();

    /**
     * Buffer which retains next line to draw at the next repaint() call
     */
    private Stack<AgentLine> lineBuffer = new Stack<AgentLine>();
    private Stack<Creature> nextCycleCreaturesBuffer = new Stack<Creature>();

    private Timer runEnvironment;
    private Timer drawEnvironment;
    private boolean panelStructureHasChanged = false;
    private Color currentBackground;

    public GraphicEnvironment() {
        //panel settings
        setMinimumSize(DEFAULT_DIMENSION);
        setOpaque(true);
        addMouseListener(internalMouseMonitor);
        addMouseMotionListener(internalMouseMonitor);
        addMouseWheelListener(internalMouseMonitor);

        //timer settings
        runEnvironment = new Timer(
                EVOLUTION_DELAY, internalEvolutionMonitor);
        drawEnvironment = new Timer(
                PAINTER_DELAY, new GraphicMonitor());
        currentBackground = getBackground();

        setBorder(
                BorderFactory.createBevelBorder(BevelBorder.LOWERED)
        );
    }

    /**
     * Check is option is activated on this panel
     */
    public boolean isActivated(int option) {
        return (options & option) == option;
    }

    void repaintEnvironment() {
        panelStructureHasChanged = true;
        repaint();
    }

    @Override
    public void paintComponent(Graphics g) {
        if (panelStructureHasChanged) {
            paintEnvironment(g);
            panelStructureHasChanged = false;
        } else {
            while (!lineBuffer.isEmpty()) {
                paintLine(g, lineBuffer.pop());
            }
        }
    }

    /**
     * Send a signal to redraw all environment due to
     * panel's setting modification
     *
     * @param g
     */
    private void paintEnvironment(Graphics g) {
        g.clearRect(0, 0, getWidth(), getHeight());
        //repaint environment structure if it has been erased
        if (monitoredEnvironment != null) {
            paintBackground(g);
            paintResources(g);
            //Draw all environment's lines
            for (Line2D line : monitoredEnvironment.getHistory())
                paintLine(g, (AgentLine) line);
            paintCreatures(g);
        }
    }

    private void paintBackground(Graphics g) {
        //paint background
        g.setColor(currentBackground);
        Point2D top = internalMouseMonitor.environmentToPanel(new Point2D.Double(
                monitoredEnvironment.getBounds().getX(),
                -monitoredEnvironment.getBounds().getY()));
        double width = internalMouseMonitor.environmentToPanel(
                monitoredEnvironment.getBounds().getWidth());
        double height = internalMouseMonitor.environmentToPanel(
                monitoredEnvironment.getBounds().getHeight());
        g.fillRect((int) top.getX(), (int) top.getY(), (int) width, (int) height);

        //environment bounds painting activated
        if ((options & BOUNDS_OPTION) == BOUNDS_OPTION) {
            g.setColor(Color.BLACK);
            //north line
            g.drawRect((int) top.getX(), (int) top.getY(), (int) width, (int) height);
        }
    }

    private void paintCreatures(Graphics g) {
        //agent initial painting activated
        if ((options & CREATURE_OPTION) == CREATURE_OPTION
                && !runEnvironment.isRunning()) {
            Graphics2D g2d = (Graphics2D) g;
            for (Creature monster : monitoredEnvironment.getCreaturePool()) {
                Point2D center = internalMouseMonitor.environmentToPanel(monster.getLocation());
                double radius =
                        (internalMouseMonitor.environmentToPanel(monster.getEnergy()
                                / Configuration.getParameter(ENERGY_AMOUNT_THRESHOLD, Double.class)))
                                * CREATURE_RADIUS;
                g2d.setPaint(new RadialGradientPaint(
                        center,
                        (float) radius,
                        new float[]{0.0f, 1.0f},
                        new Color[]{monster.getColor(), currentBackground}
                ));
                g2d.fillOval(
                        (int) (center.getX() - radius),
                        (int) (center.getY() - radius),
                        (int) (radius * 2.0),
                        (int) (radius * 2.0)
                );
            }
        }
    }

    private void paintResources(Graphics g) {
        //resource painting activated
        if ((options & RESOURCE_OPTION) == RESOURCE_OPTION) {
            Graphics2D g2d = (Graphics2D) g;
            for (Resource resource : monitoredEnvironment.getResources()) {
                Color resourceColor = resource.getGene(RESOURCE_NATURAL_COLOR, Color.class);
                Point2D center = internalMouseMonitor.environmentToPanel(resource.getLocation());
                double radius =
                        (internalMouseMonitor.environmentToPanel(resource.getEnergy()
                                / Configuration.getParameter(ENERGY_AMOUNT_THRESHOLD, Double.class)))
                                * CREATURE_RADIUS;
                g2d.setPaint(new RadialGradientPaint(
                        center,
                        (float) radius,
                        new float[]{0.0f, 1.0f},
                        new Color[]{resourceColor, currentBackground}
                ));
                g.fillOval(
                        (int) (center.getX() - radius),
                        (int) (center.getY() - radius),
                        (int) (radius * 2.0),
                        (int) (radius * 2.0)
                );
            }
        }
    }

    private void paintLine(Graphics g, AgentLine graphicalLine) {
        Graphics2D g2d = (Graphics2D) g;
        Random rand = Configuration.getRandom();


        Point2D start = internalMouseMonitor.environmentToPanel(graphicalLine.getP1());
        Point2D end = internalMouseMonitor.environmentToPanel(graphicalLine.getP2());
        Color basicColor = graphicalLine.getColor();


        double strokeWidth =
                internalMouseMonitor.environmentToPanel(
                        graphicalLine.getPower()
                                / Configuration.getParameter(ENERGY_AMOUNT_THRESHOLD, Double.class))
                        * LINE_WIDTH;

        g2d.setStroke(new BasicStroke(
                (float) strokeWidth,
                BasicStroke.CAP_ROUND,
                BasicStroke.JOIN_ROUND
        ));

        if (isActivated(FANCY_LINE_OPTION)) {
            double randomNumber = rand.nextGaussian();
            if (randomNumber < 0.0) {
                basicColor = basicColor.darker();
            } else if (randomNumber > 1.0) {
                basicColor = basicColor.brighter();
            }
        }

        g2d.setColor(basicColor);

        g2d.drawLine(
                (int) start.getX(),
                (int) start.getY(),
                (int) end.getX(),
                (int) end.getY()
        );
    }

    public void setEnvironment(Environment env) {
        drawEnvironment.stop();
        runEnvironment.stop();
        //environment settings
        monitoredEnvironment = env;
        env.addLineListener(internalLineMonitor);
        env.addEvolutionListener(internalEvolutionMonitor);
        env.addAgentListener(internalAgentMonitor);
        repaintEnvironment();
    }

    public void unsetEnvironment() {
        if (monitoredEnvironment != null) {
            drawEnvironment.stop();
            runEnvironment.stop();
            monitoredEnvironment.removeAgentListener(internalAgentMonitor);
            monitoredEnvironment.removeEvolutionListener(internalEvolutionMonitor);
            monitoredEnvironment.removeLineListener(internalLineMonitor);
            monitoredEnvironment = null;
            repaintEnvironment();
        }
    }

    public void runSimulation() {
        if (monitoredEnvironment != null || !runEnvironment.isRunning()) {
            drawEnvironment.start();
            runEnvironment.start();
            if (isActivated(CREATURE_OPTION))
                repaintEnvironment();
        }
    }

    public void stopSimulation() {
        if (monitoredEnvironment != null || runEnvironment.isRunning()) {
            drawEnvironment.stop();
            runEnvironment.stop();
            if (isActivated(CREATURE_OPTION))
                repaintEnvironment();
        }
    }

    public void setOption(int option, boolean shouldBePainted) {
        if (shouldBePainted)
            options |= option;
        else
            options &= ~option;
        switch (option) {
            case BOUNDS_OPTION:
            case RESOURCE_OPTION:
            case CREATURE_OPTION:
            case FANCY_LINE_OPTION:
                repaintEnvironment();
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
            paintEnvironment(graphics);
            return image;
        } else {
            //no environment set
            return null;
        }

    }

    public Environment dumpCurrentEnvironment() {
        Environment env = monitoredEnvironment != null
                ? monitoredEnvironment.clone()
                : null;
        if (env != null)
            env.clearAllExternalsListeners();
        return env;
    }

    public void applyBackgroundColor(Color color) {
        currentBackground = color;
        repaintEnvironment();
    }

    public void setDropMode(DropMode mode) {
        internalMouseMonitor.setDropMode(mode);
    }

    public void addCreatures(Collection<Creature> creatures) {
        if (monitoredEnvironment != null
                && !runEnvironment.isRunning()) {
            monitoredEnvironment.addCreatures(creatures);
            repaintEnvironment();
        } else
            nextCycleCreaturesBuffer.addAll(creatures);
    }

    public void addResources(Collection<Resource> resources) {
        monitoredEnvironment.addResources(resources);
        if (isActivated(RESOURCE_OPTION))
            repaintEnvironment();
    }

    public void changeLineWidth(double value) {
        LINE_WIDTH = value;
        repaintEnvironment();
    }

    /**
     * Record every line added to environment history
     */
    class GraphicMonitor
            implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            repaint();
        }
    }

    /**
     * Record every line added to environment history
     */
    class EnvironmentLineMonitor
            implements LineListener {

        @Override
        public void update(LineEvent e) {
            switch (e.getType()) {
                case ADDED:
                    //if a line has been added to monitored environment,
                    // add it to lineBuffer
                    lineBuffer.add((AgentLine) e.getValue());
                    logger.log(Level.FINEST, "{0} added to line buffer, lineBuffer size : {1}",
                            new Object[]{e.getValue(), lineBuffer.size()});
                    break;
                default:
                    logger.warning(String.format("Unhandled line event : %s", e));
            }

        }
    }

    class EnvironmentEvolutionMonitor
            implements ActionListener, EvolutionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            monitoredEnvironment.runNextCycle();
            if (!nextCycleCreaturesBuffer.isEmpty()) {
                monitoredEnvironment.addCreatures(nextCycleCreaturesBuffer);
                nextCycleCreaturesBuffer.clear();
            }
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
                    Monitor.makeBackup(monitoredEnvironment.clone());
                case CYCLE_END:
                    Monitor.updateEnvironmentInformation(
                            monitoredEnvironment, EnvironmentInformation.State.RUNNING);
                    if (monitoredEnvironment.getCreaturePool().size() == 0) {
                        drawEnvironment.stop();
                        runEnvironment.stop();
                    }
                    break;
                case ENDED:
                    Monitor.updateEnvironmentInformation(
                            monitoredEnvironment, EnvironmentInformation.State.FROZEN);
                    drawEnvironment.stop();
                    runEnvironment.stop();
                    repaint();
            }
        }
    }

    class AgentMonitor
            implements AgentListener {

        @Override
        public void update(AgentEvent e) {
            if (e.getAgent() instanceof Resource)
                switch (e.getType()) {
                    case DEATH:
                        if ((options & RESOURCE_OPTION) == RESOURCE_OPTION)
                            panelStructureHasChanged = true;
                        break;
                }
        }
    }

    public enum DropMode {
        NONE,
        AGENT,
        RESOURCE
    }

    class MouseMonitor
            extends MouseAdapter
            implements ActionListener {

        private DropMode dropMode = GraphicEnvironment.DropMode.NONE;
        private Timer dropTimer = new Timer(200, this);
        private Point mouseLocation = null;

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
        public Point2D environmentToPanel(Point2D environmentPosition) {
            //get current panel's dimension
            Dimension panelDimension = getBounds().getSize();

            //minus because environment is real mathematics cartesian coordinate
            double panelAbscissa =
                    (panelDimension.getWidth() / 2.0) + environmentPosition.getX() * panelScale;
            double panelOrdinate =
                    (panelDimension.getHeight() / 2.0) - environmentPosition.getY() * panelScale;

            return new Point2D.Double(
                    panelAbscissa + panelAbscissaDifferential,
                    panelOrdinate + panelOrdinateDifferential
            );
        }

        public Point2D panelToEnvironment(Point2D panelPosition) {
            //get current panel's dimension
            Dimension panelDimension = getBounds().getSize();

            //minus because environment is real mathematics cartesian coordinate
            double environmentAbscissa = ((panelPosition.getX() - panelAbscissaDifferential) - (panelDimension.getWidth() / 2.0))
                    / panelScale;
            double environmentOrdinate = ((panelDimension.getHeight() / 2.0) - (panelPosition.getY() - panelOrdinateDifferential))
                    / panelScale;

            return new Point2D.Double(
                    environmentAbscissa,
                    environmentOrdinate
            );
        }

        public Double environmentToPanel(Double distance) {
            //get current panel's dimension
            Dimension panelDimension = getBounds().getSize();

            double panelDistance = distance * panelScale;

            logger.finest(String.format(
                    "%.4f translated to %.4f [panelDimension=%s]",
                    distance,
                    panelDistance,
                    panelDimension
            ));

            return panelDistance;
        }

        public void setDropMode(DropMode mode) {
            dropMode = mode == null
                    ? GraphicEnvironment.DropMode.NONE
                    : mode;
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

        @Override
        public void mouseClicked(MouseEvent e) {
            dropAgent(e.getPoint());
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            switch (dropMode) {
                case AGENT:
                case RESOURCE:
                    if (dropTimer.isRunning())
                        dropTimer.stop();
                    break;
            }
            mouseLocation = null;
        }

        @Override
        public void mouseWheelMoved(MouseWheelEvent e) {
            if (isActivated(ZOOM_OPTION)
                    && e.getModifiers() == InputEvent.CTRL_MASK) {
                switch (e.getWheelRotation()) {
                    case WHEEL_UP:
                        if (panelScale < UP_SCALE_THRESHOLD)
                            panelScale += SCALE_GROW_STEP;
                        break;
                    case WHEEL_DOWN:
                        if (panelScale > DOWN_SCALE_THRESHOLD)
                            panelScale -= SCALE_GROW_STEP;
                        break;
                }
                repaintEnvironment();
            }
        }

        @Override
        public void mouseDragged(MouseEvent e) {
            if (isActivated(SCROLL_OPTION)
                    && (e.getModifiers() & InputEvent.CTRL_MASK) == InputEvent.CTRL_MASK) {
                Point currentMousePoint = e.getPoint();
                if (mouseLocation != null) {
                    //calculate diff
                    panelAbscissaDifferential +=
                            (currentMousePoint.x - mouseLocation.x);
                    panelOrdinateDifferential +=
                            (currentMousePoint.y - mouseLocation.y);
                    repaintEnvironment();
                }
                mouseLocation = currentMousePoint;
            } else {
                switch (dropMode) {
                    case AGENT:
                    case RESOURCE:
                        mouseLocation = e.getPoint();
                        if (!dropTimer.isRunning())
                            dropTimer.start();
                        break;
                }
            }
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            require(mouseLocation != null);
            dropAgent(mouseLocation);
        }

        @SuppressWarnings("unchecked")
        private void dropAgent(Point position) {
            Point2D environmentPosition =
                    internalMouseMonitor.panelToEnvironment(position);
            FieldsConfiguration configuration = Monitor.fetchConfiguration();
            configuration.updateMould(new StateFieldMould(
                    AGENT_LOCATION,
                    StandardProvider(environmentPosition)
            ));
            switch (dropMode) {
                case AGENT:
                    monitoredEnvironment.add(EnvironmentAbstractFactory
                            .getFactory(Creature.class)
                            .createAgent(configuration));
                    break;
                case RESOURCE:
                    setOption(RESOURCE_OPTION, true);
                    monitoredEnvironment.add(EnvironmentAbstractFactory
                            .getFactory(Resource.class)
                            .createAgent(configuration));
                    break;
            }
            repaintEnvironment();
        }
    }
}