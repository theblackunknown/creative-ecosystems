package org.blackpanther.ecosystem.event;

import org.blackpanther.ecosystem.Environment;

import java.util.EventObject;

/**
 * @author MACHIZAUD Andréa
 * @version 1.0-alpha - Wed May 18 02:01:09 CEST 2011
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
