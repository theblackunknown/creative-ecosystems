package org.blackpanther.ecosystem.gui.formatter;

import javax.swing.*;

/**
 * @author MACHIZAUD Andréa
 * @version 5/11/11
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
    public static final SpinnerModel generateDecimalModel() {
        return new SpinnerNumberModel(
                0.0,
                null,
                null,
                1.0);
    }

    // Domain : [0.0,POS_INF]
    public static final SpinnerModel generatePositiveDecimalModel() {
        return new SpinnerNumberModel(
                0.0,
                0.0,
                null,
                1.0);
    }

    public static final int PROBABILITY_APPROXIMATION = 100;

    /**
     * Probability approximation [0,100] -> [0,1.0]
     */
    public static final DefaultBoundedRangeModel generateProbabilityModel() {
        return new DefaultBoundedRangeModel(0, 1, 0, PROBABILITY_APPROXIMATION);
    }

    public static final int ANGLE_APPROXIMATION = 10000;

    /**
     * Angle approximation [0,10000] -> [0,2PI]
     */
    public static final DefaultBoundedRangeModel generateAngleModel() {
        return new DefaultBoundedRangeModel(0, 1, 0, ANGLE_APPROXIMATION);
    }
}
