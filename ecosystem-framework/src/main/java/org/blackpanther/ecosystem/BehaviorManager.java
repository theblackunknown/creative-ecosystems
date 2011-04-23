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
 * @version ${{version}} - ${{date}}
 */
public interface BehaviorManager {

    /**
     * Update agent state in given {@link Environment}
     * @param env - environment in which the agent evolves
     */
    public void update(Environment env, Agent agent);

}
