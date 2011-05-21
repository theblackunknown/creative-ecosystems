package org.blackpanther.ecosystem;

import java.awt.geom.Point2D;
import java.util.Collection;
import java.util.Iterator;

import static java.lang.Math.PI;
import static org.blackpanther.ecosystem.Agent.AGENT_GREED;
import static org.blackpanther.ecosystem.Agent.AGENT_MOVEMENT_COST;
import static org.blackpanther.ecosystem.Configuration.CONSUMMATION_RADIUS;
import static org.blackpanther.ecosystem.Configuration.Configuration;
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

            //check if we can still move and reach the target
            double resourceDistance = that.getLocation().distance(closestPrey.getTarget().getLocation());
            if (that.getEnergy() >=
                    that.getGene(AGENT_MOVEMENT_COST, Double.class) * that.getSpeed()
                    && resourceDistance < Configuration.getParameter(CONSUMMATION_RADIUS, Double.class)) {

                //we eat it
                that.setEnergy(that.getEnergy() + closestPrey.getTarget().getEnergy());
                that.setColor(
                        ( that.getColor().getRed() + closestPrey.getTarget().getColor().getRed() ) / 2,
                        ( that.getColor().getGreen() + closestPrey.getTarget().getColor().getGreen() ) / 2,
                        ( that.getColor().getBlue() + closestPrey.getTarget().getColor().getBlue() ) / 2
                );

                closestPrey.getTarget().detachFromEnvironment();

            }

            //otherwise get closer
            else {
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

                String format = String.format(
                        "%n (%.2f,%.2f)-(%.2f,%.2f)%n " +
                                "old orientation : %.2fPI%n " +
                                "resource orientation : %.2fPI%n " +
                                "resource relative orientation : %.2fPI%n " +
                                "new orientation : %.2fPI%n ",
                        that.getLocation().getX(), that.getLocation().getY(),
                        closestPrey.getTarget().getLocation().getX(),
                        closestPrey.getTarget().getLocation().getY(),
                        alpha / PI,
                        beta / PI,
                        resourceRelativeOrientation / PI,
                        newOrientation / PI
                );
                System.out.println("Greed : " + format);
            }
        }

        //their only goals is to eat agent, not resources
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
