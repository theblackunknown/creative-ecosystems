package org.blackpanther.ecosystem;

import org.blackpanther.ecosystem.agent.Creature;
import org.blackpanther.ecosystem.agent.Resource;

import java.util.Collection;

/**
 * @author MACHIZAUD Andréa
 * @version 1.1-alpha - Thu May 19 01:22:54 CEST 2011
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