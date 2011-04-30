package org.blackpanther.ecosystem;

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
 * @version v0.2.1 - Sun Apr 24 18:01:06 CEST 2011
 */
public interface AreaListener {

    boolean trace(Line2D to);
}
