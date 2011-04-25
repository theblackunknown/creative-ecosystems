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
    private Double fecondityRate;
    /**
     * Probability that a mutation happens
     */
    private Double mutationRate;
    /**
     * Age of the agent. Same unit as the {@link Environment}
     */
    private int age = 0;
    /**
     * Agent's identifiant. Design choice is to implement it as a {@link Color}
     */
    private Color identifiant = Color.BLACK;

    /**
     * Agent's behavior manager
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
        require(initialSpeed >= 0, "Speed must be positive");
        require(0.0 <= initialMortality && initialMortality <= 1.0,
                "A probability is expressed in [0.0,1.0] interval");
        require(0.0 <= initialFecundity && initialFecundity <= 1.0,
                "A probability is expressed in [0.0,1.0] interval");

        this.direction = initialDirection;
        this.speed = initialSpeed;
        this.mortalityRate = initialMortality;
        this.fecondityRate = initialFecundity;
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

    final void setAreaListener(final AreaListener listener) {
        areaListener = listener;
    }

    final void unsetAreaListener() {
        areaListener = null;
    }

    public Geometry.Vector2D getDirection() {
        return direction;
    }

    public Double getSpeed() {
        return speed;
    }

    public Double getMortalityRate() {
        return mortalityRate;
    }

    public Double getFecondityRate() {
        return fecondityRate;
    }

    public Double getMutationRate() {
        return mutationRate;
    }

    public Color getIdentifiant() {
        return identifiant;
    }

    void setDirection(Geometry.Vector2D direction) {
        this.direction = direction;
    }

    void setSpeed(Double speed) {
        this.speed = speed;
    }

    void setMortalityRate(Double mortalityRate) {
        this.mortalityRate = mortalityRate;
    }

    void setFecondityRate(Double fecondityRate) {
        this.fecondityRate = fecondityRate;
    }

    void setMutationRate(Double mutationRate) {
        this.mutationRate = mutationRate;
    }

    void setAge(int age) {
        this.age = age;
    }

    void setIdentifiant(Color identifiant) {
        this.identifiant = identifiant;
    }

    void setBehaviorManager(BehaviorManager behaviorManager) {
        this.behaviorManager = behaviorManager;
    }
}
