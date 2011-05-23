package org.blackpanther.ecosystem.behaviour;

import org.blackpanther.ecosystem.*;
import org.blackpanther.ecosystem.agent.Agent;
import org.blackpanther.ecosystem.agent.Creature;

import java.awt.geom.Point2D;
import java.util.Collection;
import java.util.Iterator;

import static java.lang.Math.PI;
import static org.blackpanther.ecosystem.agent.Agent.CREATURE_FLEE;
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
    public void update(Environment env, Creature monster) {
        //possible that a predator previously ate us, sounds weird....
        if (monster.isAlive())
            super.update(env, monster);
    }

    @Override
    protected void react(Environment env, Creature that, SenseResult analysis) {
        SensorTarget<Creature> closestPredator =
                getClosestPredator(that.getLocation(), analysis.getNearCreatures());

        //run away closest predator
        if (closestPredator != null) {
            double flee = that.getGene(CREATURE_FLEE, Double.class);
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

    private SensorTarget<Creature> getClosestPredator(Point2D source, Collection<SensorTarget<Creature>> agents) {
        Iterator<SensorTarget<Creature>> it = agents.iterator();
        SensorTarget<Creature> closest = null;
        double closestDistance = Double.MAX_VALUE;
        while (it.hasNext()) {
            SensorTarget<Creature> monster = it.next();

            //detect only predators
            if (PredatorBehaviour.class.isInstance(
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
