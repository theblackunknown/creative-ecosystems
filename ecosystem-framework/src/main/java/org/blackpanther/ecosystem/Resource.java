package org.blackpanther.ecosystem;

import org.blackpanther.ecosystem.event.ResourceEvent;
import org.blackpanther.ecosystem.event.ResourceListener;

import java.awt.geom.Point2D;
import java.io.Serializable;
import java.util.Stack;

/**
 * @author MACHIZAUD Andréa
 * @version 1.0-alpha - Wed May 18 02:01:09 CEST 2011
 */
public class Resource
        extends Point2D.Double
        implements Serializable {

    public static final java.lang.Double RADIUS = 1.5;

    /**
     * Amount of resources
     */
    private double amount;
    private Environment environment;

    public Resource(double x, double y, double amount) {
        super(x, y);
        this.amount = amount;
    }

    public java.lang.Double getAmount() {
        return amount;
    }

    public java.lang.Double consume() {
        environment.getEventSupport().fireResourceEvent(this, ResourceEvent.Type.DEPLETED);
        return amount;
    }

    void attachTo(Environment env) {
        this.environment = env;
    }
}
