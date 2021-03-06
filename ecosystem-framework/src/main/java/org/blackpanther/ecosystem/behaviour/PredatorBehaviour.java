package org.blackpanther.ecosystem.behaviour;

import org.blackpanther.ecosystem.Environment;
import org.blackpanther.ecosystem.SenseResult;
import org.blackpanther.ecosystem.SensorTarget;
import org.blackpanther.ecosystem.agent.Agent;
import org.blackpanther.ecosystem.agent.Creature;

import java.awt.geom.Point2D;
import java.util.Collection;
import java.util.Iterator;

import static java.lang.Math.PI;
import static org.blackpanther.ecosystem.agent.Agent.CREATURE_GREED;
import static org.blackpanther.ecosystem.agent.CreatureConstants.CREATURE_CONSUMMATION_RADIUS;
import static org.blackpanther.ecosystem.math.Geometry.PI_2;

/**
 * @author MACHIZAUD Andréa
 * @version 1.0-alpha - Tue May 24 23:49:57 CEST 2011
 */
public class PredatorBehaviour
        extends DraughtsmanBehaviour {

    private PredatorBehaviour(){}

    private static class PredatorBehaviourHolder {
        private static final PredatorBehaviour instance =
            new PredatorBehaviour();
    }

    public static PredatorBehaviour getInstance(){
        return PredatorBehaviourHolder.instance;
    }

    @Override
    protected void react(Environment env, Creature that, SenseResult analysis) {
        SensorTarget<Creature> closestPrey =
                getClosestPrey(that.getLocation(), analysis.getNearCreatures());

        //run after closest prey
        if (closestPrey != null && closestPrey.getTarget().isAlive()) {

            //check if we can still move and reach the target
            double resourceDistance = that.getLocation().distance(closestPrey.getTarget().getLocation());
            if (resourceDistance < that.getGene(CREATURE_CONSUMMATION_RADIUS, Double.class)) {

                //we eat it
                that.setEnergy(that.getEnergy() + closestPrey.getTarget().getEnergy());
                that.setColor(
                        ( that.getColor().getRed() + closestPrey.getTarget().getColor().getRed() ) / 2,
                        ( that.getColor().getGreen() + closestPrey.getTarget().getColor().getGreen() ) / 2,
                        ( that.getColor().getBlue() + closestPrey.getTarget().getColor().getBlue() ) / 2
                );
                closestPrey.getTarget().detachFromEnvironment(env);

            }

            //otherwise get closer
            else {
                double lust = that.getGene(CREATURE_GREED, Double.class);
                double alpha = (that.getOrientation() % PI_2);
                double beta = closestPrey.getOrientation();
                double resourceRelativeOrientation = (beta - alpha);
                if (resourceRelativeOrientation > PI)
                    resourceRelativeOrientation -= PI_2;
                else if (resourceRelativeOrientation < -PI)
                    resourceRelativeOrientation += PI_2;
                double newOrientation = (alpha + resourceRelativeOrientation * lust) % PI_2;

                that.setOrientation(newOrientation);
            }
        }

        //their only goals is to eat agent, not resources
    }

    private SensorTarget<Creature> getClosestPrey(Point2D source, Collection<SensorTarget<Creature>> agents) {
        Iterator<SensorTarget<Creature>> it = agents.iterator();
        SensorTarget<Creature> closest = null;
        double closestDistance = Double.MAX_VALUE;
        while (it.hasNext()) {
            SensorTarget<Creature> monster = it.next();

            //detect only preys
            if (PreyBehaviour.class.isInstance(
                    monster.getTarget()
                            .getGene(Agent.CREATURE_BEHAVIOR, BehaviorManager.class))) {

                double distance = source.distance(monster.getTarget().getLocation());

                if (closest == null) {
                    closest = monster;
                    closestDistance = distance;
                } else {
                    if (distance < closestDistance) {
                        closest = monster;
                        closestDistance = distance;
                    }
                }
            }
        }

        return closest;
    }
}
