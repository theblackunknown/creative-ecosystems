package org.blackpanther.ecosystem.event;

import org.blackpanther.ecosystem.Environment;
import org.blackpanther.ecosystem.Resource;

/**
 * @author MACHIZAUD Andréa
 * @version 5/13/11
 */
public class ResourceEvent extends EnvironmentEvent{

    private Resource resource;
    private Type type;

    public enum Type {
        DECREASED,
        DEPLETED;
    }

    public ResourceEvent(Type type, Environment source, Resource resource) {
        super(source);
        this.type = type;
        this.resource = resource;
    }

    public Resource getResource() {
        return resource;
    }

    public Type getType() {
        return type;
    }
}
