package org.blackpanther.ecosystem;

import org.blackpanther.ecosystem.math.Geometry;

import java.awt.geom.Point2D;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.blackpanther.ecosystem.Helper.isValid;
import static org.blackpanther.ecosystem.Helper.require;

/**
 * <p>
 * Singleton Pattern.
 * A class designed to kept application configuration
 * </p>
 *
 * @author MACHIZAUD Andréa
 * @version v0.2.1 - Sun Apr 24 18:01:06 CEST 2011
 */
public enum Configuration {
    Configuration;

    static {
        try {
            LogManager.getLogManager().readConfiguration(
                    Configuration.class.getClassLoader().getResourceAsStream("logging.properties")
            );
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Class logger
     */
    private static final Logger logger =
            Logger.getLogger(Configuration.class.getCanonicalName());

    /**
     * Random application parameters
     */
    public static final String RANDOM = "random";
    public static final String AGENT_LOCATION = "agent-location";
    public static final String AGENT_DIRECTIONAL_VECTOR = "agent-directional-vector";
    public static final String AGENT_CURVATURE = "agent-curvature";
    public static final String AGENT_SPEED = "agent-speed";
    public static final String AGENT_MORTALITY = "agent-mortality";
    public static final String AGENT_FECUNDITY = "agent-fecundity";
    public static final String AGENT_MUTATION = "agent-mutation";
    public static final String AGENT_DEFAULT_BEHAVIOUR_MANAGER = "agent-behaviour-manager";

    /**
     * User defined directional vector's parameter
     */
    private static final Pattern USER_DIRECTIONAL_VECTOR_PARAMETER =
            Pattern.compile(
                    "^(\\d+(?:.\\d+)?),(\\d+(?:.\\d+)?)$"
            );

    /**
     * User defined speed's parameter
     */
    private static final Pattern USER_SPEED_PARAMETER =
            Pattern.compile(
                    "^([0-3](?:.\\d{1,2}))$"
            );

    /**
     * Application's parameters with default loaded
     */
    protected final Map<String, Object> applicationProperties = new HashMap<String, Object>() {{
        put(RANDOM, new Random());
        put(AGENT_LOCATION, new Point2D.Double(0.0, 0.0));
        put(AGENT_DIRECTIONAL_VECTOR,
                new Geometry.Direction2D(1.0, 1.0));
        put(AGENT_CURVATURE, 1.0);
        put(AGENT_SPEED, 1.0);
        put(AGENT_MORTALITY, 0.2);
        put(AGENT_FECUNDITY, 0.4);
        put(AGENT_MUTATION, 0.05);
    }};

    /**
     * Load properties from a configuration file
     * <p/>
     * TODO Handle location & curvature
     *
     * @param propertyFile configuration file
     */
    @SuppressWarnings("unchecked")
    public void loadConfiguration(Properties properties) {

        //update random seed
        String userRandomSeed = properties.getProperty(RANDOM);
        if (isValid(userRandomSeed)) {
            try {
                setParameter(
                        RANDOM,
                        new Random(Long.parseLong(userRandomSeed)),
                        Random.class);
                logger.info(RANDOM + " parameter updated.");
            } catch (NumberFormatException e) {
                logger.log(Level.WARNING,
                        "Couldn't parse random parameter : '" + userRandomSeed + "'", e);
            }
        }

        //update directional vector
        String userDirectionnalVector = properties.getProperty(AGENT_DIRECTIONAL_VECTOR);
        if (isValid(userDirectionnalVector)) {
            try {
                Matcher matchData = USER_DIRECTIONAL_VECTOR_PARAMETER.matcher(userDirectionnalVector);
                if (matchData.matches()) {
                    setParameter(
                            AGENT_DIRECTIONAL_VECTOR,
                            new Geometry.Direction2D(
                                    Double.parseDouble(matchData.group(1)),
                                    Double.parseDouble(matchData.group(2))
                            ),
                            Geometry.Direction2D.class
                    );
                    logger.info(AGENT_DIRECTIONAL_VECTOR + " parameter updated.");
                } else {
                    logger.log(Level.WARNING,
                            "Couldn't parse user user directional vector : '"
                                    + userDirectionnalVector + "', "
                                    + "it must be like (0.0,0.0)-(1.0,1.0)");
                }
            } catch (NumberFormatException e) {
                logger.log(Level.SEVERE,
                        "Couldn't parse user directional vector despite of regexp !", e);
            }
        }

        //update agent speed
        String userAgentSpeed = properties.getProperty(AGENT_SPEED);
        if (isValid(userAgentSpeed)) {
            try {
                setParameter(
                        AGENT_SPEED,
                        Double.parseDouble(userAgentSpeed),
                        Double.class
                );
                logger.info(AGENT_SPEED + " parameter updated.");
            } catch (NumberFormatException e) {
                logger.log(Level.SEVERE,
                        "Couldn't parse user agent speed, it must be within [0,3] !", e);
            }
        }

        //update agent mortality
        String userAgentMortality = properties.getProperty(AGENT_MORTALITY);
        if (isValid(userAgentMortality)) {
            try {
                setParameter(
                        AGENT_MORTALITY,
                        Double.parseDouble(userAgentMortality),
                        Double.class
                );
                logger.info(AGENT_MORTALITY + " parameter updated.");
            } catch (NumberFormatException e) {
                logger.log(Level.SEVERE,
                        "Couldn't parse user agent mortality rate, it must be within [0,1] !", e);
            }
        }

        //update agent fecundity
        String userAgentFecundity = properties.getProperty(AGENT_FECUNDITY);
        if (isValid(userAgentFecundity)) {
            try {
                setParameter(
                        AGENT_FECUNDITY,
                        Double.parseDouble(userAgentFecundity),
                        Double.class
                );
                logger.info(AGENT_FECUNDITY + " parameter updated.");
            } catch (NumberFormatException e) {
                logger.log(Level.SEVERE,
                        "Couldn't parse user agent fecundity rate, it must be within [0,1] !", e);
            }
        }

        //update agent mutation
        String userAgentMutation = properties.getProperty(AGENT_MUTATION);
        if (isValid(userAgentMutation)) {
            try {
                setParameter(
                        AGENT_MUTATION,
                        Double.parseDouble(userAgentMutation),
                        Double.class
                );
                logger.info(AGENT_MUTATION + " parameter updated.");
            } catch (NumberFormatException e) {
                logger.log(Level.SEVERE,
                        "Couldn't parse user agent mutation rate, it must be within [0,1] !", e);
            }
        }

        //update default behavior class
        String userAgentDefaultBehaviourManager =
                properties.getProperty(AGENT_DEFAULT_BEHAVIOUR_MANAGER);
        if (isValid(userAgentDefaultBehaviourManager)) {
            try {
                Class<BehaviorManager> userBehaviourManagerClass =
                        (Class<BehaviorManager>) Class.forName(userAgentDefaultBehaviourManager);
                setParameter(
                        AGENT_DEFAULT_BEHAVIOUR_MANAGER,
                        userBehaviourManagerClass.newInstance(),
                        BehaviorManager.class
                );
                logger.info(AGENT_DEFAULT_BEHAVIOUR_MANAGER + " parameter updated.");
            } catch (ClassNotFoundException e) {
                logger.log(Level.SEVERE, "Couldn't found user BehaviourManager class", e);
            } catch (InstantiationException e) {
                logger.log(Level.SEVERE, "Couldn't initialize user BehaviourManager class", e);
            } catch (IllegalAccessException e) {
                logger.log(Level.SEVERE, "You don't have an access to this class", e);
            }
        }
    }

    /**
     * Rollback application parameter if an exception occurred
     * while trying to load a configuration file
     *
     * @param oldProperties backup parameters
     */
    private void rollbackProperties(final Map<String, Object> oldProperties) {
        applicationProperties.put(RANDOM, oldProperties.get(RANDOM));
    }

    public <T> void setParameter(String parameterName, T parameterValue, Class<T> parameterType) {
        checkParameterValidity(parameterName, parameterValue, parameterType);
        applicationProperties.put(parameterName, parameterValue);
    }

    @SuppressWarnings("unchecked")
    public <T> T getParameter(String parameterName, Class<T> parameterType) {
        Object correspondingParameter = applicationProperties.get(parameterName);
        if (correspondingParameter != null) {
            if (parameterType.isInstance(correspondingParameter)) {
                return (T) correspondingParameter;
            } else {
                throw new IllegalArgumentException(
                        "Requested parameter does not match given type, please check again"
                );
            }
        } else {
            throw new IllegalArgumentException(
                    "Requested parameter is not provided by the current configuration, "
                            + "maybe you should register it before");
        }
    }

    private static <T> void checkParameterValidity(
            String paramName,
            T paramValue,
            Class<T> paramType
    ) {
        if (paramName.equals(AGENT_DIRECTIONAL_VECTOR)) {
            if (!paramType.equals(Geometry.Direction2D.class)) {
                throw new IllegalArgumentException(
                        "Invalid value this parameter, it must be an "
                                + Geometry.Direction2D.class.getCanonicalName());
            }
        } else if (paramName.equals(AGENT_SPEED)) {
            if (!paramType.equals(Double.class)) {
                throw new IllegalArgumentException(
                        "Invalid value this parameter, it must be an "
                                + Double.class.getCanonicalName());
            } else {
                Double value = (Double) paramValue;
                require(0.0 <= value && value <= 3.0, "Invalid value for " + paramName + " : '" + paramValue + "'");
            }
        } else if (paramName.equals(AGENT_MORTALITY)) {
            if (!paramType.equals(Double.class)) {
                throw new IllegalArgumentException(
                        "Invalid value this parameter, it must be an "
                                + Double.class.getCanonicalName());
            } else {
                Double value = (Double) paramValue;
                require(0.0 <= value && value <= 1.0, "Invalid value for " + paramName + " : '" + paramValue + "'");
            }
        } else if (paramName.equals(AGENT_FECUNDITY)) {
            if (!paramType.equals(Double.class)) {
                throw new IllegalArgumentException(
                        "Invalid value this parameter, it must be an "
                                + Double.class.getCanonicalName());
            } else {
                Double value = (Double) paramValue;
                require(0.0 <= value && value <= 1.0, "Invalid value for " + paramName + " : '" + paramValue + "'");
            }
        } else if (paramName.equals(AGENT_MUTATION)) {
            if (!paramType.equals(Double.class)) {
                throw new IllegalArgumentException(
                        "Invalid value this parameter, it must be an "
                                + Double.class.getCanonicalName());
            } else {
                Double value = (Double) paramValue;
                require(0.0 <= value && value <= 1.0, "Invalid value for " + paramName + " : '" + paramValue + "'");
            }
        } else if (paramName.equals(AGENT_DEFAULT_BEHAVIOUR_MANAGER)) {
            if (!paramType.equals(BehaviorManager.class)) {
                throw new IllegalArgumentException(
                        "Invalid value this parameter, it must be an "
                                + BehaviorManager.class.getCanonicalName());
            }
        } else {
            throw new IllegalArgumentException("Invalid parameter name");
        }
    }

}
