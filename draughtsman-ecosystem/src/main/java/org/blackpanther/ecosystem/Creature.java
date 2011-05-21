package org.blackpanther.ecosystem;

import java.awt.*;
import java.awt.geom.Point2D;

/**
 * @author MACHIZAUD Andréa
 * @version 1.1-alpha - Thu May 19 01:22:54 CEST 2011
 */
public class Creature extends Agent {

    public Creature(
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
