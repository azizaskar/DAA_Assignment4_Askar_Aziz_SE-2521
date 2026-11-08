package com.algorithms;

import com.algorithms.editdistance.EditDistance;
import com.algorithms.editdistance.RecursiveEditDistance;
import com.algorithms.editdistance.TableEditDistance;
import com.algorithms.editdistance.TwoRowsEditDistance;
import com.algorithms.stringsearch.KmpStringSearch;
import com.algorithms.stringsearch.NaiveStringSearch;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

public class Assignment4Test {

    private RecursiveEditDistance recursiveED;
    private TableEditDistance tableED;
    private TwoRowsEditDistance twoRowsED;

    private NaiveStringSearch naiveSearch;
    private KmpStringSearch kmpSearch;

    @BeforeEach
    void setUp() {
        recursiveED = new RecursiveEditDistance();
        tableED = new TableEditDistance();
        twoRowsED = new TwoRowsEditDistance();

        naiveSearch = new NaiveStringSearch();
        kmpSearch = new KmpStringSearch();
    }

    // ==========================================
    // 1. EDIT DISTANCE TESTS
    // ==========================================

    @Test
    @DisplayName("Section 7 Reference Values: distance('kitten', 'sitting') == 3")
    void testEditDistanceReference1() {
        assertAllEditDistance("kitten", "sitting", 3);
    }

    @Test
    @DisplayName("Section 7 Reference Values: distance('flaw', 'lawn') == 2")
    void testEditDistanceReference2() {
        assertAllEditDistance("flaw", "lawn", 2);
    }

    @Test
    @DisplayName("Section 7 Reference Values: distance('intention', 'execution') == 5")
    void testEditDistanceReference3() {
        assertAllEditDistance("intention", "execution", 5);
    }

    @Test
    @DisplayName("Section 7 Reference Values: distance('', 'abc') == 3 and distance('abc', 'abc') == 0")
    void testEditDistanceReferenceEdgeCases() {
        assertAllEditDistance("", "abc", 3);
        assertAllEditDistance("abc", "", 3);
        assertAllEditDistance("abc", "abc", 0);
        assertAllEditDistance("", "", 0);
    }

    @Test
    @DisplayName("Cross-check on 100 random short strings: All 3 versions return the exact same value")
    void testEditDistanceRandom100Strings() {
        Random rng = new Random(42);
        String alphabet = "abcdefgh";

        for (int i = 0; i < 100; i++) {
            int lenA = rng.nextInt(7); // Keep length small (0..6) so recursive finishes in milliseconds
            int lenB = rng.nextInt(7);

            String a = generateRandomString(rng, alphabet, lenA);
            String b = generateRandomString(rng, alphabet, lenB);

            int resRec = recursiveED.distance(a, b);
            int resTab = tableED.distance(a, b);
            int resRow = twoRowsED.distance(a, b);

            assertEquals(resTab, resRec, "Recursive mismatch for a='" + a + "', b='" + b + "'");
            assertEquals(resTab, resRow, "TwoRows mismatch for a='" + a + "', b='" + b + "'");
        }
    }

    private void assertAllEditDistance(String a, String b, int expected) {
        assertEquals(expected, recursiveED.distance(a, b), "Recursive failed on (" + a + ", " + b + ")");
        assertEquals(expected, tableED.distance(a, b), "Table failed on (" + a + ", " + b + ")");
        assertEquals(expected, twoRowsED.distance(a, b), "TwoRows failed on (" + a + ", " + b + ")");
    }

    // ==========================================
    // 2. KMP LPS (PREFIX FUNCTION) TESTS
    // ==========================================

    @Test
    @DisplayName("Section 7 Reference Values: lps('AAACAAAA') == [0, 1, 2, 0, 1, 2, 3, 3]")
    void testLpsReference1() {
        int[] expected = {0, 1, 2, 0, 1, 2, 3, 3};
        assertArrayEquals(expected, KmpStringSearch.lps("AAACAAAA"));
    }

