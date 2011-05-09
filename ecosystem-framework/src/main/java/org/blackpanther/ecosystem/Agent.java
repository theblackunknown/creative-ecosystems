package org.blackpanther.ecosystem;

import java.awt.*;
import java.awt.geom.Point2D;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

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
    private static final Integer DEFAULT_GENOTYPE_LENGTH = 6;
    private static final Integer DEFAULT_STATE_LENGTH = 4;


    public static final String AGENT_IDENTIFIER = "agent-identifier";
    public static final String AGENT_AGE = "agent-age";
    public static final String AGENT_LOCATION = "agent-location";
    public static final String AGENT_ORIENTATION = "agent-orientation";
    public static final String AGENT_CURVATURE = "agent-curvature";
    public static final String AGENT_SPEED = "agent-speed";

    public static final String AGENT_IRRATIONALITY = "agent-irrationality";
    public static final String AGENT_MORTALITY = "agent-mortality";
    public static final String AGENT_FECUNDITY = "agent-fecundity";
    public static final String AGENT_MUTATION = "agent-mutation";
    public static final String AGENT_ORIENTATION_LAUNCHER = "agent-orientation-launcher";
    public static final String AGENT_SPEED_LAUNCHER = "agent-speed-launcher";
    public static final String AGENT_BEHAVIOUR_MANAGER = "agent-behaviour-manager";

    /*=========================================
     *                 GENOTYPE
     *=========================================
     */
    private Map<String, Object> genotype = new HashMap<String, Object>(DEFAULT_GENOTYPE_LENGTH);

    /*=========================================
     *                 PHENOTYPE
     *=========================================
     */
    private Map<String, Object> currentState = new HashMap<String, Object>(DEFAULT_STATE_LENGTH);

    /*=========================================================================
                                    MISCELLANEOUS
      =========================================================================*/
    /**
     * Agent's area listener
     */
    private AreaListener areaListener;

    /**
     * Create an agent
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
            final Double initialIrrationality,
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
        require(0.0 <= initialIrrationality && initialIrrationality <= 1.0,
                "irrationality rate is expressed in [0.0,1.0] interval not " + initialIrrationality);
        require(0.0 <= initialMortality && initialMortality <= 1.0,
                "mortality rate is expressed in [0.0,1.0] interval not " + initialMortality);
        require(0.0 <= initialFecundity && initialFecundity <= 1.0,
                "fecundity rate is expressed in [0.0,1.0] interval not " + initialFecundity);
        require(0.0 <= initialMutation && initialMutation <= 1.0,
                "mutation rate is expressed in [0.0,1.0] interval not " + initialMutation);
        require(manager != null, "You must provide a BehaviourManager");

        //FIXME Agent identifier is static
        genotype.put(AGENT_IDENTIFIER, Color.BLACK);
        genotype.put(AGENT_IRRATIONALITY, initialIrrationality);
        genotype.put(AGENT_MORTALITY, initialMortality);
        genotype.put(AGENT_FECUNDITY, initialFecundity);
        genotype.put(AGENT_MUTATION, initialMutation);
        genotype.put(AGENT_ORIENTATION_LAUNCHER, childLaunchOrientation);
        genotype.put(AGENT_SPEED_LAUNCHER, childLaunchSpeed);
        genotype.put(AGENT_BEHAVIOUR_MANAGER, manager);

        currentState.put(AGENT_AGE, 0);
        currentState.put(AGENT_LOCATION,
                new Point2D.Double(spawnLocation.getX(), spawnLocation.getY())
        );
        currentState.put(AGENT_ORIENTATION, launchOrientation);
        currentState.put(AGENT_CURVATURE, initialCurvature);
        currentState.put(AGENT_SPEED, launchSpeed);
    }

    /**
     * Update agent state according to given environment and agent behaviour manager
     *
     * @param env given environment in which the agent evolves
     */
    public final void update(final Environment env) {
        getGene(AGENT_BEHAVIOUR_MANAGER, BehaviorManager.class).update(env, this);
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
     * TODO Implement general setters
     * ================================================
     */

    @SuppressWarnings("unchecked")
    public <T> T getGene(String geneName, Class<T> geneType) {
        Object correspondingGene = genotype.get(geneName);
        if (correspondingGene != null) {
            if (geneType.isInstance(correspondingGene)) {
                return (T) correspondingGene;
            } else {
                throw new IllegalArgumentException(
                        String.format("%s gene does not match given type, please check again",
                                geneName)
                );
            }
        } else {
            throw new IllegalArgumentException(String.format(
                    "'%s'  parameter is not provided by the current configuration, "
                            + "maybe you should register it before",
                    geneName
            ));
        }
    }

    @SuppressWarnings("unchecked")
    public <T> T getState(String stateName, Class<T> stateType) {
        Object correspondingGene = currentState.get(stateName);
        if (correspondingGene != null) {
            if (stateType.isInstance(correspondingGene)) {
                return (T) correspondingGene;
            } else {
                throw new IllegalArgumentException(
                        String.format("%s state does not match given type, please check again",
                                stateName)
                );
            }
        } else {
            throw new IllegalArgumentException(String.format(
                    "'%s'  parameter is not provided by the current configuration, "
                            + "maybe you should register it before",
                    stateName
            ));
        }
    }


    /**
     * Get current agent's location in its environment
     *
     * @return current agent's position
     */
    public Point2D getLocation() {
        return getState(AGENT_LOCATION, Point2D.class);
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
        return getState(AGENT_ORIENTATION, Double.class);
    }

    /**
     * Return at which orientation a child is launched.
     *
     * @return initial child's orientation
     */
    Double getChildOrientationLauncher() {
        return getGene(AGENT_ORIENTATION_LAUNCHER, Double.class);
    }

    /**
     * <p>
     * Get the current agent's curvature angle.<br/>
     * </p>
     *
     * @return current curvature
     */
    public Double getCurvature() {
        return getState(AGENT_CURVATURE, Double.class);
    }

    /**
     * Get the current agent's speed
     *
     * @return current agent's speed
     */
    public Double getSpeed() {
        return getState(AGENT_SPEED, Double.class);
    }

    /**
     * Return at which speed a child is launched.
     *
     * @return initial child's speed
     */
    Double getChildSpeedLauncher() {
        return getGene(AGENT_SPEED_LAUNCHER, Double.class);
    }

    Double getIrrationality() {
        return getGene(AGENT_IRRATIONALITY, Double.class);
    }

    /**
     * Get the current agent's mortality rate
     *
     * @return current mortality rate
     */
    public Double getMortalityRate() {
        return getGene(AGENT_MORTALITY, Double.class);
    }

    /**
     * Get the current agent's fecundity rate
     *
     * @return current fecundity rate
     */
    public Double getFecundityRate() {
        return getGene(AGENT_FECUNDITY, Double.class);
    }

    /**
     * Get the current agent's mutation rate
     *
     * @return current mutation rate
     */
    public Double getMutationRate() {
        return getGene(AGENT_MUTATION, Double.class);
    }

    /**
     * Get the agent's age
     *
     * @return actual age of the agent
     */
    public Integer getAge() {
        return getState(AGENT_AGE, Integer.class);
    }

    public BehaviorManager getBehaviour() {
        return getGene(AGENT_BEHAVIOUR_MANAGER,BehaviorManager.class);
    }

    /*=========================================================================
     *                 SETTERS
     * Visibility is set to package because
     * they are meant to be modified by nothing except the behaviour manager
     *=========================================================================
     */

    final void setLocation(double abscissa, double ordinate) {
        getState(AGENT_LOCATION, Point2D.class).setLocation(abscissa, ordinate);
    }

    /**
     * Setter for the agent's orientation
     *
     * @param orientation the new orientation
     */
    void setOrientation(Double orientation) {
        currentState.put(AGENT_ORIENTATION, orientation);
    }

    /**
     * Setter for the agent's curvature
     *
     * @param curvature the new curvature
     */
    void setCurvature(Double curvature) {
        currentState.put(AGENT_CURVATURE, curvature);
    }


    /**
     * Setter for the agent's speed
     *
     * @param speed the new speed
     */
    void setSpeed(Double speed) {
        currentState.put(AGENT_SPEED, speed);
    }


    /**
     * Setter for the agent's age
     *
     * @param age the new age
     */
    void setAge(Integer age) {
        currentState.put(AGENT_AGE, age);
    }

    @Override
    public String toString() {
        return super.toString() + "[Age=" + getState(AGENT_AGE, Integer.class) + "]";
    }
}
