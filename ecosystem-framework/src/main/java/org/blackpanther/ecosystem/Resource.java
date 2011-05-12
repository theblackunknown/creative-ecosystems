package org.blackpanther.ecosystem;

import org.blackpanther.ecosystem.event.EnvironmentMonitor;
import org.blackpanther.ecosystem.event.ResourceEvent;
import org.blackpanther.ecosystem.math.Geometry;

import java.awt.geom.Point2D;
import java.io.Serializable;

/**
 * @author MACHIZAUD Andréa
 * @version 5/11/11
 */
public class Resource
        extends Geometry.Circle
        implements Serializable {

    public static final Integer MAX_AMOUNT = 300;

    private static int normalize(int amount) {
        if (amount > MAX_AMOUNT)
            return MAX_AMOUNT;
        else if (amount < 0)
            return 0;
        else
            return amount;
    }

    /**
     * Amount of resources
     */
    private int amount;
    private EnvironmentMonitor eventSupport;

    public Resource(Point2D location, int amount, double radius) {
        super(location, radius);
        this.amount = normalize(amount);
    }

    public Resource(double x, double y, int amount, double radius) {
        super(new Point2D.Double(x, y), radius);
        this.amount = normalize(amount);
    }

    public Resource(Point2D location, int amount) {
        this(location, amount, 3.0);
    }

    public int getAmount() {
        return amount;
    }

    public int consume(int expected) {
        if (expected <= amount) {
            amount -= expected;
            eventSupport.fireResourceEvent(this, ResourceEvent.Type.DECREASED);
            return expected;
        } else {
            int remaining = amount;
            if (remaining != 0)
                eventSupport.fireResourceEvent(this, ResourceEvent.Type.DECREASED);
            amount = 0;
            return remaining;
        }
    }

    void setEventSupport(EnvironmentMonitor eventSupport) {
        this.eventSupport = eventSupport;
    }
}
