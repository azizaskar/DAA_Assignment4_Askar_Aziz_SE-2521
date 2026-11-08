package com.algorithms.editdistance;

/**
 * Common interface for Edit Distance algorithms.
 * Measures the minimum number of insertions, deletions, and substitutions
 * required to transform string 'a' into string 'b'.
 */
public interface EditDistance {
    /**
     * Calculates the edit distance between strings a and b.
     *
     * @param a the first string
     * @param b the second string
     * @return the Levenshtein distance
     */
    int distance(String a, String b);

    /**
     * Returns the internal operation/comparison counter for experimental analysis.
     *
     * @return counter value
     */
    long getCounter();

    /**
     * Resets the operation counter to zero.
     */
    void resetCounter();

    /**
     * Returns the algorithm's descriptive name.
     *
     * @return algorithm name
     */
    String getName();
}
