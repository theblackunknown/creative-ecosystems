import org.blackpanther.ecosystem.Agent;
import org.blackpanther.ecosystem.BehaviorManager;

import java.awt.*;
import java.awt.geom.Point2D;

/**
 * @author MACHIZAUD Andréa
 * @version 5/21/11
 */
public class Creature extends Agent {

    /**
     * Create an agent
     *
     * @param spawnLocation     initial location of the agent in the environment.
     * @param orientation       initial movement orientation
     * @param launchOrientation orientation initial value for next generation from this parent
     * @param curvature         initial curvature
     * @param speed             initial speed
     * @param launchSpeed       orientation initial value for next generation from this parent
     * @param mortality         initial mortality rate
     * @param fecundity         initial fecundity rate
     * @param mutation          initial mutation rate
     * @param manager           Behaviour Strategy to use for this agent
     * @see java.awt.geom.Point2D.Double
     * @see org.blackpanther.ecosystem.BehaviorManager
     */
    public Creature(
            final Color identifier,
            final Point2D spawnLocation,
            final double energyAmount,
            final double movementCost,
            final double fecundationCost,
            final double fecundationLoss,
            final double orientation, final double launchOrientation, final double curvature, final double speed, final double launchSpeed, final double greed, final double sensorRadius, final double irrationality, final double mortality, final double fecundity, final double mutation, final BehaviorManager manager) {
        super(identifier, spawnLocation, energyAmount, movementCost, fecundationCost, fecundationLoss, orientation, launchOrientation, curvature, speed, launchSpeed, greed, sensorRadius, irrationality, mortality, fecundity, mutation, manager);
    }
}
