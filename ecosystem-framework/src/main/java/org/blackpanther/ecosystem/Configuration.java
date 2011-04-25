package org.blackpanther.ecosystem;

import org.blackpanther.ecosystem.math.Geometry;

import javax.print.Doc;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Random;
import java.util.logging.Level;
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

    /**
     * Class logger
     */
    private static final Logger logger =
            Logger.getLogger(Configuration.class.getCanonicalName());

    /**
     * Random application parameters
     */
    public static final String RANDOM = "random";
    public static final String AGENT_DIRECTIONAL_VECTOR = "agent-directional-vector";
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
                    "^\\((\\d+(?:.\\d+)?),(\\d+(?:.\\d+)?)\\)-\\((\\d+(?:.\\d+)?),(\\d+(?:.\\d+)?)\\)$"
            );

    /**
     * User defined speed's parameter
     */
    private static final Pattern USER_SPEED_PARAMETER =
            Pattern.compile(
                    "^([0-3](?:.\\d{1,2}))$"
            );

    /**
     * Application's parameters
     */
    protected final Map<String, Object> applicationProperties = new HashMap<String, Object>() {{
        put(RANDOM, new Random());
        put(AGENT_DIRECTIONAL_VECTOR,
                new Geometry.Vector2D(0.0, 0.0, 1.0, 1.0));
        put(AGENT_SPEED, 1.0);
        put(AGENT_MORTALITY, 0.2);
        put(AGENT_FECUNDITY, 0.4);
        put(AGENT_MUTATION, 0.05);
    }};

    /**
     * Get application's random generator
     *
     * @return application's random source
     */
    public Random getRandom() {
        return (Random) applicationProperties.get(RANDOM);
    }

    /**
     * Change the seed of the generator
     *
     * @param randomSeed new seed for the random generator
     */
    public void setRandomSeed(final long randomSeed) {
        ((Random) applicationProperties.get(RANDOM)).setSeed(randomSeed);
    }

    /**
     * Load properties from a configuration file
     *
     * @param propertyFile configuration file
     */
    public void loadConfiguration(final File propertyFile) {
        require(propertyFile.exists(),
                "Given property file doesn't exist");
        require(!propertyFile.isDirectory(),
                "Given property file is a directory");

        //Kept old properties for a rollback
        Map<String, Object> oldProperties = new HashMap<String, Object>();
        oldProperties.put(RANDOM, getRandom());

        Properties properties = new Properties(System.getProperties());
        FileReader reader = null;

        try {
            reader = new FileReader(propertyFile);
            properties.load(reader);

            //update random seed
            String userRandomSeed = properties.getProperty(RANDOM);
            if (isValid(userRandomSeed)) {
                try {
                    setRandomSeed(
                            Long.parseLong(userRandomSeed));
                } catch (NumberFormatException e) {
                    logger.log(Level.WARNING,
                            "Couldn't parse random parameter", e);
                }
            }

            //update directional vector
            String userDirectionnalVector = properties.getProperty(AGENT_DIRECTIONAL_VECTOR);
            if (isValid(userDirectionnalVector)) {
                try {
                    Matcher matchData = USER_DIRECTIONAL_VECTOR_PARAMETER.matcher(userDirectionnalVector);
                    if (matchData.matches()) {
                        setDirectionalVector(
                                new Geometry.Vector2D(
                                        Double.parseDouble(matchData.group(1)),
                                        Double.parseDouble(matchData.group(2)),
                                        Double.parseDouble(matchData.group(3)),
                                        Double.parseDouble(matchData.group(4))
                                )
                        );
                    } else {
                        logger.log(Level.WARNING,
                                "Couldn't parse user user directional vector, "
                                        + "it must be like (0.0,0.0)-(1.0,1.0)");
                    }
                } catch (NumberFormatException e) {
                    logger.log(Level.SEVERE,
                            "Couldn't parse user directional vector despite of regex !", e);
                }
            }

            //update agent speed
            String userAgentSpeed = properties.getProperty(AGENT_SPEED);
            if (isValid(userAgentSpeed)) {
                try {
                    Matcher matchData = USER_SPEED_PARAMETER.matcher(userAgentSpeed);
                    if (matchData.matches()) {
                        setAgentSpeed(Double.parseDouble(userAgentSpeed));
                    }
                } catch (NumberFormatException e) {
                    logger.log(Level.SEVERE,
                            "Couldn't parse user agent speed, it must be within [0,3] !", e);
                }
            }

            //update agent mortality
            String userAgentMortality = properties.getProperty(AGENT_MORTALITY);
            if (isValid(userAgentMortality)) {
                try {
                    setAgentMortality(Double.parseDouble(userAgentMortality));
                } catch (NumberFormatException e) {
                    logger.log(Level.SEVERE,
                            "Couldn't parse user agent mortality rate, it must be within [0,1] !", e);
                }
            }

            //update agent fecundity
            String userAgentFecundity = properties.getProperty(AGENT_FECUNDITY);
            if (isValid(userAgentFecundity)) {
                try {
                    setAgentFecundity(Double.parseDouble(userAgentFecundity));
                } catch (NumberFormatException e) {
                    logger.log(Level.SEVERE,
                            "Couldn't parse user agent fecundity rate, it must be within [0,1] !", e);
                }
            }

            //update agent mutation
            String userAgentMutation = properties.getProperty(AGENT_MUTATION);
            if (isValid(userAgentMutation)) {
                try {
                    setAgentMutation(Double.parseDouble(userAgentMutation));
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
                    setDefaultBehaviourManager(userBehaviourManagerClass.newInstance());
                } catch (ClassNotFoundException e) {
                    logger.log(Level.SEVERE, "Couldn't found user BehaviourManager class", e);
                } catch (InstantiationException e) {
                    logger.log(Level.SEVERE, "Couldn't initialize user BehaviourManager class", e);
                } catch (IllegalAccessException e) {
                    logger.log(Level.SEVERE, "You don't have an access to this class", e);
                }
            }


            //Handle exceptions and rollback properties
        } catch (FileNotFoundException e) {
            logger.log(Level.SEVERE,
                    "Severe error : file not found and it ought not happen !",
                    e);
            rollbackProperties(oldProperties);
        } catch (IOException e) {
            logger.log(Level.SEVERE,
                    "IO Exception on '" + propertyFile.getAbsolutePath() + "'",
                    e);
            rollbackProperties(oldProperties);
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    logger.log(Level.SEVERE,
                            "Couldn't close reader for '"
                                    + propertyFile.getAbsolutePath()
                                    + "' file",
                            e);
                }
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

    /**
     * Load properties from a configuration file
     *
     * @param filepath configuration file's filepath
     */
    public void loadConfiguration(final String filepath) {
        loadConfiguration(new File(filepath));
    }

    public void setDirectionalVector(final Geometry.Vector2D newVector) {
        applicationProperties.put(AGENT_DIRECTIONAL_VECTOR, newVector);
    }

    public void setAgentSpeed(final Double newSpeed) {
        applicationProperties.put(AGENT_SPEED, newSpeed);
    }

    public void setAgentMortality(final Double newMortalityRate) {
        applicationProperties.put(AGENT_MORTALITY, newMortalityRate);
    }

    public void setAgentFecundity(final Double newFecundityRate) {
        applicationProperties.put(AGENT_FECUNDITY, newFecundityRate);
    }

    public void setAgentMutation(final Double newMutationRate) {
        applicationProperties.put(AGENT_MUTATION, newMutationRate);
    }

    public void setDefaultBehaviourManager(final BehaviorManager newManager) {
        applicationProperties.put(AGENT_DEFAULT_BEHAVIOUR_MANAGER, newManager);
    }

    public <T> void setParameter(String parameterName, T parameterValue, Class<T> parameterType) {
        checkParameterValidity(parameterName, parameterValue, parameterType);
        applicationProperties.put(parameterName, parameterValue);
    }

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
            if (!paramType.equals(Geometry.Vector2D.class)) {
                throw new IllegalArgumentException(
                        "Invalid value this parameter, it must be an "
                                + Geometry.Vector2D.class.getCanonicalName());
            }
        } else if (paramName.equals(AGENT_SPEED)) {
            if (!paramType.equals(Double.class)) {
                throw new IllegalArgumentException(
                        "Invalid value this parameter, it must be an "
                                + Double.class.getCanonicalName());
            }
        } else if (paramName.equals(AGENT_MORTALITY)) {
            if (!paramType.equals(Double.class)) {
                throw new IllegalArgumentException(
                        "Invalid value this parameter, it must be an "
                                + Double.class.getCanonicalName());
            }
        } else if (paramName.equals(AGENT_FECUNDITY)) {
            if (!paramType.equals(Double.class)) {
                throw new IllegalArgumentException(
                        "Invalid value this parameter, it must be an "
                                + Double.class.getCanonicalName());
            }
        } else if (paramName.equals(AGENT_MUTATION)) {
            if (!paramType.equals(Double.class)) {
                throw new IllegalArgumentException(
                        "Invalid value this parameter, it must be an "
                                + Double.class.getCanonicalName());
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
