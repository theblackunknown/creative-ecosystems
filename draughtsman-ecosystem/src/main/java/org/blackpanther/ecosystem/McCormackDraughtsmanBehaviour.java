package org.blackpanther.ecosystem;


import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.util.Random;
import java.util.logging.Logger;

import static org.blackpanther.ecosystem.Configuration.Configuration;
import static org.blackpanther.ecosystem.Configuration.RANDOM;

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
        move(env, agent);
        //Step 3 - grow up
        growUp(env, agent);
    }

    /**
     * Move according to current agent's movement's characteristics
     *
     * @param that currently updated agent
     */
    protected void move(Environment env, Agent that) {
        //Step 1 - Update location according to current orientation
        Point2D oldLocation = (Point2D) that.getLocation().clone();
        that.setLocation(
                that.getLocation().getX()
                        + that.getSpeed()
                        * Math.cos(that.getOrientation()),
                that.getLocation().getY()
                        + that.getSpeed()
                        * Math.sin(that.getOrientation())
        );
        logger.finer(String.format("Changed %s 's location from %s to %s",
                that, oldLocation, that.getLocation()));

        //Step 2 - Notify AreaListener that we moved, agent can died if it cross an other line
        //It dies if it didn't move
        boolean hasDied = oldLocation.equals(that.getLocation()) ||
                env.trace(new Line2D.Double(oldLocation, that.getLocation()));

        if (!hasDied) {
            logger.finer(that + " is still alive.");

            //Step 3 - Irrationality effect, influence the current curvature
            if (Configuration.getRandom().nextDouble() <= that.getIrrationality()) {
                //Irrationality is the rate but also the DEGREE OF CHANGE
                double oldCurvature = that.getCurvature();
                that.setCurvature(
                        that.getCurvature() + (
                                Configuration.getRandom().nextGaussian() * that.getIrrationality()
                        )
                );
                logger.finer(String.format("Changed %s 's curvature from %s to %s",
                        that, oldCurvature, that.getCurvature()));
            }

            //Step 4 - Update phenotype
            Double oldOrientation = that.getOrientation();
            //FIXME change quickly orientation
            that.setOrientation(
                    that.getOrientation() + that.getCurvature()
            );
            logger.finer(String.format("Changed %s 's orientation from %.4f PI to %.4f PI",
                    that, oldOrientation / Math.PI, that.getOrientation() / Math.PI));

        } else {
            that.unsetAreaListener();
            logger.finer(that + " passed away.");
        }
    }

    /**
     * Method that make an agent to behave like McCormack describe in his essay
     */
    protected void spawn(final Environment env, final Agent that) {
        Random applicationRandom = Configuration.getRandom();
        if (applicationRandom.nextDouble() < that.getFecundityRate()) {

            //Create child
            //HELP Gaussian variation of baby's genotype
            //TODO Parametrize gaussian variation
            double probabilityVariation = 0.1;
            double curvatureVariation = 0.1;
            double orientationVariation = 0.1;
            double speedVariation = 0.1;
            Agent child = new DesignerAgent(
                    //initial position
                    that.getLocation(),
                    //initial orientation
                    that.getChildOrientationLauncher(),
                    //child orientation launcher
                    mutate(
                            that.getMutationRate(),
                            that.getChildOrientationLauncher(),
                            orientationVariation
                                    * applicationRandom.nextGaussian()),
                    //initial speed
                    mutate(
                            that.getMutationRate(),
                            that.getCurvature(),
                            curvatureVariation
                                    * applicationRandom.nextGaussian()),
                    normalizeSpeed(that.getChildSpeedLauncher()),
                    //child speed launcher
                    normalizeSpeed(mutate(
                            that.getMutationRate(),
                            that.getChildSpeedLauncher(),
                            speedVariation
                                    * applicationRandom.nextGaussian())),
                    //irrationality rate
                    normalizeProbability(mutate(
                            that.getIrrationality(),
                            that.getIrrationality(),
                            probabilityVariation
                                    * applicationRandom.nextGaussian())),
                    //mortality rate
                    normalizeProbability(mutate(
                            that.getMutationRate(),
                            that.getMortalityRate(),
                            probabilityVariation
                                    * applicationRandom.nextGaussian())),
                    //fecundity rate
                    normalizeProbability(mutate(
                            that.getMutationRate(),
                            that.getFecundityRate(),
                            probabilityVariation
                                    * applicationRandom.nextGaussian())),
                    //mutation rate
                    normalizeProbability(mutate(
                            that.getMutationRate(),
                            that.getMutationRate(),
                            probabilityVariation
                                    * applicationRandom.nextGaussian())),
                    that.getBehaviour()
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

    private static Double mutate(
            Double mutationRate,
            Double normalValue,
            Double mutationVariation
    ) {
        if (Configuration.getParameter(RANDOM, Random.class)
                .nextDouble() <= mutationRate) {
            return normalValue + mutationVariation;
        } else {
            return normalValue;
        }
    }


}
