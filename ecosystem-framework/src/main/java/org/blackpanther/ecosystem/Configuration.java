package org.blackpanther.ecosystem;

import org.blackpanther.ecosystem.math.Geometry;

import java.awt.*;
import java.io.IOException;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

import static org.blackpanther.ecosystem.Agent.*;
import static org.blackpanther.ecosystem.helper.Helper.isValid;
import static org.blackpanther.ecosystem.helper.Helper.require;


/**
 * <p>
 * Singleton Pattern.
 * A class designed to kept application configuration
 * </p>
 *
 * @author MACHIZAUD Andréa
 * @version 1.1-alpha - Thu May 19 01:22:54 CEST 2011
 */
public enum Configuration {
    Configuration;

    private static final Long serialVersionUID = 2L;

    /**
     * Random application parameters
     */
    public static final String RANDOM = "random-seed";
    public static final String SPACE_WIDTH = "space-width";
    public static final String SPACE_HEIGHT = "space-height";
    public static final String MAX_AGENT_NUMBER = "max-agent-number";
    public static final String RESOURCE_AMOUNT = "resource-amount";
    public static final String CONSUMMATION_RADIUS = "consummation-radius";

    public static final String SPEED_THRESHOLD = "speed-randomized-threshold";
    public static final String RESOURCE_AMOUNT_THRESHOLD = "resource-randomized-threshold";
    public static final String ENERGY_AMOUNT_THRESHOLD = "energy-amount-threshold";
    public static final String SENSOR_THRESHOLD = "sensor-radius-randomized-threshold";
    public static final String CURVATURE_THRESHOLD = "curvature-randomized-threshold";
    public static final String FECUNDATION_CONSUMMATION_THRESHOLD = "fecundation-cons-randomized-threshold";

    public static final String PROBABILITY_VARIATION = "probability-variation";
    public static final String CURVATURE_VARIATION = "curvature-variation";
    public static final String ANGLE_VARIATION = "angle-variation";
    public static final String SPEED_VARIATION = "speed-variation";
    public static final String COLOR_VARIATION = "color-variation";

    public static final String RED_COLOR = "agent-color-red";
    public static final String GREEN_COLOR = "agent-color-green";
    public static final String BLUE_COLOR = "agent-color-blue";

    private String[] DOUBLE_UNBOUNDED;
    private String[] ANGLE;
    private String[] POSITIVE_DOUBLE;
    private String[] PROBABILITY;
    private String[] POSITIVE_INTEGER;
    private String[] COLOR;

    /**
     * Application's parameters with default loaded
     */
    protected final Map<String, Object> applicationProperties = new HashMap<String, Object>() {{
        put(RANDOM, new Random());
    }};

    /**
     * Silent application's parameters initialization
     */
    private Configuration() {

        DOUBLE_UNBOUNDED = new String[]{
                SPACE_WIDTH,
                SPACE_HEIGHT,
                CURVATURE_THRESHOLD,
                PROBABILITY_VARIATION,
                CURVATURE_VARIATION,
                ANGLE_VARIATION,
                SPEED_VARIATION,
                COLOR_VARIATION,
                AGENT_CURVATURE
        };
        ANGLE = new String[]{
                AGENT_ORIENTATION,
                AGENT_ORIENTATION_LAUNCHER
        };
        POSITIVE_DOUBLE = new String[]{
                RESOURCE_AMOUNT_THRESHOLD,
                SPEED_THRESHOLD,
                SENSOR_THRESHOLD,
                ENERGY_AMOUNT_THRESHOLD,
                FECUNDATION_CONSUMMATION_THRESHOLD,
                CONSUMMATION_RADIUS,
                RESOURCE_AMOUNT,
                AGENT_ENERGY,
                AGENT_MOVEMENT_COST,
                AGENT_FECUNDATION_COST,
                AGENT_SPEED,
                AGENT_SPEED_LAUNCHER,
                AGENT_SENSOR_RADIUS
        };
        PROBABILITY = new String[]{
                AGENT_FECUNDATION_LOSS,
                AGENT_IRRATIONALITY,
                AGENT_GREED,
                AGENT_FLEE,
                AGENT_MORTALITY,
                AGENT_FECUNDITY,
                AGENT_MUTATION
        };
        POSITIVE_INTEGER = new String[]{
                MAX_AGENT_NUMBER
        };
        COLOR = new String[]{
                AGENT_IDENTIFIER
        };

        //pre-sort arrays
        Arrays.sort(DOUBLE_UNBOUNDED);
        Arrays.sort(ANGLE);
        Arrays.sort(POSITIVE_DOUBLE);
        Arrays.sort(PROBABILITY);
        Arrays.sort(POSITIVE_INTEGER);

        Logger classLogger = Logger.getLogger(Configuration.class.getCanonicalName());
        try {
            LogManager.getLogManager().readConfiguration(
                    getClass().getClassLoader().getResourceAsStream("logging.properties")
            );
        } catch (IOException e) {
            classLogger.log(Level.WARNING, "Logging configuration file nt found", e);
        }
        //load application properties
        Properties userProperties = new Properties(System.getProperties());
        try {
            userProperties.load(
                    Configuration.class.getClassLoader()
                            .getResourceAsStream("application.properties")
            );
            loadConfiguration(userProperties);
        } catch (IOException e) {
            classLogger.log(
                    Level.WARNING, "Problem loading default application.properties", e);
        }
    }

