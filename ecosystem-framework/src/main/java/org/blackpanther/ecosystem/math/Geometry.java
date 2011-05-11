package org.blackpanther.ecosystem.math;

import java.awt.geom.Dimension2D;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import static org.blackpanther.ecosystem.helper.Helper.require;
import static org.blackpanther.ecosystem.helper.Helper.within;

/**
 * Helper to use mathematical data
 *
 * @author MACHIZAUD Andréa
 * @version 0.2 - Wed May 11 02:54:46 CEST 2011
 */
public final class Geometry {

    private static final Logger logger =
            Logger.getLogger(
                    Geometry.class.getCanonicalName()
            );

    private static final Double COMPARISON_THRESHOLD = 0.000001;

    private Geometry() {
    }

    /**
     * FIXME Also bullshit, don't compute them all, determine with line position which side are going to collide
     *
     * @param area
     * @param line
     * @return intersectionLine
     */
    public static Line2D intersect(Rectangle2D area, Line2D line) {
        Line2D northLine = new Line2D.Double(
                area.getX(),
                area.getY(),
                area.getX() + area.getWidth(),
                area.getY()
        );
        Line2D westLine = new Line2D.Double(
                area.getX(),
                area.getY(),
                area.getX(),
                area.getY() - area.getHeight()
        );
        Line2D southLine = new Line2D.Double(
                area.getX(),
                area.getY() - area.getHeight(),
                area.getX() + area.getWidth(),
                area.getY() - area.getHeight()
        );
        Line2D eastLine = new Line2D.Double(
                area.getX() + area.getWidth(),
                area.getY(),
                area.getX() + area.getWidth(),
                area.getY() - area.getHeight()
        );
        //compute all collisions with given area
        Point2D topCollisions = getIntersection(
                northLine, line
        );
        Point2D westCollisions = getIntersection(
                westLine, line
        );
        Point2D eastCollisions = getIntersection(
                eastLine, line
        );
        Point2D southCollisions = getIntersection(
                southLine, line
        );
        //check that only two side collided
        List<Point2D> nonNullCollection = new ArrayList<Point2D>();
        if (topCollisions != null)
            nonNullCollection.add(topCollisions);
        if (westCollisions != null)
            nonNullCollection.add(westCollisions);
        if (eastCollisions != null)
            nonNullCollection.add(eastCollisions);
        if (southCollisions != null)
            nonNullCollection.add(southCollisions);

        if (nonNullCollection.isEmpty())
            return null;
        else {
            require(nonNullCollection.size() == 2, String.format(
                    "More than two collision between %s and %s",
                    area, line
            ));

            return new Line2D.Double(
                    nonNullCollection.get(0),
                    nonNullCollection.get(1)
            );
        }
    }

    private static boolean insideRange(Line2D line, Point2D point) {
        return Math.min(line.getX1(), line.getX2()) <= point.getX()
                && point.getX() <= Math.max(line.getX1(), line.getX2())
                && Math.min(line.getY1(), line.getY2()) <= point.getY()
                && point.getY() <= Math.max(line.getY1(), line.getY2());
    }

//    public static String toString(Line2D line) {
//        return line == null
//                ? "null"
//                : String.format("[%s to %s]",
//                toString(line.getP1()),
//                toString(line.getP2()));
//    }
//
//    public static String toString(Point2D point) {
//        return point == null
//                ? "null"
//                : String.format("(%.2f,%.2f)",
//                point.getX(),
//                point.getY());
//    }

    public static Point2D getIntersection(Line2D a, Line2D b) {
        //Fetch the intersection - method inspired by http://paulbourke.net/geometry/lineline2d/

        double denominator =
                (b.getY2() - b.getY1()) * (a.getX2() - a.getX1())
                        - (b.getX2() - b.getX1()) * (a.getY2() - a.getY1());

//        double numeratorBufferA =
//                (b.getX2() - b.getX1()) * (a.getY1() - b.getY1())
//                        - (b.getY2() - b.getY1()) * (a.getX1() - b.getX1());

        double directionalVectorLineA =
                ((b.getX2() - b.getX1()) * (a.getY1() - b.getY1())
                        - (b.getY2() - b.getY1()) * (a.getX1() - b.getX1()))
                        /
                        ((b.getY2() - b.getY1()) * (a.getX2() - a.getX1())
                                - (b.getX2() - b.getX1()) * (a.getY2() - a.getY1()));

//        double numeratorBufferB =
//                (a.getX2() - a.getX1()) * (a.getY1() - b.getY1())
//                        - (a.getY2() - a.getY1()) * (a.getX1() - b.getX1());

        double directionalVectorLineB =
                ((a.getX2() - a.getX1()) * (a.getY1() - b.getY1())
                        - (a.getY2() - a.getY1()) * (a.getX1() - b.getX1()))
                        /
                        ((b.getY2() - b.getY1()) * (a.getX2() - a.getX1())
                                - (b.getX2() - b.getX1()) * (a.getY2() - a.getY1()));


        //No intersection
        if (Math.abs(denominator) <= COMPARISON_THRESHOLD) { //HELP double are never equal to 0.0
//            if (Math.abs(numeratorBufferA) <= COMPARISON_THRESHOLD
//                    && Math.abs(numeratorBufferB) <= COMPARISON_THRESHOLD) {
//                logger.finer(String.format(
//                        "%s and %s are coincident",
//                        toString(a), toString(b)));
//            } else {
//                logger.finer(String.format(
//                        "%s and %s are parallel",
//                        toString(a), toString(b)));
//            }
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
//                    logger.finer(String.format(
//                            "%s and %s intersect in %s",
//                            toString(a), toString(b), toString(intersection)));
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
//                    logger.finer(String.format(
//                            "%s and %s intersect in %s",
//                            toString(a), toString(b), toString(intersection)));
                return intersection;
            } else {
//                    logger.finer(String.format(
//                            "%s and %s don't cross each other because they are not infinite",
//                            toString(a), toString(b)));
                return null;
            }
        } else {
            //no intersection
//                logger.finer(String.format(
//                        "No intersection between %s and %s !",
//                        toString(a), toString(b)));
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

}
