package org.blackpanther.ecosystem;

/**
 * Tools method to help to design others classes
 *
 * @author MACHIZAUD Andréa
 * @version 0.3 - Sun May  1 00:00:13 CEST 2011
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

    /**
     * check if a user input is valid or not,
     * based on non-null and non-empty input
     *
     * @param input user input
     * @return <code>true</code> if the given input is valid, <code>false</code> otherwise
     */
    static boolean isValid(String input) {
        return input != null && !input.trim().equals("");
    }
}
