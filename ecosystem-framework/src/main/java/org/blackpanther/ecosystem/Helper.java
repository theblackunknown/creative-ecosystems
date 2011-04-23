package org.blackpanther.ecosystem;

/**
 * Tools method to help to design others classes
 *
 * @author MACHIZAUD Andréa
 * @version ${{version}} - ${{date}}
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
