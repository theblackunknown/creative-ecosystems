package org.blackpanther.ecosystem.event;

/**
 * @author MACHIZAUD Andréa
 * @version 5/2/11
 */
public interface EnvironmentListener<T extends EnvironmentEvent> {

    public void update(T e);

}
