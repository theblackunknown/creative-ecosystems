package org.blackpanther.ecosystem.behaviour;

import org.blackpanther.ecosystem.*;

import java.awt.geom.Point2D;
import java.util.Collection;
import java.util.Iterator;

import static java.lang.Math.PI;
import static org.blackpanther.ecosystem.Agent.AGENT_FLEE;
import static org.blackpanther.ecosystem.math.Geometry.PI_2;

/**
 * @author MACHIZAUD Andréa
 * @version 19/05/11
 */
public class PreyBehaviour
        extends DraughtsmanBehaviour {

    private PreyBehaviour(){}

    private static class PreyBehaviourHolder {
        private static final PreyBehaviour instance =
            new PreyBehaviour();
    }

    public static PreyBehaviour getInstance(){
        return PreyBehaviourHolder.instance;
    }

    @Override
    public void update(Environment env, Agent agent) {
        //possible that a predator previously ate us, sounds weird....
        if (agent.isAlive())
            super.update(env, agent);
    }

    @Override
    protected void react(Environment env, Agent that, SenseResult analysis) {
        SensorTarget<Agent> closestPredator =
                getClosestPredator(that.getLocation(), analysis.getNearAgents());

        //run away closest predator
        if (closestPredator != null) {
            double flee = that.getGene(AGENT_FLEE, Double.class);
            double alpha = (that.getOrientation() % PI_2);
            double beta = closestPredator.getOrientation();
            if (beta < PI)
                beta += PI;
            else if (beta > PI)
                beta -= PI;
            double resourceRelativeOrientation = (beta - alpha);
            if (resourceRelativeOrientation > PI)
                resourceRelativeOrientation -= PI_2;
            else if (resourceRelativeOrientation < -PI)
                resourceRelativeOrientation += PI_2;
            double newOrientation = (alpha + resourceRelativeOrientation * flee) % PI_2;

            that.setOrientation(newOrientation);

            String format = String.format(
                    "%n (%.2f,%.2f)-(%.2f,%.2f)%n " +
                            "old orientation : %.2fPI%n " +
                            "resource orientation : %.2fPI%n " +
                            "resource relative orientation : %.2fPI%n " +
                            "new orientation : %.2fPI%n ",
                    that.getLocation().getX(), that.getLocation().getY(),
                    closestPredator.getTarget().getLocation().getX(),
                    closestPredator.getTarget().getLocation().getY(),
                    alpha / PI,
                    beta / PI,
                    resourceRelativeOrientation / PI,
                    newOrientation / PI
            );
            System.out.println("Flee : " + format);
        }
        //eat resources if no predator is in sight
        else
            super.react(env, that, analysis);
    }

    private SensorTarget<Agent> getClosestPredator(Point2D source, Collection<SensorTarget<Agent>> agents) {
        Iterator<SensorTarget<Agent>> it = agents.iterator();
        SensorTarget<Agent> closest = null;
        double closestDistance = Double.MAX_VALUE;
        while (it.hasNext()) {
            SensorTarget<Agent> agent = it.next();

            //detect only predators
            if (PredatorBehaviour.class.isInstance(
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
