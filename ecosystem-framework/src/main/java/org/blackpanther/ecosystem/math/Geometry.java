package org.blackpanther.ecosystem.math;

import java.awt.*;
import java.awt.geom.Dimension2D;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.util.logging.Logger;

import static org.blackpanther.ecosystem.helper.Helper.within;

/**
 * Helper to use mathematical data
 *
 * @author MACHIZAUD Andréa
 * @version 1.0-alpha - Tue May 24 23:49:57 CEST 2011
 */
public final class Geometry {

    private static final Logger logger =
            Logger.getLogger(
                    Geometry.class.getCanonicalName()
            );

    private static final Double COMPARISON_THRESHOLD = 0.000001;
    public static final Double PI_2 = 2.0 * Math.PI;

    private Geometry() {
    }

    private static boolean insideRange(Line2D line, Point2D point) {
        return Math.min(line.getX1(), line.getX2()) <= point.getX()
                && point.getX() <= Math.max(line.getX1(), line.getX2())
                && Math.min(line.getY1(), line.getY2()) <= point.getY()
                && point.getY() <= Math.max(line.getY1(), line.getY2());
    }

    public static Point2D getIntersection(Line2D a, Line2D b) {
        //Fetch the intersection - method inspired by http://paulbourke.net/geometry/lineline2d/

        double denominator =
                (b.getY2() - b.getY1()) * (a.getX2() - a.getX1())
                        - (b.getX2() - b.getX1()) * (a.getY2() - a.getY1());

        double directionalVectorLineA =
                ((b.getX2() - b.getX1()) * (a.getY1() - b.getY1())
                        - (b.getY2() - b.getY1()) * (a.getX1() - b.getX1()))
                        /
                        ((b.getY2() - b.getY1()) * (a.getX2() - a.getX1())
                                - (b.getX2() - b.getX1()) * (a.getY2() - a.getY1()));

        double directionalVectorLineB =
                ((a.getX2() - a.getX1()) * (a.getY1() - b.getY1())
                        - (a.getY2() - a.getY1()) * (a.getX1() - b.getX1()))
                        /
                        ((b.getY2() - b.getY1()) * (a.getX2() - a.getX1())
                                - (b.getX2() - b.getX1()) * (a.getY2() - a.getY1()));


        //No intersection
        if (Math.abs(denominator) <= COMPARISON_THRESHOLD) { //HELP double are never equal to 0.0
            return null;
        }
        //intersection
        else if (within(Math.abs(directionalVectorLineA), 0.0, 1.0)) {

            Point2D intersection = new Point2D.Double(
                    b.getX1() + directionalVectorLineB * (b.getX2() - b.getX1()),
                    b.getY1() + directionalVectorLineB * (b.getY2() - b.getY1())
            );

            //lines are not infinite, check if intersection is on both lines
            if (insideRange(a, intersection) && insideRange(b, intersection)) {
                return intersection;
            } else {
                logger.finer("No intersection with non-infinite lines");
                return null;
            }
        } else if (within(Math.abs(directionalVectorLineB), 0.0, 1.0)) {

            Point2D intersection = new Point2D.Double(
                    a.getX1() + directionalVectorLineA * (a.getX2() - a.getX1()),
                    a.getY1() + directionalVectorLineA * (a.getY2() - a.getY1())
            );

            //lines are not infinite, check if intersection is on both lines
            if (insideRange(a, intersection) && insideRange(b, intersection)) {
                return intersection;
            } else {
                return null;
            }
        } else {
            //no intersection
            return null;
        }
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

    public static class Circle extends Ellipse2D.Double {

        public Circle(double x, double y, double radius) {
            super(x - radius, y - radius, radius * 2.0, radius * 2.0);
        }

        public Circle(Point2D point, double radius) {
            this(point.getX(), point.getY(), radius);
        }

        public Point2D getCenter() {
            return new Point2D.Double(getCenterX(), getCenterY());
        }

        public java.lang.Double getRadius() {
            return getHeight() / 2.0;
        }
    }

}
