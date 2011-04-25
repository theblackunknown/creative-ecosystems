package org.blackpanther.ecosystem.helper;

import org.blackpanther.ecosystem.Agent;
import org.blackpanther.ecosystem.BehaviorManager;
import org.blackpanther.ecosystem.DesignerAgent;
import org.blackpanther.ecosystem.math.Geometry;

import static org.blackpanther.ecosystem.Configuration.*;

/**
 * @author MACHIZAUD Andréa
 * @version 1.0 - 4/25/11
 */
public final class AgentFactory {

    public static Agent Agent() {
        return new DesignerAgent(
                Configuration.getParameter(AGENT_DIRECTIONAL_VECTOR, Geometry.Vector2D.class),
                Configuration.getParameter(AGENT_SPEED, Double.class),
                Configuration.getParameter(AGENT_MORTALITY, Double.class),
                Configuration.getParameter(AGENT_FECUNDITY, Double.class),
                Configuration.getParameter(AGENT_MUTATION, Double.class),
                Configuration.getParameter(AGENT_DEFAULT_BEHAVIOUR_MANAGER, BehaviorManager.class)
        );
    }
}
