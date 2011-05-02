package org.blackpanther.ecosystem.event;

import org.blackpanther.ecosystem.Environment;

/**
 * @author MACHIZAUD Andréa
 * @version 5/2/11
 */
public class EvolutionEvent
        extends EnvironmentEvent {

    /**
     * Precise type of the event that happened
     */
    public enum Type {
        STARTED,
        CYCLE_END,
        ENDED
    }

    private Type type;

    /**
     * Constructs a prototypical Event.
     *
     * @param source The object on which the Event initially occurred.
     * @throws IllegalArgumentException if source is null.
     */
    public EvolutionEvent(Type eventType, Environment source) {
        super(source);
        this.type = eventType;
    }

    public Type getType() {
        return type;
    }
}
