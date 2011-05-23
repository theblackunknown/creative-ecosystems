package org.blackpanther.ecosystem.factory;

import org.blackpanther.ecosystem.agent.Creature;
import org.blackpanther.ecosystem.factory.fields.FieldsConfiguration;

import java.awt.geom.Point2D;

/**
 * @author MACHIZAUD Andréa
 * @version 5/21/11
 */
public class CreatureFactory
    extends EnvironmentFactory<Creature> {

    CreatureFactory(){}

    @Override
    public Creature createAgent(FieldsConfiguration config) {
        return new Creature(config);
    }
}
