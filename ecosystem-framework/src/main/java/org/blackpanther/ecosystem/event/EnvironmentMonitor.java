package org.blackpanther.ecosystem.event;

import org.blackpanther.ecosystem.Agent;
import org.blackpanther.ecosystem.Environment;
import org.blackpanther.ecosystem.Resource;

import java.awt.geom.Line2D;
import java.io.IOException;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * @author MACHIZAUD Andréa
 * @version 1.0-alpha - Wed May 18 02:01:09 CEST 2011
 */
public class EnvironmentMonitor
        implements Serializable {
    /**
     * Line's event listeners
     */
    private Set<LineListener> lineListeners =
            new HashSet<LineListener>();
    private Set<EvolutionListener> evolutionListeners =
            new HashSet<EvolutionListener>();
    private Set<AgentListener> agentListeners =
            new HashSet<AgentListener>();
    private Set<ResourceListener> resourceListeners =
            new HashSet<ResourceListener>();

    private Environment source;

    public EnvironmentMonitor(Environment sourceEnvironment) {
        this.source = sourceEnvironment;
    }

    /*=========================================================================
                               SERIALIZATION
      =========================================================================*/

    public void clearAllExternalsListeners(){
        lineListeners.clear();
        evolutionListeners.clear();
        agentListeners.clear();
        Iterator<ResourceListener> it = resourceListeners.iterator();
        while (it.hasNext())
            if (!(it.next() instanceof Environment.Area))
                it.remove();
    }

    /*=========================================================================
                               LISTENER HOOK
      =========================================================================*/

    public void addAgentListener(AgentListener listener) {
        agentListeners.add(listener);
    }

    public void addLineListener(LineListener listener) {
        lineListeners.add(listener);
    }

    public void addEvolutionListener(EvolutionListener listener) {
        evolutionListeners.add(listener);
    }

    public void addResourceListener(ResourceListener listener) {
        resourceListeners.add(listener);
    }

    /*=========================================================================
                               EVENT DELEGATE
      =========================================================================*/


    public void fireAgentEvent(AgentEvent.Type eventType, Agent value) {
        AgentEvent event = new AgentEvent(eventType, source, value);
        for (AgentListener listener : agentListeners) {
            listener.update(event);
        }
    }

    public void fireLineEvent(LineEvent.Type eventType, Line2D value) {
        LineEvent event = new LineEvent(eventType, source, value);
        for (LineListener listener : lineListeners) {
            listener.update(event);
        }
    }

    public void fireEvolutionEvent(EvolutionEvent.Type eventType) {
        EvolutionEvent event = new EvolutionEvent(eventType, source);
        for (EvolutionListener listener : evolutionListeners) {
            listener.update(event);
        }
    }

    public void fireResourceEvent(Resource value, ResourceEvent.Type eventType) {
        ResourceEvent event = new ResourceEvent(eventType, source, value);
        for (ResourceListener resourceListener : resourceListeners) {
            resourceListener.update(event);
        }
    }

    public void removeAgentListener(AgentListener listener) {
        agentListeners.remove(listener);
    }

    public void removeEvolutionListener(EvolutionListener listener) {
        evolutionListeners.remove(listener);
    }

    public void removeLineListener(LineListener listener) {
        lineListeners.remove(listener);
    }

    public void removeResourceListener(ResourceListener listener) {
        resourceListeners.remove(listener);
    }
}