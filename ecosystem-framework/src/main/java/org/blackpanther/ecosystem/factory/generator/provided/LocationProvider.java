package org.blackpanther.ecosystem.factory.generator.provided;

import org.blackpanther.ecosystem.factory.generator.StandardProvider;

import java.awt.geom.Point2D;

/**
 * @author MACHIZAUD Andréa
 * @version 5/22/11
 */
public class LocationProvider
    extends StandardProvider<Point2D>{

    public LocationProvider(Point2D provided) {
        super(provided);
    }
}
