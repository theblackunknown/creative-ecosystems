package org.blackpanther.ecosystem.gui.wizard;

import com.nexes.wizard.WizardModel;
import org.blackpanther.ecosystem.Agent;
import org.blackpanther.ecosystem.placement.Strategy;

import java.util.Collection;
import java.util.Properties;

import static org.blackpanther.ecosystem.placement.Strategy.generatePopulation;

/**
 * @author MACHIZAUD Andréa
 * @version 0.2 - Wed May 11 02:54:46 CEST 2011
 */
public class CreationModel
        extends WizardModel {
    //LATER - Restore a saved environment

    private Properties environmentProperties;
    private Strategy.GenerationType placementStrategy;
    private Object[] additionalParameters;

    public void setParameters(Properties properties) {
        this.environmentProperties = properties;
    }

    public Properties getEnvironmentProperties() {
        return environmentProperties;
    }

    public void setPlacementStrategy(Strategy.GenerationType placementStrategy) {
        this.placementStrategy = placementStrategy;
    }

    public void setAdditionalParameters(Object[] additionalParameters) {
        this.additionalParameters = additionalParameters;
    }

    public Collection<Agent> getPool() {
        return generatePopulation(
                placementStrategy,
                additionalParameters
        );
    }
}
