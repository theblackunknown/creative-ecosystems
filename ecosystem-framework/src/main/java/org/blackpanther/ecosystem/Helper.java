package org.blackpanther.ecosystem;

/**
 * Tools method to help to design others classes
 *
 * @author MACHIZAUD Andréa
 * @version v0.2.1 - Sun Apr 24 18:01:06 CEST 2011
 */
final class Helper {

    private Helper() {
    }

    /**
     * Check if given predicate is verified
     *
     * @param predicate    predicate which must be verified
     * @param errorMessage error message to display if predicate is not verified
     * @throws IllegalArgumentException if the given predicate is not satisfied
     */
    static void require(
            final boolean predicate,
            final String errorMessage) {
        if (!predicate) {
            throw new IllegalArgumentException("Condition unsastified"
                    + ((errorMessage != null && !errorMessage.equals(""))
                    ? " : " + errorMessage
                    : "")
            );
        }
    }

    /**
     * Check if given predicate is verified with no error message
     *
     * @param predicate predicate which must be verified
     * @throws IllegalArgumentException if the given predicate is not satisfied
     */
    static void require(
            final boolean predicate) {
        require(predicate, null);
    }
}
