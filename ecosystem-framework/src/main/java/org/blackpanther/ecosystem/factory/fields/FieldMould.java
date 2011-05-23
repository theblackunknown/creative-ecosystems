package org.blackpanther.ecosystem.factory.fields;

import org.blackpanther.ecosystem.factory.generator.ValueProvider;

/**
 * @author MACHIZAUD Andréa
 * @version 5/21/11
 */
public abstract class FieldMould<T> {

    private String name;
    private ValueProvider<T> provider;

    public FieldMould(String name, ValueProvider<T> valueProvider) {
        this.name = name;
        this.provider = valueProvider;
    }

    public String getName() {
        return name;
    }

    public T getValue() {
        return provider.getValue();
    }
}
