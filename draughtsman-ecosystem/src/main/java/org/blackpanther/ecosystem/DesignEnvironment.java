package org.blackpanther.ecosystem;

import static org.blackpanther.ecosystem.helper.CaseFactory.*;

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
        for (int row = 0; row < space.length; row++) {
            for (int column = 0; column < space[0].length; column++) {
                space[row][column] = Case(row, column);
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    public DesignEnvironment(final int size) {
        super(size);
    }

}
