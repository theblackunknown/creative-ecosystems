package org.blackpanther.ecosystem.factory.generator.random;

import org.blackpanther.ecosystem.factory.generator.RandomProvider;

import java.awt.geom.Point2D;

import static org.blackpanther.ecosystem.Configuration.*;

/**
 * @author MACHIZAUD Andréa
 * @version 1.0-alpha - Tue May 24 23:49:57 CEST 2011
 */
public class LocationProvider
    extends RandomProvider<Point2D>{

    public LocationProvider(){}

    private static class LocationProviderHolder {
        private static final LocationProvider instance =
            new LocationProvider();
    }

    public static LocationProvider getInstance(){
        return LocationProviderHolder.instance;
    }

    @Override
    public Point2D getValue() {
        return new Point2D.Double(
                Configuration.getRandom().nextDouble()
                        * Configuration.getParameter(SPACE_WIDTH, Double.class)
                        - (Configuration.getParameter(SPACE_WIDTH, Double.class) / 2.0),
                Configuration.getRandom().nextDouble()
                        * Configuration.getParameter(SPACE_HEIGHT, Double.class)
                        - (Configuration.getParameter(SPACE_HEIGHT, Double.class) / 2.0)
        );
    }
}
