package org.blackpanther.ecosystem.helper;

import org.blackpanther.ecosystem.Agent;
import org.blackpanther.ecosystem.BehaviorManager;
import org.blackpanther.ecosystem.DesignerAgent;

import java.awt.geom.Point2D;
import java.util.Random;

import static org.blackpanther.ecosystem.Configuration.*;

/**
 * TODO Why not generate a random agent ?
 *
 * @author MACHIZAUD Andréa
 * @version 0.3 - Sun May  1 00:00:13 CEST 2011
 */
public final class AgentFactory {

    public static Agent StandardAgent() {
        return new DesignerAgent(
                Configuration.getParameter(AGENT_LOCATION, Point2D.class),
                Configuration.getParameter(AGENT_ORIENTATION, Double.class),
                Configuration.getParameter(AGENT_CURVATURE, Double.class),
                Configuration.getParameter(AGENT_SPEED, Double.class),
                Configuration.getParameter(AGENT_MORTALITY, Double.class),
                Configuration.getParameter(AGENT_FECUNDITY, Double.class),
                Configuration.getParameter(AGENT_MUTATION, Double.class),
                Configuration.getParameter(AGENT_DEFAULT_BEHAVIOUR_MANAGER, BehaviorManager.class)
        );
    }

    public static Agent StandardAgent(double abscissa, double ordinate) {
        return new DesignerAgent(
                new Point2D.Double(abscissa,ordinate),
                Configuration.getParameter(AGENT_ORIENTATION, Double.class),
                Configuration.getParameter(AGENT_CURVATURE, Double.class),
                Configuration.getParameter(AGENT_SPEED, Double.class),
                Configuration.getParameter(AGENT_MORTALITY, Double.class),
                Configuration.getParameter(AGENT_FECUNDITY, Double.class),
                Configuration.getParameter(AGENT_MUTATION, Double.class),
                Configuration.getParameter(AGENT_DEFAULT_BEHAVIOUR_MANAGER, BehaviorManager.class)
        );
    }

    public static Agent RandomAgent() {
        Point2D randomPoint = new Point2D.Double(
                Configuration.getParameter(RANDOM, Random.class).nextDouble() * 2000.0 - 1000.0,
                Configuration.getParameter(RANDOM, Random.class).nextDouble() * 2000.0 - 1000.0
        );

        return new DesignerAgent(
                //position
                randomPoint,
                //orientation
                Configuration.getParameter(RANDOM, Random.class).nextDouble() * (2 * Math.PI),
                //curvature
                Configuration.getParameter(RANDOM, Random.class).nextDouble(),
                //speed
                Configuration.getParameter(RANDOM, Random.class).nextDouble() * 25.0,
                //mortality
                Configuration.getParameter(RANDOM, Random.class).nextDouble(),
                //fecundity
                Configuration.getParameter(RANDOM, Random.class).nextDouble(),
                //mutation
                Configuration.getParameter(RANDOM, Random.class).nextDouble(),
                //behaviour manager
                Configuration.getParameter(AGENT_DEFAULT_BEHAVIOUR_MANAGER, BehaviorManager.class)
        );
    }
}
