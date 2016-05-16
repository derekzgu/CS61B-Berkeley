/**
 * Created by chizhang on 5/16/16.
 */

import org.junit.Test;
import static org.junit.Assert.*;

public class TestLinkedListDeque1B {

    @Test
    public void testAddDelete() {
        StudentLinkedListDeque<Integer> sad = new StudentLinkedListDeque<Integer>();
        LinkedListDequeSolution<Integer> expected = new LinkedListDequeSolution<>();

        sad.removeFirst();
        expected.removeFirst();
        assertEquals(expected.size(), sad.size());
    }

    @Test
    public void testResize() {
        StudentLinkedListDeque<Integer> sad1 = new StudentLinkedListDeque<Integer>();
        int testSize = 20;
        for (int i = 0; i < testSize; i++) {
            sad1.addFirst(i);
            sad1.addLast(testSize - i);
        }

    }

    public static void main(String[] args) {
        jh61b.junit.TestRunner.runTests("all", TestLinkedListDeque1B.class);
    }
}
