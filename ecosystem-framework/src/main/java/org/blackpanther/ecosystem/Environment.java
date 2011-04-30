package org.blackpanther.ecosystem;

import org.blackpanther.ecosystem.math.Geometry;

import java.awt.geom.Dimension2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

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
 * @version v0.2.1 - Sun Apr 24 18:01:06 CEST 2011
 */
public abstract class Environment
        implements Serializable {

    /*
     *=========================================================================
     *                       STATIC PART
     *=========================================================================
     */

    private static final Long serialVersionUID = 1L;

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
    protected Set<Geometry.Vector2D> drawHistory = new HashSet<Geometry.Vector2D>(100, 0.65f);

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
    public Environment(final int size) {
        this(size, size);
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

    public final Set<Geometry.Vector2D> getHistory() {
        return new HashSet<Geometry.Vector2D>(drawHistory);
    }

    /**
     * Add an agent to the environment at given position.
     * The added agent will be monitored by corresponding case
     * and that till its death or till it moves from there
     *
     * @param agent the agent
     * @param x     abscissa
     * @param y     ordinate
     */
    public final void addAgent(
            final Agent agent) {
        //FIXME Check agent out of bounds
        //Put it, in the pool
        pool.add(agent);
        //And at given position
        agent.setAreaListener(getCorrespondingArea(agent.getLocation()));
    }

    private Area getCorrespondingArea(Point2D agentLocation) {
        for (Area[] row : space) {
            for (Area area : row) {
                if (area.contains(agentLocation)) {
                    return area;
                }
            }
        }
        return null;
    }

    final void recordNewLine(Geometry.Vector2D line) {
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

        //update environment state
        updatePool();

        //update timeline
        timetracker++;
    }

    /**
     * Update the environment's state
     * Internal process.
     */
    private void updatePool() {
        //Create next generation pool
        Set<Agent> nextPool = new HashSet<Agent>(pool.size());

        //update all agents
        //if they die, they are simply not kept in the next pool
        for (Agent agent : pool) {
            agent.update(this);
            if (!agent.isAlive()) {
                nextPool.add(agent);
            }
        }

        //clean the old pool
        pool.clear();

        //then update it
        pool.addAll(nextPool);
    }

    /**
     * Method to end the evolution of this world
     * and freeze its state
     */
    public final void endThisWorld() {
        endReached = true;
        endThisWorldHook();
    }

    public abstract void endThisWorldHook();

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
     * @version v0.2.1 - Sun Apr 24 18:01:06 CEST 2011
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
    }

}
