package org.blackpanther.ecosystem;

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

    public static final Integer MAX_AMOUNT = 100;

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

    public Resource(Point2D location, int amount, double radius) {
        super(location,radius);
        this.amount = normalize(amount);
    }

    public Resource(double x, double y, int amount, double radius) {
        super(new Point2D.Double(x,y),radius);
        this.amount = normalize(amount);
    }

    public Resource(Point2D location, int amount) {
        this(location, amount, 3.0);
    }

    public void setAmount(int amount) {
        this.amount = normalize(amount);
    }

    public int getAmount() {
        return amount;
    }
}
