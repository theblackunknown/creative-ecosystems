package org.blackpanther.ecosystem.factory;

import org.blackpanther.ecosystem.agent.Resource;
import org.blackpanther.ecosystem.factory.fields.FieldsConfiguration;

/**
 * @author MACHIZAUD Andréa
 * @version 5/21/11
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