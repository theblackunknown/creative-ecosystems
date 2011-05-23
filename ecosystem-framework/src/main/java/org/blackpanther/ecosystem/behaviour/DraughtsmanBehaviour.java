package org.blackpanther.ecosystem.behaviour;


import org.blackpanther.ecosystem.Environment;
import org.blackpanther.ecosystem.SenseResult;
import org.blackpanther.ecosystem.SensorTarget;
import org.blackpanther.ecosystem.agent.Creature;
import org.blackpanther.ecosystem.agent.Resource;
import org.blackpanther.ecosystem.factory.fields.FieldsConfiguration;
import org.blackpanther.ecosystem.factory.fields.GeneFieldMould;
import org.blackpanther.ecosystem.factory.fields.StateFieldMould;

import java.awt.*;
import java.awt.geom.Point2D;
import java.util.Collection;
import java.util.Iterator;
import java.util.Random;
import java.util.logging.Logger;

import static java.lang.Math.*;
import static org.blackpanther.ecosystem.agent.Agent.*;
import static org.blackpanther.ecosystem.Configuration.*;
import static org.blackpanther.ecosystem.factory.generator.StandardProvider.StandardProvider;
import static org.blackpanther.ecosystem.helper.Helper.*;
import static org.blackpanther.ecosystem.math.Geometry.PI_2;

/**
 * <p>
 * Behaviour Manager which make agent behave
 * like McCormack draughtsmen's ones. <br/>
 * Behaviour depends on agent's genotype and agent's phenotype.
 * </p>
 *
 * @author MACHIZAUD Andréa
 * @version 1.1-alpha - Thu May 19 01:22:54 CEST 2011
 */
