package org.blackpanther.ecosystem.factory.generator.random;

import org.blackpanther.ecosystem.factory.generator.RandomProvider;

import java.awt.geom.Point2D;
import java.util.Random;

import static org.blackpanther.ecosystem.Configuration.*;
import static org.blackpanther.ecosystem.Configuration.Configuration;
import static org.blackpanther.ecosystem.Configuration.SPACE_HEIGHT;

/**
 * @author MACHIZAUD Andréa
 * @version 5/22/11
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
                Configuration.getParameter(RANDOM, Random.class).nextDouble()
                        * Configuration.getParameter(SPACE_WIDTH, Double.class)
                        - (Configuration.getParameter(SPACE_WIDTH, Double.class) / 2.0),
                Configuration.getParameter(RANDOM, Random.class).nextDouble()
                        * Configuration.getParameter(SPACE_HEIGHT, Double.class)
                        - (Configuration.getParameter(SPACE_HEIGHT, Double.class) / 2.0)
        );
    }
}
