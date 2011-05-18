package org.blackpanther.ecosystem;

/**
 * @author MACHIZAUD Andréa
 * @version 1.1-alpha - Thu May 19 01:22:54 CEST 2011
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
