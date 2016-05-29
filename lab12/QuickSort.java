import edu.princeton.cs.algs4.Queue;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class QuickSort {
    /**
     * Returns a new queue that contains the given queues catenated together.
     *
     * The items in q2 will be catenated after all of the items in q1.
     */
    private static <Item extends Comparable> Queue<Item> catenate(Queue<Item> q1, Queue<Item> q2) {
        Queue<Item> catenated = new Queue<Item>();
        for (Item item : q1) {
            catenated.enqueue(item);
        }
        for (Item item: q2) {
            catenated.enqueue(item);
        }
        return catenated;
    }

    /** Returns a random item from the given queue. */
    private static <Item extends Comparable> Item getRandomItem(Queue<Item> items) {
        int pivotIndex = (int) (Math.random() * items.size());
        Item pivot = null;
        // Walk through the queue to find the item at the given index.
        for (Item item : items) {
            if (pivotIndex == 0) {
                pivot = item;
                break;
            }
            pivotIndex--;
        }
        return pivot;
    }

    /**
     * Partitions the given unsorted queue by pivoting on the given item.
     *
     * @param unsorted a Queue of unsorted items
     * @param pivot the item to pivot on
     * @param less an empty Queue. When the function completes, this queue will contain
     *             all of the items in unsorted that are less than the given pivot.
     * @param equal an empty Queue. When the function completes, this queue will contain
     *              all of the items in unsorted that are equal to the given pivot.
     * @param greater an empty Queue. When the function completes, this queue will contain
     *                all of the items in unsorted that are greater than the given pivot.
     */
    private static <Item extends Comparable> void partition(Queue<Item> unsorted, Item pivot, Queue<Item> less,
            Queue<Item> equal, Queue<Item> greater) {
        // Your code here!
        for (Item i : unsorted) {
            if (i.compareTo(pivot) < 0) less.enqueue(i);
            else if (i.compareTo(pivot) == 0) equal.enqueue(i);
            else greater.enqueue(i);
        }
    }

    /** Returns a Queue that contains the given items sorted from least to greatest. */
    public static <Item extends Comparable> Queue<Item> quickSort(Queue<Item> items) {
        // TODO: Your code here!
        // base case
        if (items.size() == 1 || items.size() == 0) return items;
        Item pivot = getRandomItem(items);
        Queue<Item> less = new Queue<>();
        Queue<Item> equal = new Queue<>();
        Queue<Item> greater = new Queue<>();
        partition(items, pivot, less, equal, greater);
        Queue<Item> sortedLess = quickSort(less);
        Queue<Item> sortedGreater = quickSort(greater);
        Queue<Item> resultTemp = catenate(sortedLess, equal);
        return catenate(resultTemp, sortedGreater);
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
    public void testQuickSort() {
        Queue<Integer> students = new Queue<>();
        int testNum = 21;
        for (int i = 0; i < testNum; i++) {
            students.enqueue(StdRandom.uniform(10000));
        }
        Queue<Integer> sortedStudent = quickSort(students);
//        System.out.println(students);
//        System.out.println(sortedStudent);
        assertTrue(isQueueSorted(sortedStudent));
    }

    public static void main(String[] args) {
        jh61b.junit.TestRunner.runTests(QuickSort.class);
    }
}
