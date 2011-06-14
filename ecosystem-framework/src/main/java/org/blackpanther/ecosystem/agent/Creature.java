package org.blackpanther.ecosystem.agent;

import org.blackpanther.ecosystem.Environment;
import org.blackpanther.ecosystem.SenseResult;
import org.blackpanther.ecosystem.behaviour.BehaviorManager;
import org.blackpanther.ecosystem.factory.fields.FieldsConfiguration;
import org.blackpanther.ecosystem.math.Geometry;

import java.awt.*;

import static org.blackpanther.ecosystem.factory.fields.FieldsConfiguration.checkCreatureConfiguration;
import static org.blackpanther.ecosystem.helper.Helper.require;

/**
 * @author MACHIZAUD Andréa
 * @version 1.0-alpha - Tue May 24 23:49:57 CEST 2011
 */
public class Creature
        extends Agent
        implements CreatureConstants {

    private boolean alive;

    public Creature(FieldsConfiguration config) {
        super(config);
        checkCreatureConfiguration(config);

        for (String stateTrait : BUILD_PROVIDED_CREATURE_STATE)
            currentState.put(stateTrait, config.getValue(stateTrait));

        for (String genotypeTrait : CREATURE_GENOTYPE) {
            genotype.put(genotypeTrait, config.getValue(genotypeTrait));
            mutableTable.put(genotypeTrait, config.isMutable(Creature.class, genotypeTrait));
        }

        currentState.put(CREATURE_AGE, 0);
    }

    public SenseResult sense(Environment environment) {
        require(alive, this + " couldn't not sense when dead");
        return environment.aggregateInformation(new Geometry.Circle(
                getLocation(),
                getGene(CREATURE_SENSOR_RADIUS, Double.class)));
    }

    /**
     * Update agent state according to given environment and agent behaviour manager
     *
     * @param env given environment in which the agent evolves
     */
    @Override
    public void update(final Environment env) {
        if (alive)
            getGene(CREATURE_BEHAVIOR, BehaviorManager.class).update(env, this);

        //otherwise it means it has been eaten

    }

    /**
     * Check if this agent is alive in any environment.
     *
     * @return <code>true</code> if the agent has been set in an {@link Environment},
     *         <code>false</code> otherwise
     */
    public final boolean isAlive() {
        return alive;
    }

    @Override
    public void attachTo(Environment env) {
        require(!alive, this + " is already alive, thus bounded to an environment");
        alive = true;
        super.attachTo(env);
    }

    @Override
    public void detachFromEnvironment(Environment env) {
        require(alive, this + " wasn't alive, thus not bounded to any environment");
        alive = false;
        super.detachFromEnvironment(env);
    }

    /*=========================================================================
                            SETTERS
    =========================================================================*/

    @Override
    public double getEnergy() {
        return getState(CREATURE_ENERGY, Double.class);
    }

    public Color getColor() {
        return getState(CREATURE_COLOR, Color.class);
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
        return getState(CREATURE_ORIENTATION, Double.class);
    }

    /**
     * Return at which orientation a child is launched.
     *
     * @return initial child's orientation
     */
    public Double getChildOrientationLauncher() {
        return getGene(CREATURE_ORIENTATION_LAUNCHER, Double.class);
    }

    /**
     * <p>
     * Get the current agent's curvature angle.<br/>
     * </p>
     *
     * @return current curvature
     */
    public Double getCurvature() {
        return getState(CREATURE_CURVATURE, Double.class);
    }

    /**
     * Get the current agent's speed
     *
     * @return current agent's speed
     */
    public Double getSpeed() {
        return getState(CREATURE_SPEED, Double.class);
    }

    /**
     * Return at which speed a child is launched.
     *
     * @return initial child's speed
     */
    public Double getChildSpeedLauncher() {
        return getGene(CREATURE_SPEED_LAUNCHER, Double.class);
    }

    public Double getSensorRadius() {
        return getGene(CREATURE_SENSOR_RADIUS, Double.class);
    }

    public Double getIrrationality() {
        return getGene(CREATURE_IRRATIONALITY, Double.class);
    }

    /**
     * Get the current agent's mortality rate
     *
     * @return current mortality rate
     */
    public Double getMortalityRate() {
        return getGene(CREATURE_MORTALITY, Double.class);
    }

    /**
     * Get the current agent's fecundity rate
     *
     * @return current fecundity rate
     */
    public Double getFecundityRate() {
        return getGene(CREATURE_FECUNDITY, Double.class);
    }

    /**
     * Get the current agent's mutation rate
     *
     * @return current mutation rate
     */
    public Double getMutationRate() {
        return getGene(CREATURE_MUTATION, Double.class);
    }

    /**
     * Get the agent's age
     *
     * @return actual age of the agent
     */
    public Integer getAge() {
        return getState(CREATURE_AGE, Integer.class);
    }

    public BehaviorManager getBehaviour() {
        return getGene(CREATURE_BEHAVIOR, BehaviorManager.class);
    }

    /*=========================================================================
                            GETTERS
    =========================================================================*/

    @Override
    public void setEnergy(Double energy) {
        currentState.put(CREATURE_ENERGY, energy < 10e-2
                ? 0.0
                : energy);
    }

    public void setColor(int red, int green, int blue) {
        currentState.put(CREATURE_COLOR, new Color(
                red, green, blue
        ));
    }

    /**
     * Setter for the agent's orientation
     *
     * @param orientation the new orientation
     */
    public void setOrientation(Double orientation) {
        currentState.put(CREATURE_ORIENTATION, orientation % Geometry.PI_2);
    }

    /**
     * Setter for the agent's curvature
     *
     * @param curvature the new curvature
     */
    public void setCurvature(Double curvature) {
        currentState.put(CREATURE_CURVATURE, curvature);
    }


    /**
     * Setter for the agent's speed
     *
     * @param speed the new speed
     */
    public void setSpeed(Double speed) {
        currentState.put(CREATURE_SPEED, speed);
    }


    /**
     * Setter for the agent's age
     *
     * @param age the new age
     */
    public void setAge(Integer age) {
        currentState.put(CREATURE_AGE, age);
    }

    @Override
    public Creature clone() {
        Creature copy = (Creature) super.clone();
        copy.alive = false;
        return copy;
    }

    @Override
    public String toString() {
        return super.toString() + "[Age=" + getAge() + ",Energy=" + getEnergy() + "]";
    }
}
