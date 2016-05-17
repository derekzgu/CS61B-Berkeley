/** If your computer is set up properly, this file should
  * compile when moved into the same directory as 
  * StudentArrayDeque.class, or if you're using
  * IntelliJ, after you have imported proj1b.jar */

import org.junit.Test;
import static org.junit.Assert.*;


public class TestArrayDeque1B {

    @Test
    public void randomTest() {
        int testNumber = 100;
        int maxTestInteger = 100;
        StudentArrayDeque<Integer> studentArray = new StudentArrayDeque<>();
        ArrayDequeSolution<Integer> solutionArray = new ArrayDequeSolution<>();
        FailureSequence fs = new FailureSequence();

        for (int i = 0; i < testNumber; i++) {
            int testFunctionIndex = StdRandom.uniform(6);
            if (testFunctionIndex == 0) {
                int num = StdRandom.uniform(maxTestInteger);
                studentArray.addFirst(num);
                solutionArray.addFirst(num);
                DequeOperation op = new DequeOperation("addFirst", num);
                fs.addOperation(op);

            } else if (testFunctionIndex == 1) {
                int num = StdRandom.uniform(maxTestInteger);
                studentArray.addLast(num);
                solutionArray.addLast(num);
                DequeOperation op = new DequeOperation("addLast", num);
                fs.addOperation(op);

            } else if (testFunctionIndex == 2) {
                DequeOperation op = new DequeOperation("isEmpty");
                fs.addOperation(op);
                assertEquals(fs.toString(), studentArray.isEmpty(), solutionArray.isEmpty());

            } else if (testFunctionIndex == 3) {
                DequeOperation op = new DequeOperation("size");
                fs.addOperation(op);
                assertEquals(fs.toString(), studentArray.size(), solutionArray.size());

            } else if (testFunctionIndex == 4) {
                DequeOperation op = new DequeOperation("removeFirst");
                fs.addOperation(op);
                assertEquals(fs.toString(), studentArray.removeFirst(), solutionArray.removeFirst());

            } else if (testFunctionIndex == 5) {
                DequeOperation op = new DequeOperation("removeLast");
                fs.addOperation(op);
                assertEquals(fs.toString(), studentArray.removeLast(), solutionArray.removeLast());

            } else if (testFunctionIndex == 6) {
                DequeOperation op = new DequeOperation("size");
                fs.addOperation(op);
                assertEquals(fs.toString(), studentArray.size(), solutionArray.size());

                int testIndex = 0;
                if (solutionArray.size() != 0) {
                    testIndex = StdRandom.uniform(solutionArray.size());
                } else{
                    testIndex = 0;
                }
                op = new DequeOperation("get", testIndex);
                fs.addOperation(op);
                assertEquals(fs.toString(), studentArray.get(testIndex), solutionArray.get(testIndex));
            }
        }
    }

    public static void main(String[] args) {
        jh61b.junit.TestRunner.runTests("all", TestArrayDeque1B.class);
    }
} 
