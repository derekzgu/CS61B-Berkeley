/** If your computer is set up properly, this file should
  * compile when moved into the same directory as 
  * StudentArrayDeque.class, or if you're using
  * IntelliJ, after you have imported proj1b.jar */

import org.junit.Test;
import static org.junit.Assert.*;


public class TestArrayDeque1B {

    @Test
    public void testAddDelete() {
        StudentArrayDeque<Integer> sad1 = new StudentArrayDeque<Integer>();
        ArrayDequeSolution<Integer> expected = new ArrayDequeSolution<>();

        sad1.removeFirst();
        expected.removeFirst();
        assertEquals(expected.size(), sad1.size());
    }

    @Test
    public void testResize() {
        StudentArrayDeque<Integer> sad1 = new StudentArrayDeque<Integer>();
        int testSize = 20;
        for (int i = 0; i < testSize; i++) {
            sad1.addFirst(i);
            sad1.addLast(testSize - i);
        }
    }

    @Test
    public void randomTest() {
        int testNumber = 100;
        for (int i = 0; i < testNumber; i++) {

        }
    }

    public static void main(String[] args) {
        jh61b.junit.TestRunner.runTests("all", TestArrayDeque1B.class);
    }
} 