    public Random getRandom() {
        return getParameter(RANDOM, Random.class);
    }

    /**
     * Load properties from a configuration file
     *
     * @param properties usersProperties
     */
    @SuppressWarnings("unchecked")
    public void loadConfiguration(Properties properties) {
        Logger logger = Logger.getLogger(Configuration.class.getCanonicalName());
        String value;

        for (String parameter : DOUBLE_UNBOUNDED) {
            value = properties.getProperty(parameter);
            if (isValid(value)) {
                try {
                    setParameter(
                            parameter,
                            Double.parseDouble(value),
                            Double.class
                    );
                } catch (NumberFormatException e) {
                    logger.log(Level.SEVERE,
                            "Couldn't parse " + parameter
                                    + ", it must be a decimal value!", e);
                }
            }
        }

        for (String parameter : POSITIVE_DOUBLE) {
            value = properties.getProperty(parameter);
            if (isValid(value)) {
                try {
                    setParameter(
                            parameter,
                            Double.parseDouble(value),
                            Double.class
                    );
                } catch (NumberFormatException e) {
                    logger.log(Level.SEVERE,
                            "Couldn't parse " + parameter
                                    + ", it must be a positive decimal value!", e);
                }
            }
        }

        for (String parameter : ANGLE) {
            value = properties.getProperty(parameter);
            if (isValid(value)) {
                try {
                    setParameter(
                            parameter,
                            Double.parseDouble(value) % Geometry.PI_2,
                            Double.class
                    );
                } catch (NumberFormatException e) {
                    logger.log(Level.SEVERE,
                            "Couldn't parse " + parameter
                                    + ", it must be within [0,2PI] !", e);
                }
            }
        }

        for (String parameter : PROBABILITY) {
            value = properties.getProperty(parameter);
            if (isValid(value)) {
                try {
                    setParameter(
                            parameter,
                            Double.parseDouble(value),
                            Double.class
                    );
                } catch (NumberFormatException e) {
                    logger.log(Level.SEVERE,
                            "Couldn't parse " + parameter
                                    + ", it must be within [0,1] !", e);
                }
            }
        }

        for (String parameter : POSITIVE_INTEGER) {
            value = properties.getProperty(parameter);
            if (isValid(value)) {
                try {
                    setParameter(
                            parameter,
                            Integer.parseInt(value),
                            Integer.class
                    );
                } catch (NumberFormatException e) {
                    logger.log(Level.SEVERE,
                            "Couldn't parse " + parameter
                                    + ", it must be a positive integer value!", e);
                }
            }
        }

        String redValue = properties.getProperty(RED_COLOR);
        String greenValue = properties.getProperty(GREEN_COLOR);
        String blueValue = properties.getProperty(BLUE_COLOR);
        if (isValid(redValue) && isValid(greenValue) && isValid(blueValue)) {
            try {
                setParameter(
                        AGENT_IDENTIFIER,
                        new Color(
                                Integer.parseInt(redValue),
                                Integer.parseInt(greenValue),
                                Integer.parseInt(blueValue)
                        ),
                        Color.class
                );
            } catch (NumberFormatException e) {
                logger.log(Level.SEVERE,
                        "Couldn't parse " + RED_COLOR + ", " + GREEN_COLOR + " and " + BLUE_COLOR
                                + ", they must be all within [0-255]!", e);
            }
        }

        //update default behavior class
        value = properties.getProperty(AGENT_BEHAVIOUR);
        if (isValid(value)) {
            try {
                Class<BehaviorManager> userBehaviourManagerClass =
                        (Class<BehaviorManager>) Class.forName(value);
                setParameter(
                        AGENT_BEHAVIOUR,
                        userBehaviourManagerClass.newInstance(),
                        BehaviorManager.class
                );
            } catch (ClassNotFoundException e) {
                logger.log(Level.SEVERE, "Couldn't found user BehaviourManager class", e);
            } catch (InstantiationException e) {
                logger.log(Level.SEVERE, "Couldn't initialize user BehaviourManager class", e);
            } catch (IllegalAccessException e) {
                logger.log(Level.SEVERE, "You don't have an access to this class", e);
            }
        }
    }

    public <T> void setParameter(String parameterName, T parameterValue, Class<T> parameterType) {
        checkParameterValidity(parameterName, parameterValue, parameterType);
        applicationProperties.put(parameterName, parameterValue);
    }


