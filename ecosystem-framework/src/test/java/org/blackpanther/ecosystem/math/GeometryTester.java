package org.blackpanther.ecosystem.math;

import org.junit.BeforeClass;


import org.junit.Test;

import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.io.IOException;
import java.util.logging.LogManager;

import static org.blackpanther.ecosystem.math.Geometry.getIntersection;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

/**
 * @author MACHIZAUD Andréa
 * @version 0.2 - Wed May 11 02:54:46 CEST 2011
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

    }
}
