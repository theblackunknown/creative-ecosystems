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
 * @version v0.2.1 - Sun Apr 24 18:01:06 CEST 2011
 */
public interface BehaviorManager {

    /**
     * Update agent state in given {@link Environment}
     *
     * @param env   environment in which the agent evolves
     * @param that  monitored agent
     */
    void update(Environment env, Agent that);

    void move(Agent that);

    void mutate(Environment env, Agent that);

    void spawn(Environment env, Agent that);

}
