package org.blackpanther.ecosystem.factory.generator.random;

import org.blackpanther.ecosystem.Configuration;
import org.blackpanther.ecosystem.factory.generator.RandomProvider;

/**
 * @author MACHIZAUD Andréa
 * @version 5/22/11
 */
public class DoubleProvider
        extends RandomProvider<Double> {

    private double min;
    private double max;

    public DoubleProvider(double min, double max) {
        this.min = min;
        this.max = max;
    }

    @Override
    public Double getValue() {
        return min +
                Configuration.Configuration.getRandom().nextDouble()
                        * (max - min);
    }
}
