package org.blackpanther.ecosystem;

import java.awt.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import static org.blackpanther.ecosystem.Configuration.Configuration;
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

    /**
     * Simple check for a environment space
     * which must contains no null-case
     * @param env
     *      Environment to be checked
     * @return
     *      false is there is at least one null case, true otherwise
     */
    private static boolean spaceMustBeNonNull(Environment env) {
        for(Case[] row : env.space) {
            for(Case spaceCase : row) {
                if ( spaceCase == null ) {
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
    protected Case[][] space;
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
     * Default constructor which specified space bounds
     *
     * @param width  space's width
     * @param height space's height
     */
    public Environment(
            final int width,
            final int height
    ) {
        //Check preconditions
        require(width > 0, "Width must be positive and non-zero");
        require(height > 0, "Height must be positive and non-zero");

        //initialize space
        space = new Case[width][height];
        for(int row = 0; row < space.length;row++) {
            for(int column = 0; column < space[0].length; column++) {
                space = new this.Case(row,column);
            }
        }
        initializeSpace();
        //postcondition
        require(spaceMustBeNonNull(),
            "Wrong initialization : there is at least one null case");

        //initalize timeline
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
     * Add an agent to the environment at given position.
     * The added agent will be monitored by corresponding case
     * and that till its death or till it moves from there
     *
     * @param agent the agent
     * @param x     abscissa
     * @param y     ordinate
     */
    public final void addAgent(
            final Agent agent,
            final int x,
            final int y) {
        require(!endReached, "This environment has been frozen");
        require(0 <= x && x < space.length,
                "You can't add an agent out of space's bounds");
        require(0 <= y && y < space[0].length,
                "You can't add an agent out of space's bounds");
        //Put it, in the pool
        pool.add(agent);
        //And at given position
        space[x][y].addAgent(agent);
    }

    /**
     * Add an agent at a random position
     * The added agent will be monitored by corresponding case
     * and that till its death or till it moves from there
     *
     * @param agent the agent
     */
    public final void addAgent(
            final Agent agent) {
        addAgent(
                agent,
                Configuration.getRandom().nextInt() % space.length,
                Configuration.getRandom().nextInt() % space[0].length
        );
    }

    /**
     * Dump the current space state
     *
     * @return space's state
     */
    public final Case[][] dumpSpace() {
        return space.clone();
    }

    /**
     * Get current time (expressend as number of evolution's cycle)
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
     * <p>
     *      Iterate over one cycle.
     *      The current process is described below :
     * </p>
     * <ol>
     *  <li>Update every agent in the pool.</li>
     *  <li>Remove all agent which died at this cycle</li>
     *  <li>Increment timeline</li>
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
            if (!agent.isNowhere()) {
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
    public void endThisWorld() {
        endReached = true;
    }
        

    /**
     * <p>
     * Component designed to represent a state of a grid space
     * It can be consider as a small viewport of the global space.
     * It has the ability to monitor agent in its area,
     * for example it can provide useful informations 
     *  - like the number of close agents
     *  - how close they are
     *  - which they are
     * to its own population within its area
     * </p>
     *
     * @author MACHIZAUD Andréa
     * @version v0.2.1 - Sun Apr 24 18:01:06 CEST 2011
     * FIXME Test Another representation
     */
    public class Case
            implements Serializable, AreaListener {

        /** Serialization support */
        private static final Long serialVersionUID = 1L;

        /** 
         * Buffer of agent within its area 
         * <note>Updated at each cycle due to agents' interaction!</note>
         */
        private Set<Agent> subpopulation = new HashSet<Agent>();
        /**
         * Position of this case within global space
         */
        private Point coordinates;

        /**
         * Default constructor to which we inform 
         * about where it is within the global space
         * @param x     abscissa
         * @param y     ordinate
         */
        public Case(
                final int x,
                final int y) {
            coordinates = new Point(x, y);
        }

        /**
         * Add an agent to subpool
         * @param agent
         *      new agent in this area
         */
        public final void addAgent(final Agent agent) {
            require(!endReached, "This environment has been frozen");
            subpopulation.add(agent);
            agent.setAreaListener(this);
        }


        /**
         * Remove an agent from subpool
         * @param agent
         *      old agent in this area
         */
        public final void removeAgent(final Agent agent) {
            require(!endReached, "This environment has been frozen");
            subpopulation.remove(agent);
            agent.unsetAreaListener();
        }

        /**
         * Dump the current subpopulation
         * @return 
         *      current subpopulation
         */
        public final Set<Agent> getSubpopulation() {
            return new HashSet<Agent>(subpopulation);
        }
    }

}
