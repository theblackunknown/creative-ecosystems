package org.blackpanther.ecosystem.agent;

/**
 * @author MACHIZAUD Andréa
 * @version 1.0-alpha - Tue May 24 23:49:57 CEST 2011
 */
public interface AgentConstants {

    public static final String AGENT_LOCATION = "agent-location";
    public static final String AGENT_ENERGY = "agent-energy";

    public static final String AGENT_NATURAL_COLOR = "agent-natural-color";

    public static final String[] AGENT_STATE = new String[]{
            AGENT_ENERGY,
            AGENT_LOCATION
    };

    public static final String[] AGENT_GENOTYPE = new String[]{
            AGENT_NATURAL_COLOR
    };
    public static final String[] BUILD_PROVIDED_AGENT_STATE = new String[]{
            AGENT_ENERGY,
            AGENT_LOCATION,
    };

    public static final String[] CUSTOMIZABLE_AGENT_STATE = new String[]{
            AGENT_ENERGY,
    };

}
