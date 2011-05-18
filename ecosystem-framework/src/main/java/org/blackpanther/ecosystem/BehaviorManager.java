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
 * @version 1.0-alpha - Wed May 18 02:01:09 CEST 2011
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
