package org.blackpanther.ecosystem;

/**
 * @author MACHIZAUD Andréa
 * @version 1.0-alpha - Wed May 18 02:01:09 CEST 2011
 */
public class SensorTarget<T> {

    public static final <T> SensorTarget<T> detected(
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