package org.blackpanther.ecosystem.helper;

import org.blackpanther.ecosystem.Agent;
import org.blackpanther.ecosystem.BehaviorManager;
import org.blackpanther.ecosystem.DesignerAgent;

import java.awt.geom.Point2D;
import java.util.Random;

import static org.blackpanther.ecosystem.Agent.*;
import static org.blackpanther.ecosystem.Configuration.*;


/**
 * TODO Why not generate a random agent ?
 *
 * @author MACHIZAUD Andréa
 * @version 1.0-alpha - Wed May 18 02:01:08 CEST 2011
 */
public final class AgentFactory {

    public static Agent StandardAgent(double abscissa, double ordinate) {
        return new DesignerAgent(
                new Point2D.Double(abscissa, ordinate),
                Configuration.getParameter(AGENT_ENERGY, Double.class),
                Configuration.getParameter(AGENT_MOVEMENT_COST, Double.class),
                Configuration.getParameter(AGENT_FECUNDATION_COST, Double.class),
                Configuration.getParameter(AGENT_FECUNDATION_LOSS, Double.class),
                Configuration.getParameter(AGENT_ORIENTATION, Double.class),
                Configuration.getParameter(AGENT_ORIENTATION_LAUNCHER, Double.class),
                Configuration.getParameter(AGENT_CURVATURE, Double.class),
                Configuration.getParameter(AGENT_SPEED, Double.class),
                Configuration.getParameter(AGENT_SPEED_LAUNCHER, Double.class),
                Configuration.getParameter(AGENT_GREED, Double.class),
                Configuration.getParameter(AGENT_SENSOR_RADIUS, Double.class),
                Configuration.getParameter(AGENT_IRRATIONALITY, Double.class),
                Configuration.getParameter(AGENT_MORTALITY, Double.class),
                Configuration.getParameter(AGENT_FECUNDITY, Double.class),
                Configuration.getParameter(AGENT_MUTATION, Double.class),
                Configuration.getParameter(AGENT_BEHAVIOUR, BehaviorManager.class)
        );
    }

    public static Agent StandardAgent(double abscissa, double ordinate, double orientation) {
        return new DesignerAgent(
                new Point2D.Double(abscissa, ordinate),
                Configuration.getParameter(AGENT_ENERGY, Double.class),
                Configuration.getParameter(AGENT_MOVEMENT_COST, Double.class),
                Configuration.getParameter(AGENT_FECUNDATION_COST, Double.class),
                Configuration.getParameter(AGENT_FECUNDATION_LOSS, Double.class),
                orientation,
                Configuration.getParameter(AGENT_ORIENTATION_LAUNCHER, Double.class),
                Configuration.getParameter(AGENT_CURVATURE, Double.class),
                Configuration.getParameter(AGENT_SPEED, Double.class),
                Configuration.getParameter(AGENT_SPEED_LAUNCHER, Double.class),
                Configuration.getParameter(AGENT_GREED, Double.class),
                Configuration.getParameter(AGENT_SENSOR_RADIUS, Double.class),
                Configuration.getParameter(AGENT_IRRATIONALITY, Double.class),
                Configuration.getParameter(AGENT_MORTALITY, Double.class),
                Configuration.getParameter(AGENT_FECUNDITY, Double.class),
                Configuration.getParameter(AGENT_MUTATION, Double.class),
                Configuration.getParameter(AGENT_BEHAVIOUR, BehaviorManager.class)
        );
    }

    public static Agent RandomAgent() {
        Point2D randomPoint = new Point2D.Double(
                Configuration.getParameter(RANDOM, Random.class).nextDouble()
                        * Configuration.getParameter(SPACE_WIDTH, Double.class)
                        - (Configuration.getParameter(SPACE_WIDTH, Double.class) / 2.0),
                Configuration.getParameter(RANDOM, Random.class).nextDouble()
                        * Configuration.getParameter(SPACE_HEIGHT, Double.class)
                        - (Configuration.getParameter(SPACE_HEIGHT, Double.class) / 2.0)
        );

        Random applicationRandom = Configuration.getRandom();

        return new DesignerAgent(
                //position
                randomPoint,
                //energy amount
                applicationRandom.nextDouble()
                        * Configuration.getParameter(ENERGY_AMOUNT_THRESHOLD, Double.class),
                //movement consummation
                applicationRandom.nextDouble(),
                //fecundation consummation
                applicationRandom.nextDouble()
                        * Configuration.getParameter(FECUNDATION_CONSUMMATION_THRESHOLD, Double.class),
                //fecundation loss
                applicationRandom.nextDouble(),
                //orientation
                applicationRandom.nextDouble() * (2 * Math.PI),
                //orientation launcher
                applicationRandom.nextDouble() * (2 * Math.PI),
                //curvature
                (applicationRandom.nextDouble() * Configuration.getParameter(CURVATURE_THRESHOLD, Double.class) * 2.0)
                        - Configuration.getParameter(CURVATURE_THRESHOLD, Double.class),
                //speed
                applicationRandom.nextDouble() * Configuration.getParameter(SPEED_THRESHOLD, Double.class),
                //speed launcher
                applicationRandom.nextDouble() * Configuration.getParameter(SPEED_THRESHOLD, Double.class),
                //greediness
                applicationRandom.nextDouble(),
                //sensor radius
                applicationRandom.nextDouble() * Configuration.getParameter(SENSOR_THRESHOLD, Double.class),
                //irrationality
                applicationRandom.nextDouble(),
                //mortality
                applicationRandom.nextDouble(),
                //fecundity
                applicationRandom.nextDouble(),
                //mutation
                applicationRandom.nextDouble(),
                //behaviour manager
                Configuration.getParameter(AGENT_BEHAVIOUR, BehaviorManager.class)
        );
    }
}
