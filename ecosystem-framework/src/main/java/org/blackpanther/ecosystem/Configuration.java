package org.blackpanther.ecosystem;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Random;
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
 * @version 0.2 - Wed May 11 02:54:46 CEST 2011
 */
public enum Configuration {
    Configuration;

    /**
     * Class logger
     */
    private static final Logger logger =
            Logger.getLogger(Configuration.class.getCanonicalName());

    /**
     * Random application parameters
     */
    public static final String RANDOM = "random-seed";
    public static final String SPAWN_ABSCISSA_THRESHOLD = "spawn-abscissa-threshold";
    public static final String SPAWN_ORDINATE_THRESHOLD = "spawn-ordinate-threshold";
    //HELP Kept because it is hidden by JAVA implmentation
    private long randomSeed;

    /**
     * Application's parameters with default loaded
     */
    protected final Map<String, Object> applicationProperties = new HashMap<String, Object>() {{
        randomSeed = 42L;
        put(RANDOM, new Random(42L));
        put(SPAWN_ABSCISSA_THRESHOLD, 2000.0);
        put(SPAWN_ORDINATE_THRESHOLD, 2000.0);
        put(AGENT_ORIENTATION, Math.PI);
        put(AGENT_CURVATURE, 0.0);
        put(AGENT_SPEED, 5.0);
        put(AGENT_SENSOR_RADIUS, 0.4);
        put(AGENT_IRRATIONALITY, 0.35);
        put(AGENT_MORTALITY, 0.10);
        put(AGENT_FECUNDITY, 0.20);
        put(AGENT_MUTATION, 0.05);
        put(AGENT_ORIENTATION_LAUNCHER, Math.PI / 2);
        put(AGENT_SPEED_LAUNCHER, 3.0);
    }};

