package org.blackpanther.ecosystem;

import org.blackpanther.ecosystem.agent.Creature;
import org.blackpanther.ecosystem.agent.Resource;
import org.blackpanther.ecosystem.event.*;
import org.blackpanther.ecosystem.line.TraceHandler;
import org.blackpanther.ecosystem.math.Geometry;

import java.awt.geom.Dimension2D;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.io.Serializable;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

import static org.blackpanther.ecosystem.SensorTarget.detected;
import static org.blackpanther.ecosystem.helper.Helper.EPSILON;
import static org.blackpanther.ecosystem.helper.Helper.computeOrientation;
import static org.blackpanther.ecosystem.math.Geometry.getIntersection;

/**
 * <p>
 * Component designed to represent an general ecosystem
 * But let's first use some basic non general features...
 * </p>
 * <ul>
 * <li>2D grid as space</li>
 * <li>fixed-size bounds (<i>size means number of case width</i>)</li>
 * <li>Trace timeline by cycle</li>
 * </ul>
 *
 * @author MACHIZAUD Andréa
 * @version 1.0-alpha - Tue May 24 23:49:57 CEST 2011
 */
public class Environment
        implements Serializable, Cloneable, AgentListener {

    /*
     *=========================================================================
     *                       STATIC PART
     *=========================================================================
     */
    private static final Logger logger =
            Logger.getLogger(
                    Environment.class.getCanonicalName()
            );

    private static final long serialVersionUID = 5L;

    private static long idGenerator = 0L;
    private static int AREA_COLUMN_NUMBER = 50;
    private static int AREA_ROW_NUMBER = 50;


    /*
    *=========================================================================
    *                       CLASS ATTRIBUTES
    *=========================================================================
    */

    private Long id = ++idGenerator;
    /**
     * Environment space
     */
    protected Area[][] space;
    private Rectangle2D bounds;
    /**
     * Time tracker
     */
    protected int timetracker;
    /**
     * Population
     */
    protected Collection<Creature> creaturePool = new ArrayList<Creature>();
    private Collection<Resource> resourcePool = new ArrayList<Resource>();

    /*
     *=========================================================================
     *                       MISCELLANEOUS
     *=========================================================================
     */
    /**
     * Component that can monitor an environment
     *
     * @see java.beans.PropertyChangeSupport
     */
    protected EnvironmentMonitor eventSupport;
    private Stack<Creature> nextGenerationBuffer = new Stack<Creature>();
    private Stack<Creature> deadAgents = new Stack<Creature>();
    private Line2D[] boundLines = new Line2D[4];

    public Environment(double width, double height) {
        this(new Geometry.Dimension(width, height));
    }

    public Environment(Dimension2D dimension) {
        this.bounds = dimension == null
                ? new Rectangle2D.Double(
                -Double.MAX_VALUE / 2.0,
                -Double.MAX_VALUE / 2.0,
                Double.MAX_VALUE,
                Double.MAX_VALUE)
                : new Rectangle2D.Double(
                -dimension.getWidth() / 2.0,
                -dimension.getHeight() / 2.0,
                dimension.getWidth(),
                dimension.getHeight());
        //NORTH LINE
        boundLines[0] = new Line2D.Double(
                bounds.getX(), bounds.getY(),
                bounds.getX() + bounds.getWidth(), bounds.getY()
        );
        //EAST LINE
        boundLines[1] = new Line2D.Double(
                bounds.getX() + bounds.getWidth(), bounds.getY(),
                bounds.getX() + bounds.getWidth(), bounds.getY() + bounds.getHeight()
        );
        //SOUTH LINE
        boundLines[2] = new Line2D.Double(
                bounds.getX(), bounds.getY() + bounds.getHeight(),
                bounds.getX() + bounds.getWidth(), bounds.getY() + bounds.getHeight()
        );
        //WEST LINE
        boundLines[3] = new Line2D.Double(
                bounds.getX(), bounds.getY(),
                bounds.getX(), bounds.getY() + bounds.getHeight()
        );
        //initialize space
        space = new Area[AREA_ROW_NUMBER][AREA_COLUMN_NUMBER];
        double abscissaStartValue = bounds.getX();
        double ordinateStartValue = bounds.getY();
        double abscissaStep = bounds.getWidth() / AREA_COLUMN_NUMBER;
        double ordinateStep = bounds.getHeight() / AREA_ROW_NUMBER;

        double ordinateCursor = ordinateStartValue;
        double abscissaCursor;
        for (int rowIndex = 0;
             rowIndex < AREA_ROW_NUMBER;
             ordinateCursor += ordinateStep,
                     rowIndex++) {
            abscissaCursor = abscissaStartValue;
            for (int columnIndex = 0;
                 columnIndex < AREA_COLUMN_NUMBER;
                 abscissaCursor += abscissaStep,
                         columnIndex++) {
                space[rowIndex][columnIndex] = new Area(
                        abscissaCursor, ordinateCursor,
                        abscissaStep, ordinateStep
                );
            }
        }

        //initialize timeline
        timetracker = 0;

        getEventSupport().addAgentListener(this);
    }

    /**
     * Internal unique environment identifier
     *
     * @return environment identifier
     */
    public Long getId() {
        return id;
    }

    /**
     * Get current time (expressed as number of evolution's cycle)
     * since evolution has begun
     *
     * @return number of cycles since evolution's beginning
     */
    public final int getTime() {
        return timetracker;
    }

    public Rectangle2D getBounds() {
        return bounds;
    }

    /**
     * Dump the current global agent's creaturePool at the current state
     *
     * @return copy of agent's creaturePool
     */
    public final Collection<Creature> getCreaturePool() {
        return creaturePool;
    }

    /**
     * Get the whole environment draw's history
     *
     * @return environment's draw history
     */
    public Set<Line2D> getHistory() {
        Set<Line2D> wholeHistory = new HashSet<Line2D>();
        for (Area[] row : space)
            for (Area area : row)
                wholeHistory.addAll(area.getHistory());
        return wholeHistory;
    }

    public final Collection<Resource> getResources() {
        return resourcePool;
    }

    /**
     * Add an monster to the environment.
     * The added monster will be monitored by corresponding case
     * and that till its death or till it moves from there
     *
     * @param mosntergent the monster
     */
    public final void add(final Creature monster) {
        if (bounds.contains(monster.getLocation())) {
            monster.attachTo(this);
            creaturePool.add(monster);
        }
    }

    public final void add(final Resource resource) {
        if (bounds.contains(resource.getLocation())) {
            resource.attachTo(this);
            resourcePool.add(resource);
        }
    }


    /**
     * Add an agent collection to the environment.
     * The added agent will be monitored by corresponding case
     * and that till its death or till it moves from there
     *
     * @param monsters the agent collection
     */
    public final void addCreatures(Collection<Creature> monsters) {
        for (Creature monster : monsters)
            add(monster);
    }

    public final void addResources(
            final Collection<Resource> resources) {
        for (Resource resource : resources)
            add(resource);
    }

    private Set<Area> getCrossedArea(Point2D from, Point2D to) {
        Set<Area> crossedArea = new HashSet<Area>(
                AREA_ROW_NUMBER * AREA_COLUMN_NUMBER);
        for (Area[] row : space) {
            for (final Area area : row) {
                //line is totally contained by this area
                // OMG this is so COOL !
                if (area.contains(from) || area.contains(to)) {
                    crossedArea.add(area);
                }
            }
        }
        return crossedArea;
    }

    long totalComparison = 0;

    /**
     * <p>
     * Iterate over one cycle.
     * The current process is described below :
     * </p>
     * <ol>
     * <li>Update every agent in the creaturePool.</li>
     * <li>Remove all agent which died at this cycle</li>
     * <li>Increment timeline</li>
     * </ol>
     * <p>
     * Environment is ended if no more agents are alive at after the update step.
     * </p>
     */
    public final void runNextCycle() {
        if (timetracker == 0)
            getEventSupport().fireEvolutionEvent(EvolutionEvent.Type.STARTED);

        long start = System.nanoTime();
        //update environment state
        updatePool();
        logger.fine(String.format(
                "Pool updated in %d milliseconds",
                TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - start)
        ));
        logger.fine(String.format(
                "%d comparisons made in this cycle",
                totalComparison
        ));
        totalComparison = 0;

        //update timeline
        logger.info(String.format(
                "%s 's cycle %d ended, %d agents remaining",
                this,
                timetracker,
                creaturePool.size()));
        timetracker++;
        getEventSupport().fireEvolutionEvent(EvolutionEvent.Type.CYCLE_END);

        if (creaturePool.size() == 0) {
            endThisWorld();
        }
    }

    /**
     * Update the environment's state
     * Internal process.
     */
    private void updatePool() {

        //update all agents
        //if they die, they are simply not kept in the next creaturePool
        for (Creature monster : creaturePool) {
            monster.update(this);
        }
        for (Creature monster : deadAgents)
            creaturePool.remove(monster);

        deadAgents.clear();
        creaturePool.addAll(nextGenerationBuffer);
        nextGenerationBuffer.clear();
    }

    /**
     * Method to end the evolution of this world
     * and freeze its state
     */
    public final void endThisWorld() {
        logger.info(String.format("The evolution's game paused. %s's statistics[%d cycle]", this, timetracker));
        getEventSupport().fireEvolutionEvent(EvolutionEvent.Type.ENDED);
    }

    @Override
    public String toString() {
        return String.format("Environment#%s", Long.toHexString(id));
    }

    @Override
    public Environment clone() {
        Environment copy = new Environment(getBounds().getWidth(), getBounds().getHeight());
        for (Creature monster : creaturePool)
            copy.add(monster.clone());
        for (Resource resource : resourcePool)
            copy.add(resource.clone());
        for (int i = 0; i < copy.space.length; i++)
            for (int j = 0; j < copy.space[0].length; j++)
                copy.space[i][j].internalDrawHistory.addAll(
                        space[i][j].internalDrawHistory);
        return copy;
    }

    /*=====================================================================
    *                   LISTENERS
    *=====================================================================
    */

    public void addAgentListener(AgentListener listener) {
        getEventSupport().addAgentListener(listener);
    }

    public void addLineListener(LineListener listener) {
        getEventSupport().addLineListener(listener);
    }

    public void addEvolutionListener(EvolutionListener listener) {
        getEventSupport().addEvolutionListener(listener);
    }

    public void nextGeneration(Creature child) {
        child.attachTo(this);
        nextGenerationBuffer.push(child);
    }

    public void kill(Creature dead) {
        deadAgents.push(dead);
    }

    long comparison = 0;

    public boolean move(Creature that, Point2D from, Point2D to) {
        Set<Area> crossedAreas = getCrossedArea(from, to);
        boolean collision = false;
        for (Area area : crossedAreas) {
            collision = area.trace(that, from, to) || collision;
        }
        logger.finer(String.format(
                "%d comparison to place a line",
                comparison
        ));
        totalComparison += comparison;
        comparison = 0;
        return collision;
    }

    public void removeAgentListener(AgentListener listener) {
        getEventSupport().removeAgentListener(listener);
    }

    public void removeEvolutionListener(EvolutionListener listener) {
        getEventSupport().removeEvolutionListener(listener);
    }

    public void removeLineListener(LineListener listener) {
        getEventSupport().removeLineListener(listener);
    }

    public EnvironmentMonitor getEventSupport() {
        if (eventSupport == null)
            eventSupport = new EnvironmentMonitor(this);
        return eventSupport;
    }

    public void clearAllExternalsListeners() {
        eventSupport.clearAllExternalsListeners();
    }

    @Override
    public void update(AgentEvent e) {
        switch (e.getType()) {
            case DEATH:
                if (e.getAgent() instanceof Resource) {
                    resourcePool.remove(e.getAgent());
                    break;
                }
        }
    }

    public SenseResult aggregateInformation(Geometry.Circle circle) {
        Collection<SensorTarget<Creature>> detectedAgents = new LinkedList<SensorTarget<Creature>>();
        for (Creature monster : creaturePool)
            if (circle.contains(monster.getLocation())
                    && !(monster.getLocation().distance(circle.getCenter()) < EPSILON)) {
                double agentOrientation =
                        computeOrientation(
                                circle.getCenter(),
                                monster.getLocation());
                detectedAgents.add(detected(agentOrientation, monster));
            }
        Collection<SensorTarget<Resource>> detectedResources = new LinkedList<SensorTarget<Resource>>();
        for (Resource resource : resourcePool)
            if (circle.contains(resource.getLocation())
                    && !(resource.getLocation().distance(circle.getCenter()) < EPSILON)) {
                double resourceOrientation =
                        computeOrientation(
                                circle.getCenter(),
                                resource.getLocation());
                detectedResources.add(detected(resourceOrientation, resource));
            }

        return new SenseResult(detectedAgents, detectedResources);
    }

    /**
     * <p>
     * Component designed to represent a state of a grid space
     * It can be consider as a small viewport of the global space.
     * It has the ability to monitor agent in its area,
     * for example it can provide useful information
     * - like the number of close agents
     * - how close they are
     * - which they are
     * to its own population within its area
     * </p>
     *
     * @author MACHIZAUD Andréa
     * @version 1.0-alpha - Tue May 24 23:49:57 CEST 2011
     */
    public class Area
            extends Rectangle2D.Double
            implements Serializable {

        private Collection<Line2D> internalDrawHistory = new LinkedList<Line2D>();

        public Area(double x, double y, double w, double h) {
            super(x, y, w, h);
        }

        public Collection<Line2D> getHistory() {
            return internalDrawHistory;
        }

        /**
         * Add a line to draw in the drawn line history.
         * It determines if the given line cross an already drawn one,
         * if that event happens, it will save a line from given line's origin to the intersection point
         *
         * @param line line to draw
         * @return <code>true</code> if draughtsman must be die after his movement,
         *         <code>false</code> otherwise.
         */
        public boolean trace(Creature that, Point2D from, Point2D to) {
            Line2D agentLine = TraceHandler.trace(from, to, that);
            //go fly away little birds, you no longer belong to me ...
            if (!bounds.contains(to)) {
                //detect which border has been crossed and where
                for (Line2D border : boundLines) {
                    Point2D intersection = getIntersection(border, agentLine);

                    if (intersection != null) {
                        Line2D realLine = TraceHandler.trace(from, intersection, that);
                        //We add a drawn line from agent's old location till intersection
                        internalDrawHistory.add(realLine);

                        getEventSupport().fireLineEvent(LineEvent.Type.ADDED, realLine);
                        //Yes, unfortunately, the agent died - this is Sparta here dude
                        return true;
                    }
                }
                throw new RuntimeException("Border detection failed");
            }

            for (Line2D historyLine : internalDrawHistory) {
                //check if this line can cross the history line before computing intersection (compute is cheaper)
                if (!TraceHandler.canCross(agentLine, historyLine)) {
                    Point2D intersection = getIntersection(historyLine, agentLine);
                    if (intersection != null
                            //Intersection with the line's start is not an intersection
                            && !intersection.equals(from)) {
                        Line2D realLine = TraceHandler.trace(from, intersection, that);
                        //We add a drawn line from agent's old location till intersection
                        internalDrawHistory.add(realLine);
                        getEventSupport().fireLineEvent(LineEvent.Type.ADDED, realLine);
                        //Yes, unfortunately, the agent died - this is Sparta here dude
                        return true;
                    }
                }
            }

            //Everything went better than expected
            internalDrawHistory.add(agentLine);
            getEventSupport().fireLineEvent(LineEvent.Type.ADDED, agentLine);
            return false;
        }
    }


}