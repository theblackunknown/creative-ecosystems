package org.blackpanther.ecosystem.helper;

import javax.swing.*;
import java.awt.*;
import java.net.URL;

/**
 * Tools method to help to design others classes
 *
 * @author MACHIZAUD Andréa
 * @version 0.2 - Wed May 11 02:54:46 CEST 2011
 */
public final class Helper {

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

    public static URL getImage(String imagePath) {
        return Helper.class.getClassLoader().getResource(imagePath);
    }

    private static final Dimension FIELD_DIMENSION = new Dimension(400, 50);

    public static JPanel createLabeledField(final String labelName, final Component field) {
        return new JPanel(new GridLayout(2,1)) {{
            JLabel label = new JLabel(labelName);
            label.setLabelFor(field);
            add(label);
            add(field);
            setPreferredSize(FIELD_DIMENSION);
        }};
    }
}
