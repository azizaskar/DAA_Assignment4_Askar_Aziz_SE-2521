package com.algorithms.experiment;

import java.util.Random;

/**
 * Deterministic synthetic input generator with fixed seeds for reproducible experiments.
 */
public class InputGenerator {

    private final Random random;

    public InputGenerator(long seed) {
        this.random = new Random(seed);
    }

    /**
     * Generates a random string of length n over the specified alphabet.
     */
    public String randomString(int n, String alphabet) {
        StringBuilder sb = new StringBuilder(n);
        for (int i = 0; i < n; i++) {
            sb.append(alphabet.charAt(random.nextInt(alphabet.length())));
        }
        return sb.toString();
    }

    /**
     * Generates a string with mutations (substitutions/insertions/deletions) to simulate typos.
     */
    public String mutate(String s, double mutationRate, String alphabet) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < s.length(); i++) {
            if (random.nextDouble() < mutationRate) {
                // Mutate: pick replace, delete, or insert
                int op = random.nextInt(3);
                if (op == 0) {
                    // Substitute
                    sb.append(alphabet.charAt(random.nextInt(alphabet.length())));
                } else if (op == 1) {
                    // Delete: do not append
                } else {
                    // Insert
                    sb.append(alphabet.charAt(random.nextInt(alphabet.length())));
                    sb.append(s.charAt(i));
                }
            } else {
                sb.append(s.charAt(i));
            }
        }
        return sb.toString();
    }

    /**
     * Worst-case generator for Naive String Search:
     * Text T consists of 'A's ending with 'B', and pattern P is 'A's ending with 'B'.
     * Forces Naive to check almost all m characters before failing at the last one on every shift.
     */
    public static String naiveWorstCaseText(int n) {
        return "A".repeat(Math.max(1, n - 1)) + "B";
    }

    public static String naiveWorstCasePattern(int m) {
        return "A".repeat(Math.max(1, m - 1)) + "B";
    }

    /**
     * Periodic repeated string for overlap testing (e.g., "ABABABAB...").
     */
    public static String periodicString(int n, String period) {
        StringBuilder sb = new StringBuilder(n);
        while (sb.length() < n) {
            sb.append(period);
        }
        return sb.substring(0, n);
    }
}
