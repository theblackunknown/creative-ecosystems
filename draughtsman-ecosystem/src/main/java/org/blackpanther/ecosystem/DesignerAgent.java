package org.blackpanther.ecosystem;

import org.blackpanther.ecosystem.math.Geometry;

/**
 * @author MACHIZAUD Andréa
 * @version 1.0 - 4/24/11
 */
public class DesignerAgent extends Agent {

    /**
     * {@inheritDoc}
     */
    public DesignerAgent(
            final Geometry.Vector2D initialDirection,
            final double initialSpeed,
            final double initialMortality,
            final double initialFecundity,
            final double initialMutation,
            final BehaviorManager manager) {
        super(initialDirection, initialSpeed, initialMortality, initialFecundity, initialMutation, manager);
    }

}
