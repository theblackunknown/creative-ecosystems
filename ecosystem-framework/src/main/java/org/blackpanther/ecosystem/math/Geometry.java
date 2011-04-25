package org.blackpanther.ecosystem.math;

import java.awt.geom.Point2D;

/**
 * Helper to use mathematical data
 *
 * @author MACHIZAUD Andréa
 * @version v0.2.1 - Sun Apr 24 18:01:06 CEST 2011
 */
public final class Geometry {

    private Geometry() {
    }

    /**
     * A math vector designed to be used in a 2D-space
     */
    public static class Vector2D {
        /**
         * Start point of this vector
         */
        private Point2D start;
        /**
         * End point of this vector
         */
        private Point2D destination;

        /**
         * Default constructor which define a vector
         * with a start point and a destination point
         *
         * @param x1 start abscissa
         * @param y1 start ordinate
         * @param x2 destination abscissa
         * @param y2 destination ordinate
         */
        public Vector2D(
                final double x1,
                final double y1,
                final double x2,
                final double y2) {
            start = new ImmutablePoint(x1, y1);
            destination = new ImmutablePoint(x2, y2);
        }

        /**
         * Constructor with {@link Point2D} instead of abscissa and ordinate
         *
         * @param startPoint       start
         * @param destinationPoint destination
         */
        public Vector2D(
                final Point2D startPoint,
                final Point2D destinationPoint) {
            this(startPoint.getX(), startPoint.getY(),
                    destinationPoint.getX(), destinationPoint.getY());
        }

        /**
         * Constructor which set a vector from <pre>(0.0,0.0)</pre>
         * to given destination point
         *
         * @param destinationPoint destination
         */
        public Vector2D(
                final Point2D destinationPoint
        ) {
            this(0.0, 0.0, destinationPoint.getX(), destinationPoint.getY());
        }

        /**
         * Give the start point of this vector
         *
         * @return start point
         */
        public final Point2D getStart() {
            return start;
        }

        /**
         * Give the end point of this vector
         *
         * @return end point
         */
        public final Point2D getDestination() {
            return destination;
        }
    }

    /**
     * Implement Point2D class in order to have an immutable geometric point
     */
    private static class ImmutablePoint extends Point2D {

        private java.lang.Double abscissa;
        private java.lang.Double ordinate;

        public ImmutablePoint(
                final double x,
                final double y) {
            this.abscissa = x;
            this.ordinate = y;
        }

        public ImmutablePoint(
                final Point2D p) {
            this(p.getX(), p.getY());
        }

        @Override
        public double getX() {
            return abscissa;
        }

        @Override
        public double getY() {
            return ordinate;
        }

        @Override
        public void setLocation(
                final double x,
                final double y) {
            throw new UnsupportedOperationException("Immutable Point's location can be changed");
        }
    }
}
