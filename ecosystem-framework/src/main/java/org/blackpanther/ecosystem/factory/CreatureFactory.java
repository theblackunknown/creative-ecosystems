package org.blackpanther.ecosystem.factory;

import org.blackpanther.ecosystem.factory.fields.FieldsConfiguration;

/**
 * @author MACHIZAUD Andréa
 * @version 5/21/11
 */
public class CreatureFactory
    extends EnvironmentFactory<Creature> {

    @Override
    public Creature createAgent(FieldsConfiguration config) {
        return new Creature(
            config
        );
    }
}
