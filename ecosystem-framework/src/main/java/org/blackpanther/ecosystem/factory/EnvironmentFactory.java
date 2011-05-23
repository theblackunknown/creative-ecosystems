package org.blackpanther.ecosystem.factory;

import org.blackpanther.ecosystem.factory.fields.FieldsConfiguration;

/**
 * @author MACHIZAUD Andréa
 * @version 5/21/11
 */
public abstract class EnvironmentFactory<T> {

    abstract public T createAgent(FieldsConfiguration config);

}
