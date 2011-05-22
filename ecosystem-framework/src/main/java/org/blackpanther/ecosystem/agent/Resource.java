package org.blackpanther.ecosystem.agent;

import org.blackpanther.ecosystem.Agent;
import org.blackpanther.ecosystem.Environment;
import org.blackpanther.ecosystem.factory.fields.FieldsConfiguration;

import static org.blackpanther.ecosystem.factory.fields.FieldsConfiguration.checkCreatureConfiguration;

/**
 * @author MACHIZAUD Andréa
 * @version 5/22/11
 */
public class Resource
        extends Agent {

    public Resource(FieldsConfiguration config) {
        super(config);
        checkCreatureConfiguration(config);
    }

    /**
     * A resource don't update
     */
    @Override
    public void update(Environment env) {
    }

    @Override
    public void setEnergy(Double energy) {
        super.setEnergy(energy);
    }
}