    /**
     * Silent application's parameters initialization
     */
    private Configuration() {
        Logger classLogger = Logger.getLogger(Configuration.class.getCanonicalName());
        //load logging properties
        try {
            LogManager.getLogManager().readConfiguration(
                    Configuration.class.getClassLoader()
                            .getResourceAsStream("logging.properties")
            );
        } catch (IOException e) {
            classLogger.log(
                    Level.WARNING, "Problem loading default logging.properties", e);
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

    public long getRandomSeed() {
        return randomSeed;
    }

    /**
     * Load properties from a configuration file
     * <p/>
     * TODO Handle location & curvature
     *
     * @param properties usersProperties
     */
    @SuppressWarnings("unchecked")
    public void loadConfiguration(Properties properties) {
        Logger logger = Logger.getLogger(Configuration.class.getCanonicalName());

        logger.finer("Before : \n" +
                RANDOM + "=" + getRandomSeed() + "\n" +
                applicationProperties.toString()
                        .replaceAll("[,]", "\n"));
        //update random seed
        String userRandomSeed = properties.getProperty(RANDOM);
        if (isValid(userRandomSeed)) {
            try {
                getParameter(
                        RANDOM,
                        Random.class)
                        .setSeed(Long.parseLong(userRandomSeed));
                randomSeed = Long.parseLong(userRandomSeed);
                logger.fine(RANDOM + " parameter updated.");
            } catch (NumberFormatException e) {
                logger.log(Level.WARNING,
                        "Couldn't parse random parameter : '" + userRandomSeed + "'", e);
            }
        }

        //update spawn abscissa threshold
        String userAbscissaThreshold = properties.getProperty(SPAWN_ABSCISSA_THRESHOLD);
        if (isValid(userAbscissaThreshold)) {
            try {
                setParameter(
                        SPAWN_ABSCISSA_THRESHOLD,
                        Double.parseDouble(userAbscissaThreshold),
                        Double.class
                );
                logger.fine(SPAWN_ABSCISSA_THRESHOLD + " parameter updated.");
            } catch (NumberFormatException e) {
                logger.log(Level.SEVERE,
                        "Couldn't parse user spawn abscissa threshold, it must be a decimal value!", e);
            }
        }

        //update spawn ordinate threshold
        String userOrdinateThreshold = properties.getProperty(SPAWN_ORDINATE_THRESHOLD);
        if (isValid(userOrdinateThreshold)) {
            try {
                setParameter(
                        SPAWN_ORDINATE_THRESHOLD,
                        Double.parseDouble(userOrdinateThreshold),
                        Double.class
                );
                logger.fine(SPAWN_ORDINATE_THRESHOLD + " parameter updated.");
            } catch (NumberFormatException e) {
                logger.log(Level.SEVERE,
                        "Couldn't parse user spawn ordinate threshold, it must be a decimal value!", e);
            }
        }

        //update orientation
        String userOrientation = properties.getProperty(AGENT_ORIENTATION);
        if (isValid(userOrientation)) {
            try {
                setParameter(
                        AGENT_ORIENTATION,
                        Double.parseDouble(userOrientation),
                        Double.class
                );
                logger.fine(AGENT_ORIENTATION + " parameter updated.");
            } catch (NumberFormatException e) {
                logger.log(Level.SEVERE,
                        "Couldn't parse user orientation, it must be within [0,2PI] !", e);
            }
        }

        //update orientation launcher
        String userOrientationLauncher = properties.getProperty(AGENT_ORIENTATION_LAUNCHER);
        if (isValid(userOrientationLauncher)) {
            try {
                setParameter(
                        AGENT_ORIENTATION_LAUNCHER,
                        Double.parseDouble(userOrientationLauncher),
                        Double.class
                );
                logger.fine(AGENT_ORIENTATION_LAUNCHER + " parameter updated.");
            } catch (NumberFormatException e) {
                logger.log(Level.SEVERE,
                        "Couldn't parse user orientation launcher, it must be within [0,2PI] !", e);
            }
        }

        //update curvature
        String userCurvature = properties.getProperty(AGENT_CURVATURE);
        if (isValid(userCurvature)) {
            try {
                setParameter(
                        AGENT_CURVATURE,
                        Double.parseDouble(userCurvature),
                        Double.class
                );
                logger.fine(AGENT_CURVATURE + " parameter updated.");
            } catch (NumberFormatException e) {
                logger.log(Level.SEVERE,
                        "Couldn't parse user curvature, it must be a decimal value !", e);
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
                logger.fine(AGENT_SPEED + " parameter updated.");
            } catch (NumberFormatException e) {
                logger.log(Level.SEVERE,
                        "Couldn't parse user agent speed, it must be positive !", e);
            }
        }

        //update agent speed
        String userAgentSpeedLauncher = properties.getProperty(AGENT_SPEED_LAUNCHER);
        if (isValid(userAgentSpeedLauncher)) {
            try {
                setParameter(
                        AGENT_SPEED_LAUNCHER,
                        Double.parseDouble(userAgentSpeedLauncher),
                        Double.class
                );
                logger.fine(AGENT_SPEED_LAUNCHER + " parameter updated.");
            } catch (NumberFormatException e) {
                logger.log(Level.SEVERE,
                        "Couldn't parse user agent speed launcher, it must be positive !", e);
            }
        }

        //update agent sensor radius
        String userAgentSensorRadius = properties.getProperty(AGENT_SENSOR_RADIUS);
        if (isValid(userAgentSensorRadius)) {
            try {
                setParameter(
                        AGENT_SENSOR_RADIUS,
                        Double.parseDouble(userAgentSensorRadius),
                        Double.class
                );
                logger.fine(AGENT_SENSOR_RADIUS + " parameter updated.");
            } catch (NumberFormatException e) {
                logger.log(Level.SEVERE,
                        "Couldn't parse user agent sensor radius, it must be positive !", e);
            }
        }

        //update agent mortality
        String userAgentIrrationality = properties.getProperty(AGENT_IRRATIONALITY);
        if (isValid(userAgentIrrationality)) {
            try {
                setParameter(
                        AGENT_IRRATIONALITY,
                        Double.parseDouble(userAgentIrrationality),
                        Double.class
                );
                logger.fine(AGENT_IRRATIONALITY + " parameter updated.");
            } catch (NumberFormatException e) {
                logger.log(Level.SEVERE,
                        "Couldn't parse user agent irrationality rate, it must be within [0,1] !", e);
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
                logger.fine(AGENT_MORTALITY + " parameter updated.");
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
                logger.fine(AGENT_FECUNDITY + " parameter updated.");
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
                logger.fine(AGENT_MUTATION + " parameter updated.");
            } catch (NumberFormatException e) {
                logger.log(Level.SEVERE,
                        "Couldn't parse user agent mutation rate, it must be within [0,1] !", e);
            }
        }

        //update default behavior class
        String userAgentDefaultBehaviourManager =
                properties.getProperty(AGENT_BEHAVIOUR);
        if (isValid(userAgentDefaultBehaviourManager)) {
            try {
                Class<BehaviorManager> userBehaviourManagerClass =
                        (Class<BehaviorManager>) Class.forName(userAgentDefaultBehaviourManager);
                setParameter(
                        AGENT_BEHAVIOUR,
                        userBehaviourManagerClass.newInstance(),
                        BehaviorManager.class
                );
                logger.fine(AGENT_BEHAVIOUR + " parameter updated.");

                logger.finer("After : \n" +
                        RANDOM + "=" + getRandomSeed() + "\n" +
                        applicationProperties.toString()
                                .replaceAll("[,]", "\n"));
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

    private static <T> void checkParameterValidity(
            String paramName,
            T paramValue,
            Class<T> paramType
    ) {
        if (paramName.equals(RANDOM)) {
            if (!paramType.equals(Random.class)) {
                throw new IllegalArgumentException(
                        "Invalid value this parameter, it must be an "
                                + Random.class.getCanonicalName());
            }
        } else if (paramName.equals(SPAWN_ABSCISSA_THRESHOLD)
                || paramName.equals(SPAWN_ORDINATE_THRESHOLD)
                || paramName.equals(AGENT_CURVATURE)) {
            if (!paramType.equals(Double.class)) {
                throw new IllegalArgumentException(
                        "Invalid value this parameter, it must be an "
                                + Double.class.getCanonicalName());
            }
        } else if (paramName.equals(AGENT_ORIENTATION)
                || paramName.equals(AGENT_ORIENTATION_LAUNCHER)) {
            if (!paramType.equals(Double.class)) {
                throw new IllegalArgumentException(
                        "Invalid value this parameter, it must be an "
                                + Double.class.getCanonicalName());
            } else {
                Double value = (Double) paramValue;
                require(0.0 <= value && value <= (2.0 * Math.PI),
                        "Invalid value for " + paramName + " : '" + paramValue + "'");
            }
        } else if (paramName.equals(AGENT_SPEED)
                || paramName.equals(AGENT_SPEED_LAUNCHER)
                || paramName.equals(AGENT_SENSOR_RADIUS)) {
            if (!paramType.equals(Double.class)) {
                throw new IllegalArgumentException(
                        "Invalid value this parameter, it must be an "
                                + Double.class.getCanonicalName());
            } else {
                Double value = (Double) paramValue;
                require(0.0 <= value,
                        "Invalid value for " + paramName + " : '" + paramValue + "'");
            }
        } else if (paramName.equals(AGENT_IRRATIONALITY)
                || paramName.equals(AGENT_MORTALITY)
                || paramName.equals(AGENT_FECUNDITY)
                || paramName.equals(AGENT_MUTATION)) {
            if (!paramType.equals(Double.class)) {
                throw new IllegalArgumentException(
                        "Invalid value this parameter, it must be an "
                                + Double.class.getCanonicalName());
            } else {
                Double value = (Double) paramValue;
                require(0.0 <= value && value <= 1.0,
                        "Invalid value for " + paramName + " : '" + paramValue + "'");
            }
        } else if (paramName.equals(AGENT_BEHAVIOUR)) {
            if (!paramType.equals(BehaviorManager.class)) {
                throw new IllegalArgumentException(
                        "Invalid value this parameter, it must be a "
                                + BehaviorManager.class.getCanonicalName() + " class");
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
            if (entry.getKey().equals(RANDOM))
                parameters.put(entry.getKey(), getRandomSeed());
            else
                parameters.put(entry.getKey(), entry.getValue());
        return parameters;
    }
}
