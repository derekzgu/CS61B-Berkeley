import java.util.*;

/**
 * Created by chizhang on 6/5/16.
 */
public class Trie {
    TrieNode root;

    public Trie() {
        root = new TrieNode();
    }

    public void insert(String name, GraphNode n) {
        TrieNode currentTrieNode = root;
        for (int i = 0; i < name.length(); i++) {
            char c = name.charAt(i);
            if (!currentTrieNode.hasChar(c)) {
                // construct a new node
                TrieNode newTrieNode = new TrieNode(c);
                currentTrieNode.addChild(c, newTrieNode);
            }
            currentTrieNode = currentTrieNode.getSuccessor(c);
        }
        // the end of an insertion
        currentTrieNode.setToWord();
        currentTrieNode.addGraphNode(n);
    }

    public List<String> getNamesByPrefix(String prefix) {
        // step 1: find the TrieNode given the prefix
        TrieNode prefixTrieNode = searchNode(prefix);
        if (prefixTrieNode == null) return new ArrayList<>();
        // step 2: find all the names
        return findNamesByNode(prefix, prefixTrieNode);
    }

    // find the TrieNode given exact name
    public TrieNode searchNode(String name) {
        TrieNode currentTrieNode = root;
        for (int i = 0; i < name.length(); i++) {
            char c = name.charAt(i);
            if (!currentTrieNode.hasChar(c)) return null;
            currentTrieNode = currentTrieNode.getSuccessor(c);
        }
        return currentTrieNode;
    }

    // private helper method
    // find all the names given start node and prefix, use dfs and recursion
    private List<String> findNamesByNode(String prefix, TrieNode start) {
        List<String> result = new ArrayList<>();
        if (start.isWord()) {
            result.add(start.getFullName());
        }
        if (start.isLeaf()) return result;
        Map<Character, TrieNode> children = start.getChildren();
        for (Map.Entry<Character, TrieNode> entry : children.entrySet()) {
            char c = entry.getKey();
            TrieNode s = entry.getValue();
            List<String> tempResult = findNamesByNode(prefix + Character.toString(c), s);
            result.addAll(tempResult);
        }
        return result;
    }
}
