package org.blackpanther.ecosystem;

import java.util.Set;

/**
 * @author MACHIZAUD Andréa
 * @version 5/11/11
 */
public class SenseResult {

    private Set<SensorTarget<Agent>> nearAgents;
    private Set<SensorTarget<Resource>> nearResources;

    public SenseResult(
            Set<SensorTarget<Agent>> nearAgents,
            Set<SensorTarget<Resource>> nearResources) {
        this.nearAgents = nearAgents;
        this.nearResources = nearResources;
    }

    public Set<SensorTarget<Agent>> getNearAgents() {
        return nearAgents;
    }

    public Set<SensorTarget<Resource>> getNearResources() {
        return nearResources;
    }
}
