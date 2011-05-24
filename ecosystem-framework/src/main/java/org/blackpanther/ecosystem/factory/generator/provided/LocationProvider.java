package org.blackpanther.ecosystem.factory.generator.provided;

import org.blackpanther.ecosystem.factory.generator.StandardProvider;

import java.awt.geom.Point2D;

/**
 * @author MACHIZAUD Andréa
 * @version 1.0-alpha - Tue May 24 23:49:58 CEST 2011
 */
public class LocationProvider
    extends StandardProvider<Point2D>{

    public LocationProvider(Point2D provided) {
        super(provided);
    }
}
