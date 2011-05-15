package org.blackpanther.ecosystem;

import org.blackpanther.ecosystem.event.AgentEvent;
import org.blackpanther.ecosystem.event.EnvironmentMonitor;
import org.blackpanther.ecosystem.math.Geometry;

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
 * @version 0.2 - Wed May 11 02:54:46 CEST 2011
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
    public static final String AGENT_ENERGY = "agent-energy";
    public static final String AGENT_LOCATION = "agent-location";
    public static final String AGENT_ORIENTATION = "agent-orientation";
    public static final String AGENT_CURVATURE = "agent-curvature";
    public static final String AGENT_SPEED = "agent-speed";

    public static final String AGENT_MOVEMENT_COST = "agent-movement-cost";
    public static final String AGENT_FECUNDATION_COST = "agent-fecundation-cost";
    public static final String AGENT_FECUNDATION_LOSS = "agent-fecundation-loss";
    public static final String AGENT_GREED = "agent-greed";
    public static final String AGENT_SENSOR_RADIUS = "agent-sensor-radius";
    public static final String AGENT_IRRATIONALITY = "agent-irrationality";
    public static final String AGENT_MORTALITY = "agent-mortality";
    public static final String AGENT_FECUNDITY = "agent-fecundity";
    public static final String AGENT_MUTATION = "agent-mutation";
    public static final String AGENT_ORIENTATION_LAUNCHER = "agent-orientation-launcher";
    public static final String AGENT_SPEED_LAUNCHER = "agent-speed-launcher";
    public static final String AGENT_BEHAVIOUR = "agent-behaviour-manager";

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
    private EnvironmentMonitor eventSupport;

    /**
     * Create an agent
     *
     * @param spawnLocation     initial location of the agent in the environment.
     * @param orientation       initial movement orientation
     * @param launchOrientation orientation initial value for next generation from this parent
     * @param curvature         initial curvature
     * @param speed             initial speed
     * @param launchSpeed       orientation initial value for next generation from this parent
     * @param mortality         initial mortality rate
     * @param fecundity         initial fecundity rate
     * @param mutation          initial mutation rate
     * @param manager           Behaviour Strategy to use for this agent
     * @see java.awt.geom.Point2D.Double
     * @see org.blackpanther.ecosystem.BehaviorManager
     */
    public Agent(
            final Point2D spawnLocation,
            final double energyAmount,
            final double movementCost,
            final double fecundationCost,
            final double fecundationLoss,
            final double orientation,
            final double launchOrientation,
            final double curvature,
            final double speed,
            final double launchSpeed,
            final double greed,
            final double sensorRadius,
            final double irrationality,
            final double mortality,
            final double fecundity,
            final double mutation,
            final BehaviorManager manager
    ) {
        require(spawnLocation != null);
        require(energyAmount >= 0.0, String.valueOf(energyAmount));
        require(movementCost >= 0.0, String.valueOf(movementCost));
        require(0.0 <= fecundationCost, String.valueOf(fecundationCost));
        require(0.0 <= fecundationLoss && fecundationLoss <= 1.0, String.valueOf(fecundationLoss));
        require(speed >= 0.0, String.valueOf(speed));
        require(0.0 <= greed && greed <= 1.0, String.valueOf(greed));
        require(sensorRadius >= 0.0, String.valueOf(sensorRadius));
        require(0.0 <= irrationality && irrationality <= 1.0, String.valueOf(irrationality));
        require(0.0 <= mortality && mortality <= 1.0, String.valueOf(mortality));
        require(0.0 <= fecundity && fecundity <= 1.0, String.valueOf(fecundity));
        require(0.0 <= mutation && mutation <= 1.0, String.valueOf(mutation));
        require(manager != null, "You must provide a BehaviourManager");

        genotype.put(AGENT_IDENTIFIER, Color.BLACK);
        genotype.put(AGENT_MOVEMENT_COST, movementCost);
        genotype.put(AGENT_FECUNDATION_COST, fecundationCost);
        genotype.put(AGENT_FECUNDATION_LOSS, fecundationLoss);
        genotype.put(AGENT_GREED, greed);
        genotype.put(AGENT_SENSOR_RADIUS, sensorRadius);
        genotype.put(AGENT_IRRATIONALITY, irrationality);
        genotype.put(AGENT_MORTALITY, mortality);
        genotype.put(AGENT_FECUNDITY, fecundity);
        genotype.put(AGENT_MUTATION, mutation);
        genotype.put(AGENT_ORIENTATION_LAUNCHER, launchOrientation);
        genotype.put(AGENT_SPEED_LAUNCHER, launchSpeed);
        genotype.put(AGENT_BEHAVIOUR, manager);

        setAge(0);
        currentState.put(AGENT_LOCATION,
                new Point2D.Double(spawnLocation.getX(), spawnLocation.getY()));
        setEnergy(energyAmount);
        setOrientation(orientation);
        setCurvature(curvature);
        setSpeed(speed);
    }

    /**
     * Update agent state according to given environment and agent behaviour manager
     *
     * @param env given environment in which the agent evolves
     */
    public final void update(final Environment env) {
        getGene(AGENT_BEHAVIOUR, BehaviorManager.class).update(env, this);
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

    public void setEventSupport(EnvironmentMonitor monitor) {
        this.eventSupport = monitor;
        eventSupport.fireAgentEvent(AgentEvent.Type.BORN, this);
    }

    /**
     * Set the {@link AreaListener}
     *
     * @param listener the new area listener
     */
    final void attachTo(final AreaListener listener) {
        areaListener = listener;
    }

    /**
     * Unset the current area listener if any
     */
    final void detachFromEnvironment() {
        if (eventSupport != null)
            eventSupport.fireAgentEvent(AgentEvent.Type.DEATH, this);
        areaListener = null;
    }

    public SenseResult sense() {
        if (areaListener == null)
            throw new IllegalStateException(
                    "Couldn't not sense outside an environment");
        return areaListener.aggregateInformation(new Geometry.Circle(
                getLocation(),
                getGene(AGENT_SENSOR_RADIUS, Double.class)));
    }

    /* ================================================
     *               GETTERS
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

    public double getEnergy() {
        return getState(AGENT_ENERGY, Double.class);
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
    public Double getChildSpeedLauncher() {
        return getGene(AGENT_SPEED_LAUNCHER, Double.class);
    }

    public Double getSensorRadius() {
        return getGene(AGENT_SENSOR_RADIUS, Double.class);
    }

    public Double getIrrationality() {
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
        return getGene(AGENT_BEHAVIOUR, BehaviorManager.class);
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

    final void setEnergy(Double energy) {
        currentState.put(AGENT_ENERGY, energy);
    }

    /**
     * Setter for the agent's orientation
     *
     * @param orientation the new orientation
     */
    void setOrientation(Double orientation) {
        currentState.put(AGENT_ORIENTATION, orientation % Geometry.PI_2);
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