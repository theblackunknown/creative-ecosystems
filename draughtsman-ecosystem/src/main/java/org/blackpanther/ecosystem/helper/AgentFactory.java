package org.blackpanther.ecosystem.helper;

import org.blackpanther.ecosystem.Agent;
import org.blackpanther.ecosystem.BehaviorManager;
import org.blackpanther.ecosystem.DesignerAgent;

import java.awt.geom.Point2D;
import java.util.Random;
import java.util.logging.Logger;

import static org.blackpanther.ecosystem.Configuration.*;
import static org.blackpanther.ecosystem.Agent.*;



/**
 * TODO Why not generate a random agent ?
 *
 * @author MACHIZAUD Andréa
 * @version 0.3 - Sun May  1 00:00:13 CEST 2011
 */
public final class AgentFactory {

    private static final Logger logger =
            Logger.getLogger(
                    AgentFactory.class.getCanonicalName()
            );

    public static Agent StandardAgent(double abscissa, double ordinate) {
        return new DesignerAgent(
                new Point2D.Double(abscissa, ordinate),
                Configuration.getParameter(AGENT_ORIENTATION, Double.class),
                Configuration.getParameter(AGENT_ORIENTATION_LAUNCHER, Double.class),
                Configuration.getParameter(AGENT_CURVATURE, Double.class),
                Configuration.getParameter(AGENT_SPEED, Double.class),
                Configuration.getParameter(AGENT_SPEED_LAUNCHER, Double.class),
                Configuration.getParameter(AGENT_IRRATIONALITY, Double.class),
                Configuration.getParameter(AGENT_MORTALITY, Double.class),
                Configuration.getParameter(AGENT_FECUNDITY, Double.class),
                Configuration.getParameter(AGENT_MUTATION, Double.class),
                Configuration.getParameter(AGENT_BEHAVIOUR_MANAGER, BehaviorManager.class)
        );
    }

    public static Agent RandomAgent() {
        Point2D randomPoint = new Point2D.Double(
                (Configuration.getParameter(RANDOM, Random.class).nextDouble()
                        * Configuration.getParameter(SPAWN_ABSCISSA_THRESHOLD, Double.class) * 2 )
                        - Configuration.getParameter(SPAWN_ABSCISSA_THRESHOLD, Double.class),
                (Configuration.getParameter(RANDOM, Random.class).nextDouble()
                        * Configuration.getParameter(SPAWN_ORDINATE_THRESHOLD, Double.class) * 2 )
                        - Configuration.getParameter(SPAWN_ORDINATE_THRESHOLD, Double.class)
        );

        logger.finest(String.format(
                "Random point generated (%.2f,%.2f)",
                randomPoint.getX(),
                randomPoint.getY()
        ));

        return new DesignerAgent(
                //position
                randomPoint,
                //orientation
                Configuration.getParameter(RANDOM, Random.class).nextDouble() * (2 * Math.PI),
                //orientation launcher
                Configuration.getParameter(RANDOM, Random.class).nextDouble() * (2 * Math.PI),
                //curvature
                Configuration.getParameter(RANDOM, Random.class).nextDouble(),
                //speed
                Configuration.getParameter(RANDOM, Random.class).nextDouble() * 10.0,//TODO Speed threshold
                //speed launcher
                Configuration.getParameter(RANDOM, Random.class).nextDouble() * 10.0,//TODO Speed threshold
                //irrationality
                Configuration.getParameter(RANDOM, Random.class).nextDouble(),
                //mortality
                Configuration.getParameter(RANDOM, Random.class).nextDouble(),
                //fecundity
                Configuration.getParameter(RANDOM, Random.class).nextDouble(),
                //mutation
                Configuration.getParameter(RANDOM, Random.class).nextDouble(),
                //behaviour manager
                Configuration.getParameter(AGENT_BEHAVIOUR_MANAGER, BehaviorManager.class)
        );
    }
}
