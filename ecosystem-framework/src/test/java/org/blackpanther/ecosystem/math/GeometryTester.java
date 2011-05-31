package org.blackpanther.ecosystem.math;

import org.junit.Test;

import java.awt.geom.Line2D;
import java.awt.geom.Point2D;

import static org.blackpanther.ecosystem.math.Geometry.getIntersection;
import static org.junit.Assert.*;

/**
 * @author MACHIZAUD Andréa
 * @version 1.0-alpha - Tue May 24 23:49:58 CEST 2011
 */
public class GeometryTester {

    @Test
    public void intersectionPoint() {
        Point2D firstIntersection = new Point2D.Double(1.0, 1.0);
        Point2D secondIntersection = new Point2D.Double(1.0, 0.0);

        assertEquals(
                "Fail at basic intersection between " +
                        "[(0.0,2.0),(2.0,0.0) & (0.0,0.0),(2.0,2.0)]",
                firstIntersection,
                getIntersection(
                        new Line2D.Double(0.0, 2.0, 2.0, 0.0),
                        new Line2D.Double(0.0, 0.0, 2.0, 2.0)
                )
        );

        assertEquals(
                "Fail at basic intersection between " +
                        "[(0.0,1.0),(3.0,-2.0) & (0.0,0.0),(2.0,0.0)]",
                secondIntersection,
                getIntersection(
                        new Line2D.Double(0.0, 1.0, 3.0, -2.0),
                        new Line2D.Double(0.0, 0.0, 2.0, 0.0)
                )
        );

        assertNull(
                "Not intersection because liens are not infinite",
                getIntersection(
                        new Line2D.Double(0.0, -2.0, 2.0, -2.0),
                        new Line2D.Double(0.0, 1.0, 3.0, -2.0)
                )
        );

        assertNull(
                "Not intersection because liens are not infinite",
                getIntersection(
                        new Line2D.Double(0.0, 1.0, 3.0, -2.0),
                        new Line2D.Double(0.0, 0.0, 0.0, -2.0)
                )
        );

        assertNotNull(
                "???",
                getIntersection(
                        new Line2D.Double(198.0, 383.73, 199.82, 377.18),
                        new Line2D.Double(200.55, 383.55, 197.63, 377.72)
                )
        );

    }
}
