package org.blackpanther.ecosystem;

/**
 * @author MACHIZAUD Andréa
 * @version 5/11/11
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

    public double getDirection() {
        return direction;
    }

    public T getTarget() {
        return target;
    }
}
