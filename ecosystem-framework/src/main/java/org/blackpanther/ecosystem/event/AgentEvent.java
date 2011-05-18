package org.blackpanther.ecosystem.event;

import org.blackpanther.ecosystem.Agent;
import org.blackpanther.ecosystem.Environment;

/**
 * @author MACHIZAUD Andréa
 * @version 1.0-alpha - Wed May 18 02:01:08 CEST 2011
 */
public class AgentEvent
        extends EnvironmentEvent {

    private Type type;
    private Agent agent;

    public enum Type {
        BORN,
        DEATH
    }

    public AgentEvent(Type type, Environment source, Agent agent) {
        super(source);
        this.type = type;
        this.agent = agent;
    }

    public Type getType() {
        return type;
    }

    public Agent getAgent() {
        return agent;
    }
}
