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
    private Set<WeakReference<LineListener>> lineListeners =
            new HashSet<WeakReference<LineListener>>();
    private Set<WeakReference<EvolutionListener>> evolutionListeners =
            new HashSet<WeakReference<EvolutionListener>>();

    private Environment source;

    public EnvironmentMonitor(Environment sourceEnvironment) {
        this.source = sourceEnvironment;
    }

    /*=========================================================================
                               LISTENER HOOK
      =========================================================================*/

    public void addLineListener(LineListener listener) {
        lineListeners.add(new WeakReference<LineListener>(listener));
    }

    /*=========================================================================
                               EVENT DELEGATE
      =========================================================================*/

    public void fireLineEvent(LineEvent.Type eventType, Line2D value) {
        LineEvent event = new LineEvent(eventType,source,value);
        for (WeakReference<LineListener> listener : lineListeners) {
            LineListener realListener = listener.get();
            if (realListener != null) {
                realListener.update(event);
            }
        }
    }

    public void fireEvolutionEvent(EvolutionEvent.Type eventType) {
        EvolutionEvent event = new EvolutionEvent(eventType,source);
        for (WeakReference<EvolutionListener> listener : evolutionListeners) {
            EvolutionListener realListener = listener.get();
            if (realListener != null) {
                realListener.update(event);
            }
        }
    }
}
