package org.blackpanther.ecosystem.factory.fields;

import org.blackpanther.ecosystem.factory.generator.ValueProvider;

/**
 * @author MACHIZAUD Andréa
 * @version 1.0-alpha - Tue May 24 23:49:57 CEST 2011
 */
public class GeneFieldMould<T>
        extends FieldMould<T> {

    private boolean mutable;

    public GeneFieldMould(
            String name,
            ValueProvider<T> valueProvider,
            boolean mutable) {
        super(name, valueProvider);
        this.mutable = mutable;
    }

    public boolean isMutable() {
        return mutable;
    }
}
