package org.blackpanther.ecosystem.gui.formatter;

import javax.swing.*;

/**
 * @author MACHIZAUD Andréa
 * @version 0.2 - Wed May 11 02:54:46 CEST 2011
 */
public final class RangeModels {

    //Domain : [|0,POS_INF|]
    public static final SpinnerModel generatePositiveLongModel() {
        return new SpinnerNumberModel(
                new Long(0L),
                new Long(0L),
                new Long(Long.MAX_VALUE),
                new Long(1L));
    }

    // Domain : Real
    public static final SpinnerModel generateDoubleModel() {
        return new SpinnerNumberModel(
                0.0,
                null,
                null,
                1.0);
    }

    // Domain : [0.0,POS_INF]
    public static final SpinnerModel generatePositiveDoubleModel() {
        return new SpinnerNumberModel(
                0.0,
                0.0,
                null,
                1.0);
    }

    // Domain : [0.0,1.0]
    public static final SpinnerModel generatePercentageModel() {
        return new SpinnerNumberModel(
                0.0,
                0.0,
                1.0,
                0.1);
    }

    // Domain : [0.0,2PI]
    public static final SpinnerModel generateAngleModel() {
        return new SpinnerNumberModel(
                0.0,
                0.0,
                2.0*Math.PI,
                0.1);
    }
}
