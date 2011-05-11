package org.blackpanther.ecosystem;

import java.awt.geom.Point2D;

/**
 * @author MACHIZAUD Andréa
 * @version 0.2 - Wed May 11 02:54:46 CEST 2011
 */
public class DesignerAgent extends Agent {

    public DesignerAgent(
            final Point2D spawnLocation,
            final Double initialOrientation,
            final Double childOrientation,
            final double initialCurvature,
            final double initialSpeed,
            final double childSpeed,
            final double initialIrrationality,
            final double initialMortality,
            final double initialFecundity,
            final double initialMutation,
            final BehaviorManager manager) {
        super(spawnLocation, initialOrientation, childOrientation, initialCurvature, initialSpeed, childSpeed, initialIrrationality, initialMortality, initialFecundity, initialMutation, manager);
    }

}
