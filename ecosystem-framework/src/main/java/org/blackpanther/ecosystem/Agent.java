package org.blackpanther.ecosystem;

import org.blackpanther.ecosystem.math.Geometry;

import java.awt.*;
import java.awt.geom.Point2D;
import java.io.Serializable;

import static org.blackpanther.ecosystem.Helper.require;

/**
 * <p>
 * Component which represent an agent within an ecosystem.
 * It implements all basic features for an agent,
 * specific behaviour are left to subclasses
 * </p>
 *
 * @author MACHIZAUD Andréa
 * @version 0.3 - Sun May  1 00:00:13 CEST 2011
 */
public abstract class Agent
        implements Serializable {

    /**
     * Serializable identifier
     */
    private static final Long serialVersionUID = 1L;

    /*=========================================
     *                 GENOTYPE
     *=========================================
     */

    /**
     * Direction in which the agent moves
     */
    private Geometry.Direction2D initial_direction;
    /**
     * Speed of the agent. Varies in <pre>[0,3]</pre>
     */
    private Double initial_speed;
    /**
     * Probability that the agent dies
     */
    private Double mortalityRate;
    /**
     * Probability that the agent spawn a child
     */
    private Double fecundityRate;
    /**
     * Probability that a mutation happens
     */
    private Double mutationRate;
    /**
     * Agent's identifier. Design choice is to implement it as a {@link Color}
     */
    private Color identifier = Color.BLACK;

    //TODO Extensible genotype

    /*=========================================
     *                 PHENOTYPE
     *=========================================
     */
    /**
     * Age of the agent. Same unit as the {@link Environment}
     */
    private int age = 0;
    /**
     * Current dx & dy
     */
    private Geometry.Direction2D current_direction;
    /**
     * Current ddx & ddy
     */
    private double current_curvature;
    /**
     * Current speed
     */
    private double current_speed;
    /**
     * Current location if the agent is still alive,
     * otherwise position where it died
     */
    private Point2D current_location;

    //TODO Extensible phenotype

    /**
     * Agent's behavior manager.
     * Responsible for agent's interaction
     */
    private BehaviorManager behaviorManager;
    /**
     * Agent's area listener
     */
    private AreaListener areaListener;

    /**
     * Create an agent
     * TODO Handle parents
     * TODO Don't keep same genotype than parents
     * TODO Make genotype and phenotype more general (e.g Map with type checking at runtime)
     *
     * @param spawnLocation    initial location of the agent in the environment
     * @param initialDirection its initial movement direction
     * @param initialCurvature initial curvature
     * @param initialSpeed     initial speed
     * @param initialMortality initial mortality rate
     * @param initialFecundity initial fecundity rate
     * @param initialMutation  initial mutation rate
     * @param manager          Behaviour Strategy to use for this agent
     */
    public Agent(
            final Point2D spawnLocation,
            final Geometry.Direction2D initialDirection,
            final double initialCurvature,
            final double initialSpeed,
            final double initialMortality,
            final double initialFecundity,
            final double initialMutation,
            final BehaviorManager manager
    ) {
        //TODO Normalize range
        require(initialSpeed >= 0, "Speed must be positive");
        require(0.0 <= initialMortality && initialMortality <= 1.0,
                "A probability is expressed in [0.0,1.0] interval");
        require(0.0 <= initialFecundity && initialFecundity <= 1.0,
                "A probability is expressed in [0.0,1.0] interval");
        require(0.0 <= initialMutation && initialMutation <= 1.0,
                "A probability is expressed in [0.0,1.0] interval");
        require(manager != null, "You must provide a BehaviourManager");

        this.current_location = new Point2D.Double(spawnLocation.getX(), spawnLocation.getY());
        this.current_direction = this.initial_direction = initialDirection;
        this.current_curvature = initialCurvature;
        this.current_speed = this.initial_speed = initialSpeed;
        this.mortalityRate = initialMortality;
        this.fecundityRate = initialFecundity;
        this.mutationRate = initialMutation;
        this.behaviorManager = manager;
    }

    /**
     * Update agent state according to given environment and agent behaviour manager
     *
     * @param env given environment in which the agent evolves
     */
    public final void update(final Environment env) {
        behaviorManager.update(env, this);
    }

    /**
     * Check if this agent has been set in any environment
     *
     * @return <code>true</code> if the agent has been set in an {@link Environment},
     *         <code>false</code> otherwise
     */
    public final boolean isAlive() {
        return areaListener != null;
    }

    /**
     * Set the {@link AreaListener}
     *
     * @param listener the new area listener
     */
    final void setAreaListener(final AreaListener listener) {
        areaListener = listener;
    }

    /**
     * Unset the current area listener if any
     */
    final void unsetAreaListener() {
        areaListener = null;
    }

    AreaListener getAreaListener() {
        return areaListener;
    }

    /* ================================================
     *               GETTERS
     * ================================================
     */


    public Point2D getLocation() {
        return current_location;
    }

    /**
     * Get the current agent's direction
     *
     * @return current direction
     */
    public Geometry.Direction2D getDirection() {
        return current_direction;
    }

    /**
     * Get the current agent's curvature
     *
     * @return current curvature
     */
    public Double getCurvature() {
        return current_curvature;
    }

    /**
     * Get the current agent's initial_speed
     *
     * @return current initial_speed
     */
    public Double getSpeed() {
        return current_speed;
    }

    /**
     * Get the current agent's mortality rate
     *
     * @return current mortality rate
     */
    public Double getMortalityRate() {
        return mortalityRate;
    }

    /**
     * Get the current agent's fecundity rate
     *
     * @return current fecundity rate
     */
    public Double getFecundityRate() {
        return fecundityRate;
    }

    /**
     * Get the current agent's mutation rate
     *
     * @return current mutation rate
     */
    public Double getMutationRate() {
        return mutationRate;
    }

    /**
     * Get the agent's age
     *
     * @return actual age of the agent
     */
    public Integer getAge() {
        return age;
    }

    /*=========================================================================
     *                 SETTERS
     * Visibility is set to package because
     * they are meant to be modified by nothing except the behaviour manager
     *=========================================================================
     */

    final void setLocation(Point2D target) {
        setLocation(target.getX(), target.getY());
    }

    final void setLocation(double abscissa, double ordinate) {
        this.current_location = new Point2D.Double(abscissa, ordinate);
    }

    /**
     * Setter for the agent's direction
     *
     * @param direction the new direction
     */
    void setDirection(Geometry.Direction2D direction) {
        this.current_direction = direction;
    }

    /**
     * Setter for the agent's curvature
     *
     * @param direction the new curvature
     */
    void setCurvature(Double curvature) {
        this.current_curvature = curvature;
    }


    /**
     * Setter for the agent's speed
     *
     * @param initial_speed the new speed
     */
    void setSpeed(Double speed) {
        this.current_speed = speed;
    }


    /**
     * Setter for the agent's age
     *
     * @param age the new age
     */
    void setAge(int age) {
        this.age = age;
    }


    /**
     * Setter for the agent's behavior manager
     *
     * @param behaviorManager the new behavior manager
     */
    void setBehaviorManager(BehaviorManager behaviorManager) {
        this.behaviorManager = behaviorManager;
    }

    /**
     * FIXME Erase it as soon as another solution pop out
     *
     * @param v
     */
    void setFecundityRate(double v) {
        this.fecundityRate = v;
    }

    @Override
    public String toString() {
        return String.format("%s[Age=%d]",
                super.toString(), age);
    }
}
