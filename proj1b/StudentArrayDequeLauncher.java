/** If your computer is set up properly, this file should
  * compile when moved into the same directory as 
  * StudentArrayDeque.class, or if you're using
  * IntelliJ, after you have imported proj1b.jar */

import org.junit.Test;
import static org.junit.Assert.*;


public class StudentArrayDequeLauncher {

    @Test
    public void testAddDelete() {
        StudentArrayDeque<Integer> sad1 = new StudentArrayDeque<Integer>();
        sad1.addLast(5);
        sad1.addLast(10);
        sad1.addFirst(11);
        sad1.addFirst(12);
        assertEquals(4, sad1.size());  // size
        assertEquals(12, sad1.removeFirst().intValue());
        assertEquals(10, sad1.removeLast().intValue());
    }

    @Test
    public void testResize() {
        StudentArrayDeque<Integer> sad1 = new StudentArrayDeque<Integer>();
        int testSize = 20;
        for (int i = 0; i < testSize; i++) {
            sad1.addFirst(i);
            sad1.addLast(testSize - i);
            sad1.printDeque();
            System.out.println();
        }
        /*
        assertEquals(testSize, sad1.size());
        for (int i = 0; i < testSize; i++) {
            assertEquals(i, sad1.removeLast().intValue());
            sad1.printDeque();
            System.out.println();
        }
        assertEquals(0, sad1.size());
        assertEquals(null, sad1.removeFirst());
        assertEquals(null, sad1.get(StdRandom.uniform(10)));
        */
    }


    public static void main(String[] args) {
        jh61b.junit.TestRunner.runTests("all", StudentArrayDequeLauncher.class);
    }
} 
