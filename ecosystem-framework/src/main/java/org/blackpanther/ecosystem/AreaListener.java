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
 * @version 1.0-alpha - Wed May 18 02:01:09 CEST 2011
 */
public interface AreaListener {

    boolean trace(Line2D to);

    SenseResult aggregateInformation(Geometry.Circle circle);
}
