package org.blackpanther.ecosystem;

import java.awt.*;
import java.awt.geom.Point2D;
import java.io.Serializable;
import java.util.Random;

import static org.blackpanther.ecosystem.Configuration.Configuration;
import static org.blackpanther.ecosystem.Configuration.RANDOM;
import static org.blackpanther.ecosystem.helper.Helper.require;

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
     * Direction in which the agent moves.
     * Expressed as an angle [0,2PI]
     */
    private Double childOrientationLauncher;
    /**
     * Speed of the agent. Varies in <pre>[0,3]</pre>
     */
    private Double childSpeedLauncher;
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
    /**
     * Agent's behavior manager.
     * Responsible for agent's interaction
     */
    private BehaviorManager behaviorManager;

    //TODO Extensible genotype

    /*=========================================
     *                 PHENOTYPE
     *=========================================
     */
    /**
     * Current dx & dy
     */
    private Double current_orientation;
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

    /*=========================================================================
                                    MISCELLANEOUS
      =========================================================================*/
    /**
     * Age of the agent. Same unit as the {@link Environment}
     */
    private int age = 0;
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
     * @param spawnLocation          initial location of the agent in the environment.
     * @param launchOrientation      initial movement orientation
     * @param childLaunchOrientation orientation initial value for next generation from this parent
     * @param initialCurvature       initial curvature
     * @param launchSpeed            initial speed
     * @param childLaunchSpeed       orientation initial value for next generation from this parent
     * @param initialMortality       initial mortality rate
     * @param initialFecundity       initial fecundity rate
     * @param initialMutation        initial mutation rate
     * @param manager                Behaviour Strategy to use for this agent
     * @see java.awt.geom.Point2D.Double
     * @see org.blackpanther.ecosystem.BehaviorManager
     */
    public Agent(
            final Point2D spawnLocation,
            final Double launchOrientation,
            final Double childLaunchOrientation,
            final Double initialCurvature,
            final Double launchSpeed,
            final Double childLaunchSpeed,
            final Double initialMortality,
            final Double initialFecundity,
            final Double initialMutation,
            final BehaviorManager manager
    ) {
        //HELP preconditions
        require(spawnLocation != null,
                "Initial position must be provided");
        require(launchOrientation != null,
                "Initial orientation must be provided");
        require(initialCurvature != null,
                "Initial curvature must be provided");
        require(launchSpeed != null && launchSpeed >= 0.0,
                "Initial speed must be provided and be positive");
        require(0.0 <= initialMortality && initialMortality <= 1.0,
                "mortality rate is expressed in [0.0,1.0] interval not " + initialMortality);
        require(0.0 <= initialFecundity && initialFecundity <= 1.0,
                "fecundity rate is expressed in [0.0,1.0] interval not " + initialFecundity);
        require(0.0 <= initialMutation && initialMutation <= 1.0,
                "mutation rate is expressed in [0.0,1.0] interval not " + initialMutation);
        require(manager != null, "You must provide a BehaviourManager");

        this.current_location =
                new Point2D.Double(spawnLocation.getX(), spawnLocation.getY());
        this.childOrientationLauncher =
                childLaunchOrientation == null
                        ? Configuration.getParameter(RANDOM, Random.class)
                            .nextDouble() * 2 * Math.PI
                        : childLaunchOrientation;
        this.current_orientation = launchOrientation;
        this.current_curvature = initialCurvature;
        this.current_speed = launchSpeed;
        this.childSpeedLauncher =
                childLaunchSpeed == null
                        ? Configuration.getParameter(RANDOM, Random.class)
                            .nextDouble() * 2 * Math.PI
                        : childLaunchSpeed;
        this.mortalityRate = initialMortality;
        this.fecundityRate = initialFecundity;
        this.mutationRate = initialMutation;
        this.behaviorManager = manager;
    }

    /**
     * Old constructor w/o child initial values
     *
     * @param spawnLocation     initial location of the agent in the environment.
     * @param launchOrientation initial movement orientation
     * @param initialCurvature  initial curvature
     * @param launchSpeed       initial speed
     * @param initialMortality  initial mortality rate
     * @param initialFecundity  initial fecundity rate
     * @param initialMutation   initial mutation rate
     * @param manager           Behaviour Strategy to use for this agent
     * @see java.awt.geom.Point2D.Double
     * @see org.blackpanther.ecosystem.BehaviorManager
     */
    public Agent(
            final Point2D spawnLocation,
            final Double launchOrientation,
            final Double initialCurvature,
            final Double launchSpeed,
            final Double initialMortality,
            final Double initialFecundity,
            final Double initialMutation,
            final BehaviorManager manager
    ) {
        this(
                spawnLocation,
                launchOrientation,
                null,
                initialCurvature,
                launchSpeed,
                null,
                initialMortality,
                initialFecundity,
                initialMutation,
                manager
        );
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
     * Check if this agent is alive in any environment.
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

    /**
     * Return agent's AreaListener
     *
     * @return current agent's AreaListener
     * @see AreaListener
     */
    final AreaListener getAreaListener() {
        return areaListener;
    }

    /* ================================================
     *               GETTERS
     * ================================================
     */


    /**
     * Get current agent's location in its environment
     *
     * @return current agent's position
     */
    public Point2D getLocation() {
        return current_location;
    }

    /**
     * <p>
     * Get the current agent's orientation angle.<br/>
     * Orientation angle is within [0,2PI].
     * </p>
     *
     * @return current orientation
     */
    public Double getOrientation() {
        return current_orientation;
    }

    /**
     * Return at which orientation a child is launched.
     *
     * @return initial child's orientation
     */
    Double getChildOrientationLauncher() {
        return childOrientationLauncher;
    }

    /**
     * <p>
     * Get the current agent's curvature angle.<br/>
     * </p>
     *
     * @return current curvature
     */
    public Double getCurvature() {
        return current_curvature;
    }

    /**
     * Get the current agent's speed
     *
     * @return current agent's speed
     */
    public Double getSpeed() {
        return current_speed;
    }

    /**
     * Return at which speed a child is launched.
     *
     * @return initial child's speed
     */
    Double getChildSpeedLauncher() {
        return childSpeedLauncher;
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
     * Setter for the agent's orientation
     *
     * @param orientation the new orientation
     */
    void setOrientation(Double orientation) {
        this.current_orientation = orientation;
    }

    /**
     * Setter for the agent's curvature
     *
     * @param curvature the new curvature
     */
    void setCurvature(Double curvature) {
        this.current_curvature = curvature;
    }


    /**
     * Setter for the agent's speed
     *
     * @param speed the new speed
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
