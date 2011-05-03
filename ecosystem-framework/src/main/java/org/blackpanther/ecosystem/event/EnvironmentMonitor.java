package org.blackpanther.ecosystem.event;

import org.blackpanther.ecosystem.Environment;

import java.awt.geom.Line2D;
import java.lang.ref.WeakReference;
import java.util.HashSet;
import java.util.Set;

/**
 * @author MACHIZAUD Andréa
 * @version 5/2/11
 */
public class EnvironmentMonitor {
    /**
     * Line's event listeners
     */
    private Set<LineListener> lineListeners =
            new HashSet<LineListener>();
    private Set<EvolutionListener> evolutionListeners =
            new HashSet<EvolutionListener>();

    private Environment source;

    public EnvironmentMonitor(Environment sourceEnvironment) {
        this.source = sourceEnvironment;
    }

    /*=========================================================================
                               LISTENER HOOK
      =========================================================================*/

    public void addLineListener(LineListener listener) {
        lineListeners.add(listener);
    }

    public void addEvolutionListener(EvolutionListener listener) {
        evolutionListeners.add(listener);
    }

    /*=========================================================================
                               EVENT DELEGATE
      =========================================================================*/

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
}
