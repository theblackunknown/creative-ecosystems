package org.blackpanther.ecosystem.factory.generator;

import org.blackpanther.ecosystem.behaviour.BehaviorManager;
import org.blackpanther.ecosystem.factory.generator.provided.BehaviorProvider;
import org.blackpanther.ecosystem.factory.generator.provided.ColorProvider;
import org.blackpanther.ecosystem.factory.generator.provided.DoubleProvider;
import org.blackpanther.ecosystem.factory.generator.provided.LocationProvider;

import java.awt.*;
import java.awt.geom.Point2D;

/**
 * @author MACHIZAUD Andréa
 * @version 1.0-alpha - Tue May 24 23:49:57 CEST 2011
 */
public abstract class StandardProvider<T>
        extends ValueProvider<T> {

    private T providedValue;

    protected StandardProvider(T provided) {
        this.providedValue = provided;
    }

    @Override
    public T getValue() {
        return providedValue;
    }

    public static ValueProvider StandardProvider(Object providedValue) {
        if (Color.class.isInstance(providedValue))
            return new ColorProvider(Color.class.cast(providedValue));
        else if (Point2D.class.isInstance(providedValue))
            return new LocationProvider(Point2D.class.cast(providedValue));
        else if (Double.class.isInstance(providedValue))
            return new DoubleProvider(Double.class.cast(providedValue));
        else if (BehaviorManager.class.isInstance(providedValue))
            return new BehaviorProvider(BehaviorManager.class.cast(providedValue));
        else
            throw new IllegalArgumentException(
                    "Unsupported value : " +
                            "no provider available for " + providedValue +
                            " of type " + providedValue.getClass());
    }
}
