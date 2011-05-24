package org.blackpanther.ecosystem.factory;

import org.blackpanther.ecosystem.agent.Agent;
import org.blackpanther.ecosystem.agent.Creature;
import org.blackpanther.ecosystem.agent.Resource;

import java.util.HashMap;
import java.util.Map;

/**
 * @author MACHIZAUD Andréa
 * @version 1.0-alpha - Tue May 24 23:49:57 CEST 2011
 */
public class EnvironmentAbstractFactory {

    private EnvironmentAbstractFactory() {
    }

    private static final Map<Class, EnvironmentFactory> factoryRegister = new HashMap<Class, EnvironmentFactory>(2) {{
        put(Creature.class, new CreatureFactory());
        put(Resource.class, new ResourceFactory());
    }};

    @SuppressWarnings("unchecked")
    public static <T extends Agent> EnvironmentFactory<T> getFactory(Class<T> agentType) {
        return factoryRegister.get(agentType);
    }

}
