package org.blackpanther.ecosystem.event;

import org.blackpanther.ecosystem.Environment;

import java.util.EventObject;

/**
 * @author MACHIZAUD Andréa
 * @version 5/2/11
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
