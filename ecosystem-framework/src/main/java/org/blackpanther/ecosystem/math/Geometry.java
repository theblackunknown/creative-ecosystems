package org.blackpanther.ecosystem.math;

import java.awt.geom.Dimension2D;
import java.awt.geom.Point2D;

/**
 * Helper to use mathematical data
 *
 * @author MACHIZAUD Andréa
 * @version 0.3 - Sun May  1 00:00:13 CEST 2011
 */
public final class Geometry {

    private Geometry() {
    }

    public static class Dimension
            extends Dimension2D {

        private double width;
        private double height;

        public Dimension(
                final double width,
                final double height
        ) {
            super();
            this.width = width;
            this.height = height;
        }

        @Override
        public double getWidth() {
            return width;
        }

        @Override
        public double getHeight() {
            return height;
        }

        @Override
        public void setSize(double v, double v1) {
            this.width = v;
            this.height = v1;
        }
    }

    /**
     * A math vector designed to be used in a 2D-space
     */
    public static class Direction2D {
        /**
         * Abscissa variation
         */
        private Double dx;
        /**
         * Ordinate variation
         */
        private Double dy;

        public Direction2D(
                final double dx,
                final double dy
        ) {
            this.dx = dx;
            this.dy = dy;
        }

        /**
         * Constructor which set dx and dy corresponding to abscissa et ordinate of given respectively
         *
         * @param variation destination
         */
        public Direction2D(
                final Point2D variation
        ) {
            this(variation.getX(), variation.getY());
        }

        /**
         * Give abscissa variation
         *
         * @return
         *      abscissa variation
         */
        public final Double getDx() {
            return dx;
        }

        /**
         * Give ordinate variation
         *
         * @return
         *      ordinate variation
         */
        public final Double getDy() {
            return dy;
        }

        @Override
        public String toString() {
            return String.format("Direction2D[dx = %.3f, dy = %.3f]",dx,dy);
        }
    }

    /**
     * Implement Point2D class in order to have an immutable geometric point
     */
    public static class ImmutablePoint extends Point2D {

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
