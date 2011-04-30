package org.blackpanther.ecosystem;

import org.blackpanther.ecosystem.math.Geometry;

import java.awt.geom.Point2D;

/**
 * @author MACHIZAUD Andréa
 * @version 1.0 - 4/24/11
 */
public class DesignerAgent extends Agent {

    /**
     * {@inheritDoc}
     */
    public DesignerAgent(
            final Point2D spawnLocation,
            final Geometry.Direction2D initialDirection,
            final double initialCurvature,
            final double initialSpeed,
            final double initialMortality,
            final double initialFecundity,
            final double initialMutation,
            final BehaviorManager manager) {
        super(spawnLocation, initialDirection, initialCurvature, initialSpeed, initialMortality, initialFecundity, initialMutation, manager);
    }

}
