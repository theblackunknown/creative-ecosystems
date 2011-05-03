package org.blackpanther.ecosystem;

import org.blackpanther.ecosystem.math.Geometry;

import java.awt.geom.Point2D;

/**
 * @author MACHIZAUD Andréa
 * @version 0.3 - Sun May  1 00:00:13 CEST 2011
 */
public class DesignerAgent extends Agent {

    /**
     * {@inheritDoc}
     */
    public DesignerAgent(
            final Point2D spawnLocation,
            final Double initialOrientation,
            final double initialCurvature,
            final double initialSpeed,
            final double initialMortality,
            final double initialFecundity,
            final double initialMutation,
            final BehaviorManager manager) {
        super(spawnLocation, initialOrientation, initialCurvature, initialSpeed, initialMortality, initialFecundity, initialMutation, manager);
    }

}
