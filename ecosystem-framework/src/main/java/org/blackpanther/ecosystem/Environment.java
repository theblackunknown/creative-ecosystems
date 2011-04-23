package org.blackpanther.ecosystem;

import java.awt.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import static org.blackpanther.ecosystem.Helper.require;
import static org.blackpanther.ecosystem.Configuration.Configuration;

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
 * @version ${{version}} - ${{date}}
 */
public abstract class Environment
        implements Serializable {

    private static final Long serialVersionUID = 1L;

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

    /**
     * Default constructor which specified space bounds
     *
     * @param width  - space's width
     * @param height - space's height
     */
    public Environment(
            int width,
            int height
    ) {
        //Check preconditions
        require(width > 0, "Width must be positive and non-zero");
        require(height > 0, "Height must be positive and non-zero");

        //initialize space
        space = new Case[width][height];
        for (int row = 0; row < width; row++)
            for (int column = 0; column < height; column++)
                space[row][column] = new Case(row,column);

        //initalize timeline
        timetracker = 0;

        //initialize pool
        pool = new HashSet<Agent>();
    }

    /**
     * Constructor for a square-shape space
     *
     * @param size - space's width & height
     */
    public Environment(int size) {
        this(size, size);
    }

    /**
     * Add an agent to the environment at given position
     *
     * @param agent - the agent
     * @param x     - abscissa
     * @param y     - ordinate
     */
    public void addAgent(Agent agent, int x, int y) {
        require(0 <= x && x < space.length, "You can't add an agent out of space's bounds");
        require(0 <= y && y < space[0].length, "You can't add an agent out of space's bounds");
        //Put it, in the pool
        pool.add(agent);
        //And at given position
        space[x][y].addAgent(agent);
    }

    /**
     * Add an agent at a random position
     * @param agent - the agent
     */
    public void addAgent(Agent agent) {
        addAgent(
                agent,
                Configuration.getRandom().nextInt() % space.length,
                Configuration.getRandom().nextInt() % space[0].length
        );
    }

    /**
     * Get a space at its current state
     *
     * @return space's state
     */
    public Case[][] dumpSpace() {
        return space.clone();
    }

    /**
     * Get current time since evolution has begun
     *
     * @return number of cycles since evolution's beginning
     */
    public int getTime() {
        return timetracker;
    }

    /**
     * Get a copy of agent's pool at the current state
     *
     * @return copy of agent's pool
     */
    public Set<Agent> getPool() {
        return new HashSet<Agent>(pool);
    }

    /**
     * Iterate over one cycle
     */
    public void runNextCycle() {
        //update environment state
        updatePool();

        //update timeline
        timetracker++;
    }

    /**
     * Update the environment's state
     */
    private void updatePool() {
        //Create next generation pool
        Set<Agent> nextPool = new HashSet<Agent>(pool.size());

        //update all agents
        //if they die, they are simply not kept in the next pool
        for (Agent agent : pool) {
            agent.update(this);
            if( !agent.isNowhere() )
                 nextPool.add(agent);
        }

        //clean the old pool
        pool.clear();

        //then update it
        pool.addAll( nextPool );
    }

    /**
     * <p>
     * Component designed to represent a state of a grid space
     * But I don't know yet which information to save in
     * </p>
     *
     * @author MACHIZAUD Andréa
     * @version ${{version}} - ${{date}}
     */
    protected static class Case
            implements Serializable, AreaListener {

        private static final Long serialVersionUID = 1L;

        protected Set<Agent> subpopulation;
        protected Point coordinates;

        public Case(int x, int y) {
            coordinates = new Point(x, y);
        }

        public void addAgent(Agent agent) {
            subpopulation.add(agent);
            agent.setAreaListener(this);
        }

        public void removeAgent(Agent agent) {
            subpopulation.remove(agent);
            agent.unsetAreaListener();
        }
    }

}
