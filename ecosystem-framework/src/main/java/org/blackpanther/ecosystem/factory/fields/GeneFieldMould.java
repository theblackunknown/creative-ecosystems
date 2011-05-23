package org.blackpanther.ecosystem.factory.fields;

import org.blackpanther.ecosystem.factory.generator.ValueProvider;

/**
 * @author MACHIZAUD Andréa
 * @version 5/21/11
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
