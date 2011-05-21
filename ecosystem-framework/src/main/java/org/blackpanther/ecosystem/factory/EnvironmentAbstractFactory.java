package org.blackpanther.ecosystem.factory;

import org.blackpanther.ecosystem.Resource;

import java.util.HashMap;
import java.util.Map;

/**
 * @author MACHIZAUD Andréa
 * @version 5/21/11
 */
public class EnvironmentAbstractFactory {

    private EnvironmentAbstractFactory() {
    }

    private static final Map<Class, EnvironmentFactory> factoryRegister = new HashMap<Class, EnvironmentFactory>(2) {{
        put(Creature.class, new CreatureFactory());
        put(Resource.class, new ResourceFactory());
    }};

    public static EnvironmentFactory getFactory(Class agentType) {
        return factoryRegister.get(agentType);
    }

}
