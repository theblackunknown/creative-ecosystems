package org.blackpanther.ecosystem.factory.generator;

import org.blackpanther.ecosystem.BehaviorManager;
import org.blackpanther.ecosystem.factory.generator.random.BehaviorProvider;
import org.blackpanther.ecosystem.factory.generator.random.ColorProvider;
import org.blackpanther.ecosystem.factory.generator.random.DoubleProvider;

import java.awt.*;

/**
 * @author MACHIZAUD Andréa
 * @version 5/21/11
 */
public abstract class RandomProvider<T>
        extends ValueProvider<T> {

    public static ValueProvider RandomProvider(Class valueType) {
        if (valueType.equals(Color.class))
            return ColorProvider.getInstance();
        else if (valueType.equals(BehaviorManager.class))
            return BehaviorProvider.getInstance();
        else
            throw new IllegalArgumentException(
                    "Unsupported value : " +
                            "no random provider of type " + valueType);
    }

    public static ValueProvider RandomProvider(double min, double max) {
        return new DoubleProvider(min, max);
    }
}
