import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;

import static org.junit.Assert.*;

/**
 * Class for doing Radix sort
 *
 * @author Akhil Batra
 * @version 1.4 - April 14, 2016
 *
 **/
public class RadixSort
{

    /**
     * Does Radix sort on the passed in array with the following restrictions:
     *  The array can only have ASCII Strings (sequence of 1 byte characters)
     *  The sorting is stable and non-destructive
     *  The Strings can be variable length (all Strings are not constrained to 1 length)
     *
     * @param asciis String[] that needs to be sorted
     *
     * @return String[] the sorted array
     **/
    public static String[] sort(String[] asciis) {
        String[] copy = new String[asciis.length];
        System.arraycopy(asciis, 0, copy, 0, asciis.length);
        int maxLength = 0;
        for (String s: asciis) {
            if (s.length() > maxLength) maxLength = s.length();
        }
        for (int i = 0; i < maxLength; i++) {
            sortHelper(copy, 0, asciis.length, i);
        }
        return copy;
    }

    /**
     * Radix sort helper function that recursively calls itself to achieve the sorted array
     *  destructive method that changes the passed in array, asciis
     *
     * @param asciis String[] to be sorted
     * @param start int for where to start sorting in this method (includes String at start)
     * @param end int for where to end sorting in this method (does not include String at end)
     * @param index the index of the character the method is currently sorting on
     *
     **/
    private static void sortHelper(String[] asciis, int start, int end, int index)
    {
        //TODO use if you want to
        int[] counts = new int[257];  // 0 is for empty, length < index, 1-256 for ascii
        for (int i = 0; i < counts.length; i++) counts[i] = 0;

        for (int i = start; i < end; i++) {
            if (asciis[i].length() == 0) counts[0] += 1;
            else if (asciis[i].length() < index + 1) counts[(int) asciis[i].charAt(0) + 1] += 1;
            else counts[(int) asciis[i].charAt(asciis[i].length() - 1 - index) + 1] += 1;
        }
        int[] starts = new int[257];
        starts[0] = 0;
        int accumulate = starts[0];
        for (int i = 1; i < starts.length; i++) {
            starts[i] = accumulate + counts[i - 1];
            accumulate += counts[i - 1];
        }
        String[] tempResult = new String[end - start];
        for (int i = start; i < end; i++) {
            if (asciis[i].length() == 0) {
                int asc = 0;
                tempResult[starts[asc]] = asciis[i];
                starts[asc] += 1;
            } else if (asciis[i].length() < index + 1)  {
                int asc = (int) asciis[i].charAt(0) + 1;
                tempResult[starts[asc]] = asciis[i];
                starts[asc] += 1;
            } else {
                int asc = (int) asciis[i].charAt(asciis[i].length() - 1 - index) + 1;
                tempResult[starts[asc]] = asciis[i];
                starts[asc] += 1;
            }
        }
        System.arraycopy(tempResult, 0, asciis, start, end - start);
    }

    private String randomStringGenerator(int length) {
        String randomString = "";
        for (int i = 0; i < length; i++) {
            Character randomChar = (char) StdRandom.uniform(256);
            randomString += randomChar.toString();
        }
        return randomString;
    }

    private void assertSorted(String[] testString, int maxIndex) {
        for (int i = 1; i < Math.max(testString.length, maxIndex); i++) {
            assertTrue(testString[i - 1].compareTo(testString[i]) < 1);
        }
    }

    @Test
    public void testSortHelper() {
        String[] testString = new String[5];
        testString[0] = "b";
        testString[1] = "a";
        testString[2] = "ab";
        testString[3] = "sc";
        testString[4] = "op";
        sortHelper(testString, 0, 5, 0);
        sortHelper(testString, 0, 5, 1);
        assertSorted(testString, 5);
    }

    @Test
    public void testRadix() {
        int arrayLength = StdRandom.uniform(1, 20);
        int maxStringLength = 10;
        String[] testString = new String[arrayLength];
        for (int i = 0; i < testString.length; i++) {
            testString[i] = randomStringGenerator(StdRandom.uniform(maxStringLength));
        }
        // radix sort is non-destructive
        String[] radixSortResult = sort(testString);
        assertSorted(radixSortResult, radixSortResult.length);
        Arrays.sort(testString);
        assertArrayEquals(testString, radixSortResult);
    }
}
