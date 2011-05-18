package org.blackpanther.ecosystem.helper;

import org.junit.Test;

import java.awt.geom.Point2D;

import static org.blackpanther.ecosystem.helper.Helper.computeOrientation;
import static org.junit.Assert.assertTrue;
import static java.lang.Math.*;

/**
 * @author MACHIZAUD Andréa
 * @version 1.1-alpha - Thu May 19 01:22:54 CEST 2011
 */
public class HelperTester {

    public static final double DECIMAL_PRECISION = 0.0001;

    @Test
    public void testOrientationComputation() {
        Point2D source = new Point2D.Double();
        Point2D target = new Point2D.Double();
        double angle;
        double expected;

        source.setLocation(0.0,0.0);
        target.setLocation(-0.5,-0.5);
        angle = computeOrientation(source, target);
        expected = (-3.0 / 4.0) * Math.PI;

        assertTrue(abs(angle - expected) < DECIMAL_PRECISION);
        assertTrue(abs(cos(angle) - cos(expected)) < DECIMAL_PRECISION);

        source.setLocation(0.0,0.0);
        target.setLocation(-0.5,0.5);
        angle = computeOrientation(source, target);
        expected = (3.0 / 4.0) * Math.PI;

        assertTrue(abs(angle - expected) < DECIMAL_PRECISION);
        assertTrue(abs(cos(angle) - cos(expected)) < DECIMAL_PRECISION);

        source.setLocation(0.0,0.0);
        target.setLocation(0.5,0.5);
        angle = computeOrientation(source, target);
        expected = (1.0 / 4.0) * Math.PI;

        assertTrue(abs(angle - expected) < DECIMAL_PRECISION);
        assertTrue(abs(cos(angle) - cos(expected)) < DECIMAL_PRECISION);

        source.setLocation(0.0,0.0);
        target.setLocation(0.5,-0.5);
        angle = computeOrientation(source, target);
        expected = (-1.0 / 4.0) * Math.PI;

        assertTrue(abs(angle - expected) < DECIMAL_PRECISION);
        assertTrue(abs(cos(angle) - cos(expected)) < DECIMAL_PRECISION);

        source.setLocation(0.0,0.0);
        target.setLocation(0.0, 1.0);
        angle = computeOrientation(source, target);
        expected = Math.PI / 2.0;

        assertTrue(abs(angle - expected) < DECIMAL_PRECISION);
        assertTrue(abs(cos(angle) - cos(expected)) < DECIMAL_PRECISION);
    }

}
