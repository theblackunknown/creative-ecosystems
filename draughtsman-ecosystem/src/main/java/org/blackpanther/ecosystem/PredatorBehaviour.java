package org.blackpanther.ecosystem;

import java.awt.geom.Point2D;
import java.util.Collection;
import java.util.Iterator;

import static java.lang.Math.PI;
import static org.blackpanther.ecosystem.Agent.AGENT_GREED;
import static org.blackpanther.ecosystem.math.Geometry.PI_2;

/**
 * @author MACHIZAUD Andréa
 * @version 19/05/11
 */
public class PredatorBehaviour
        extends DraughtsmanBehaviour {

    @Override
    protected void react(Environment env, Agent that, SenseResult analysis) {
        SensorTarget<Agent> closestPrey =
                getClosestPrey(that.getLocation(), analysis.getNearAgents());

        //run after closest prey
        if (closestPrey != null) {
                double lust = that.getGene(AGENT_GREED, Double.class);
                double alpha = (that.getOrientation() % PI_2);
                double beta = closestPrey.getOrientation();
                double resourceRelativeOrientation = (beta - alpha);
                if (resourceRelativeOrientation > PI)
                    resourceRelativeOrientation -= PI_2;
                else if (resourceRelativeOrientation < -PI)
                    resourceRelativeOrientation += PI_2;
                double newOrientation = (alpha + resourceRelativeOrientation * lust) % PI_2;

                that.setOrientation(newOrientation);
        } else
            super.react(env, that, analysis);
    }

    private SensorTarget<Agent> getClosestPrey(Point2D source, Collection<SensorTarget<Agent>> agents) {
        Iterator<SensorTarget<Agent>> it = agents.iterator();
        SensorTarget<Agent> closest = null;
        double closestDistance = Double.MAX_VALUE;
        while (it.hasNext()) {
            SensorTarget<Agent> agent = it.next();

            //detect only preys
            if (PreyBehaviour.class.isInstance(
                    agent.getTarget()
                            .getGene(Agent.AGENT_BEHAVIOUR, BehaviorManager.class))) {

                double distance = source.distance(agent.getTarget().getLocation());

                if (closest == null) {
                    closest = agent;
                    closestDistance = distance;
                } else {
                    if (distance < closestDistance) {
                        closest = agent;
                        closestDistance = distance;
                    }
                }
            }
        }

        return closest;
    }
}
