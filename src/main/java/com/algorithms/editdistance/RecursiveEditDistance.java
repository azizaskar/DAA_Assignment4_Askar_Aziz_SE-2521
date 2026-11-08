package com.algorithms.editdistance;

/**
 * A. Recursive Edit Distance (No memoization/caching, slow on purpose).
 *
 * DP Design in Comments:
 * - State: dist(i, j) = minimum edit distance between prefix a[0..i-1] and prefix b[0..j-1].
 * - Base Case: dist(0, j) = j (insert j characters); dist(i, 0) = i (delete i characters).
 * - Transition: If a[i-1] == b[j-1], dist(i, j) = dist(i-1, j-1);
 *               else dist(i, j) = 1 + min(dist(i-1, j), dist(i, j-1), dist(i-1, j-1)).
 */
public class RecursiveEditDistance implements EditDistance {

    private long callCounter = 0;

    @Override
    public int distance(String a, String b) {
        if (a == null) a = "";
        if (b == null) b = "";
        return recurse(a, b, a.length(), b.length());
    }

    private int recurse(String a, String b, int i, int j) {
        callCounter++;

        // Base cases
        if (i == 0) return j;
        if (j == 0) return i;

        // Transition
        if (a.charAt(i - 1) == b.charAt(j - 1)) {
            return recurse(a, b, i - 1, j - 1);
        } else {
            int insertCost = recurse(a, b, i, j - 1);      // Insert into a
            int deleteCost = recurse(a, b, i - 1, j);      // Delete from a
            int replaceCost = recurse(a, b, i - 1, j - 1); // Substitute char

            return 1 + Math.min(insertCost, Math.min(deleteCost, replaceCost));
        }
    }

    @Override
    public long getCounter() {
        return callCounter;
    }

    @Override
    public void resetCounter() {
        callCounter = 0;
    }

    @Override
    public String getName() {
        return "Recursive";
    }
}
