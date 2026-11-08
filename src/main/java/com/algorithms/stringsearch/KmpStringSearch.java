package com.algorithms.stringsearch;

import java.util.ArrayList;
import java.util.List;

/**
 * Knuth-Morris-Pratt (KMP) String Search algorithm.
 * Precomputes the Longest Proper Prefix that is also a Suffix (LPS) array,
 * allowing linear-time search O(n + m) without moving backwards in text T.
 */
public class KmpStringSearch implements StringSearch {

    private long comparisons = 0;

    /**
     * Builds the Longest Prefix Suffix (LPS) array for pattern p.
     * Must be public as specified in assignment requirements.
     *
     * @param p the search pattern
     * @return LPS integer array where lps[i] is length of longest proper prefix of p[0..i] that is also a suffix of p[0..i].
     */
    public static int[] lps(String p) {
        if (p == null || p.isEmpty()) {
            return new int[0];
        }

        int m = p.length();
        int[] lps = new int[m];
        lps[0] = 0; // Length of lps for a single character is always 0

        int len = 0; // Length of previous longest prefix suffix
        int i = 1;

        while (i < m) {
            if (p.charAt(i) == p.charAt(len)) {
                len++;
                lps[i] = len;
                i++;
            } else {
                if (len != 0) {
                    len = lps[len - 1]; // Fallback to previous known prefix
                } else {
                    lps[i] = 0;
                    i++;
                }
            }
        }

        return lps;
    }

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

        if (m > n) {
            return occurrences;
        }

        int[] lps = lps(pattern);

        int i = 0; // Index in text (never moves backward!)
        int j = 0; // Index in pattern

        while (i < n) {
            comparisons++;
            if (text.charAt(i) == pattern.charAt(j)) {
                i++;
                j++;
            }

            if (j == m) {
                // Match found at start index (i - j)
                occurrences.add(i - j);
                // Shift pattern according to LPS to support overlapping matches
                j = lps[j - 1];
            } else if (i < n && text.charAt(i) != pattern.charAt(j)) {
                // Mismatch after j matches
                if (j != 0) {
                    j = lps[j - 1]; // Do not increment i, shift pattern
                } else {
                    i++; // Character 0 mismatched, move forward in text
                }
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
        return "KMP";
    }
}
