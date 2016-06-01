import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;

/**
 * Created by chizhang on 6/1/16.
 */
public class BinaryTrie implements Serializable {

    private Node root;
    private Map<Character, BitSequence> lookupTable;

    public BinaryTrie(Map<Character, Integer> frequencyTable) {
        PriorityQueue<Node> helperPQ = transform(frequencyTable);
        root = construct(helperPQ);
        lookupTable = new HashMap<>();
    }

    public Match longestPrefixMatch(BitSequence querySequence) {
        Node currentNode = root;
        for (int i = 0; i < querySequence.length(); i++) {
            if (currentNode.isLeaf()) {
                return new Match(querySequence.firstNBits(i), currentNode.getCharacter());
            } else if (querySequence.bitAt(i) == 0) {
                currentNode = currentNode.left;
            } else {
                currentNode = currentNode.right;
            }
        }
        return null;
    }

    public Map<Character, BitSequence> buildLookupTable() {
        inOrderTraverse(new BitSequence(), root);
        return this.lookupTable;
    }

    private void inOrderTraverse(BitSequence prefix, Node n) {
        if (n.isLeaf()) {
            this.lookupTable.put(n.getCharacter(), prefix);
            return;
        }
        inOrderTraverse(prefix.appended(0), n.left);
        inOrderTraverse(prefix.appended(1), n.right);
    }

    // private helper method
    private PriorityQueue<Node> transform(Map<Character, Integer> frequencyTable) {
        PriorityQueue<Node> result = new PriorityQueue<>();
        for (Map.Entry<Character, Integer> entry : frequencyTable.entrySet()) {
            result.add(new Node(entry.getKey(), entry.getValue()));
        }
        return result;
    }

    // return the root node after construction
    private Node construct(PriorityQueue<Node> helperPQ) {
        while (true) {
            Node peek = helperPQ.remove();
            if (helperPQ.isEmpty()) {
                return peek;
            }
            Node secondPeek = helperPQ.remove();
            Node combined = new Node(peek.getFrequency() + secondPeek.getFrequency(), peek, secondPeek);
            helperPQ.add(combined);
        }
    }

    private class Node implements Comparable<Node> {

        private Character c;      // if c is null, then it is not a leaf node
        private Integer frequency;
        private Node left;   // pointing to the left node (0)
        private Node right;  // pointing to the right node (1)

        public Node(Character c, Integer frequency) {
            this.c = c;
            this.frequency = frequency;
            left = null;
            right = null;
        }

        public Node(Integer frequency, Node left, Node right) {
            this.c = null;
            this.frequency = frequency;
            this.left = left;
            this.right = right;
        }

        @Override
        public int compareTo(Node n) {
            return this.frequency - n.frequency;
        }

        public boolean isLeaf() {
            return c != null;
        }

        public Integer getFrequency() {
            return frequency;
        }

        public char getCharacter() {
            return c;
        }
    }
}
