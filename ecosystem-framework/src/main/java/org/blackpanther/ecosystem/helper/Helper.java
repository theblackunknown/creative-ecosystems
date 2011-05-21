package org.blackpanther.ecosystem.helper;

import org.blackpanther.ecosystem.math.Geometry;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Point2D;
import java.net.URL;

/**
 * Tools method to help to design others classes
 *
 * @author MACHIZAUD Andréa
 * @version 1.1-alpha - Thu May 19 01:22:54 CEST 2011
 */
public final class Helper {

    public static final double EPSILON = 0.001;

    private Helper() {
    }

    /**
     * Check if given predicate is verified
     *
     * @param predicate    predicate which must be verified
     * @param errorMessage error message to display if predicate is not verified
     * @throws IllegalArgumentException if the given predicate is not satisfied
     */
    public static void require(
            final boolean predicate,
            final String errorMessage) {
        if (!predicate) {
            throw new IllegalArgumentException("Condition unsatisfied"
                    + ((errorMessage != null && !errorMessage.equals(""))
                    ? " : " + errorMessage
                    : "")
            );
        }
    }

    /**
     * Check if given predicate is verified with no error message
     *
     * @param predicate predicate which must be verified
     * @throws IllegalArgumentException if the given predicate is not satisfied
     */
    public static void require(
            final boolean predicate) {
        require(predicate, null);
    }

    /**
     * check if a user input is valid or not,
     * based on non-null and non-empty input
     *
     * @param input user input
     * @return <code>true</code> if the given input is valid, <code>false</code> otherwise
     */
    public static boolean isValid(String input) {
        return input != null && !input.trim().equals("");
    }

    public static void error(String errorMessage) {
        throw new RuntimeException(errorMessage);
    }

    public static boolean within(double number, double inf, double sup) {
        return inf <= number && number < sup;
    }

    public static Double normalizeAngle(double angle) {
        return angle % Geometry.PI_2;
    }

    public static Integer normalizeColor(double colorAmount) {
        if (colorAmount < 0.0)
            return 0;
        else if (colorAmount > 255.0)
            return 255;
        else
            return (int) colorAmount;
    }

    public static Double normalizeProbability(double probability) {
        if (probability < 0.0)
            return 0.0;
        else if (probability > 1.0)
            return 1.0;
        else
            return probability;
    }

    public static Double normalizePositiveDouble(double speed) {
        if (speed < 0.0)
            return 0.0;
        else
            return speed;
    }

    public static URL getURL(String imagePath) {
        return Helper.class.getClassLoader().getResource(imagePath);
    }

    public static Icon getIcon(String imagePath) {
        return new ImageIcon(getURL(imagePath));
    }

    public static Icon getIcon(URL imagePath) {
        return new ImageIcon(imagePath);
    }

    private static final Dimension FIELD_DIMENSION = new Dimension(130, 50);

    public static JPanel createLabeledField(final String labelName, final Component field) {
        return new JPanel(new GridLayout(2, 1)) {{
            JLabel label = new JLabel(labelName);
            label.setLabelFor(field);
            add(label);
            add(field);
            setPreferredSize(FIELD_DIMENSION);
        }};
    }

    /**
     * Get angle in positive rotation (counter-clockwise)
     * of the angle with source as center and target a point on the circle
     */
    public static Double computeOrientation(Point2D source, Point2D target) {
        double angle = Math.atan2(
                target.getY() - source.getY(),
                target.getX() - source.getX());
        return angle < 0.0
                ? angle + Geometry.PI_2
                : angle;
    }
}