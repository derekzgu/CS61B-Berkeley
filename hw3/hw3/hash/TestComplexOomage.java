package hw3.hash;

import org.junit.Test;
import static org.junit.Assert.*;

import java.util.Set;
import java.util.HashSet;
import java.util.ArrayList;
import java.util.List;
import edu.princeton.cs.algs4.StdRandom;


public class TestComplexOomage {

    @Test
    public void testHashCodeDeterministic() {
        ComplexOomage so = ComplexOomage.randomComplexOomage();
        int hashCode = so.hashCode();
        for (int i = 0; i < 100; i += 1) {
            assertEquals(hashCode, so.hashCode());
        }
    }

    public boolean haveNiceHashCodeSpread(Set<ComplexOomage> oomages) {
        /* TODO: Write a utility function that ensures that the oomages have
         * hashCodes that would distribute them fairly evenly across
         * buckets To do this, mod each's hashCode by M = 10,
         * and ensure that no bucket has fewer than N / 50
         * Oomages and no bucket has more than N / 2.5 Oomages.
         */
        int M = 10;
        int N = oomages.size();
        int [] numOfOomageBucket = new int[M];
        for (int i = 0; i < M; i++) {
            numOfOomageBucket[i] = 0;
        }
        for (ComplexOomage someOomage : oomages) {
            int bucket = (someOomage.hashCode() & 0x7FFFFFFF) % M;
            numOfOomageBucket[bucket] += 1;
        }
        for (int i = 0; i < M; i++) {
            if (numOfOomageBucket[i] < (double) N / 50.0 || numOfOomageBucket[i] > (double) N / 2.5)
                return false;
        }
        return true;
    }


    @Test
    public void testRandomItemsHashCodeSpread() {
        HashSet<ComplexOomage> oomages = new HashSet<ComplexOomage>();
        int N = 10000;

        for (int i = 0; i < N; i += 1) {
            oomages.add(ComplexOomage.randomComplexOomage());
        }

        assertTrue(haveNiceHashCodeSpread(oomages));
    }

    @Test
    public void testWithDeadlyParams() {
        /* TODO: Create a Set that shows the flaw in the hashCode function.
         */
        HashSet<ComplexOomage> oomages = new HashSet<ComplexOomage>();
        int numSamples = 10000;
        int index = 0;
        while (index < numSamples) {
            int N = StdRandom.uniform(1, 6);
            ArrayList<Integer> params = new ArrayList<Integer>(N);
            for (int i = 0; i < N; i += 1) {
                params.add(StdRandom.uniform(0, 255));
            }
            for (int i = 0; i < 4; i++) {
                params.add(0);    // the last four digits is the same for all samples
            }
            oomages.add(new ComplexOomage(params));
            index += 1;
        }
        assertTrue(haveNiceHashCodeSpread(oomages));
    }

    /** Calls tests for SimpleOomage. */
    public static void main(String[] args) {
        jh61b.junit.textui.runClasses(TestComplexOomage.class);
    }
}
