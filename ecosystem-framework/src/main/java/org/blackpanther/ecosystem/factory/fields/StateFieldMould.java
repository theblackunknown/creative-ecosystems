package org.blackpanther.ecosystem.factory.fields;

import org.blackpanther.ecosystem.factory.generator.ValueProvider;

/**
 * @author MACHIZAUD Andréa
 * @version 1.0-alpha - Tue May 24 23:49:57 CEST 2011
 */
public class StateFieldMould<T>
        extends FieldMould<T> {

    public StateFieldMould(String name, ValueProvider<T> valueProvider) {
        super(name, valueProvider);
    }
}
