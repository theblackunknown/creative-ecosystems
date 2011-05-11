package org.blackpanther.ecosystem.event;

import org.blackpanther.ecosystem.Environment;

import java.util.EventObject;

/**
 * @author MACHIZAUD Andréa
 * @version 0.2 - Wed May 11 02:54:46 CEST 2011
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
