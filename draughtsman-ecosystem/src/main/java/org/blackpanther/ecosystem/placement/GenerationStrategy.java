package org.blackpanther.ecosystem.placement;

import org.blackpanther.ecosystem.Agent;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;

import static org.blackpanther.ecosystem.Configuration.*;
import static org.blackpanther.ecosystem.helper.AgentFactory.RandomAgent;
import static org.blackpanther.ecosystem.helper.AgentFactory.StandardAgent;
import static org.blackpanther.ecosystem.helper.ResourceFactory.RandomResource;
import static org.blackpanther.ecosystem.helper.ResourceFactory.StandardResource;


/**
 * @author MACHIZAUD Andréa
 * @version 1.1-alpha - Thu May 19 01:22:54 CEST 2011
 */
public class GenerationStrategy {

    public enum PlacementType {
        NONE,
        STANDARD_POSITION_PROVIDED,
        STANDARD_POSITION_RANDOMIZED,
        STANDARD_CIRCLE,
        RANDOM
    }

    public enum BehaviourType {
        SAME,
        HALF,
        RANDOM
    }

    private enum GenerationItemType {
        STANDARD,
        RANDOM
    }

    private static <T> T generateItem(
            Class<T> itemType,
            GenerationItemType type,
            Object... additionalParameters) {
        switch (type) {
            case STANDARD:
                if (itemType.equals(Agent.class)) {
                    if (additionalParameters.length == 3)
                        return itemType.cast(StandardAgent(
                                (Double) additionalParameters[0],
                                (Double) additionalParameters[1],
                                (Double) additionalParameters[2]
                        ));
                    else
                        return itemType.cast(StandardAgent(
                                (Double) additionalParameters[0],
                                (Double) additionalParameters[1]
                        ));
                } else if (itemType.equals(Resource.class)) {
                    return itemType.cast(StandardResource(
                            (Double) additionalParameters[0],
                            (Double) additionalParameters[1]
                    ));
                }
                break;
            case RANDOM:
                if (itemType.equals(Agent.class)) {
                    return itemType.cast(RandomAgent());
                } else if (itemType.equals(Resource.class)) {
                    return itemType.cast(RandomResource());
                }
                break;
        }
        throw new Error("Unexpected case");
    }

    public static <T> Collection<T> generatePopulation(
            Class<T> itemType,
            PlacementType genType,
            Object... additionalParameters) {
        List<T> pool = new ArrayList<T>();
        switch (genType) {
            case NONE:
                break;
            case STANDARD_POSITION_PROVIDED: {
                Double XLimit = (Double) additionalParameters[0];
                Double YLimit = (Double) additionalParameters[1];
                Double step = (Double) additionalParameters[2];

                for (double i = -XLimit; i < XLimit; i += step) {
                    for (double j = -YLimit; j < YLimit; j += step) {
                        pool.add(
                                generateItem(itemType, GenerationItemType.STANDARD,
                                        i, j)
                        );
                    }
                }
                break;
            }
            case STANDARD_POSITION_RANDOMIZED: {
                Long numberOfAgent = (Long) additionalParameters[0];
                for (long number = numberOfAgent; number-- > 0;) {
                    pool.add(
                            generateItem(itemType, GenerationItemType.STANDARD,
                                    Configuration.getParameter(RANDOM, Random.class).nextDouble()
                                            * Configuration.getParameter(SPACE_WIDTH, Double.class)
                                            - (Configuration.getParameter(SPACE_WIDTH, Double.class) / 2.0),
                                    Configuration.getParameter(RANDOM, Random.class).nextDouble()
                                            * Configuration.getParameter(SPACE_HEIGHT, Double.class)
                                            - (Configuration.getParameter(SPACE_HEIGHT, Double.class) / 2.0)
                            )
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
                            generateItem(itemType, GenerationStrategy.GenerationItemType.STANDARD,
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
                            generateItem(itemType, GenerationStrategy.GenerationItemType.RANDOM)
                    );
                break;
            }
        }
        return pool;
    }
}
