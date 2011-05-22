package org.blackpanther.ecosystem;

/**
 * @author MACHIZAUD Andréa
 * @version 1.1-alpha - Thu May 19 01:22:54 CEST 2011
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