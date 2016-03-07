/**
 * Created by chizhang on 3/6/16.
 */

public class ArrayDeque<Item> {
    private int frontIndex;
    private int backIndex;
    private Item[] items;
    private int size;
    private static int RFACTOR = 2;

    public ArrayDeque() {
        size = 0;
        items = (Item[]) new Object[8];
        frontIndex = 0;
        backIndex = 0;
    }

    private void resize(int capacity) {
        Item[] newitem = (Item[]) new Object[capacity];
        if (frontIndex > backIndex) {
            System.arraycopy(items, frontIndex, newitem, 0, size - frontIndex);
            System.arraycopy(items, 0, newitem, size - frontIndex, backIndex + 1);
        } else {
            System.arraycopy(items, frontIndex, newitem, 0, size);
        }
        frontIndex = 0;
        backIndex = size - 1;
    }

    public void addFirst(Item item) {
        if (size == items.length) {
            resize(size * RFACTOR);
        }
        if (frontIndex == 0) {
            frontIndex = items.length - 1;
        } else {
            frontIndex -= 1;
        }
        items[frontIndex] = item;
        size += 1;
    }

    public void addLast(Item item) {
        if (size == items.length) {
            resize(size * RFACTOR);
        }
        if (backIndex == items.length) {
            backIndex = 0;
        } else {
            backIndex = backIndex + 1;
        }
        items[backIndex] = item;
        size += 1;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public int size() {
        return size;
    }

    public void printDeque() {
        if (backIndex > frontIndex) {
            for (int i = frontIndex; i < backIndex + 1; i++) {
                System.out.print(items[i] + " ");
            }
            System.out.println();
        } else {
            for (int i = frontIndex; i < items.length; i++) {
                System.out.print(items[i] + " ");
            }
            for (int i = 0; i < backIndex + 1; i++) {
                System.out.print(items[i] + " ");
            }
            System.out.println();
        }
    }

    private void shrink() {
        int capacity = items.length;
        if (size > 15 && size < capacity / 4) {
            resize(capacity / 2);
        }
    }


    public Item removeFirst() {
        Item oldFirst= items[frontIndex];
        frontIndex = (frontIndex + 1) % items.length;
        size -= 1;
        shrink();
        return oldFirst;
    }

    public Item removeLast() {
        Item oldLast = items[backIndex];
        backIndex = (backIndex - 1) % items.length;
        size -= 1;
        shrink();
        return oldLast;
    }

    public Item get(int index) {
        int actualIndex = (index + frontIndex) % items.length;
        return items[actualIndex];
    }
}
