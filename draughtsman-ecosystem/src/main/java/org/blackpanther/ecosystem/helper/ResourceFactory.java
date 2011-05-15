package org.blackpanther.ecosystem.helper;

import org.blackpanther.ecosystem.Resource;

import java.util.Random;

import static org.blackpanther.ecosystem.Configuration.*;

/**
 * @author MACHIZAUD Andréa
 * @version 5/14/11
 */
public class ResourceFactory {

    public static Resource StandardResource(double abscissa, double ordinate) {
        return new Resource(
                abscissa, ordinate,
                Configuration.getParameter(RESOURCE_AMOUNT, Double.class)
        );
    }

    public static Resource RandomResource() {
        return new Resource(
                Configuration.getParameter(RANDOM, Random.class).nextDouble()
                        * Configuration.getParameter(SPACE_WIDTH, Double.class)
                        - (Configuration.getParameter(SPACE_WIDTH, Double.class) / 2.0),
                Configuration.getParameter(RANDOM, Random.class).nextDouble()
                        * Configuration.getParameter(SPACE_HEIGHT, Double.class)
                        - (Configuration.getParameter(SPACE_HEIGHT, Double.class) / 2.0),
                Configuration.getParameter(RANDOM, Random.class).nextDouble()
                        * Configuration.getParameter(RESOURCE_AMOUNT_THRESHOLD, Double.class)
        );
    }
}
