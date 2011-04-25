package org.blackpanther.ecosystem.helper;

import org.blackpanther.ecosystem.BasicCase;
import org.blackpanther.ecosystem.Environment;

/**
 * @author MACHIZAUD Andréa
 * @version 1.0 - 4/25/11
 */
public final class CaseFactory {

    public static Environment.Case Case(
            final int abscissa,
            final int ordinate
    ) {
        return new BasicCase(abscissa, ordinate);
    }
}
