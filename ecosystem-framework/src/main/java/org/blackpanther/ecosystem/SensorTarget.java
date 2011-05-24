package org.blackpanther.ecosystem;

import org.blackpanther.ecosystem.agent.Agent;

/**
 * @author MACHIZAUD Andréa
 * @version 1.0-alpha - Tue May 24 23:49:57 CEST 2011
 */
public class SensorTarget<T extends Agent> {

    public static final <T extends Agent> SensorTarget<T> detected(
            Double orientation, T target) {
        return new SensorTarget<T>(orientation, target);
    }

    private Double direction;
    private T target;

    private SensorTarget(Double direction, T target) {
        this.direction = direction;
        this.target = target;
    }

    public double getOrientation() {
        return direction;
    }

    public T getTarget() {
        return target;
    }
}