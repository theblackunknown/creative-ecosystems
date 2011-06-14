package org.blackpanther.ecosystem.factory.fields;

import org.blackpanther.ecosystem.factory.generator.RandomProvider;
import org.blackpanther.ecosystem.factory.generator.ValueProvider;

import java.io.Serializable;

/**
 * @author MACHIZAUD Andréa
 * @version 1.0-alpha - Tue May 24 23:49:57 CEST 2011
 */
public abstract class FieldMould<T>
        implements Serializable {

    private static final long serialVersionUID = 1L;

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

    public boolean isRandomized(){
        return provider instanceof RandomProvider;
    }
}
