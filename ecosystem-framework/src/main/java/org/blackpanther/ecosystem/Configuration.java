package org.blackpanther.ecosystem;

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

    /** Class logger */
    private static final Logger logger =
            Logger.getLogger(Configuration.class.getCanonicalName());

    /**
     * Random application parameters
     */
    public static final String RANDOM = "random";

    /**
     * Applciation's random source
     */
    private Random randomSource = new Random();

    /**
     * Get application's random generator
     * @return application's random source
     */
    public Random getRandom() {
        return randomSource;
    }

    /**
     * Change the seed of the generator
     * @param randomSeed
     *          new seed for the random generator
     */
    public void setRandomSeed(final long randomSeed) {
        randomSource.setSeed(randomSeed);
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
        oldProperties.put(RANDOM, randomSource);

        Properties properties = new Properties(System.getProperties());
        FileReader reader = null;

        try {
            reader = new FileReader(propertyFile);
            properties.load(reader);

            //update random seed
            try {
                setRandomSeed(
                        Long.parseLong(
                                properties.getProperty(RANDOM)));
            } catch (NumberFormatException e) {
                logger.log(Level.WARNING,
                        "Couldn't parse random parameter", e);
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
        Random randomParameter = (Random) oldProperties.get(RANDOM);
        if (randomParameter != null) {
            randomSource = randomParameter;
        }
    }

    /**
     * Load properties from a configuration file
     *
     * @param filepath configuration file's filepath
     */
    public void loadConfiguration(final String filepath) {
        loadConfiguration(new File(filepath));
    }
}
