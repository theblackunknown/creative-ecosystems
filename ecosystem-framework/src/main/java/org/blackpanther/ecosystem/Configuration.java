package org.blackpanther.ecosystem;

import java.io.IOException;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

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
public enum Configuration
        implements ApplicationConstants {
    Configuration;

    private static final Long serialVersionUID = 3L;

    /**
     * Application's parameters with default loaded
     */
    protected final Map<String, Object> applicationProperties = new HashMap<String, Object>();

    private final Random generator = new Random();

    /**
     * Silent application's parameters initialization
     */
    private Configuration() {

        //pre-sort arrays
        Arrays.sort(DOUBLE_UNBOUNDED);
        Arrays.sort(POSITIVE_DOUBLE);
        Arrays.sort(POSITIVE_INTEGER);

        Logger classLogger = Logger.getLogger(Configuration.class.getCanonicalName());
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
        return generator;
    }

    /**
     * Load properties from a configuration file
     *
     * @param properties usersProperties
     */
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
    }

    public <T> void setParameter(String parameterName, T parameterValue, Class<T> parameterType) {
        checkParameterValidity(parameterName, parameterValue, parameterType);
        applicationProperties.put(parameterName, parameterValue);
    }


    public <T> T getParameter(String parameterName, Class<T> parameterType) {
        Object correspondingParameter = applicationProperties.get(parameterName);
        if (correspondingParameter != null) {
            if (parameterType.isInstance(correspondingParameter)) {
                return parameterType.cast(correspondingParameter);
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
}