package org.blackpanther.ecosystem.agent;

import org.blackpanther.ecosystem.Environment;
import org.blackpanther.ecosystem.event.AgentEvent;
import org.blackpanther.ecosystem.factory.fields.FieldsConfiguration;

import java.awt.geom.Point2D;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import static org.blackpanther.ecosystem.factory.fields.FieldsConfiguration.checkAgentConfiguration;
import static org.blackpanther.ecosystem.helper.Helper.require;

/**
 * <p>
 * Component which represent an agent within an ecosystem.
 * It implements all basic features for an agent,
 * specific behaviour are left to subclasses
 * </p>
 *
 * @author MACHIZAUD Andréa
 * @version 1.0-alpha - Tue May 24 23:49:57 CEST 2011
 */
public abstract class Agent
        implements Serializable, Cloneable, CreatureConstants {

    /**
     * Serializable identifier
     */

    private static final long serialVersionUID = 6L;
    private static long idGenerator = 0;

    /*=========================================
     *                 GENOTYPE
     *=========================================
     */
    protected Map<String, Object> genotype = new HashMap<String, Object>(AGENT_GENOTYPE.length);
    protected Map<String, Boolean> mutableTable = new HashMap<String, Boolean>(AGENT_GENOTYPE.length);

    /*=========================================
     *                 PHENOTYPE
     *=========================================
     */
    protected Map<String, Object> currentState = new HashMap<String, Object>(AGENT_STATE.length);

    /*=========================================================================
                                    MISCELLANEOUS
      =========================================================================*/
    /**
     * Agent's area listener
     */
    private Long id = ++idGenerator;

    protected Agent(FieldsConfiguration config) {
        checkAgentConfiguration(config);

        for (String stateTrait : BUILD_PROVIDED_AGENT_STATE)
            currentState.put(stateTrait, config.getValue(stateTrait));

        for (String genotypeTrait : AGENT_GENOTYPE) {
            genotype.put(genotypeTrait, config.getValue(genotypeTrait));
            mutableTable.put(genotypeTrait, config.isMutable(Agent.class, genotypeTrait));
        }
    }

    abstract public void update(final Environment env);


    public void attachTo(Environment env) {
        require(env != null);
        env.getEventSupport().fireAgentEvent(AgentEvent.Type.BORN, this);
    }

    /**
     * Unset the current area listener if any
     */
    public void detachFromEnvironment(Environment env) {
        env.getEventSupport().fireAgentEvent(AgentEvent.Type.DEATH, this);
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

    public boolean isMutable(String genotypeTrait) {
        return mutableTable.get(genotypeTrait);
    }

    public double getEnergy() {
        return getState(AGENT_ENERGY, Double.class);
    }

    /**
     * Get current agent's location in its environment
     *
     * @return current agent's position
     */
    public Point2D getLocation() {
        return getState(AGENT_LOCATION, Point2D.class);
    }

    /*=========================================================================
     *                 SETTERS
     * Visibility is set to package because
     * they are meant to be modified by nothing except the behaviour manager
     *=========================================================================
     */

    public void setLocation(double abscissa, double ordinate) {
        currentState.put(AGENT_LOCATION, new Point2D.Double(abscissa, ordinate));
    }

    public void setEnergy(Double energy) {
        currentState.put(AGENT_ENERGY, energy < 0.0
                ? 0.0
                : energy);
    }

    @Override
    public Agent clone() {
        try {
            Agent copy = (Agent) super.clone();
            copy.currentState = new HashMap<String, Object>(this.currentState);
            copy.genotype = new HashMap<String, Object>(this.genotype);
            copy.mutableTable = new HashMap<String, Boolean>(this.mutableTable);
            return copy;
        } catch (CloneNotSupportedException e) {
            throw new Error(e);
        }
    }

    @Override
    public String toString() {
        return super.toString() + "[Agent" + Long.toHexString(id) + "]";
    }
}