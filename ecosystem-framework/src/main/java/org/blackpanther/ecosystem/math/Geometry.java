package org.blackpanther.ecosystem.math;

import java.awt.geom.Point2D;

/**
 * Helper to use mathematical data
 *
 * @author MACHIZAUD Andréa
 * @version 1.0 - 4/23/11
 */
public final class Geometry {

    /**
     * A math vector designed to be used in a 2D-space
     */
    public class Vector2D {
        private Point2D start;
        private Point2D destination;

        public Vector2D(double x1, double y1, double x2, double y2) {
            start = new ImmutablePoint(x1, y1);
            destination = new ImmutablePoint(x2, y2);
        }

        public Vector2D(Point2D start, Point2D destination) {
            this(start.getX(), start.getY(), destination.getX(), destination.getY());
        }

        public Point2D getStart() {
            return start;
        }

        public Point2D getDestination() {
            return destination;
        }
    }

    /**
     * Implement Point2D class in order to have an immutable geometric point
     */
    private class ImmutablePoint extends Point2D {

        private java.lang.Double x;
        private java.lang.Double y;

        public ImmutablePoint(double x, double y) {
            this.x = x;
            this.y = y;
        }

        public ImmutablePoint(Point2D p) {
            this(p.getX(), p.getY());
        }

        @Override
        public double getX() {
            return x;
        }

        @Override
        public double getY() {
            return y;
        }

        @Override
        public void setLocation(double x, double y) {
            throw new UnsupportedOperationException("Immutable Point's location can be changed");
        }
    }
}
