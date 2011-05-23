package org.blackpanther.ecosystem.factory;

import org.blackpanther.ecosystem.agent.Agent;
import org.blackpanther.ecosystem.agent.Creature;
import org.blackpanther.ecosystem.factory.fields.FieldsConfiguration;
import org.blackpanther.ecosystem.factory.fields.StateFieldMould;

import java.awt.geom.Dimension2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Collection;

import static org.blackpanther.ecosystem.Configuration.*;
import static org.blackpanther.ecosystem.agent.AgentConstants.AGENT_LOCATION;
import static org.blackpanther.ecosystem.agent.CreatureConstants.CREATURE_ORIENTATION;
import static org.blackpanther.ecosystem.factory.generator.StandardProvider.StandardProvider;

/**
 * @author MACHIZAUD Andréa
 * @version 22/05/11
 */
public class PopulationFactory {
    private static final java.util.logging.Logger logger =
            java.util.logging.Logger.getLogger(PopulationFactory.class.getCanonicalName());

    @SuppressWarnings("unchecked")
    public static <T extends Agent> Collection<T> generatePool(
            Class<T> species,
            FieldsConfiguration agentConfiguration,
            Integer agentNumber) {
        Collection<T> pool = new ArrayList<T>(agentNumber);
        for (long number = agentNumber; number-- > 0; ) {
            //update location
            agentConfiguration.updateMould(new StateFieldMould(
                    AGENT_LOCATION,
                    StandardProvider(new Point2D.Double(
                            Configuration.getParameter(SPACE_WIDTH, Double.class) * Configuration.getRandom().nextDouble()
                                    - Configuration.getParameter(SPACE_WIDTH, Double.class) / 2.0,
                            Configuration.getParameter(SPACE_HEIGHT, Double.class) * Configuration.getRandom().nextDouble()
                                    - Configuration.getParameter(SPACE_HEIGHT, Double.class) / 2.0
                    ))
            ));
            //add specific agent in the pool
            pool.add(EnvironmentAbstractFactory
                    .getFactory(species)
                    .createAgent(agentConfiguration));
        }
        return pool;
    }

    @SuppressWarnings("unchecked")
    public static <T extends Agent> Collection<T> generateGridPool(
            Class<T> species,
            FieldsConfiguration agentConfiguration,
            Dimension2D gridDimension,
            double step) {
        Collection<T> pool = new ArrayList<T>(
                (int) ((gridDimension.getHeight() * gridDimension.getWidth()) / step));
        double widthBy2 = gridDimension.getWidth() / 2.0;
        double heightBy2 = gridDimension.getHeight() / 2.0;
        for (double abscissa = -widthBy2;
             abscissa < widthBy2;
             abscissa += step) {
            for (double ordinate = -heightBy2;
                 ordinate < heightBy2;
                 ordinate += step) {
                //update location
                agentConfiguration.updateMould(new StateFieldMould(
                        AGENT_LOCATION,
                        StandardProvider(new Point2D.Double(abscissa, ordinate))
                ));
                //add specific agent in the pool
                pool.add(species.cast(
                        EnvironmentAbstractFactory
                                .getFactory(species)
                                .createAgent(agentConfiguration)));
            }
        }
        return pool;
    }

    @SuppressWarnings("unchecked")
    public static <T extends Agent> Collection<T> generateCirclePool(
            Class<T> species,
            FieldsConfiguration agentConfiguration,
            Integer agentNumber,
            Double circleRadius) {
        Collection<T> pool = new ArrayList<T>(agentNumber);
        double incrementation = (2.0 * Math.PI) / agentNumber;
        double orientation = 0.0;
        for (long number = agentNumber; number-- > 0; orientation += incrementation) {
            //update location
            agentConfiguration.updateMould(new StateFieldMould(
                    AGENT_LOCATION,
                    StandardProvider(new Point2D.Double(
                            circleRadius * Math.cos(orientation),
                            circleRadius * Math.sin(orientation)))
            ));
            //add specific agent in the pool
            pool.add(EnvironmentAbstractFactory
                    .getFactory(species)
                    .createAgent(agentConfiguration));
        }
        return pool;
    }

    @SuppressWarnings("unchecked")
    public static Collection<Creature> generateCreatureCirclePool(
            FieldsConfiguration agentConfiguration,
            Integer agentNumber,
            Double circleRadius) {
        Collection<Creature> pool = new ArrayList<Creature>(agentNumber);
        double incrementation = (2.0 * Math.PI) / agentNumber;
        double orientation = 0.0;
        for (long number = agentNumber; number-- > 0; orientation += incrementation) {
            //update location
            agentConfiguration.updateMould(new StateFieldMould(
                    AGENT_LOCATION,
                    StandardProvider(new Point2D.Double(
                            circleRadius * Math.cos(orientation),
                            circleRadius * Math.sin(orientation)))
            ));
            //update orientation
            agentConfiguration.updateMould(new StateFieldMould(
                    CREATURE_ORIENTATION,
                    StandardProvider(orientation)
            ));
            //add specific agent in the pool
            pool.add(EnvironmentAbstractFactory
                    .getFactory(Creature.class)
                    .createAgent(agentConfiguration));
        }
        return pool;
    }

}
