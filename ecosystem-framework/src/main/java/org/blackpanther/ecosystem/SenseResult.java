package org.blackpanther.ecosystem;

import org.blackpanther.ecosystem.agent.Creature;
import org.blackpanther.ecosystem.agent.Resource;

import java.util.Collection;

/**
 * @author MACHIZAUD Andréa
 * @version 1.0-alpha - Tue May 24 23:49:57 CEST 2011
 */
public class SenseResult {

    private Collection<SensorTarget<Creature>> nearCreatures;
    private Collection<SensorTarget<Resource>> nearResources;

    public SenseResult(
            Collection<SensorTarget<Creature>> nearCreatures,
            Collection<SensorTarget<Resource>> nearResources) {
        this.nearCreatures = nearCreatures;
        this.nearResources = nearResources;
    }

    public Collection<SensorTarget<Creature>> getNearCreatures() {
        return nearCreatures;
    }

    public Collection<SensorTarget<Resource>> getNearResources() {
        return nearResources;
    }
}