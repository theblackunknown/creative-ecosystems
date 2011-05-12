package org.blackpanther.ecosystem;


import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.util.Collection;
import java.util.Iterator;
import java.util.Random;
import java.util.logging.Logger;

import static org.blackpanther.ecosystem.Agent.AGENT_GREED;
import static org.blackpanther.ecosystem.Agent.AGENT_LUST;
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
 * @version 0.2 - Wed May 11 02:54:46 CEST 2011
 */
public class DraughtsmanBehaviour
        implements BehaviorManager {

    /**
     * class logger
     */
    private static final Logger logger =
            Logger.getLogger(
                    DraughtsmanBehaviour.class.getCanonicalName()
            );

    /**
     * <p>
     * Let agents behave like McCormack's idea.
     * </p>
     * {@inheritDoc}
     */
    @Override
    public final void update(Environment env, Agent agent) {
        sense(env, agent);
        capt(env, agent);
        //Step 1 - spawn
        spawn(env, agent);
        //Step 2 - move ( and trace )
        move(env, agent);
        //Step 3 - grow up
        growUp(env, agent);
    }

    /**
     * Sense around him
     */
    protected void sense(Environment env, Agent that) {
        SenseResult analysis = that.sense();
        SensorTarget<Agent> closestAgent = getClosestAgent(
                that.getLocation(), analysis.getNearAgents());
        //TODO Handle predator-pray

        SensorTarget<Resource> closestResource = getClosestResource(
                that.getLocation(), analysis.getNearResources());
        if (closestResource != null) {
            double lust = that.getGene(AGENT_LUST, Double.class);
            double angleGap = closestResource.getOrientation() - that.getOrientation();
            that.setOrientation(
                    that.getOrientation()
                            + lust * angleGap);
            double twoPI = 2.0 * Math.PI;
            System.out.println(String.format(
                    "%n old orientation : %.4fPI%n new orientation : %.4fPI%n resource orientation : %.4fPI%n",
                    (closestResource.getOrientation() - angleGap) % twoPI,
                    that.getOrientation() % twoPI,
                    closestResource.getOrientation() % twoPI
            ));
        }
    }

    protected void capt(Environment env, Agent that) {
        SenseResult analysis = that.sense();
        int numberOfCrossedResource = 0;
        Collection<SensorTarget<Resource>> closeResources = analysis.getNearResources();
        for (SensorTarget<Resource> target : closeResources)
            if (target.getTarget().contains(that.getLocation()))
                numberOfCrossedResource++;
        for (SensorTarget<Resource> target : closeResources)
            if (target.getTarget().contains(that.getLocation()))
                target.getTarget().consume(
                        that.getGene(AGENT_GREED, Integer.class) / numberOfCrossedResource
                );
    }

    private static SensorTarget<Resource> getClosestResource(Point2D source, Collection<SensorTarget<Resource>> resources) {
        Iterator<SensorTarget<Resource>> it = resources.iterator();
        if (it.hasNext()) {
            SensorTarget<Resource> closest = it.next();
            double closestDistance = source.distance(
                    closest.getTarget().getCenter()) - closest.getTarget().getRadius();
            while (it.hasNext()) {
                SensorTarget<Resource> res = it.next();
                double distance = source.distance(
                        res.getTarget().getCenter()) - res.getTarget().getRadius();
                if (distance < closestDistance) {
                    closest = res;
                    closestDistance = distance;
                }
            }
            return closest;
        } else return null;
    }

    private static SensorTarget<Agent> getClosestAgent(Point2D source, Collection<SensorTarget<Agent>> agents) {
        Iterator<SensorTarget<Agent>> it = agents.iterator();
        if (it.hasNext()) {
            SensorTarget<Agent> closest = it.next();
            double closestDistance = source.distance(closest.getTarget().getLocation());
            while (it.hasNext()) {
                SensorTarget<Agent> agent = it.next();
                double distance = source.distance(closest.getTarget().getLocation());
                if (distance < closestDistance) {
                    closest = agent;
                    closestDistance = distance;
                }
            }
            return closest;
        } else return null;
    }

    /**
     * Move according to current agent's movement's characteristics
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
            that.detachFromEnvironment();
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
                    //initial sensor radius
                    normalizeSpeed(mutate(
                            that.getMutationRate(),
                            that.getSensorRadius(),
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
            that.detachFromEnvironment();
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