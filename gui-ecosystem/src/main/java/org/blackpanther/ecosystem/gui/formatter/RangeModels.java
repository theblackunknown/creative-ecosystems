package org.blackpanther.ecosystem.gui.formatter;

import org.blackpanther.ecosystem.math.Geometry;

import javax.swing.*;

/**
 * @author MACHIZAUD Andréa
 * @version 1.0-alpha - Tue May 24 23:49:58 CEST 2011
 */
public final class RangeModels {

    private RangeModels(){}

    //Domain : [|0,POS_INF|]
    public static SpinnerModel generatePositiveIntegerModel() {
        return new SpinnerNumberModel(
                0,
                0,
                Integer.MAX_VALUE,
                1);
    }

    // Domain : Real
    public static SpinnerModel generateDoubleModel() {
        return new SpinnerNumberModel(
                0.0,
                null,
                null,
                1.0);
    }

    // Domain : [0.0,POS_INF]
    public static SpinnerModel generatePositiveDoubleModel() {
        return new SpinnerNumberModel(
                0.0,
                0.0,
                null,
                1.0);
    }

    // Domain : [0.0,1.0]
    public static SpinnerModel generatePercentageModel() {
        return new SpinnerNumberModel(
                0.0,
                0.0,
                1.0,
                0.1);
    }

    // Domain : [0.0,2PI]
    public static SpinnerModel generateAngleModel() {
        return new SpinnerNumberModel(
                0.0,
                0.0,
                Geometry.PI_2.doubleValue(),
                0.1);
    }
}
