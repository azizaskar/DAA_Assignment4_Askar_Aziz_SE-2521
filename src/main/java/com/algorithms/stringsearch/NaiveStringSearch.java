package com.algorithms.stringsearch;

import java.util.ArrayList;
import java.util.List;

/**
 * Naive String Search algorithm.
 * Checks all possible starting positions i in text T, comparing character by character.
 */
public class NaiveStringSearch implements StringSearch {

    private long comparisons = 0;

    @Override
    public List<Integer> search(String text, String pattern) {
        if (pattern == null || pattern.isEmpty()) {
            throw new IllegalArgumentException("Pattern cannot be null or empty.");
        }
        if (text == null) {
            text = "";
        }

        List<Integer> occurrences = new ArrayList<>();
        int n = text.length();
        int m = pattern.length();

        // If pattern is longer than text, it cannot appear
        if (m > n) {
            return occurrences;
        }

        // Test every starting index in text
        for (int i = 0; i <= n - m; i++) {
            boolean match = true;
            for (int j = 0; j < m; j++) {
                comparisons++;
                if (text.charAt(i + j) != pattern.charAt(j)) {
                    match = false;
                    break;
                }
            }
            if (match) {
                occurrences.add(i);
            }
        }

        return occurrences;
    }

    @Override
    public long getCounter() {
        return comparisons;
    }

    @Override
    public void resetCounter() {
        comparisons = 0;
    }

    @Override
    public String getName() {
        return "Naive";
    }
}
