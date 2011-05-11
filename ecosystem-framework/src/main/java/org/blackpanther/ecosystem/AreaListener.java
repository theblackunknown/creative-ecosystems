package org.blackpanther.ecosystem;

import org.blackpanther.ecosystem.math.Geometry;

import java.awt.geom.Line2D;

/**
 * <p>
 * Observer-Listener Pattern.
 * Designed to assign an area listener to a agent,
 * practically a case observes agent on its area
 * and can provide them some information around it.
 * </p>
 *
 * @author MACHIZAUD Andréa
 * @version 0.2 - Wed May 11 02:54:46 CEST 2011
 */
public interface AreaListener {

    boolean trace(Line2D to);

    SenseResult aggregateInformation(Geometry.Circle circle);
}
