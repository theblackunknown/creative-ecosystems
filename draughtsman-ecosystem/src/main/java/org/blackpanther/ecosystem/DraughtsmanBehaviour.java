package org.blackpanther.ecosystem;


import java.awt.geom.Point2D;
import java.util.Collection;
import java.util.Iterator;
import java.util.Random;
import java.util.logging.Logger;

import static java.lang.Math.*;
import static org.blackpanther.ecosystem.Agent.*;
import static org.blackpanther.ecosystem.Configuration.*;
import static org.blackpanther.ecosystem.Configuration.Configuration;
import static org.blackpanther.ecosystem.helper.Helper.normalizePositiveDouble;
import static org.blackpanther.ecosystem.helper.Helper.normalizeProbability;
import static org.blackpanther.ecosystem.math.Geometry.PI_2;

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
        //Step 1 - react with anything detected
        react(env, agent);
        //Step 1 - sense around myself
        sense(env, agent);
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
            double lust = that.getGene(AGENT_GREED, Double.class);
            double alpha = (that.getOrientation() % PI_2); // 0 - 2PI
            double beta = closestResource.getOrientation();//
            double resourceRelativeOrientation = (beta - alpha);
            if (resourceRelativeOrientation > PI)
                resourceRelativeOrientation -= PI_2;
            else if (resourceRelativeOrientation < -PI)
                resourceRelativeOrientation += PI_2;
            double newOrientation = (alpha + resourceRelativeOrientation * lust) % PI_2;

            that.setOrientation(newOrientation);

            String format = String.format(
                    "%n (%.2f,%.2f)-(%.2f,%.2f)%n " +
                            "old orientation : %.2fPI%n " +
                            "resource orientation : %.2fPI%n " +
                            "resource relative orientation : %.2fPI%n " +
                            "new orientation : %.2fPI%n ",
                    that.getLocation().getX(), that.getLocation().getY(),
                    closestResource.getTarget().getX(),
                    closestResource.getTarget().getY(),
                    alpha / PI,
                    beta / PI,
                    resourceRelativeOrientation / PI,
                    newOrientation / PI
            );
            logger.fine(format);
        }
    }

    protected void react(Environment env, Agent that) {
        SenseResult analysis = that.sense();
        //fetch closest resource
        SensorTarget<Resource> closestResource =
                getClosestResource(that.getLocation(), analysis.getNearResources());
        if (closestResource != null) {
            //check if we can still move
            if (that.getEnergy() >=
                    that.getGene(AGENT_MOVEMENT_COST, Double.class) * that.getSpeed()) {
                //if we cross the resource
                double distance = that.getLocation().distance(closestResource.getTarget());
                if (distance < Configuration.getParameter(CONSUMMATION_RADIUS, Double.class)) {
                    //we eat it
                    that.setEnergy(that.getEnergy() + closestResource.getTarget().consume());
                }
            }
        }


    }

    private static SensorTarget<Resource> getClosestResource(Point2D source, Collection<SensorTarget<Resource>> resources) {
        Iterator<SensorTarget<Resource>> it = resources.iterator();
        if (it.hasNext()) {
            SensorTarget<Resource> closest = it.next();
            double closestDistance = source.distance(closest.getTarget());
            while (it.hasNext()) {
                SensorTarget<Resource> res = it.next();
                double distance = source.distance(res.getTarget());
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
        boolean hasDied;
        if (that.getEnergy() >=
                that.getGene(AGENT_MOVEMENT_COST, Double.class) * that.getSpeed()) {

            //Step 1 - Consume energy
            that.setEnergy(
                    that.getEnergy() - that.getGene(AGENT_MOVEMENT_COST, Double.class) * that.getSpeed()
            );

            //Step 2 - Update location according to current orientation
            Point2D oldLocation = (Point2D) that.getLocation().clone();
            that.setLocation(
                    that.getLocation().getX()
                            + that.getSpeed()
                            * cos(that.getOrientation()),
                    that.getLocation().getY()
                            + that.getSpeed()
                            * sin(that.getOrientation())
            );

            logger.finer(String.format("Changed %s 's location from %s to %s",
                    that, oldLocation, that.getLocation()));

            //Step 3 - Notify AreaListener that we moved, agent can died if it cross an other line
            //It dies if it didn't move
            hasDied = oldLocation.equals(that.getLocation()) ||
                    env.move(that, oldLocation, that.getLocation());
        } else {
            logger.finer(String.format("%s's has no enough energy (%s) to move with his current speed (%s)",
                    that, that.getEnergy(), that.getSpeed()));
            hasDied = true;
        }
        if (!hasDied) {
            logger.finer(that + " is still alive.");

            //Step 4 - Irrationality effect, influence the current curvature
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

            //Step 5 - Update state
            Double oldOrientation = that.getOrientation();
            that.setOrientation(that.getOrientation() + that.getCurvature());
            logger.finer(String.format("Changed %s 's orientation from %.4f PI to %.4f PI",
                    that, oldOrientation / PI, that.getOrientation() / PI));

        } else {
            that.detachFromEnvironment();
            logger.finer(that + " passed away.");
        }
    }

    /**
     * Method that make an agent to behave like McCormack describe in his essay
     */
    protected void spawn(final Environment env, final Agent that) {
        //check if the environment can hold more agents
        if (env.getPool().size() < Configuration.getParameter(MAX_AGENT_NUMBER, Integer.class)) {

            Random applicationRandom = Configuration.getRandom();
            if (applicationRandom.nextDouble() < that.getFecundityRate()
                    && that.getEnergy() >= that.getGene(AGENT_FECUNDATION_COST, Double.class)) {

                //Create child
                //HELP Gaussian variation of baby's genotype
                //TODO Parametrize gaussian variation
                double probabilityVariation = Configuration.getParameter(PROBABILITY_VARIATION, Double.class);
                double curvatureVariation = Configuration.getParameter(CURVATURE_VARIATION, Double.class);
                double orientationVariation = Configuration.getParameter(ANGLE_VARIATION, Double.class);
                double speedVariation = Configuration.getParameter(SPEED_VARIATION, Double.class);
                double consummationVariation = Configuration.getParameter(CONSUMMATION_VARIATION, Double.class);
                Agent child = new DesignerAgent(
                        //initial position
                        that.getLocation(),
                        //initial amount of energy
                        that.getEnergy() * that.getGene(AGENT_FECUNDATION_LOSS, Double.class),
                        //movement cost
                        normalizePositiveDouble(mutate(
                                that.getMutationRate(),
                                that.getGene(AGENT_MOVEMENT_COST, Double.class),
                                consummationVariation
                                        * applicationRandom.nextGaussian())),
                        //fecundation cost
                        normalizePositiveDouble(mutate(
                                that.getMutationRate(),
                                that.getGene(AGENT_FECUNDATION_COST, Double.class),
                                consummationVariation
                                        * applicationRandom.nextGaussian())),
                        //fecundation loss
                        normalizeProbability(mutate(
                                that.getMutationRate(),
                                that.getGene(AGENT_FECUNDATION_LOSS, Double.class),
                                probabilityVariation
                                        * applicationRandom.nextGaussian())),
                        //initial orientation
                        that.getChildOrientationLauncher(),
                        //child orientation launcher
                        mutate(
                                that.getMutationRate(),
                                that.getChildOrientationLauncher(),
                                orientationVariation
                                        * applicationRandom.nextGaussian()),
                        //initial curvature
                        mutate(
                                that.getMutationRate(),
                                that.getCurvature(),
                                curvatureVariation
                                        * applicationRandom.nextGaussian()),
                        //initial speed
                        normalizePositiveDouble(that.getChildSpeedLauncher()),
                        //child speed launcher
                        normalizePositiveDouble(mutate(
                                that.getMutationRate(),
                                that.getChildSpeedLauncher(),
                                speedVariation
                                        * applicationRandom.nextGaussian())),
                        //greediness
                        normalizeProbability(mutate(
                                that.getMutationRate(),
                                that.getGene(AGENT_GREED, Double.class),
                                probabilityVariation
                                        * applicationRandom.nextGaussian())),
                        //initial sensor radius
                        normalizePositiveDouble(mutate(
                                that.getMutationRate(),
                                that.getSensorRadius(),
                                speedVariation
                                        * applicationRandom.nextGaussian())),
                        //irrationality rate
                        normalizeProbability(mutate(
                                that.getMutationRate(),
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

                //Step 2 - loss of energy
                that.setEnergy(
                        that.getEnergy() * that.getGene(AGENT_FECUNDATION_LOSS, Double.class)
                );

                //Add into environment
                env.nextGeneration(
                        child
                );
            }
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