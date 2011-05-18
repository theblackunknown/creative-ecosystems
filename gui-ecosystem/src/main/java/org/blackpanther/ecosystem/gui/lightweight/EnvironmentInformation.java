package org.blackpanther.ecosystem.gui.lightweight;

import org.blackpanther.ecosystem.Environment;

/**
 * @author MACHIZAUD Andréa
 * @version 1.0-alpha - Wed May 18 02:01:10 CEST 2011
 */
public class EnvironmentInformation {

    public static EnvironmentInformation dump(Environment env, State state) {
        return new EnvironmentInformation(
                env.getId(),
                state,
                env.getPool().size(),
                env.getTime()
        );
    }

    private long id;

    public enum State {
        NOT_YET_STARTED("[NOT STARTED]"),
        RUNNING("[RUNNING]"),
        PAUSED("[PAUSED]"),
        FROZEN("[FROZEN]");

        private String representation;

        State(String s) {
            this.representation = s;
        }

        @Override
        public String toString() {
            return representation;
        }
    }

    private State state;
    private Integer poolSize;
    private Integer generationCounter;

    private EnvironmentInformation(long id, State state, Integer poolSize, Integer generationCounter) {
        this.id = id;
        this.state = state;
        this.poolSize = poolSize;
        this.generationCounter = generationCounter;
    }

    public long getId() {
        return id;
    }

    public State getState() {
        return state;
    }

    public Integer getPoolSize() {
        return poolSize;
    }

    public Integer getGenerationCounter() {
        return generationCounter;
    }
}
