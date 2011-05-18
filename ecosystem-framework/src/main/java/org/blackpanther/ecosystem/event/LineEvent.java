package org.blackpanther.ecosystem.event;

import org.blackpanther.ecosystem.Environment;

import java.awt.geom.Line2D;
import java.util.EventObject;

/**
 * @author MACHIZAUD Andréa
 * @version 1.1-alpha - Thu May 19 01:22:54 CEST 2011
 */
public class LineEvent
        extends EnvironmentEvent {

    /**
     * Precise type of the event that happened
     */
    public enum Type {
        ADDED
    }

    /**
     * Event's type
     */
    private Type type;
    /**
     * Line concerned by this event
     */
    private Line2D line;

    /**
     * Construct an line event object
     *
     * @param eventType
     *      type of event that happened
     * @param source
     *      source's environment
     * @param concernedLine
     *      line concerned by this event
     */
    public LineEvent(LineEvent.Type eventType, Environment source, Line2D concernedLine) {
        super(source);
        this.type = eventType;
        this.line = concernedLine;
    }

    public Type getType() {
        return type;
    }

    public Line2D getValue() {
        return line;
    }
}
