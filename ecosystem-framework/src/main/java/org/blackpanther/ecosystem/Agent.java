package org.blackpanther.ecosystem;

import org.blackpanther.ecosystem.math.Geometry;

import java.awt.*;
import java.io.Serializable;

import static org.blackpanther.ecosystem.Helper.require;

/**
 * <p>
 * Component which represent an agent within an ecosystem.
 * It implements all basic features for an agent, specific behaviour are left to subclasses
 * </p>
 *
 * @author MACHIZAUD Andréa
 * @version ${{version}} - ${{date}}
 */
public abstract class Agent
        implements Serializable {

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
     * @param direction - its initial movement direction
     * @param speed     - initial speed
     * @param mortality - initial mortality rate
     * @param fecundity - initial fecundity rate
     * @param manager   - Behaviour Strategy to use for this agent
     */
    public Agent(
            final Geometry.Vector2D direction,
            final double speed,
            final double mortality,
            final double fecundity,
            final BehaviorManager manager
    ) {
        require(speed >= 0, "Speed must be positive");
        require(0.0 <= mortality && mortality <= 1.0,"A probability is expressed in [0.0,1.0] interval");
        require(0.0 <= fecundity && fecundity <= 1.0,"A probability is expressed in [0.0,1.0] interval");

        this.direction = direction;
        this.speed = speed;
        this.mortalityRate = mortality;
        this.fecondityRate = fecundity;
        this.behaviorManager = manager;
    }

    /**
     * Update agent state according to given environment and agent behaviour manager
     * @param env - given environment in which the agent evolves
     */
    public void update(Environment env) {
        behaviorManager.update(env,this);
    }

    /**
     * Check if this agent has been set in any environment
     * @return <code>true</code> if the agent has been set in an {@link Environment}, <code>false</code> otherwise
     */
    public boolean isNowhere() {
        return areaListener == null;
    }

    void setAreaListener(AreaListener listener) {
        areaListener = listener;
    }

    void unsetAreaListener() {
        areaListener = null;
    }


}
