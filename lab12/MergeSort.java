import edu.princeton.cs.algs4.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class MergeSort {
    /**
     * Removes and returns the smallest item that is in q1 or q2.
     *
     * The method assumes that both q1 and q2 are in sorted order, with the smallest item first. At
     * most one of q1 or q2 can be empty (but both cannot be empty).
     *
     * @param q1 A Queue in sorted order from least to greatest.
     * @param q2 A Queue in sorted order from least to greatest.
     * @return The smallest item that is in q1 or q2.
     */
    private static <Item extends Comparable> Item getMin(Queue<Item> q1, Queue<Item> q2) {
        if (q1.isEmpty()) {
            return q2.dequeue();
        } else if (q2.isEmpty()) {
            return q1.dequeue();
        } else {
            // Peek at the minimum item in each queue (which will be at the front, since the
            // queues are sorted) to determine which is smaller.
            Comparable q1Min = q1.peek();
            Comparable q2Min = q2.peek();
            if (q1Min.compareTo(q2Min) <= 0) {
                // Make sure to call dequeue, so that the minimum item gets removed.
                return q1.dequeue();
            } else {
                return q2.dequeue();
            }
        }
    }

    /** Returns a queue of queues that each contain one item from items. */
    private static <Item extends Comparable> Queue<Queue<Item>> makeSingleItemQueues(Queue<Item> items) {
        // Your code here!
        Queue<Queue<Item>> wrapperQueue = new Queue<>();
        for (Item i : items) {
            Queue<Item> singleItemQueue = new Queue<>();
            singleItemQueue.enqueue(i);
            wrapperQueue.enqueue(singleItemQueue);
        }
        return wrapperQueue;
    }

    /**
     * Returns a new queue that contains the items in q1 and q2 in sorted order.
     *
     * This method should take time linear in the total number of items in q1 and q2.  After
     * running this method, q1 and q2 will be empty, and all of their items will be in the
     * returned queue.
     *
     * @param q1 A Queue in sorted order from least to greatest.
     * @param q2 A Queue in sorted order from least to greatest.
     * @returns A Queue containing all of the q1 and q2 in sorted order, from least to
     *     greatest.
     *
     */
    private static <Item extends Comparable> Queue<Item> mergeSortedQueues(Queue<Item> q1, Queue<Item> q2) {
        // Your code here!
        Queue<Item> mergedQueue = new Queue<>();
        while (!q1.isEmpty() || !q2.isEmpty()) {
            mergedQueue.enqueue(getMin(q1, q2));
        }
        return mergedQueue;
    }

    /** Returns a Queue that contains the given items sorted from least to greatest. */
    public static <Item extends Comparable> Queue<Item> mergeSort(Queue<Item> items) {
        // Your code here!
        Queue<Queue<Item>> helperQueue = makeSingleItemQueues(items);
        while (helperQueue.size() != 1) {
            Queue<Item> first = helperQueue.dequeue();
            Queue<Item> second = helperQueue.dequeue();
            Queue<Item> mergedQueue = mergeSortedQueues(first, second);
            helperQueue.enqueue(mergedQueue);
        }
        return helperQueue.dequeue();
    }

    private <Item extends Comparable> boolean isQueueSorted(Queue<Item> q) {
        Item first = null;
        for (Item i : q) {
            if (first != null && first.compareTo(i) > 0) {
                return false;
            }
            first = i;
        }
        return true;
    }

    @Test
    public void testMergeSort() {
        Queue<Integer> students = new Queue<>();
        int testNum = 21;
        for (int i = 0; i < testNum; i++) {
            students.enqueue(StdRandom.uniform(10000));
        }
        Queue<Integer> sortedStudent = mergeSort(students);
        assertTrue(isQueueSorted(sortedStudent));
    }

    public static void main(String[] args) {
        jh61b.junit.TestRunner.runTests(MergeSort.class);
    }
}
