package org.blackpanther.ecosystem;

/**
 * @author MACHIZAUD Andréa
 * @version 0.3 - Sun May  1 00:00:13 CEST 2011
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

}
