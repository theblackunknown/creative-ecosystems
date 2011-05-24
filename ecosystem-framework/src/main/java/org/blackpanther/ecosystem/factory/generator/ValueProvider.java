package org.blackpanther.ecosystem.factory.generator;

import java.io.Serializable;

/**
 * @author MACHIZAUD Andréa
 * @version 5/21/11
 */
public abstract class ValueProvider<T>
        implements Serializable {

    abstract public T getValue();

}
