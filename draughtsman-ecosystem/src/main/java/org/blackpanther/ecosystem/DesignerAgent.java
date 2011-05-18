package org.blackpanther.ecosystem;

import java.awt.*;
import java.awt.geom.Point2D;

/**
 * @author MACHIZAUD Andréa
 * @version 1.0-alpha - Wed May 18 02:01:08 CEST 2011
 */
public class DesignerAgent extends Agent {

    public DesignerAgent(
            Color identifier,
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
        super(identifier, spawnLocation, energyAmount, movementCost, fecundationCost,
                fecundationLoss, orientation, launchOrientation, curvature,
                speed, launchSpeed, greed, sensorRadius, irrationality, mortality,
                fecundity, mutation, manager);
    }
}
