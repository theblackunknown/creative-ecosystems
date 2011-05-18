package org.blackpanther.ecosystem.event;

import org.blackpanther.ecosystem.Environment;

import java.util.EventObject;

/**
 * @author MACHIZAUD Andréa
 * @version 1.1-alpha - Thu May 19 01:22:54 CEST 2011
 */
abstract class EnvironmentEvent
        extends EventObject {

    public EnvironmentEvent(Environment source) {
        super(source);
    }

    public Environment getSource() {
        return (Environment) super.getSource();
    }
}
