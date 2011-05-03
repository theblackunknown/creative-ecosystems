package org.blackpanther.ecosystem;


import org.blackpanther.ecosystem.math.Geometry;

import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.util.Random;
import java.util.logging.Logger;

import static org.blackpanther.ecosystem.Configuration.*;

/**
 * @author MACHIZAUD Andréa
 * @version 0.3 - Sun May  1 00:00:13 CEST 2011
 */
public class McCormackDraughtsmanBehaviour
        implements BehaviorManager {

    private static final Logger logger =
            Logger.getLogger(
                    McCormackDraughtsmanBehaviour.class.getCanonicalName()
            );

    /**
     * {@inheritDoc}
     * <p/>
     * Method that make an agent to behave like McCormack describe in his essay
     */
    @Override
    public void update(Environment env, Agent agent) {
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
     * {@inheritDoc}
     * <p/>
     * Method that make an agent to behave like McCormack describe in his essay
     */
    @Override
    public void move(Agent that) {
        //Step 1 - Update location according to current orientation
        Point2D oldLocation = that.getLocation();
        that.setLocation(
                that.getLocation().getX()
                        + that.getSpeed()
                        * Math.cos( that.getOrientation() ),
                that.getLocation().getY()
                        + that.getSpeed()
                        * Math.sin( that.getOrientation() )
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
                    ( that.getOrientation() + that.getCurvature() ) % ( 2 * Math.PI )
            );
            logger.fine(String.format("Changed %s 's orientation from %s to %s",
                    that, oldOrientation, that.getOrientation()));

        } else {
            that.unsetAreaListener();
            logger.fine(that + " passed away.");
        }
    }

    /**
     * {@inheritDoc}
     * <p/>
     * Method that make an agent to behave like McCormack describe in his essay
     */
    @Override
    public void mutate(Environment env, Agent that) {
        //HELP No mutation for the moment - XMen have not exist yet
    }

    /**
     * {@inheritDoc}
     * <p/>
     * Method that make an agent to behave like McCormack describe in his essay
     */
    @Override
    public void spawn(Environment env, Agent that) {
        double randomValue = Configuration.getParameter(RANDOM, Random.class)
                .nextDouble();
        logger.finer(String.format("Random spawn's value : %f", randomValue));
        if (randomValue < that.getFecundityRate()) {

            //HELP Generate a new generation randomly
            //TODO Try to make parent gene influence on it
            Double childOrientation =
                    Configuration.getParameter(RANDOM, Random.class).nextDouble() * 2 * Math.PI;

            //Create child
            //HELP Arbitrary implementation
            //TODO Parametrize it
            Agent child = new DesignerAgent(
                    that.getLocation(),
                    childOrientation,
                    //Static curvature to have always the same draw pattern
                    Configuration.getParameter(AGENT_CURVATURE, Double.class),
                    Configuration.getParameter(AGENT_SPEED, Double.class),
                    //Let children live longer
                    that.getMortalityRate() / 4,
                    //Let children be less fecund
                    that.getFecundityRate() / 3,
                    Configuration.getParameter(AGENT_MUTATION, Double.class),
                    Configuration.getParameter(AGENT_DEFAULT_BEHAVIOUR_MANAGER, BehaviorManager.class)
            );

            //Reduce parent fecundity rate (He is exhausted)
            that.setFecundityRate(
                    that.getFecundityRate() / 1.2
            );

            //Add into environment
            env.nextGeneration(
                    child
            );
        }
    }

    @Override
    public void growUp(Environment env, Agent that) {
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


}
