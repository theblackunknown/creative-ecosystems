package org.blackpanther.ecosystem;

import org.blackpanther.ecosystem.event.*;

import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.io.Serializable;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.Stack;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

import static org.blackpanther.ecosystem.helper.Helper.error;
import static org.blackpanther.ecosystem.helper.Helper.require;
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
 * @version 0.2 - Wed May 11 02:54:46 CEST 2011
 */
public abstract class Environment
        implements Serializable {

    /*
     *=========================================================================
     *                       STATIC PART
     *=========================================================================
     */
    private static final Logger logger =
            Logger.getLogger(
                    Environment.class.getCanonicalName()
            );

    private static final Long serialVersionUID = 1L;

    private static long idGenerator = 0L;
    private static int AREA_COLUMN_NUMBER = 3;
    private static int AREA_ROW_NUMBER = 3;

    /**
     * Simple check for a environment space
     * which must contains no null-case
     *
     * @param env Environment to be checked
     * @return false is there is at least one null case, true otherwise
     */
    private static boolean spaceMustBeNonNull(Environment env) {
        for (Area[] row : env.space) {
            for (Area spaceArea : row) {
                if (spaceArea == null) {
                    return false;
                }
            }
        }
        return true;
    }


    /*
    *=========================================================================
    *                       CLASS ATTRIBUTES
    *=========================================================================
    */

    private Long id;
    /**
     * Environment space
     */
    protected Area[][] space;
    /**
     * Time tracker
     */
    protected int timetracker;
    /**
     * Population
     */
    protected Set<Agent> pool;

    /*
     *=========================================================================
     *                       MISCELLANEOUS
     *=========================================================================
     */

    /**
     * Mark whether this environment has been frozen or not
     */
    private boolean endReached;
    /**
     * Component that can monitor an environment
     *
     * @see java.beans.PropertyChangeSupport
     */
    protected EnvironmentMonitor eventSupport;
    private Stack<Agent> nextGenerationBuffer = new Stack<Agent>();


    /**
     * Default constructor which specified space bounds
     */
    public Environment() {
        this.id = ++idGenerator;
        //initialize space
        space = new Area[AREA_ROW_NUMBER][AREA_COLUMN_NUMBER];
        double ordinateCursor = -Double.MAX_VALUE / 2.0;
        double abscissaCursor;
        for (int rowIndex = 0;
             rowIndex < AREA_ROW_NUMBER;
             ordinateCursor += (Double.MAX_VALUE / AREA_ROW_NUMBER),
                     rowIndex++) {

            abscissaCursor = -Double.MAX_VALUE / 2.0;
            for (int columnIndex = 0;
                 columnIndex < AREA_COLUMN_NUMBER;
                 abscissaCursor += (Double.MAX_VALUE / AREA_COLUMN_NUMBER),
                         columnIndex++) {
                space[rowIndex][columnIndex] = new Area(
                        abscissaCursor, ordinateCursor,
                        Double.MAX_VALUE / AREA_COLUMN_NUMBER, Double.MAX_VALUE / AREA_ROW_NUMBER
                );
            }
        }
        //template implementation to fill space at initialization
        initializeSpace();
        //postcondition
        require(spaceMustBeNonNull(this),
                "Wrong initialization : there is at least one null case");

        //initialize timeline
        timetracker = 0;

        //initialize pool
        pool = new HashSet<Agent>();

        //initialize environment monitor
        eventSupport = new EnvironmentMonitor(this);
    }

    /**
     * The way you initialize your space in the constructor
     */
    protected abstract void initializeSpace();

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

    /**
     * Dump the current global agent's pool at the current state
     *
     * @return copy of agent's pool
     */
    public final Set<Agent> getPool() {
        return new HashSet<Agent>(pool);
    }

    /**
     * Get the whole environment draw's history
     * TODO Good to build it every time ?
     *
     * @return environment's draw history
     */
    public final Set<Line2D> getHistory() {
        Set<Line2D> wholeHistory = new HashSet<Line2D>();
        for (Area[] row : space)
            for (Area area : row)
                wholeHistory.addAll(area.getHistory());
        return wholeHistory;
    }

    /**
     * Add an agent to the environment.
     * The added agent will be monitored by corresponding case
     * and that till its death or till it moves from there
     *
     * @param agent the agent
     */
    public final void addAgent(
            final Agent agent) {
        //Put it, in the pool
        pool.add(agent);
        //And at given position
        agent.setAreaListener(getCorrespondingArea(agent.getLocation()));
    }


    /**
     * Add an agent collection to the environment.
     * The added agent will be monitored by corresponding case
     * and that till its death or till it moves from there
     *
     * @param agents the agent collection
     */
    public final void addAgent(Collection<Agent> agents) {
        for (Agent agent : agents) {
            addAgent(agent);
        }
    }

    /**
     * Determine in which environment area a point is located
     *
     * @param agentLocation location to find a spot
     * @return corresponding area
     */
    private Area getCorrespondingArea(Point2D agentLocation) {
        //Rectangle2D is not good enough for me ...
        for (Area[] row : space)
            for (Area area : row)
                if (area.contains(agentLocation))
                    return area;
        error(String.format("No area able to manage (%.2f,%.2f)",
                agentLocation.getX(), agentLocation.getY()));
        //never reached - fuck that
        return null;
    }

    private Set<Area> getCrossedArea(Line2D line) {
        Set<Area> crossedArea = new HashSet<Area>(
                AREA_ROW_NUMBER * AREA_COLUMN_NUMBER);
        for (Area[] row : space) {
            for (final Area area : row) {
                //line is totally contained by this area
                // OMG this is so COOL !
                if (area.contains(line.getP1())
                        || area.contains(line.getP2())) {
                    crossedArea.add(area);
                }
            }
        }

//        require(crossedArea.size() > 0,
//                Geometry.toString(line) + " cross no area");

        return crossedArea;
    }

    long totalComparison = 0;

    /**
     * <p>
     * Iterate over one cycle.
     * The current process is described below :
     * </p>
     * <ol>
     * <li>Update every agent in the pool.</li>
     * <li>Remove all agent which died at this cycle</li>
     * <li>Increment timeline</li>
     * </ol>
     * <p>
     * Environment is ended if no more agents are alive at after the update step.
     * </p>
     */
    public final void runNextCycle() {
        require(!endReached, "This environment has been frozen");

        if (timetracker == 0)
            eventSupport.fireEvolutionEvent(EvolutionEvent.Type.STARTED);

        long start = System.nanoTime();
        //update environment state
        boolean noMoreAgent = updatePool();
        logger.fine(String.format(
                "Pool updated in %d milliseconds",
                TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - start)
        ));
        logger.fine(String.format(
                "%d comparisons made in this cycle",
                totalComparison
        ));

        totalComparison = 0;

        if (noMoreAgent) {
            endThisWorld();
        } else {
            //update timeline
            logger.info(String.format(
                    "%s 's cycle %d ended, %d agents remaining",
                    this,
                    timetracker,
                    pool.size()));
            timetracker++;
            eventSupport.fireEvolutionEvent(EvolutionEvent.Type.CYCLE_END);
        }
    }

    /**
     * Update the environment's state
     * Internal process.
     *
     * @return if any any agents remain in the environment
     */
    private boolean updatePool() {

        //update all agents
        //if they die, they are simply not kept in the next pool
        for (Agent agent : pool) {
            agent.update(this);
            if (agent.isAlive()) {
                nextGenerationBuffer.add(agent);
            } else {
                //TODO AgentEvent - DEAD
            }
        }
        //clean the old pool
        pool.clear();

        if (nextGenerationBuffer.isEmpty()) {
            return true;
        } else {
            //then update it
            pool.addAll(nextGenerationBuffer);
            nextGenerationBuffer.clear();
            return false;
        }
    }

    /**
     * Method to end the evolution of this world
     * and freeze its state
     */
    public final void endThisWorld() {
        endReached = true;
        logger.info(String.format("The evolution's game ended. %s's statistics[%d cycle]", this, timetracker));
        eventSupport.fireEvolutionEvent(EvolutionEvent.Type.ENDED);
    }

    @Override
    public String toString() {
        return String.format("Environment#%s", Long.toHexString(id));
    }

    /*=====================================================================
    *                   LISTENERS
    *=====================================================================
    */

    public void addLineListener(LineListener listener) {
        eventSupport.addLineListener(listener);
    }

    public void addEvolutionListener(EvolutionListener listener) {
        eventSupport.addEvolutionListener(listener);
    }

    public void nextGeneration(Agent child) {
        //TODO AgentEvent - BORN
        child.setAreaListener(getCorrespondingArea(child.getLocation()));
        nextGenerationBuffer.push(child);
    }


    long comparison = 0;

    public boolean trace(Line2D line) {
        Set<Area> crossedAreas = getCrossedArea(line);
        boolean agentDeath = false;
        for (Area area : crossedAreas) {
            agentDeath = area.trace(line) || agentDeath;
        }
        logger.finer(String.format(
                "%d comparison to place a line",
                comparison
        ));
        totalComparison += comparison;
        comparison = 0;
        return agentDeath;
    }

    public void removeEvolutionListener(EvolutionListener listener) {
        eventSupport.removeEvolutionListener(listener);
    }

    public void removeLineListener(LineListener listener) {
        eventSupport.removeLineListener(listener);
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
     * @version 0.2 - Wed May 11 02:54:46 CEST 2011
     */
    public class Area
            extends Rectangle2D.Double
            implements Serializable, AreaListener {

        private Set<Line2D> internalDrawHistory = new HashSet<Line2D>(100, 0.65f);

        /**
         * Default constructor to which we inform
         * about where it is within the global space
         *
         * @param x
         * @param y
         * @param w
         * @param h
         */
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
        @Override
        public boolean trace(Line2D line) {

            for (Line2D historyLine : internalDrawHistory) {
                comparison++;

                Point2D intersection = getIntersection(historyLine, line);

                if (intersection != null
                        //Intersection with the line's start is not an intersection
                        && !intersection.equals(line.getP1())) {

                    Line2D realLine = new Line2D.Double(
                            line.getP1(),
                            intersection
                    );

                    //We add a drawn line from agent's old location till intersection
                    internalDrawHistory.add(realLine);
                    eventSupport.fireLineEvent(LineEvent.Type.ADDED, realLine);
                    //Yes, unfortunately, the agent died - this is Sparta here dude
                    return true;
                }

            }

            //Everything went better than expected
            internalDrawHistory.add(line);
            eventSupport.fireLineEvent(LineEvent.Type.ADDED, line);
            return false;
        }
    }


}
