package org.blackpanther.ecosystem;

import org.blackpanther.ecosystem.event.EnvironmentMonitor;
import org.blackpanther.ecosystem.event.ResourceEvent;
import org.blackpanther.ecosystem.event.ResourceListener;

import java.awt.geom.Point2D;
import java.io.Serializable;
import java.util.Stack;

/**
 * @author MACHIZAUD Andréa
 * @version 5/11/11
 */
public class Resource
        extends Point2D.Double
        implements Serializable {

    public static final java.lang.Double RADIUS = 3.0;

    /**
     * Amount of resources
     */
    private double amount;
    private EnvironmentMonitor eventSupport;
    private Stack<ResourceListener> toBeCleared;

    public Resource(double x, double y, double amount) {
        super(x, y);
        this.amount = amount;
    }

    public java.lang.Double getAmount() {
        return amount;
    }

    public java.lang.Double consume() {
        toBeCleared = new Stack<ResourceListener>();
        eventSupport.fireResourceEvent(this, ResourceEvent.Type.DEPLETED);
        return amount;
    }

    void setEventSupport(EnvironmentMonitor eventSupport) {
        this.eventSupport = eventSupport;
    }
}
