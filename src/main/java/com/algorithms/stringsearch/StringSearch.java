package com.algorithms.stringsearch;

import java.util.List;

/**
 * Common interface for String Search (Pattern Matching) algorithms.
 * Searches for all occurrences of pattern P in text T, including overlaps.
 */
public interface StringSearch {
    /**
     * Finds all starting positions of pattern in text in ascending order.
     *
     * @param text the source text T
     * @param pattern the search pattern P
     * @return list of 0-based start indices
     * @throws IllegalArgumentException if pattern is empty
     */
    List<Integer> search(String text, String pattern);

    /**
     * Returns the internal character comparison counter.
     *
     * @return comparison count
     */
    long getCounter();

    /**
     * Resets the comparison counter.
     */
    void resetCounter();

    /**
     * Returns the algorithm's descriptive name.
     *
     * @return algorithm name
     */
    String getName();
}
