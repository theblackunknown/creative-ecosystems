package org.blackpanther.ecosystem;

import java.awt.*;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;

/**
 * @author MACHIZAUD Andréa
 * @version 1.1-alpha - Thu May 19 01:22:54 CEST 2011
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
