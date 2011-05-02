package org.blackpanther.ecosystem.helper;

import org.blackpanther.ecosystem.Agent;
import org.blackpanther.ecosystem.BehaviorManager;
import org.blackpanther.ecosystem.DesignerAgent;
import org.blackpanther.ecosystem.math.Geometry;

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

    public static Agent Agent() {
        return new DesignerAgent(
                Configuration.getParameter(AGENT_LOCATION, Point2D.class),
                Configuration.getParameter(AGENT_DIRECTIONAL_VECTOR, Geometry.Direction2D.class),
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
                Configuration.getParameter(RANDOM, Random.class).nextDouble() * 200.0,
                Configuration.getParameter(RANDOM, Random.class).nextDouble() * 200.0
        );

        Geometry.Direction2D randomDirection = new Geometry.Direction2D(
                Configuration.getParameter(RANDOM, Random.class).nextDouble(),
                Configuration.getParameter(RANDOM, Random.class).nextDouble()
        );

        return new DesignerAgent(
                randomPoint,
                randomDirection,
                Configuration.getParameter(RANDOM, Random.class).nextDouble(),
                Configuration.getParameter(RANDOM, Random.class).nextDouble() * 3.0,
                Configuration.getParameter(RANDOM, Random.class).nextDouble(),
                Configuration.getParameter(RANDOM, Random.class).nextDouble() * 0.3,
                Configuration.getParameter(RANDOM, Random.class).nextDouble(),
                Configuration.getParameter(AGENT_DEFAULT_BEHAVIOUR_MANAGER, BehaviorManager.class)
        );
    }
}
