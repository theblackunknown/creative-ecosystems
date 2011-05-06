package org.blackpanther.ecosystem;


import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.util.Random;
import java.util.logging.Logger;

import static org.blackpanther.ecosystem.Configuration.*;
import static org.blackpanther.ecosystem.Configuration.Configuration;

/**
 * <p>
 * Behaviour Manager which make agent behave
 * like McCormack draughtsmen's ones. <br/>
 * Behaviour depends on agent's genotype and agent's phenotype.
 * </p>
 *
 * @author MACHIZAUD Andréa
 * @version 0.3 - Sun May  1 00:00:13 CEST 2011
 */
public class McCormackDraughtsmanBehaviour
        implements BehaviorManager {

    /**
     * class logger
     */
    private static final Logger logger =
            Logger.getLogger(
                    McCormackDraughtsmanBehaviour.class.getCanonicalName()
            );

    /**
     * <p>
     * Let agents behave like McCormack's idea.
     * </p>
     * {@inheritDoc}
     */
    @Override
    public final void update(Environment env, Agent agent) {
        //Step 1 - spawn
        spawn(env, agent);
        //Step 2 - move ( and trace )
        move(agent);
        //Step 3 - mutate
        mutate(env, agent);
        //Step 4 - grow up
        growUp(env, agent);
    }

    /**
     * Move according to current agent's movement's characteristics
     *
     * @param that currently updated agent
     */
    protected void move(Agent that) {
        //Step 1 - Update location according to current orientation
        Point2D oldLocation = that.getLocation();
        that.setLocation(
                that.getLocation().getX()
                        + that.getSpeed()
                        * Math.cos(that.getOrientation()),
                that.getLocation().getY()
                        + that.getSpeed()
                        * Math.sin(that.getOrientation())
        );
        logger.fine(String.format("Changed %s 's location from %s to %s",
                that, oldLocation, that.getLocation()));

        //Step 2 - Notify AreaListener that we moved, agent can died if it cross an other line
        //It dies if it didn't move
        boolean hasDied = oldLocation.equals(that.getLocation()) ||
                that.getAreaListener().trace(new Line2D.Double(oldLocation, that.getLocation()));

        if (!hasDied) {
            logger.fine(that + " is still alive.");

            //Step 3 - Update phenotype
            //TODO DEPRECATED vector rotation - http://en.wikipedia.org/wiki/Rotation_(mathematics)#Matrix_algebra
            Double oldOrientation = that.getOrientation();
            that.setOrientation(
                    (that.getOrientation() + that.getCurvature()) % (2 * Math.PI)
            );
            logger.fine(String.format("Changed %s 's orientation from %s to %s",
                    that, oldOrientation, that.getOrientation()));

        } else {
            that.unsetAreaListener();
            logger.fine(that + " passed away.");
        }
    }

    /**
     * Method that make an agent to behave like McCormack describe in his essay
     */
    protected void mutate(Environment env, Agent that) {
        //HELP No mutation for the moment - XMen have not exist yet
    }

    /**
     * Method that make an agent to behave like McCormack describe in his essay
     */
    protected void spawn(final Environment env, final Agent that) {
        double randomValue = Configuration.getParameter(RANDOM, Random.class)
                .nextDouble();
        logger.finer(String.format("Random spawn's value : %f", randomValue));
        if (randomValue < that.getFecundityRate()) {

            //Create child
            //HELP Gaussian variation of baby's genotype
            //TODO Parametrize gaussian variation
            double probabilityVariation = 0.1;
            double curvatureVariation = 0.1;
            double orientationVariation = 0.1;
            double speedVariation = 0.1;
            Agent child = new DesignerAgent(
                    that.getLocation(),
                    that.getChildOrientationLauncher(),
                    that.getChildOrientationLauncher()
                            + orientationVariation
                            * Configuration.getParameter(RANDOM, Random.class).nextGaussian(),
                    that.getCurvature()
                            + curvatureVariation
                            * Configuration.getParameter(RANDOM, Random.class).nextGaussian(),
                    normalizeSpeed(that.getChildSpeedLauncher()),
                    normalizeSpeed(that.getChildSpeedLauncher()
                            + speedVariation
                            * Configuration.getParameter(RANDOM, Random.class).nextGaussian()),
                    normalizeProbability(
                            that.getMortalityRate()
                                    + probabilityVariation
                                    * Configuration.getParameter(RANDOM, Random.class).nextGaussian()),
                    normalizeProbability(
                            that.getFecundityRate()
                                    + probabilityVariation
                                    * Configuration.getParameter(RANDOM, Random.class).nextGaussian()),
                    normalizeProbability(
                            that.getMutationRate()
                                    + probabilityVariation
                                    * Configuration.getParameter(RANDOM, Random.class).nextGaussian()),
                    Configuration.getParameter(AGENT_DEFAULT_BEHAVIOUR_MANAGER, BehaviorManager.class)
            );

            //Add into environment
            env.nextGeneration(
                    child
            );
        }
    }

    protected void growUp(Environment env, Agent that) {
        double randomValue = Configuration.getParameter(RANDOM, Random.class)
                .nextDouble();
        //TODO update phenotype death's chance according to age and mortality rate
        double deathChance = that.getMortalityRate() * (that.getAge() / 10);
        logger.finer(String.format("[Random mortality's value = %f, death's chance = %f]", randomValue, deathChance));
        if (randomValue < deathChance) {
            that.unsetAreaListener();
            logger.fine(that + " died naturally.");
        } else {
            logger.fine(that + " didn't die yet.");
            that.setAge(that.getAge() + 1);
        }
    }

    private static Double normalizeProbability(double probability) {
        if (probability < 0.0)
            return 0.0;
        else if (probability > 1.0)
            return 1.0;
        else
            return probability;
    }

    private static Double normalizeSpeed(double speed) {
        if (speed < 0.0)
            return 0.0;
        else
            return speed;
    }


}
