package org.blackpanther.ecosystem;

/**
 * @author MACHIZAUD Andréa
 * @version 1.0-alpha - Wed May 18 02:01:08 CEST 2011
 */
public class DesignEnvironment extends Environment {

    public DesignEnvironment(Double width, Double height) {
        super(width, height);    //To change body of overridden methods use File | Settings | File Templates.
    }

    @Override
    protected void initializeSpace() {
        //Nothing special about space initialization
    }
}
