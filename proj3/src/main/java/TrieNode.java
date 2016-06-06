import java.util.*;

/**
 * Created by chizhang on 6/5/16.
 */
public class TrieNode {
    private Character c;
    private Map<Character, TrieNode> children;
    private List<GraphNode> nodes;   // a set of GraphNode which has the same name
    private boolean isWord;

    public TrieNode(Character c) {
        this.c = c;
        this.children = new HashMap<>();
        this.nodes = new ArrayList<>();
        this.isWord = false;
    }

    public TrieNode() {
        this(null);
    }

    public boolean hasChar(Character c) {
        return children.containsKey(c);
    }

    public boolean isLeaf() {
        return children.size() == 0;
    }

    public void addChild(Character c, TrieNode n) {
        children.put(c, n);
    }

    // this function can only be called at the end of a string match
    public void addGraphNode(GraphNode n) {
        nodes.add(n);
    }

    public String getFullName() {
        return nodes.get(0).getName();
    }

    public TrieNode getSuccessor(Character c) {
        return children.get(c);
    }

    public Map<Character, TrieNode> getChildren() {
        return children;
    }

    public void setToWord() {
        this.isWord = true;
    }

    public boolean isWord() {
        return isWord;
    }

    public List<GraphNode> getGraphNodes() {
        return this.nodes;
    }
}
