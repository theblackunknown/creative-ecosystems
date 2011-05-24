package org.blackpanther.ecosystem.factory;

import org.blackpanther.ecosystem.agent.Creature;
import org.blackpanther.ecosystem.factory.fields.FieldsConfiguration;

import java.awt.geom.Point2D;

/**
 * @author MACHIZAUD Andréa
 * @version 1.0-alpha - Tue May 24 23:49:57 CEST 2011
 */
public class CreatureFactory
    extends EnvironmentFactory<Creature> {

    CreatureFactory(){}

    @Override
    public Creature createAgent(FieldsConfiguration config) {
        return new Creature(config);
    }
}
