# DAA Assignment 4: Dynamic Programming and String Algorithms

**Student:** Askar Aziz  
**Group:** SE-2521  
**Course:** Design and Analysis of Algorithms (DAA)  
**Instructor / Lecturer:** Taubakabyl Nurlybek  

---

## 1. Project Overview

This repository contains full, clean, and rigorously tested implementations of **Dynamic Programming (Edit Distance)** and **String Searching (Naive vs. Knuth-Morris-Pratt)** algorithms in Java 17+, developed according to the DAA Assignment 4 specification.

### Implemented Algorithms:
1. **Edit Distance Family** (implementing `com.algorithms.editdistance.EditDistance`):
   - `RecursiveEditDistance`: Direct recursive formulation demonstrating $O(3^{\min(n, m)})$ exponential call tree explosion. Includes an internal recursive call counter.
   - `TableEditDistance`: Classic bottom-up 2D Dynamic Programming using an $(n+1) \times (m+1)$ table. Space: $O(n \cdot m)$, Time: $O(n \cdot m)$. Includes an internal cell-update counter.
   - `TwoRowsEditDistance`: Space-optimized DP retaining only the current and previous rows. Space: $O(\min(n, m))$, Time: $O(n \cdot m)$. Includes an internal cell-update counter.
2. **String Searching Family** (implementing `com.algorithms.stringsearch.StringSearch`):
   - `NaiveStringSearch`: Straightforward sliding window checking pattern against text. In worst-case periodic patterns ($A^{n-1}B$ vs $A^{m-1}B$), performs $\Theta(n \cdot m)$ character comparisons.
   - `KmpStringSearch`: Knuth-Morris-Pratt linear-time algorithm using a precomputed Longest Prefix-Suffix (`lps`) table. Never backtracks the text index $i$, achieving guaranteed $O(n + m)$ character comparisons. Exposes `public int[] lps(String p)` for standalone verification.

---

## 2. Directory Structure

```text
DAA_Assignment4_Askar_Aziz_SE-2521/
├── pom.xml                                      # Maven configuration (Java 17, JUnit 5)
├── README.md                                    # This build & execution guide
├── REPORT.md                                    # Comprehensive analytical report
├── REPORT.docx                                  # Professional Word report with embedded plots
├── results/
│   ├── results.csv                              # Empirical benchmarking output dataset
│   └── plots/
│       ├── plot1_edit_distance_time.png         # Log-scale execution time vs input size
│       ├── plot2_edit_distance_counters.png     # DP cell updates vs theoretical O(n*m)
│       └── plot3_string_search_comparisons.png  # Character comparisons: Naive vs KMP
└── src/
    ├── main/java/com/algorithms/
    │   ├── editdistance/
    │   │   ├── EditDistance.java                # Interface with distance(), counters, name
    │   │   ├── RecursiveEditDistance.java       # Exponential recursive solver
    │   │   ├── TableEditDistance.java           # Bottom-up full 2D table DP
    │   │   └── TwoRowsEditDistance.java         # Space-optimized 2-row DP
    │   ├── stringsearch/
    │   │   ├── StringSearch.java                # Interface with search(), counters, name
    │   │   ├── NaiveStringSearch.java           # O(n*m) naive string matching
    │   │   └── KmpStringSearch.java             # O(n+m) KMP with public lps()
    │   └── experiment/
    │       ├── InputGenerator.java              # Deterministic test dataset generator (fixed seeds)
    │       ├── BenchmarkRunner.java             # Automated multi-run benchmark harness
    │       └── ChartGenerator.java              # Pure Java 2D scientific chart renderer
    └── test/java/com/algorithms/
        └── Assignment4Test.java                 # 14 JUnit 5 tests covering all edge & reference cases
```

---

## 3. How to Build, Test, and Run

### Prerequisites
- **JDK 17 or higher** (tested with OpenJDK 25 and OpenJDK 17)
- **Apache Maven 3.8+**

### Step 1: Run Unit Tests
To compile and execute the complete test suite:
```bash
mvn clean test
```
**Test Coverage Includes:**
- Section 7 reference examples (`kitten` $\to$ `sitting` $= 3$, `intention` $\to$ `execution` $= 5$, etc.).
- Edge cases: identical strings, empty strings (distance $= \max(n, m)$), completely disjoint alphabets.
- Empty pattern validation throwing `IllegalArgumentException`.
- Overlapping pattern matches in KMP (e.g. `ABA` in `ABABA` returning `[0, 2]`).
- Pattern longer than text returning an empty list.
- 100 randomly generated cross-checks verifying that `RecursiveEditDistance`, `TableEditDistance`, and `TwoRowsEditDistance` yield identical values.
- 100 randomly generated cross-checks verifying `NaiveStringSearch` and `KmpStringSearch` produce identical index match lists.

### Step 2: Run Benchmarks
To run the automated empirical experiments:
```bash
mvn compile
java -cp target/classes com.algorithms.experiment.BenchmarkRunner
```
This executes 5 runs per configuration (taking the median wall-clock runtime) and exports the data to `results/results.csv`.

### Step 3: Generate Visual Plots
To regenerate the high-resolution PNG charts:
```bash
java -cp target/classes com.algorithms.experiment.ChartGenerator
```
The resulting charts will be written directly into `results/plots/`.

---

## 4. Git Repository & Submission Info

- **Branch:** `main`
- **Release Tag:** `v1.0`
- **Submission Archive:** `DAA_Assignment4_Askar_Aziz_SE-2521.zip`

---

## 5. AI Use Acknowledgment

In accordance with academic integrity guidelines:
- **Tools Consulted:** Generative AI assistant (Google Antigravity / Gemini) was utilized during the development lifecycle for scaffolding test cases, formatting markdown documentation, and assisting in Java 2D plot coordinate layout calculations.
- **Verification:** All algorithm implementations (state transitions, recursive call graphs, LPS table generation, and zero-allocation sliding windows) were independently reviewed, validated against textbook algorithmic invariants (CLRS), and formally tested with JUnit 5.
