package com.algorithms.editdistance;

/**
 * B. Table Bottom-Up Edit Distance.
 *
 * DP Design in Comments:
 * - State: dp[i][j] = minimum edit distance between prefix a[0..i-1] and prefix b[0..j-1] in a (n+1)x(m+1) table.
 * - Base Case: dp[0][j] = j for all 0 <= j <= m; dp[i][0] = i for all 0 <= i <= n.
 * - Transition: If a[i-1] == b[j-1], dp[i][j] = dp[i-1][j-1];
 *               else dp[i][j] = 1 + min(dp[i-1][j], dp[i][j-1], dp[i-1][j-1]).
 */
public class TableEditDistance implements EditDistance {

    private long cellUpdateCounter = 0;

    @Override
    public int distance(String a, String b) {
        if (a == null) a = "";
        if (b == null) b = "";

        int n = a.length();
        int m = b.length();

        int[][] dp = new int[n + 1][m + 1];

        // Base cases
        for (int i = 0; i <= n; i++) {
            dp[i][0] = i;
        }
        for (int j = 0; j <= m; j++) {
            dp[0][j] = j;
        }

        // Bottom-up table filling
        for (int i = 1; i <= n; i++) {
            for (int j = 1; j <= m; j++) {
                cellUpdateCounter++;
                if (a.charAt(i - 1) == b.charAt(j - 1)) {
                    dp[i][j] = dp[i - 1][j - 1];
                } else {
                    int deleteCost = dp[i - 1][j];
                    int insertCost = dp[i][j - 1];
                    int replaceCost = dp[i - 1][j - 1];
                    dp[i][j] = 1 + Math.min(deleteCost, Math.min(insertCost, replaceCost));
                }
            }
        }

        return dp[n][m];
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
        return "Table";
    }
}
