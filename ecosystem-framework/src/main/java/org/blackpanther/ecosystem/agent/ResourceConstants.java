package org.blackpanther.ecosystem.agent;

/**
 * @author MACHIZAUD Andréa
 * @version 5/27/11
 */
public interface ResourceConstants
    extends AgentConstants{

    public static final String RESOURCE_ENERGY = "resource-energy";

    public static final String RESOURCE_NATURAL_COLOR = "resource-natural-color";

    public static final String[] RESOURCE_STATE = new String[]{
            RESOURCE_ENERGY,
            AGENT_LOCATION
    };

    public static final String[] RESOURCE_GENOTYPE = new String[]{
            RESOURCE_NATURAL_COLOR
    };
    public static final String[] BUILD_PROVIDED_RESOURCE_STATE = new String[]{
            RESOURCE_ENERGY,
            AGENT_LOCATION,
    };

    public static final String[] CUSTOMIZABLE_RESOURCE_STATE = new String[]{
            RESOURCE_ENERGY,
    };
}
