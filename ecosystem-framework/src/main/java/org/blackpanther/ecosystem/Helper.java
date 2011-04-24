package org.blackpanther.ecosystem;

/**
 * Tools method to help to design others classes
 *
 * @author MACHIZAUD Andréa
 * @version 0.2 - Sun Apr 24 02:41:42 CEST 2011
 */
final class Helper {

    /**
     * Check if given predicate is verified
     * @param predicate - predicate which must be verified
     * @param errorMessage - error message to display if predicate is not verified
     * @throws IllegalArgumentException if the given predicate is not true
     */
    static void require(boolean predicate, String errorMessage) {
        if (!predicate)
            throw new IllegalArgumentException("Condition unsastified" +
                    (
                            (errorMessage != null && !errorMessage.equals("")) ?
                                    " : " + errorMessage
                                    : "")
            );
    }

    /**
     * Check if given predicate is verified with no error message
     * @param predicate - predicate which must be verified
     * @throws IllegalArgumentException if the given predicate is not true
     */
    static void require(boolean predicate) {
        require(predicate, null);
    }
}
