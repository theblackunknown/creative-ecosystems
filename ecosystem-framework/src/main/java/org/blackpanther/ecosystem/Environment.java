package org.blackpanther.ecosystem;

import org.blackpanther.ecosystem.event.*;
import org.blackpanther.ecosystem.math.Geometry;

import java.awt.geom.Dimension2D;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.io.Serializable;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.Stack;
import java.util.logging.Logger;

import static org.blackpanther.ecosystem.Helper.require;

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
 * @version 0.3 - Sun May  1 00:00:13 CEST 2011
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

    private static final Integer AREA_WIDTH_SPLIT = 2;
    private static final Integer AREA_HEIGHT_SPLIT = 2;

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
    /**
     * Agent draw's result history
     */
    protected Set<Line2D> drawHistory = new HashSet<Line2D>(100, 0.65f);

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
    private Dimension2D dimension;


    /**
     * Default constructor which specified space bounds
     *
     * @param width  space's width
     * @param height space's height
     */
    public Environment(
            final double width,
            final double height
    ) {
        //Check preconditions
        require(width > 0, "Width must be positive and non-zero");
        require(height > 0, "Height must be positive and non-zero");

        this.id = ++idGenerator;
        this.dimension = new Geometry.Dimension(width, height);
        //initialize space - space is split in smaller areas
        //compute area dimension
        double areaWidth = width / AREA_WIDTH_SPLIT;
        double areaHeight = height / AREA_HEIGHT_SPLIT;
        //create each area
        space = new Area[AREA_WIDTH_SPLIT][AREA_HEIGHT_SPLIT];
        for (int row = 0; row < AREA_WIDTH_SPLIT; row++) {
            for (int column = 0; column < AREA_HEIGHT_SPLIT; column++) {
                space[row][column] = new Area(
                        (double) row * areaWidth,
                        (double) column * areaHeight,
                        areaWidth,
                        areaHeight);
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
     * Constructor for a square-shape space
     *
     * @param size space's width & height
     */
    public Environment(final double size) {
        this(size, size);
    }

    public Long getId() {
        return id;
    }

    public Dimension2D getDimension() {
        return (Dimension2D) this.dimension.clone();
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

    public final Set<Line2D> getHistory() {
        return new HashSet<Line2D>(drawHistory);
    }

    /**
     * Add an agent to the environment at given position.
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

    public final void addAgent(Collection<Agent> agents) {
        for (Agent agent : agents) {
            addAgent(agent);
        }
    }

    private Area getCorrespondingArea(Point2D agentLocation) {
        for (Area[] row : space) {
            for (Area area : row) {
                if (area.contains(agentLocation)) {
                    return area;
                }
            }
        }
        //FIXME Negative coordinates make position falls here
        return space[0][0];
    }

    final void recordNewLine(Line2D line) {
        eventSupport.fireLineEvent(LineEvent.Type.ADDED, line);
        drawHistory.add(line);
    }

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
     */
    public final void runNextCycle() {
        require(!endReached, "This environment has been frozen");

        if( timetracker == 0 )
            eventSupport.fireEvolutionEvent(EvolutionEvent.Type.STARTED);

        //update environment state
        boolean noMoreAgent = updatePool();

        if (noMoreAgent) {
            endThisWorld();
        } else {
            //update timeline
            logger.info(String.format("%s 's cycle %d ended, %d agents remaining", this, timetracker, pool.size()));
            timetracker++;
            eventSupport.fireEvolutionEvent(EvolutionEvent.Type.CYCLE_END);
        }
    }

    /**
     * Update the environment's state
     * Internal process.
     */
    private boolean updatePool() {

        //update all agents
        //if they die, they are simply not kept in the next pool
        for (Agent agent : pool) {
            agent.update(this);
            if (agent.isAlive()) {
                nextGenerationBuffer.add(agent);
            }
        }

        if (nextGenerationBuffer.isEmpty()) {
            return true;
        } else {
            //FIXME Next generation is lost !
            //clean the old pool
            pool.clear();

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
        child.setAreaListener(getCorrespondingArea(child.getLocation()));
        nextGenerationBuffer.push(child);
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
     * @version 0.3 - Sun May  1 00:00:13 CEST 2011
     */
    public class Area
            extends Rectangle2D
            implements Serializable, AreaListener {

        private Point2D location;
        private Dimension2D dimension;

        /**
         * Default constructor to which we inform
         * about where it is within the global space
         *
         * @param x abscissa
         * @param y ordinate
         */
        public Area(
                final double x,
                final double y,
                final double width,
                final double height) {
            location = new Point2D.Double(x, y);
            dimension = new Geometry.Dimension(width, height);
        }

        public Point2D getLocation() {
            return location;
        }

        public Dimension2D getDimension() {
            return dimension;
        }

        @Override
        public void setRect(double v, double v1, double v2, double v3) {
            throw new UnsupportedOperationException();
        }

        @Override
        public int outcode(double v, double v1) {
            throw new UnsupportedOperationException();
        }

        @Override
        public Rectangle2D createIntersection(Rectangle2D rectangle2D) {
            throw new UnsupportedOperationException();
        }

        @Override
        public Rectangle2D createUnion(Rectangle2D rectangle2D) {
            throw new UnsupportedOperationException();
        }

        @Override
        public double getX() {
            return location.getX();
        }

        @Override
        public double getY() {
            return location.getY();
        }

        @Override
        public double getWidth() {
            return dimension.getWidth();
        }

        @Override
        public double getHeight() {
            return dimension.getHeight();
        }

        @Override
        public boolean isEmpty() {
            throw new UnsupportedOperationException();
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
            logger.entering(
                    Area.class.getCanonicalName(),
                    "boolean trace(Line2D line)",
                    line);

            for (Line2D historyLine : drawHistory) {
                if (historyLine.intersectsLine(line)) {
                    //Fetch the intersection - method inspired by http://paulbourke.net/geometry/pointline/
                    double deltaX = (historyLine.getX2() - historyLine.getX1());
                    double deltaY = (historyLine.getY2() - historyLine.getY1());
                    double u = (
                            (line.getX1() - historyLine.getX1()) * deltaX
                                    + (line.getY1() - historyLine.getY1()) * deltaY
                    ) / (
                            Math.pow(deltaX, 2) + Math.pow(deltaY, 2)
                    );
                    logger.finest(String.format("[DeltaX : %.4f, DeltaY : %.4f, u : %.4f", deltaX, deltaY, u));

                    Point2D intersection = new Point2D.Double(
                            historyLine.getX1() + u * deltaX,
                            historyLine.getY1() + u * deltaY
                    );

                    //Intersection with the line's start is not an intersection
                    if (!intersection.equals(line.getP1())) {
                        logger.fine(String.format("Intersection detected : %s", intersection));

                        //We add a drawn line from agent's old location till intersection
                        recordNewLine(new Line2D.Double(
                                line.getP1(),
                                intersection
                        ));

                        //Yes, unfortunately, the agent died - this is Sparta here dude
                        return true;
                    }
                }
            }

            logger.fine("No intersection");

            //Everything went better than expected
            recordNewLine(line);
            return false;
        }
    }


}
