package org.blackpanther.ecosystem;

import java.awt.*;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;

/**
 * @author MACHIZAUD Andréa
 * @version 5/18/11
 */
public class ColorfulTrace
        extends Line2D.Double {

    private Color color;

    public ColorfulTrace(Point2D x, Point2D y, Color c) {
        super(x, y);
        this.color = c;
    }

    public Color getColor() {
        return color;
    }
}
