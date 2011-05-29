package org.blackpanther.ecosystem.agent;

import org.blackpanther.ecosystem.Environment;
import org.blackpanther.ecosystem.factory.fields.FieldsConfiguration;

import static org.blackpanther.ecosystem.factory.fields.FieldsConfiguration.checkResourceConfiguration;

/**
 * @author MACHIZAUD Andréa
 * @version 1.0-alpha - Tue May 24 23:49:57 CEST 2011
 */
public class Resource
        extends Agent
        implements ResourceConstants {

    public Resource(FieldsConfiguration config) {
        super(config);
        checkResourceConfiguration(config);
        for (String stateTrait : BUILD_PROVIDED_RESOURCE_STATE)
            currentState.put(stateTrait, config.getValue(stateTrait));

        for (String genotypeTrait : RESOURCE_GENOTYPE) {
            genotype.put(genotypeTrait, config.getValue(genotypeTrait));
            mutableTable.put(genotypeTrait, config.isMutable(Resource.class, genotypeTrait));
        }
    }

    /**
     * A resource don't update
     */
    @Override
    public void update(Environment env) {
    }

    @Override
    public double getEnergy() {
        return getState(RESOURCE_ENERGY, Double.class);
    }

    @Override
    public void setEnergy(Double energy) {
        currentState.put(RESOURCE_ENERGY, energy < 0.0
                ? 0.0
                : energy);
    }

    @Override
    public Resource clone() {
        return (Resource) super.clone();
    }

    @Override
    public String toString() {
        return super.toString();
    }
}