    @SuppressWarnings("unchecked") //Trust me please
    public <T> T getParameter(String parameterName, Class<T> parameterType) {
        Object correspondingParameter = applicationProperties.get(parameterName);
        if (correspondingParameter != null) {
            if (parameterType.isInstance(correspondingParameter)) {
                return (T) correspondingParameter;
            } else {
                throw new IllegalArgumentException(
                        String.format("%s parameter does not match given type, please check again",
                                parameterName)
                );
            }
        } else {
            throw new IllegalArgumentException(String.format(
                    "'%s'  parameter is not provided by the current configuration, "
                            + "maybe you should register it before",
                    parameterName
            ));
        }
    }

    private <T> void checkParameterValidity(
            String paramName,
            T paramValue,
            Class<T> paramType
    ) {
        //double unbounded
        if (Arrays.binarySearch(DOUBLE_UNBOUNDED, paramName) >= 0) {
            if (!paramType.equals(Double.class)) {
                throw new IllegalArgumentException(
                        "Invalid value this parameter, it must be an "
                                + Double.class.getCanonicalName());
            }
            //angle
        } else if (Arrays.binarySearch(ANGLE, paramName) >= 0) {
            if (!paramType.equals(Double.class)) {
                throw new IllegalArgumentException(
                        "Invalid value this parameter, it must be an "
                                + Double.class.getCanonicalName());
            } else {
                Double value = (Double) paramValue;
                require(0.0 <= value && value <= (2.0 * Math.PI),
                        "Invalid value for " + paramName + " : '" + paramValue + "'");
            }
            //positive double
        } else if (Arrays.binarySearch(POSITIVE_DOUBLE, paramName) >= 0) {
            if (!paramType.equals(Double.class)) {
                throw new IllegalArgumentException(
                        "Invalid value this parameter, it must be an "
                                + Double.class.getCanonicalName());
            } else {
                Double value = (Double) paramValue;
                require(0.0 <= value,
                        "Invalid value for " + paramName + " : '" + paramValue + "'");
            }
            //probability
        } else if (Arrays.binarySearch(PROBABILITY, paramName) >= 0) {
            if (!paramType.equals(Double.class)) {
                throw new IllegalArgumentException(
                        "Invalid value this parameter, it must be an "
                                + Double.class.getCanonicalName());
            } else {
                Double value = (Double) paramValue;
                require(0.0 <= value && value <= 1.0,
                        "Invalid value for " + paramName + " : '" + paramValue + "'");
            }
            //color
        }else if (Arrays.binarySearch(COLOR, paramName) >= 0) {
            if (!paramType.equals(Color.class)) {
                throw new IllegalArgumentException(
                        "Invalid value this parameter, it must be an "
                                + Color.class.getCanonicalName());
            }
            //behaviour
        } else if (paramName.equals(AGENT_BEHAVIOUR)) {
            if (!paramType.equals(BehaviorManager.class)) {
                throw new IllegalArgumentException(
                        "Invalid value this parameter, it must be a "
                                + BehaviorManager.class.getCanonicalName() + " class");
            }
            //positive integer
        } else if (Arrays.binarySearch(POSITIVE_INTEGER, paramName) >= 0) {
            if (!paramType.equals(Integer.class)) {
                throw new IllegalArgumentException(
                        "Invalid value this parameter, it must be a "
                                + Integer.class.getCanonicalName() + " class");
            } else {
                Integer value = (Integer) paramValue;
                require(0 <= value,
                        "Invalid value for " + paramName + " : '" + paramValue + "'");
            }
        } else {
            throw new IllegalArgumentException("Invalid parameter name : " + paramName);
        }
    }


    @Override
    public String toString() {
        return applicationProperties.toString()
                .replaceAll("\\[", "")
                .replaceAll("\\]", "")
                .replaceAll(",", "\n");
    }

    public Properties parameters() {
        Properties parameters = new Properties();
        for (Map.Entry<String, Object> entry : applicationProperties.entrySet())
            if (entry.getKey().equals(AGENT_BEHAVIOUR))
                parameters.put(entry.getKey(),
                        getParameter(AGENT_BEHAVIOUR, BehaviorManager.class).getClass().getCanonicalName());
            else if( entry.getKey().equals(AGENT_IDENTIFIER)){
                Color agentIdentifier = getParameter(AGENT_IDENTIFIER,Color.class);
                parameters.put(RED_COLOR, agentIdentifier.getRed());
                parameters.put(GREEN_COLOR, agentIdentifier.getGreen());
                parameters.put(BLUE_COLOR, agentIdentifier.getBlue());
            } else
                parameters.put(entry.getKey(), entry.getValue());
        return parameters;
    }

    public static Properties textify(Properties props) {
        for (Map.Entry<Object, Object> entry : props.entrySet())
            props.put(entry.getKey(), String.valueOf(entry.getValue()));
        return props;
    }
}