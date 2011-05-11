package org.blackpanther.ecosystem.placement;

import org.blackpanther.ecosystem.Agent;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;

import static org.blackpanther.ecosystem.Configuration.*;
import static org.blackpanther.ecosystem.helper.AgentFactory.RandomAgent;
import static org.blackpanther.ecosystem.helper.AgentFactory.StandardAgent;


/**
 * @author MACHIZAUD Andréa
 * @version 0.2 - Wed May 11 02:54:45 CEST 2011
 */
public class Strategy {

    public enum GenerationType {
        STANDARD_POSITION_PROVIDED,
        STANDARD_POSITION_RANDOMIZED,
        STANDARD_CIRCLE,
        RANDOM;
    }

    public static Collection<Agent> generatePopulation(
            GenerationType genType,
            Object... additionalParameters) {
        List<Agent> pool = new ArrayList<Agent>();
        switch (genType) {
            case STANDARD_POSITION_PROVIDED: {
                Double XLimit = (Double) additionalParameters[0];
                Double YLimit = (Double) additionalParameters[1];
                Double step = (Double) additionalParameters[2];

                for (double i = -XLimit; i < XLimit; i += step) {
                    for (double j = -YLimit; j < YLimit; j += step) {
                        pool.add(
                                StandardAgent(i, j)
                        );
                    }
                }
                break;
            }
            case STANDARD_POSITION_RANDOMIZED: {
                Long numberOfAgent = (Long) additionalParameters[0];
                for (long number = numberOfAgent; number-- > 0;) {
                    Point2D randomPoint = new Point2D.Double(
                            (Configuration.getParameter(RANDOM, Random.class).nextDouble()
                                    * Configuration.getParameter(SPAWN_ABSCISSA_THRESHOLD, Double.class) * 2)
                                    - Configuration.getParameter(SPAWN_ABSCISSA_THRESHOLD, Double.class),
                            (Configuration.getParameter(RANDOM, Random.class).nextDouble()
                                    * Configuration.getParameter(SPAWN_ORDINATE_THRESHOLD, Double.class) * 2)
                                    - Configuration.getParameter(SPAWN_ORDINATE_THRESHOLD, Double.class)
                    );
                    pool.add(
                            StandardAgent(randomPoint.getX(), randomPoint.getY())
                    );
                }
                break;
            }
            case STANDARD_CIRCLE: {
                Long numberOfAgent = (Long) additionalParameters[0];
                Double rayon = (Double) additionalParameters[1];
                double incrementation = (2.0 * Math.PI) / numberOfAgent;
                double orientation = 0.0;
                for (long number = numberOfAgent; number-- > 0; orientation += incrementation)
                    pool.add(
                            StandardAgent(
                                    rayon * Math.cos(orientation),
                                    rayon * Math.sin(orientation),
                                    orientation
                            )
                    );
                break;
            }
            case RANDOM: {
                Long numberOfAgent = (Long) additionalParameters[0];
                for (long number = numberOfAgent; number-- > 0;)
                    pool.add(
                            RandomAgent()
                    );
                break;
            }
        }
        return pool;
    }
}
