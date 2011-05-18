package org.blackpanther.ecosystem;

import java.io.Serializable;

/**
 * <p>
 * Strategy Pattern Design.
 * Agent's behavior delegate operations to this interface
 * to update at each cycle, and to interact with the {@link Environment}
 * and thus other {@link Agent}
 * </p>
 *
 * @author MACHIZAUD Andréa
 * @version 1.1-alpha - Thu May 19 01:22:54 CEST 2011
 */
public interface BehaviorManager
        extends Serializable {

    /**
     * Update agent state in given {@link Environment}
     *
     * @param env  environment in which the agent evolves
     * @param that monitored agent
     */
    void update(Environment env, Agent that);

}
