/**
 * Created by chizhang on 3/6/16.
 */


public class LinkedListDeque<Item> {
    public class Node {
        public Item item;
        public Node prev;
        public Node next;

        public Node(Item _item, Node _prev, Node _next) {
            item = _item;
            prev = _prev;
            next = _next;
        }
    }

    private Node frontSentinel;
    private Node backSentinel;
    private int size;

    public LinkedListDeque() {
        size = 0;
        frontSentinel = new Node(null, null, null);
        backSentinel = new Node(null, frontSentinel, null);
        frontSentinel.next = backSentinel;
    }

    public void addFirst(Item item) {
        Node newFirstNode = new Node(item, frontSentinel, frontSentinel.next);
        frontSentinel.next.prev = newFirstNode;
        frontSentinel.next = newFirstNode;
        size += 1;
    }

    public void addLast(Item item) {
        Node newLastNode = new Node(item, backSentinel.prev, backSentinel);
        backSentinel.prev.next = newLastNode;
        backSentinel.prev = newLastNode;
        size += 1;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public int size() {
        return size;
    }

    public void printDeque() {
        if (isEmpty()) {
            return;
        }
        Node ptr = frontSentinel.next;
        while (ptr != backSentinel) {
            System.out.print(ptr.item + " ");
            ptr = ptr.next;
        }
        System.out.println();
    }

    public Item removeFirst() {
        if (isEmpty()) {
            return null;
        }
        Node firstItem = frontSentinel.next;
        frontSentinel.next = firstItem.next;
        firstItem.next.prev = frontSentinel;
        size -= 1;
        return firstItem.item;
    }

    public Item removeLast() {
        if (isEmpty()) {
            return null;
        }
        Node lastItem = backSentinel.prev;
        backSentinel.prev = lastItem.prev;
        lastItem.prev.next = backSentinel;
        size -= 1;
        return lastItem.item;
    }

    public Item get(int index) {
        // null cases
        if (size == 0) {
            return null;
        } else if (index < 0 || index >= size) {
            return null;
        }

        if (index > size / 2) {
            Node ptr = backSentinel;
            while (size - index > 0) {
                ptr = ptr.prev;
                index += 1;
            }
            return ptr.item;
        } else {
            Node ptr = frontSentinel.next;
            while (index > 0) {
                ptr = ptr.next;
                index -= 1;
            }
            return ptr.item;
        }
    }

    private Item getRecursiveHelper(Node currNode, int index) {
        if (index == 0) {
            return currNode.item;
        } else {
            return getRecursiveHelper(currNode.next, index - 1);
        }
    }

    public Item getRecursive(int index) {
        // null cases
        if (size == 0) {
            return null;
        } else if (index < 0 || index >= size) {
            return null;
        }

        return getRecursiveHelper(frontSentinel.next, index);
    }
}
