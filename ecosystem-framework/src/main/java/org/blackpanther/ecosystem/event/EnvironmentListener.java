package org.blackpanther.ecosystem.event;

/**
 * @author MACHIZAUD Andréa
 * @version 0.2 - Wed May 11 02:54:46 CEST 2011
 */
public interface EnvironmentListener<T extends EnvironmentEvent> {

    public void update(T e);

}
