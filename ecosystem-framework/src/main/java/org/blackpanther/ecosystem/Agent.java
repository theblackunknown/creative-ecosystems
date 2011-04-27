package org.blackpanther.ecosystem;

import org.blackpanther.ecosystem.math.Geometry;

import java.awt.Color;
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
 * @version v0.2.1 - Sun Apr 24 18:01:06 CEST 2011
 */
public abstract class Agent
        implements Serializable {

    /** Serializable identifier */
    private static final Long serialVersionUID = 1L;

    /**
     * Direction in which the agent moves
     */
    private Geometry.Vector2D direction;
    /**
     * Speed of the agent. Varies in <pre>[0,3]</pre>
     */
    private Double speed;
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
     * Age of the agent. Same unit as the {@link Environment}
     */
    private int age = 0;
    /**
     * Agent's identifier. Design choice is to implement it as a {@link Color}
     */
    private Color identifier = Color.BLACK;

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
     *
     * @param initialDirection
     *      its initial movement direction
     * @param initialSpeed
     *      initial speed
     * @param initialMortality
     *      initial mortality rate
     * @param initialFecundity
     *      initial fecundity rate
     * @param manager
     *      Behaviour Strategy to use for this agent
     */
    public Agent(
            final Geometry.Vector2D initialDirection,
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
        require(manager != null,"You must provide a BehaviourManager");

        this.direction = initialDirection;
        this.speed = initialSpeed;
        this.mortalityRate = initialMortality;
        this.fecundityRate = initialFecundity;
        this.mutationRate = initialMutation;
        this.behaviorManager = manager;
    }

    /**
     * Update agent state according to given environment and agent behaviour manager
     *
     * @param env
     *      given environment in which the agent evolves
     */
    public final void update(final Environment env) {
        behaviorManager.update(env, this);
    }

    /**
     * Check if this agent has been set in any environment
     *
     * @return <code>true</code> if the agent has been set in an {@link Environment},
     *  <code>false</code> otherwise
     */
    public final boolean isNowhere() {
        return areaListener == null;
    }

    /**
     * Set the arealistener
     * @param listener 
     *      the new area listener
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
     * Get the current agent's direction
     * @return
     *      current direction
     */
    public Geometry.Vector2D getDirection() {
        return direction;
    }

    /**
     * Get the current agent's speed
     * @return 
     *      current speed
     */
    public Double getSpeed() {
        return speed;
    }

    /**
     * Get the current agent's mortality rate
     * @return 
     *      current mortality rate
     */
    public Double getMortalityRate() {
        return mortalityRate;
    }

    /**
     * Get the current agent's fecundity rate
     * @return 
     *      current fecundity rate
     */
    public Double getfecundityRate() {
        return fecundityRate;
    }

    /**
     * Get the current agent's mutation rate
     * @return 
     *      current mutation rate
     */
    public Double getMutationRate() {
        return mutationRate;
    }

    /**
     * Get the agent's age
     * @return 
     *      actual age of the agent
     */
    public Integer void getAge() {
        return age;
    }

    /**
     * Get the agent's identifier
     * @return 
     *      agent's color identifier
     */
    public Color getIdentifier() {
        return identifier;
    }
    
    /*=========================================================================
     * Setter Part
     * Visibility is set to package because
     * they are meant to be modified by nothing except the behaviour manager
     *=========================================================================
     */

    /**
     * Setter for the agent's direction
     * @param direction
     *      the new direction
     */
    void setDirection(Geometry.Vector2D direction) {
        this.direction = direction;
    }


    /**
     * Setter for the agent's speed
     * @param speed
     *      the new speed
     */
    void setSpeed(Double speed) {
        this.speed = speed;
    }


    /**
     * Setter for the agent's mortality rate
     * @param mortalityRate
     *      the new mortality rate
     */
    void setMortalityRate(Double mortalityRate) {
        this.mortalityRate = mortalityRate;
    }


    /**
     * Setter for the agent's fecundity rate
     * @param fecundityRate
     *      the new fecundity rate
     */
    void setfecundityRate(Double fecundityRate) {
        this.fecundityRate = fecundityRate;
    }


    /**
     * Setter for the agent's mutation rate
     * @param mutationRate
     *      the new mutation rate
     */
    void setMutationRate(Double mutationRate) {
        this.mutationRate = mutationRate;
    }


    /**
     * Setter for the agent's age
     * @param age
     *      the new age
     */
    void setAge(int age) {
        this.age = age;
    }


    /**
     * Setter for the agent's identifier
     * @param identifier
     *      the new identifier
     */
    void setidentifier(Color identifier) {
        this.identifier = identifier;
    }


    /**
     * Setter for the agent's behavior manager
     * @param behaviorManager
     *      the new behavior manager
     */
    void setBehaviorManager(BehaviorManager behaviorManager) {
        this.behaviorManager = behaviorManager;
    }
}
