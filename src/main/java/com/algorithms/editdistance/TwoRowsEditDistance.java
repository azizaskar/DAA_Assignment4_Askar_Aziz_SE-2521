package com.algorithms.editdistance;

/**
 * C. Two Rows Memory-Optimized Edit Distance.
 *
 * DP Design in Comments:
 * - State: prev[j] and curr[j] representing dp[i-1][j] and dp[i][j], reducing space from O(n*m) to O(m).
 * - Base Case: prev[j] = j for all 0 <= j <= m; curr[0] = i for the current row.
 * - Transition: If a[i-1] == b[j-1], curr[j] = prev[j-1];
 *               else curr[j] = 1 + min(prev[j], curr[j-1], prev[j-1]); then prev becomes curr.
 */
public class TwoRowsEditDistance implements EditDistance {

    private long cellUpdateCounter = 0;

    @Override
    public int distance(String a, String b) {
        if (a == null) a = "";
        if (b == null) b = "";

        // Optional optimization: ensure second string is shorter to minimize space O(min(n, m))
        int n = a.length();
        int m = b.length();

        int[] prev = new int[m + 1];
        int[] curr = new int[m + 1];

        // Base case for row 0
        for (int j = 0; j <= m; j++) {
            prev[j] = j;
        }

        // Compute row by row
        for (int i = 1; i <= n; i++) {
            curr[0] = i; // Base case for column 0 of current row

            for (int j = 1; j <= m; j++) {
                cellUpdateCounter++;
                if (a.charAt(i - 1) == b.charAt(j - 1)) {
                    curr[j] = prev[j - 1];
                } else {
                    int deleteCost = prev[j];      // from dp[i-1][j]
                    int insertCost = curr[j - 1];  // from dp[i][j-1]
                    int replaceCost = prev[j - 1]; // from dp[i-1][j-1]
                    curr[j] = 1 + Math.min(deleteCost, Math.min(insertCost, replaceCost));
                }
            }

            // Slide window: swap or copy curr to prev
            int[] temp = prev;
            prev = curr;
            curr = temp;
        }

        return prev[m];
    }

    @Override
    public long getCounter() {
        return cellUpdateCounter;
    }

    @Override
    public void resetCounter() {
        cellUpdateCounter = 0;
    }

    @Override
    public String getName() {
        return "TwoRows";
    }
}
