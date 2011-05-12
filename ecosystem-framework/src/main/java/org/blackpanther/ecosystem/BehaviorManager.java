package org.blackpanther.ecosystem;

/**
 * <p>
 * Strategy Pattern Design.
 * Agent's behavior delegate operations to this interface
 * to update at each cycle, and to interact with the {@link Environment}
 * and thus other {@link Agent}
 * </p>
 *
 * @author MACHIZAUD Andréa
 * @version 0.2 - Wed May 11 02:54:46 CEST 2011
 */
public interface    BehaviorManager {

    /**
     * Update agent state in given {@link Environment}
     *
     * @param env   environment in which the agent evolves
     * @param that  monitored agent
     */
    void update(Environment env, Agent that);

}