    @Test
    @DisplayName("Section 7 Reference Values: lps('ABABCABAB') == [0, 0, 1, 2, 0, 1, 2, 3, 4]")
    void testLpsReference2() {
        int[] expected = {0, 0, 1, 2, 0, 1, 2, 3, 4};
        assertArrayEquals(expected, KmpStringSearch.lps("ABABCABAB"));
    }

    @Test
    @DisplayName("LPS Edge Cases: Empty or single character")
    void testLpsEdgeCases() {
        assertArrayEquals(new int[0], KmpStringSearch.lps(""));
        assertArrayEquals(new int[]{0}, KmpStringSearch.lps("A"));
        assertArrayEquals(new int[]{0, 1, 2, 3}, KmpStringSearch.lps("AAAA"));
    }

    // ==========================================
    // 3. STRING SEARCH TESTS (NAIVE & KMP)
    // ==========================================

    @Test
    @DisplayName("Section 7 Reference Values: search('ABABDABACDABABCABAB', 'ABABCABAB') == [10]")
    void testSearchReference1() {
        List<Integer> expected = List.of(10);
        assertEquals(expected, naiveSearch.search("ABABDABACDABABCABAB", "ABABCABAB"));
        assertEquals(expected, kmpSearch.search("ABABDABACDABABCABAB", "ABABCABAB"));
    }

    @Test
    @DisplayName("Section 7 Reference Values: search('AAAAA', 'AA') == [0, 1, 2, 3] (overlapping)")
    void testSearchReferenceOverlapping1() {
        List<Integer> expected = List.of(0, 1, 2, 3);
        assertEquals(expected, naiveSearch.search("AAAAA", "AA"));
        assertEquals(expected, kmpSearch.search("AAAAA", "AA"));
    }

    @Test
    @DisplayName("Section 7 Reference Values: search('ABABABA', 'ABA') == [0, 2, 4] (overlapping)")
    void testSearchReferenceOverlapping2() {
        List<Integer> expected = List.of(0, 2, 4);
        assertEquals(expected, naiveSearch.search("ABABABA", "ABA"));
        assertEquals(expected, kmpSearch.search("ABABABA", "ABA"));
    }

    @Test
    @DisplayName("Edge Cases: Pattern longer than text returns empty list")
    void testSearchPatternLongerThanText() {
        assertTrue(naiveSearch.search("Short", "VeryLongPatternHere").isEmpty());
        assertTrue(kmpSearch.search("Short", "VeryLongPatternHere").isEmpty());
    }

    @Test
    @DisplayName("Edge Cases: Empty pattern throws IllegalArgumentException")
    void testSearchEmptyPatternThrows() {
        assertThrows(IllegalArgumentException.class, () -> naiveSearch.search("Any text", ""));
        assertThrows(IllegalArgumentException.class, () -> kmpSearch.search("Any text", ""));
        assertThrows(IllegalArgumentException.class, () -> naiveSearch.search("Any text", null));
        assertThrows(IllegalArgumentException.class, () -> kmpSearch.search("Any text", null));
    }

    @Test
    @DisplayName("Cross-check on 100 random texts and patterns: Naive and KMP return the exact same list")
    void testSearchRandom100Texts() {
        Random rng = new Random(101);
        String alphabet = "AB"; // Small alphabet increases overlap chances

        for (int i = 0; i < 100; i++) {
            int textLen = rng.nextInt(50) + 10;
            int patLen = rng.nextInt(5) + 1;

            String text = generateRandomString(rng, alphabet, textLen);
            String pattern = generateRandomString(rng, alphabet, patLen);

            List<Integer> naiveResult = naiveSearch.search(text, pattern);
            List<Integer> kmpResult = kmpSearch.search(text, pattern);

            assertEquals(naiveResult, kmpResult, "Mismatch on text='" + text + "', pattern='" + pattern + "'");
        }
    }

    private String generateRandomString(Random rng, String alphabet, int length) {
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            sb.append(alphabet.charAt(rng.nextInt(alphabet.length())));
        }
        return sb.toString();
    }
}
