package lab8;

import java.security.Key;
import java.util.Iterator;
import java.util.Set;

/**
 * Created by chizhang on 5/24/16.
 */
public class BSTMap<K extends Comparable<K>, V> implements Map61B<K, V> {

    private Node root;
    private int numItems;

    public BSTMap() {
        root = null;
        numItems = 0;
    }

    public void clear() {
        root = null;
        numItems = 0;
    }

    /* Returns true if this map contains a mapping for the specified key. */
    public boolean containsKey(K key) {
        return get(key) != null;
    }

    /* Returns the value to which the specified key is mapped, or null if this
     * map contains no mapping for the key.
     */
    public V get(K key) {
        return get(root, key);
    }

    private V get(Node n, K key) {
        if (n == null) return null;
        else if (key.compareTo(n.key) < 0) return get(n.left, key);
        else if (key.compareTo(n.key) > 0) return get(n.right, key);
        else return n.value;
    }

    /* Returns the number of key-value mappings in this map. */
    public int size() {
        return numItems;
    }

    /* Associates the specified value with the specified key in this map. */
    public void put(K key, V value) {
        root = put(root, key, value);
        numItems += 1;
    }

    private Node put(Node n, K key, V value) {
        if (n == null) {
            return new Node(key, value);
        }
        if (key.compareTo(n.key) < 0) {
            n.left = put(n.left, key, value);
        } else if (key.compareTo(n.key) > 0) {
            n.right = put(n.right, key, value);
        } else {
            n.value = value;
        }
        return n;
    }

    /* Returns a Set view of the keys contained in this map. */
    public Set<K> keySet() {
        throw new UnsupportedOperationException("keySet is not supported.");
    }

    /* Removes the mapping for the specified key from this map if present.
     * Not required for Lab 8. If you don't implement this, throw an
     * UnsupportedOperationException. */
    public V remove(K key) {
        throw new UnsupportedOperationException("remove is not supported.");
    }

    /* Removes the entry for the specified key only if it is currently mapped to
     * the specified value. Not required for Lab 8. If you don't implement this,
     * throw an UnsupportedOperationException.*/
    public V remove(K key, V value) {
        throw new UnsupportedOperationException("remove is not supported.");
    }

    public Iterator<K> iterator() {
        throw new UnsupportedOperationException("iterator is not supported.");
    }

    private class Node {
        private K key;
        private V value;
        private Node left;
        private Node right;

        public Node(K key, V value) {
            this.key = key;
            this.value = value;
        }
    }



}
