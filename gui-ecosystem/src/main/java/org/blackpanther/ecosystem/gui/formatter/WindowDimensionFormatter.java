package org.blackpanther.ecosystem.gui.formatter;

import org.blackpanther.ecosystem.math.Geometry;

import javax.swing.*;
import java.awt.geom.Dimension2D;
import java.text.ParseException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author MACHIZAUD Andréa
 * @version 1.0-alpha - Wed May 18 02:01:10 CEST 2011
 */
public class WindowDimensionFormatter
        extends JFormattedTextField.AbstractFormatter {

    private static final Pattern DIMENSION_PATTERN =
            Pattern.compile("(\\d+(?:.\\d+))x(\\d+(?:.\\d+))");

    private WindowDimensionFormatter() {
    }

    private static class WindowDimensionFormatterHolder {
        private static final WindowDimensionFormatter instance =
                new WindowDimensionFormatter();
    }

    public static WindowDimensionFormatter getInstance() {
        return WindowDimensionFormatterHolder.instance;
    }

    @Override
    public Dimension2D stringToValue(String text) throws ParseException {
        if (text.equals(""))
            return null;
        else {
            Matcher matcher = DIMENSION_PATTERN.matcher(text);
            if (matcher.matches()) {
                return new Geometry.Dimension(
                        Double.parseDouble(matcher.group(1)),
                        Double.parseDouble(matcher.group(2))
                );
            } else {
                throw new ParseException(
                        "Invalid input for setting environment's dimension, "
                                + "e.g 1280x800",
                        0
                );
            }
        }
    }

    @Override
    public String valueToString(Object value) throws ParseException {
        return (value == null)
                ? ""
                : String.format(
                "%.2fx%.2f",
                ((Dimension2D) value).getWidth(),
                ((Dimension2D) value).getHeight()
        );
    }
}