public class DraughtsmanBehaviour
        implements BehaviorManager {

    public DraughtsmanBehaviour() {
    }

    private static class DraughtsmanBehaviourHolder {
        private static final DraughtsmanBehaviour instance =
                new DraughtsmanBehaviour();
    }

    public static DraughtsmanBehaviour getInstance() {
        return DraughtsmanBehaviourHolder.instance;
    }

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
    public void update(Environment env, Creature agent) {
        SenseResult analysis = agent.sense(env);
        //Step 1 - react with anything detected
        react(env, agent, analysis);
        //Step 2 - spawn
        spawn(env, agent);
        //Step 3 - move ( and trace )
        move(env, agent);
        //Step 4 - grow up
        growUp(env, agent);
    }

    protected void react(Environment env, Creature that, SenseResult analysis) {
        //fetch closest resource
        SensorTarget<Resource> closestResource =
                getClosestResource(that.getLocation(), analysis.getNearResources());
        if (closestResource != null) {

            //check if we can still move and reach resource
            double resourceDistance = that.getLocation().distance(closestResource.getTarget().getLocation());
            if (resourceDistance < Configuration.getParameter(CONSUMMATION_RADIUS, Double.class)) {
                //we eat it
                that.setEnergy(that.getEnergy() + closestResource.getTarget().getEnergy());
                that.setColor(
                        (that.getColor().getRed()
                                + closestResource.getTarget().getGene(AGENT_NATURAL_COLOR, Color.class).getRed()) / 2,
                        (that.getColor().getGreen()
                                + closestResource.getTarget().getGene(AGENT_NATURAL_COLOR, Color.class).getGreen()) / 2,
                        (that.getColor().getBlue()
                                + closestResource.getTarget().getGene(AGENT_NATURAL_COLOR, Color.class).getBlue()) / 2
                );

                closestResource.getTarget().detachFromEnvironment(env);
            }

            //otherwise get closer
            else {
                double lust = that.getGene(CREATURE_GREED, Double.class);
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
                        closestResource.getTarget().getLocation().getX(),
                        closestResource.getTarget().getLocation().getY(),
                        alpha / PI,
                        beta / PI,
                        resourceRelativeOrientation / PI,
                        newOrientation / PI
                );
                logger.fine(format);
            }
        }
    }

    /**
     * Move according to current agent's movement's characteristics
     */
    protected void move(Environment env, Creature that) {
        boolean hasDied;
        if (that.getEnergy() >=
                that.getGene(CREATURE_MOVEMENT_COST, Double.class) * that.getSpeed()) {

            //Step 1 - Update location according to current orientation
            Point2D oldLocation = (Point2D) that.getLocation().clone();
            that.setLocation(
                    that.getLocation().getX()
                            + that.getSpeed()
                            * cos(that.getOrientation()),
                    that.getLocation().getY()
                            + that.getSpeed()
                            * sin(that.getOrientation())
            );

            //Step 2 - Consume energy
            that.setEnergy(
                    that.getEnergy() - that.getGene(CREATURE_MOVEMENT_COST, Double.class) * that.getSpeed()
            );

            logger.finer(String.format("Changed %s 's location from %s to %s",
                    that, oldLocation, that.getLocation()));

            //Step 3 - agent can died if it cross an other line
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
            that.detachFromEnvironment(env);
            logger.finer(that + " passed away.");
        }
    }

    /**
     * Method that make an agent to behave like McCormack describe in his essay
     */
    protected void spawn(final Environment env, final Creature that) {
        //check if the environment can hold more agents
        if (env.getCreaturePool().size() < Configuration.getParameter(MAX_AGENT_NUMBER, Integer.class)) {

            Random applicationRandom = Configuration.getRandom();
            if (applicationRandom.nextDouble() < that.getFecundityRate()
                    && that.getEnergy() >= that.getGene(CREATURE_FECUNDATION_COST, Double.class)) {

                //Create child
                Creature child = new Creature(
                        generateConfigurationFromParent(that));

                //Step 2 - loss of energy
                that.setEnergy(
                        that.getEnergy() *
                                (1 - that.getGene(CREATURE_FECUNDATION_LOSS, Double.class))
                );

                //Add into environment
                env.nextGeneration(
                        child
                );
            }
        }
    }

    @SuppressWarnings("unchecked")
    private FieldsConfiguration generateConfigurationFromParent(Creature parent) {
        return new FieldsConfiguration(
                        new StateFieldMould(CREATURE_COLOR, StandardProvider(
                                parent.getColor()
                        )),
                        new StateFieldMould(AGENT_LOCATION, StandardProvider(
                                parent.getLocation()
                        )),
                        new StateFieldMould(AGENT_ENERGY, StandardProvider(
                                parent.getEnergy() * parent.getGene(CREATURE_FECUNDATION_LOSS, Double.class)
                        )),
                        new StateFieldMould(CREATURE_CURVATURE, StandardProvider(
                                parent.getCurvature()
                        )),
                        new StateFieldMould(CREATURE_ORIENTATION, StandardProvider(
                                parent.getGene(CREATURE_ORIENTATION_LAUNCHER, Double.class)
                        )),
                        new StateFieldMould(CREATURE_SPEED, StandardProvider(
                                parent.getGene(CREATURE_SPEED_LAUNCHER, Double.class)
                        )),
                        new GeneFieldMould(AGENT_NATURAL_COLOR, StandardProvider(
                                parent.isMutable(AGENT_NATURAL_COLOR)
                                        ? mutate(
                                        parent.getMutationRate(),
                                        parent.getColor())
                                        : parent.getGene(AGENT_NATURAL_COLOR, Color.class)),
                                parent.isMutable(AGENT_NATURAL_COLOR)
                        ),
                        new GeneFieldMould(CREATURE_MOVEMENT_COST, StandardProvider(
                                parent.getGene(CREATURE_MOVEMENT_COST, Double.class)),
                                false
                        ),
                        new GeneFieldMould(CREATURE_FECUNDATION_COST, StandardProvider(
                                parent.getGene(CREATURE_FECUNDATION_COST, Double.class)),
                                false
                        ),
                        new GeneFieldMould(CREATURE_FECUNDATION_LOSS, StandardProvider(
                                parent.isMutable(CREATURE_FECUNDATION_LOSS)
                                        ? normalizeProbability(mutate(
                                        parent.getMutationRate(),
                                        parent.getGene(CREATURE_FECUNDATION_LOSS, Double.class),
                                        Configuration.getParameter(PROBABILITY_VARIATION, Double.class)
                                                * Configuration.getRandom().nextGaussian()))
                                        : parent.getGene(CREATURE_FECUNDATION_LOSS, Double.class)),
                                parent.isMutable(CREATURE_FECUNDATION_LOSS)
                        ),
                        new GeneFieldMould(CREATURE_GREED, StandardProvider(
                                parent.isMutable(CREATURE_GREED)
                                        ? normalizeProbability(mutate(
                                        parent.getMutationRate(),
                                        parent.getGene(CREATURE_GREED, Double.class),
                                        Configuration.getParameter(PROBABILITY_VARIATION, Double.class)
                                                * Configuration.getRandom().nextGaussian()))
                                        : parent.getGene(CREATURE_GREED, Double.class)),
                                parent.isMutable(CREATURE_GREED)
                        ),
                        new GeneFieldMould(CREATURE_FLEE, StandardProvider(
                                parent.isMutable(CREATURE_FLEE)
                                        ? normalizeProbability(mutate(
                                        parent.getMutationRate(),
                                        parent.getGene(CREATURE_FLEE, Double.class),
                                        Configuration.getParameter(PROBABILITY_VARIATION, Double.class)
                                                * Configuration.getRandom().nextGaussian()))
                                        : parent.getGene(CREATURE_FLEE, Double.class)),
                                parent.isMutable(CREATURE_FLEE)
                        ),
                        new GeneFieldMould(CREATURE_SENSOR_RADIUS, StandardProvider(
                                parent.isMutable(CREATURE_SENSOR_RADIUS)
                                        ? normalizeProbability(mutate(
                                        parent.getMutationRate(),
                                        parent.getGene(CREATURE_SENSOR_RADIUS, Double.class),
                                        Configuration.getParameter(SPEED_VARIATION, Double.class)
                                                * Configuration.getRandom().nextGaussian()))
                                        : parent.getGene(CREATURE_SENSOR_RADIUS, Double.class)),
                                parent.isMutable(CREATURE_SENSOR_RADIUS)
                        ),
                        new GeneFieldMould(CREATURE_IRRATIONALITY, StandardProvider(
                                parent.isMutable(CREATURE_IRRATIONALITY)
                                        ? normalizeProbability(mutate(
                                        parent.getMutationRate(),
                                        parent.getGene(CREATURE_IRRATIONALITY, Double.class),
                                        Configuration.getParameter(PROBABILITY_VARIATION, Double.class)
                                                * Configuration.getRandom().nextGaussian()))
                                        : parent.getGene(CREATURE_IRRATIONALITY, Double.class)),
                                parent.isMutable(CREATURE_IRRATIONALITY)
                        ),
                        new GeneFieldMould(CREATURE_MORTALITY, StandardProvider(
                                parent.isMutable(CREATURE_MORTALITY)
                                        ? normalizeProbability(mutate(
                                        parent.getMutationRate(),
                                        parent.getGene(CREATURE_MORTALITY, Double.class),
                                        Configuration.getParameter(PROBABILITY_VARIATION, Double.class)
                                                * Configuration.getRandom().nextGaussian()))
                                        : parent.getGene(CREATURE_MORTALITY, Double.class)),
                                parent.isMutable(CREATURE_MORTALITY)
                        ),
                        new GeneFieldMould(CREATURE_FECUNDITY, StandardProvider(
                                parent.isMutable(CREATURE_FECUNDITY)
                                        ? normalizeProbability(mutate(
                                        parent.getMutationRate(),
                                        parent.getGene(CREATURE_FECUNDITY, Double.class),
                                        Configuration.getParameter(PROBABILITY_VARIATION, Double.class)
                                                * Configuration.getRandom().nextGaussian()))
                                        : parent.getGene(CREATURE_FECUNDITY, Double.class)),
                                parent.isMutable(CREATURE_FECUNDITY)
                        ),
                        new GeneFieldMould(CREATURE_MUTATION, StandardProvider(
                                parent.isMutable(CREATURE_MUTATION)
                                        ? normalizeProbability(mutate(
                                        parent.getMutationRate(),
                                        parent.getGene(CREATURE_MUTATION, Double.class),
                                        Configuration.getParameter(PROBABILITY_VARIATION, Double.class)
                                                * Configuration.getRandom().nextGaussian()))
                                        : parent.getGene(CREATURE_MUTATION, Double.class)),
                                parent.isMutable(CREATURE_MUTATION)
                        ),
                        new GeneFieldMould(CREATURE_ORIENTATION_LAUNCHER, StandardProvider(
                                parent.isMutable(CREATURE_ORIENTATION_LAUNCHER)
                                        ? normalizeProbability(mutate(
                                        parent.getMutationRate(),
                                        parent.getGene(CREATURE_ORIENTATION_LAUNCHER, Double.class),
                                        Configuration.getParameter(ANGLE_VARIATION, Double.class)
                                                * Configuration.getRandom().nextGaussian()))
                                        : parent.getGene(CREATURE_ORIENTATION_LAUNCHER, Double.class)),
                                parent.isMutable(CREATURE_ORIENTATION_LAUNCHER)
                        ),
                        new GeneFieldMould(CREATURE_SPEED_LAUNCHER, StandardProvider(
                                parent.isMutable(CREATURE_SPEED_LAUNCHER)
                                        ? normalizeProbability(mutate(
                                        parent.getMutationRate(),
                                        parent.getGene(CREATURE_SPEED_LAUNCHER, Double.class),
                                        Configuration.getParameter(SPEED_VARIATION, Double.class)
                                                * Configuration.getRandom().nextGaussian()))
                                        : parent.getGene(CREATURE_SPEED_LAUNCHER, Double.class)),
                                parent.isMutable(CREATURE_SPEED_LAUNCHER)
                        ),
                        new GeneFieldMould(CREATURE_BEHAVIOR, StandardProvider(
                                parent.getGene(CREATURE_BEHAVIOR, BehaviorManager.class)),
                                false
                        )
                );
    }

    protected void growUp(Environment env, Creature that) {
        double randomValue = Configuration.getRandom()
                .nextDouble();
        //TODO update phenotype death's chance according to age and mortality rate
        double deathChance = that.getMortalityRate();
        logger.finer(String.format("[Random mortality's value = %f, death's chance = %f]", randomValue, deathChance));
        if (randomValue < deathChance) {
            that.detachFromEnvironment(env);
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
        if (Configuration.getRandom()
                .nextDouble() <= mutationRate) {
            return normalValue + mutationVariation;
        } else {
            return normalValue;
        }
    }

    private static Double mutate(
            Double mutationRate,
            Integer normalValue,
            Double mutationVariation
    ) {
        if (Configuration.getRandom()
                .nextDouble() <= mutationRate) {
            return normalValue + mutationVariation;
        } else {
            return (double) normalValue;
        }
    }

    private static Color mutate(
            Double mutationRate,
            Color normalValue
    ) {
        return new Color(
                normalizeColor(mutate(
                        mutationRate,
                        normalValue.getRed(),
                        Configuration.getParameter(COLOR_VARIATION, Double.class)
                                * Configuration.getRandom().nextGaussian()
                )),
                normalizeColor(mutate(
                        mutationRate,
                        normalValue.getGreen(),
                        Configuration.getParameter(COLOR_VARIATION, Double.class)
                                * Configuration.getRandom().nextGaussian()

                )),
                normalizeColor(mutate(
                        mutationRate,
                        normalValue.getBlue(),
                        Configuration.getParameter(COLOR_VARIATION, Double.class)
                                * Configuration.getRandom().nextGaussian()

                ))
        );
    }

    protected static SensorTarget<Resource> getClosestResource(Point2D source, Collection<SensorTarget<Resource>> resources) {
        Iterator<SensorTarget<Resource>> it = resources.iterator();
        if (it.hasNext()) {
            SensorTarget<Resource> closest = it.next();
            double closestDistance = source.distance(closest.getTarget().getLocation());
            while (it.hasNext()) {
                SensorTarget<Resource> res = it.next();
                double distance = source.distance(res.getTarget().getLocation());
                if (distance < closestDistance) {
                    closest = res;
                    closestDistance = distance;
                }
            }
            return closest;
        } else return null;
    }

    protected static SensorTarget<Creature> getClosestCreature(Point2D source, Collection<SensorTarget<Creature>> monsters) {
        Iterator<SensorTarget<Creature>> it = monsters.iterator();
        if (it.hasNext()) {
            SensorTarget<Creature> closest = it.next();
            double closestDistance = source.distance(closest.getTarget().getLocation());
            while (it.hasNext()) {
                SensorTarget<Creature> monster = it.next();
                double distance = source.distance(monster.getTarget().getLocation());
                if (distance < closestDistance) {
                    closest = monster;
                    closestDistance = distance;
                }
            }
            return closest;
        } else return null;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName();
    }
}