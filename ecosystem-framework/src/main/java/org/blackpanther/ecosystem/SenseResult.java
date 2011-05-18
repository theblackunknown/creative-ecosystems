package org.blackpanther.ecosystem;

import java.util.Collection;

/**
 * @author MACHIZAUD Andréa
 * @version 1.1-alpha - Thu May 19 01:22:54 CEST 2011
 */
public class SenseResult {

    private Collection<SensorTarget<Agent>> nearAgents;
    private Collection<SensorTarget<Resource>> nearResources;

    public SenseResult(
            Collection<SensorTarget<Agent>> nearAgents,
            Collection<SensorTarget<Resource>> nearResources) {
        this.nearAgents = nearAgents;
        this.nearResources = nearResources;
    }

    public Collection<SensorTarget<Agent>> getNearAgents() {
        return nearAgents;
    }

    public Collection<SensorTarget<Resource>> getNearResources() {
        return nearResources;
    }
}