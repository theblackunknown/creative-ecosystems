package org.blackpanther.ecosystem.gui.formatter;

import org.blackpanther.ecosystem.math.Geometry;

import javax.swing.*;

/**
 * @author MACHIZAUD Andréa
 * @version 1.0-alpha - Tue May 24 23:49:58 CEST 2011
 */
public final class RangeModels {

    private RangeModels() {
    }

    //Domain : [|0,POS_INF|]
    public static SpinnerNumberModel generatePositiveIntegerModel() {
        return new SpinnerNumberModel(
                0,
                0,
                Integer.MAX_VALUE,
                1);
    }

    //Domain : [|0,POS_INF|]
    public static SpinnerNumberModel generatePositiveDoubleModel() {
        return generateDoubleModel(0.0, Double.MAX_VALUE);
    }

    //Domain : [|0,POS_INF|]
    public static SpinnerNumberModel generateDoubleModel() {
        return generateDoubleModel(null, null);
    }

    // Domain : Real
    public static SpinnerNumberModel generateDoubleModel(Double max) {
        return generateDoubleModel(0.0, max);
    }

    // Domain : Real
    public static SpinnerNumberModel generateDoubleModel(Double min, Double max) {
        return new SpinnerNumberModel(
                Double.valueOf(0.0),
                min == null ? null : min,
                max == null ? null : max,
                Double.valueOf(1.0));
    }

    // Domain : [0.0,1.0]
    public static SpinnerNumberModel generatePercentageModel() {
        return new SpinnerNumberModel(
                0.0,
                0.0,
                1.0,
                0.1);
    }

    // Domain : [0.0,2PI]
    public static SpinnerNumberModel generateAngleModel() {
        return new SpinnerNumberModel(
                0.0,
                0.0,
                Geometry.PI_2.doubleValue(),
                0.1);
    }
}
