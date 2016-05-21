/**
 * Created by chizhang on 5/20/16.
 */
public interface Deque<Item> {

    void addFirst(Item e);
    void addLast(Item e);
    boolean isEmpty();
    int size();
    void printDeque();
    Item get(int i);
    Item removeFirst();
    Item removeLast();
}
