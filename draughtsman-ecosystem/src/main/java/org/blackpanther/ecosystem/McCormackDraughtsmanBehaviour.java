package org.blackpanther.ecosystem;


import org.blackpanther.ecosystem.math.Geometry;

import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.util.Random;
import java.util.logging.Logger;

import static org.blackpanther.ecosystem.Configuration.*;

/**
 * @author MACHIZAUD Andréa
 * @version 1.0 - 4/25/11
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
    }

    /**
     * {@inheritDoc}
     * <p/>
     * Method that make an agent to behave like McCormack describe in his essay
     */
    @Override
    public void move(Agent that) {
        //Step 1 - Update location according to current direction
        Point2D oldLocation = that.getLocation();
        that.setLocation(
                that.getLocation().getX()
                        + that.getSpeed()
                        * that.getDirection().getDx(),
                that.getLocation().getY()
                        + that.getSpeed()
                        * that.getDirection().getDy()
        );
        logger.fine(String.format("Changed %s 's location from %s to %s",
                that, oldLocation, that.getLocation()));

        //Step 2 - Notify AreaListener that we moved, agent can died if it cross an other line
        boolean hasDied = that.getAreaListener().trace(new Line2D.Double(oldLocation, that.getLocation()));

        if (!hasDied) {
            logger.fine(that + " is still alive.");

            //Step 3 - Update phenotype
            //vector rotation - http://en.wikipedia.org/wiki/Rotation_(mathematics)#Matrix_algebra
            Geometry.Direction2D oldDirection = that.getDirection();
            that.setDirection(new Geometry.Direction2D(
                    that.getDirection().getDx() * Math.cos(that.getCurvature())
                            - that.getDirection().getDy() * Math.sin(that.getCurvature()),
                    that.getDirection().getDx() * Math.sin(that.getCurvature())
                            - that.getDirection().getDy() * Math.cos(that.getCurvature())
            ));
            logger.fine(String.format("Changed %s 's direction from %s to %s",
                    that, oldDirection, that.getDirection()));
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
        if (Configuration.getParameter(RANDOM, Random.class)
                .nextDouble() < that.getFecundityRate()) {

            //HELP Generate a new generation randomly
            //TODO Try to make parent gene influence on it
            Geometry.Direction2D childDirection = new Geometry.Direction2D(
                    Configuration.getParameter(RANDOM, Random.class).nextDouble() * 5,
                    Configuration.getParameter(RANDOM, Random.class).nextDouble() * 5
            );

            //Create child
            //HELP Arbitrary implementation
            //TODO Parametrize it
            Agent child = new DesignerAgent(
                    that.getLocation(),
                    childDirection,
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
                    that.getFecundityRate() / 3
            );

            //Add into environment
            env.addAgent(
                    child
            );
        }
    }


}
