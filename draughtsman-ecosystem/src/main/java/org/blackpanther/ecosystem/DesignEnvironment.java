package org.blackpanther.ecosystem;

/**
 * @author MACHIZAUD Andréa
 * @version 1.0 - 4/24/11
 */
public class DesignEnvironment extends Environment {

    /**
     * {@inheritDoc}
     */
    public DesignEnvironment(final int width, final int height) {
        super(width, height);
    }

    @Override
    protected void initializeSpace() {
        //Nothing special about space initialization
    }

    @Override
    public void endThisWorldHook() {
        //Nothing special to do when this world ends
    }

}
