package org.blackpanther.ecosystem;

/**
 * @author MACHIZAUD Andréa
 * @version 1.0-alpha - Tue May 24 23:49:57 CEST 2011
 */
public interface ApplicationConstants {

    public static final String SPACE_WIDTH = "space-width";
    public static final String SPACE_HEIGHT = "space-height";

    public static final String MAX_AGENT_NUMBER = "max-agent-number";
    public static final String CONSUMMATION_RADIUS = "consummation-radius";

    public static final String SPEED_THRESHOLD = "speed-threshold";
    public static final String ENERGY_AMOUNT_THRESHOLD = "energy-threshold";
    public static final String SENSOR_THRESHOLD = "sensor-radius-threshold";
    public static final String CURVATURE_THRESHOLD = "curvature-threshold";
    public static final String RESOURCE_THRESHOLD = "resource-threshold";
    public static final String FECUNDATION_CONSUMMATION_THRESHOLD = "fecundation-consummation-threshold";

    public static final String PROBABILITY_VARIATION = "probability-variation";
    public static final String CURVATURE_VARIATION = "curvature-variation";
    public static final String ANGLE_VARIATION = "angle-variation";
    public static final String SPEED_VARIATION = "speed-variation";
    public static final String COLOR_VARIATION = "color-variation";

    public static final String LINE_OBSTRUCTION_OPTION = "line-obstruction";

    /*=========================================================================
                          TYPE
      =========================================================================*/

    public static final String[] DOUBLE_UNBOUNDED = new String[]{
            CURVATURE_THRESHOLD,
            PROBABILITY_VARIATION,
            CURVATURE_VARIATION,
            ANGLE_VARIATION,
            SPEED_VARIATION,
    };
    public static final String[] POSITIVE_DOUBLE = new String[]{
            SPACE_WIDTH,
            SPACE_HEIGHT,
            RESOURCE_THRESHOLD,
            SPEED_THRESHOLD,
            SENSOR_THRESHOLD,
            ENERGY_AMOUNT_THRESHOLD,
            FECUNDATION_CONSUMMATION_THRESHOLD,
            CONSUMMATION_RADIUS,
    };
    public static final String[] POSITIVE_INTEGER = new String[]{
            MAX_AGENT_NUMBER,
            COLOR_VARIATION
    };
    public static final String[] BOOLEAN = new String[]{
            LINE_OBSTRUCTION_OPTION
    };

    /*=========================================================================
                             CATEGORY
      =========================================================================*/

    public static final String[] MISCELLANEOUS = new String[]{
            SPACE_WIDTH,
            SPACE_HEIGHT,
            CONSUMMATION_RADIUS,
            MAX_AGENT_NUMBER,
    };

    public static final String[] THRESHOLD = new String[]{
            CURVATURE_THRESHOLD,
            RESOURCE_THRESHOLD,
            SPEED_THRESHOLD,
            SENSOR_THRESHOLD,
            ENERGY_AMOUNT_THRESHOLD,
            FECUNDATION_CONSUMMATION_THRESHOLD,
    };

    public static final String[] VARIATION = new String[]{
            PROBABILITY_VARIATION,
            CURVATURE_VARIATION,
            ANGLE_VARIATION,
            SPEED_VARIATION,
            COLOR_VARIATION
    };


}
