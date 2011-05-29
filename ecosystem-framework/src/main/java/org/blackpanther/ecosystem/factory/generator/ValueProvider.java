package org.blackpanther.ecosystem.factory.generator;

import java.io.Serializable;

/**
 * @author MACHIZAUD Andréa
 * @version 1.0-alpha - Tue May 24 23:49:57 CEST 2011
 */
public abstract class ValueProvider<T>
        implements Serializable {

    private static final long serialVersionUID = 1L;

    abstract public T getValue();

}
