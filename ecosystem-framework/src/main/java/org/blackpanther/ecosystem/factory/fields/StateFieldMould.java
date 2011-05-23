package org.blackpanther.ecosystem.factory.fields;

import org.blackpanther.ecosystem.factory.generator.ValueProvider;

/**
 * @author MACHIZAUD Andréa
 * @version 5/22/11
 */
public class StateFieldMould<T>
        extends FieldMould<T> {

    public StateFieldMould(String name, ValueProvider<T> valueProvider) {
        super(name, valueProvider);
    }
}
