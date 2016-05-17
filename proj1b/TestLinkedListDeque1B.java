/**
 * Created by chizhang on 5/16/16.
 */

import org.junit.Test;
import org.omg.PortableInterceptor.SYSTEM_EXCEPTION;

import static org.junit.Assert.*;

public class TestLinkedListDeque1B {
    @Test
    public void randomTest() {
        int testNumber = 100;
        int maxTestInteger = 100;
        StudentLinkedListDeque<Integer> studentLinkedList = new StudentLinkedListDeque<>();
        LinkedListDequeSolution<Integer> solutionLinkedList = new LinkedListDequeSolution<>();
        for (int i = 0; i < testNumber; i++) {
            int testFunctionIndex = StdRandom.uniform(7);
            if (testFunctionIndex == 0) {
                int num = StdRandom.uniform(maxTestInteger);
                studentLinkedList.addFirst(num);
                solutionLinkedList.addFirst(num);
                System.out.println("addFirst " + num);
            } else if (testFunctionIndex == 1) {
                int num = StdRandom.uniform(maxTestInteger);
                studentLinkedList.addLast(num);
                solutionLinkedList.addLast(num);
                System.out.println("addLast " + num);
            } else if (testFunctionIndex == 2) {
                System.out.println("Test isEmpty");
                assertEquals(studentLinkedList.isEmpty(), solutionLinkedList.isEmpty());
            } else if (testFunctionIndex == 3) {
                System.out.println("Test size");
                assertEquals(studentLinkedList.size(), solutionLinkedList.size());
                System.out.println("size passes");
            } else if (testFunctionIndex == 4) {
                System.out.println("Test removeFirst");
                assertEquals(studentLinkedList.removeFirst(), solutionLinkedList.removeFirst());
                System.out.println("removeFirst passes");
            } else if (testFunctionIndex == 5) {
                System.out.println("Test removeLast");
                assertEquals(studentLinkedList.removeLast(), solutionLinkedList.removeLast());
                System.out.println("removeLast passes");
            } else if (testFunctionIndex == 6) {
                assertTrue(solutionLinkedList.size() >= 0);
                int index = StdRandom.uniform(solutionLinkedList.size());
                System.out.println("Test get " + index);
                assertEquals(studentLinkedList.get(index), solutionLinkedList.get(index));
                System.out.println("get passes");
            }
        }
    }

    public static void main(String[] args) {
        jh61b.junit.TestRunner.runTests("all", TestLinkedListDeque1B.class);
    }
}
