package org.blackpanther.ecosystem.factory.generator;

import org.blackpanther.ecosystem.BehaviorManager;
import org.blackpanther.ecosystem.factory.generator.provided.BehaviorProvider;
import org.blackpanther.ecosystem.factory.generator.provided.ColorProvider;
import org.blackpanther.ecosystem.factory.generator.provided.DoubleProvider;

import java.awt.*;

/**
 * @author MACHIZAUD Andréa
 * @version 5/22/11
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
