package org.blackpanther.ecosystem.agent;

/**
 * @author MACHIZAUD Andréa
 * @version 1.0-alpha - Tue May 24 23:49:57 CEST 2011
 */
public interface CreatureConstants
        extends AgentConstants {

    public static final String CREATURE_COLOR = "creature-color";
    public static final String CREATURE_AGE = "creature-age";
    public static final String CREATURE_ORIENTATION = "creature-orientation";
    public static final String CREATURE_CURVATURE = "creature-curvature";
    public static final String CREATURE_SPEED = "creature-speed";

    public static final String CREATURE_MOVEMENT_COST = "creature-movement-cost";
    public static final String CREATURE_FECUNDATION_COST = "creature-fecundation-cost";
    public static final String CREATURE_FECUNDATION_LOSS = "creature-fecundation-loss";
    public static final String CREATURE_GREED = "creature-greed";
    public static final String CREATURE_FLEE = "creature-flee";
    public static final String CREATURE_SENSOR_RADIUS = "creature-sensor-radius";
    public static final String CREATURE_IRRATIONALITY = "creature-irrationality";
    public static final String CREATURE_MORTALITY = "creature-mortality";
    public static final String CREATURE_FECUNDITY = "creature-fecundity";
    public static final String CREATURE_MUTATION = "creature-mutation";
    public static final String CREATURE_ORIENTATION_LAUNCHER = "creature-orientation-launcher";
    public static final String CREATURE_SPEED_LAUNCHER = "creature-speed-launcher";
    public static final String CREATURE_BEHAVIOR = "creature-behavior";

    public static final String[] CREATURE_STATE = new String[]{
            CREATURE_AGE,
            CREATURE_COLOR,
            CREATURE_CURVATURE,
            AGENT_ENERGY,
            AGENT_LOCATION,
            CREATURE_ORIENTATION,
            CREATURE_SPEED
    };

    public static final String[] CREATURE_GENOTYPE = new String[]{
            AGENT_NATURAL_COLOR,
            CREATURE_MOVEMENT_COST,
            CREATURE_FECUNDATION_COST,
            CREATURE_FECUNDATION_LOSS,
            CREATURE_GREED,
            CREATURE_FLEE,
            CREATURE_SENSOR_RADIUS,
            CREATURE_IRRATIONALITY,
            CREATURE_MORTALITY,
            CREATURE_FECUNDITY,
            CREATURE_MUTATION,
            CREATURE_ORIENTATION_LAUNCHER,
            CREATURE_SPEED_LAUNCHER,
            CREATURE_BEHAVIOR
    };

    /**
     * Trait provided to create an creature
     */
    public static final String[] BUILD_PROVIDED_CREATURE_STATE = new String[]{
            CREATURE_COLOR,
            CREATURE_CURVATURE,
            AGENT_ENERGY,
            AGENT_LOCATION,
            CREATURE_ORIENTATION,
            CREATURE_SPEED
    };

    public static final String[] CUSTOMIZABLE_CREATURE_STATE = new String[]{
            CREATURE_COLOR,
            CREATURE_CURVATURE,
            AGENT_ENERGY,
            CREATURE_ORIENTATION,
            CREATURE_SPEED
    };
}
