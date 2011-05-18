package org.blackpanther.ecosystem.event;

import org.blackpanther.ecosystem.Environment;
import org.blackpanther.ecosystem.Resource;

/**
 * @author MACHIZAUD Andréa
 * @version 1.1-alpha - Thu May 19 01:22:54 CEST 2011
 */
public class ResourceEvent extends EnvironmentEvent{

    private Resource resource;
    private Type type;

    public enum Type {
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
