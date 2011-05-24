package org.blackpanther.ecosystem.factory;

import org.blackpanther.ecosystem.agent.Resource;
import org.blackpanther.ecosystem.factory.fields.FieldsConfiguration;

/**
 * @author MACHIZAUD Andréa
 * @version 1.0-alpha - Tue May 24 23:49:57 CEST 2011
 */
public class ResourceFactory
        extends EnvironmentFactory<Resource> {

    ResourceFactory() {
    }

    @Override
    public Resource createAgent(FieldsConfiguration config) {
        return new Resource(config);
    }

}
