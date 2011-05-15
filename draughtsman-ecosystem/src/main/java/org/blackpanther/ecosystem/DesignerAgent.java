package org.blackpanther.ecosystem;

import java.awt.geom.Point2D;

/**
 * @author MACHIZAUD Andréa
 * @version 0.2 - Wed May 11 02:54:46 CEST 2011
 */
public class DesignerAgent extends Agent {

    public DesignerAgent(
            Point2D spawnLocation,
            double energyAmount,
            double movementCost,
            double fecundationCost,
            double fecundationLoss,
            double orientation,
            double launchOrientation,
            double curvature,
            double speed,
            double launchSpeed,
            double greed,
            double sensorRadius,
            double irrationality,
            double mortality,
            double fecundity,
            double mutation,
            BehaviorManager manager) {
        super(spawnLocation, energyAmount, movementCost, fecundationCost,
                fecundationLoss, orientation, launchOrientation, curvature,
                speed, launchSpeed, greed, sensorRadius, irrationality, mortality,
                fecundity, mutation, manager);
    }
}
