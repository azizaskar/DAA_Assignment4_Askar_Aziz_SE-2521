package com.algorithms.experiment;

import com.algorithms.editdistance.EditDistance;
import com.algorithms.editdistance.RecursiveEditDistance;
import com.algorithms.editdistance.TableEditDistance;
import com.algorithms.editdistance.TwoRowsEditDistance;
import com.algorithms.stringsearch.KmpStringSearch;
import com.algorithms.stringsearch.NaiveStringSearch;
import com.algorithms.stringsearch.StringSearch;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Benchmark runner that conducts the experiments described in the Experiment Design Card.
 * Measures median runtime (3-5 runs) and algorithm-internal counters.
 * Outputs results to results/results.csv.
 */
public class BenchmarkRunner {

    private static final int SEED = 42;
    private static final int RUNS = 5;

    public static void main(String[] args) {
        System.out.println("Starting Benchmark Experiments for DAA Assignment 4...");

        String csvPath = "results/results.csv";
        try (PrintWriter writer = new PrintWriter(new FileWriter(csvPath))) {
            // Write CSV header
            writer.println("family,algorithm,input_type,n,m,time_ms,counter,counter_type");

            runEditDistanceExperiments(writer);
            runStringSearchExperiments(writer);

            System.out.println("Benchmark successfully completed. Results written to: " + csvPath);
        } catch (IOException e) {
            System.err.println("Error writing to CSV: " + e.getMessage());
        }
    }

    private static void runEditDistanceExperiments(PrintWriter writer) {
        System.out.println("Running Edit Distance benchmarks...");
        InputGenerator gen = new InputGenerator(SEED);

        List<EditDistance> allAlgos = List.of(
                new RecursiveEditDistance(),
                new TableEditDistance(),
                new TwoRowsEditDistance()
        );

        List<EditDistance> dpAlgos = List.of(
                new TableEditDistance(),
                new TwoRowsEditDistance()
        );

        // 1. Recursive vs DP on small sizes (n: 4, 8, 12, 14, 16)
        int[] smallSizes = {4, 8, 11, 13, 15};
        for (int n : smallSizes) {
            String a = gen.randomString(n, "ACGT");
            String b = gen.mutate(a, 0.3, "ACGT");

            for (EditDistance algo : allAlgos) {
                benchmarkEditDistance(writer, algo, "random_small", a, b);
            }
        }

        // 2. Large scale on DP algorithms (Table and TwoRows) for sizes (n: 100, 500, 1000, 2500, 5000)
        int[] dpSizes = {100, 500, 1000, 2000, 4000};
        for (int n : dpSizes) {
            // Random inputs (average case)
            String aRand = gen.randomString(n, "abcdefghijklmnopqrstuvwxyz");
            String bRand = gen.randomString(n, "abcdefghijklmnopqrstuvwxyz");
            for (EditDistance algo : dpAlgos) {
                benchmarkEditDistance(writer, algo, "random_large", aRand, bRand);
            }

            // Extreme mismatch (worst-case: every char differs, maximum edit cost n)
            String aDiff = "A".repeat(n);
            String bDiff = "B".repeat(n);
            for (EditDistance algo : dpAlgos) {
                benchmarkEditDistance(writer, algo, "worst_all_diff", aDiff, bDiff);
            }

            // Near-identical strings (best-case typo: single substitution at middle)
            String aIden = "A".repeat(n);
            String bIden = "A".repeat(n / 2) + "C" + "A".repeat(n - (n / 2) - 1);
            for (EditDistance algo : dpAlgos) {
                benchmarkEditDistance(writer, algo, "near_identical", aIden, bIden);
            }
        }
    }

    private static void benchmarkEditDistance(PrintWriter writer, EditDistance algo, String inputType, String a, String b) {
        double[] times = new double[RUNS];
        long finalCounter = 0;

        for (int r = 0; r < RUNS; r++) {
            algo.resetCounter();
            long start = System.nanoTime();
            algo.distance(a, b);
            long end = System.nanoTime();
            times[r] = (end - start) / 1_000_000.0; // to ms
            finalCounter = algo.getCounter();
        }

        Arrays.sort(times);
        double medianTime = times[RUNS / 2];

        String counterType = algo.getName().equals("Recursive") ? "recursive_calls" : "cell_updates";

        writer.printf(java.util.Locale.US, "EditDistance,%s,%s,%d,%d,%.4f,%d,%s%n",
                algo.getName(), inputType, a.length(), b.length(), medianTime, finalCounter, counterType);
    }

    private static void runStringSearchExperiments(PrintWriter writer) {
        System.out.println("Running String Search benchmarks...");
        InputGenerator gen = new InputGenerator(SEED);

        List<StringSearch> algos = List.of(
                new NaiveStringSearch(),
                new KmpStringSearch()
        );

        // Sizes for text length N: 5000, 20000, 50000, 100000, 200000
        int[] textSizes = {10000, 50000, 100000, 250000, 500000};
        int patternLen = 100;

        for (int n : textSizes) {
            // Type 1: Random natural text (large alphabet, e.g. English text)
            String randText = gen.randomString(n, "abcdefghijklmnopqrstuvwxyz ");
            String randPattern = randText.substring(n / 2, (n / 2) + patternLen); // Guaranteed match
            for (StringSearch algo : algos) {
                benchmarkSearch(writer, algo, "random_text", randText, randPattern);
            }

            // Type 2: Worst Case for Naive Search: Text = "AAAA...AB", Pattern = "AAA...AB"
            // Naive performs (n - m + 1) * m comparisons = O(n*m), while KMP does O(n + m)
            String worstText = InputGenerator.naiveWorstCaseText(n);
            String worstPattern = InputGenerator.naiveWorstCasePattern(patternLen);
            for (StringSearch algo : algos) {
                benchmarkSearch(writer, algo, "worst_case_repetitive", worstText, worstPattern);
            }

            // Type 3: Highly periodic overlapping pattern (e.g., "ABABABAB...")
            String periodicText = InputGenerator.periodicString(n, "AB");
            String periodicPattern = InputGenerator.periodicString(patternLen, "AB");
            for (StringSearch algo : algos) {
                benchmarkSearch(writer, algo, "periodic_overlap", periodicText, periodicPattern);
            }
        }
    }

    private static void benchmarkSearch(PrintWriter writer, StringSearch algo, String inputType, String text, String pattern) {
        double[] times = new double[RUNS];
        long finalCounter = 0;

        for (int r = 0; r < RUNS; r++) {
            algo.resetCounter();
            long start = System.nanoTime();
            algo.search(text, pattern);
            long end = System.nanoTime();
            times[r] = (end - start) / 1_000_000.0;
            finalCounter = algo.getCounter();
        }

        Arrays.sort(times);
        double medianTime = times[RUNS / 2];

        writer.printf(java.util.Locale.US, "StringSearch,%s,%s,%d,%d,%.4f,%d,char_comparisons%n",
                algo.getName(), inputType, text.length(), pattern.length(), medianTime, finalCounter);
    }
}
