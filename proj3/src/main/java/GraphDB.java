import org.xml.sax.SAXException;

import java.io.File;
import java.io.IOException;
import java.util.*;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

/**
 * Wraps the parsing functionality of the MapDBHandler as an example.
 * You may choose to add to the functionality of this class if you wish.
 * @author Alan Yao
 */
public class GraphDB {

    // a graph is a map from id to GraphNode

    private Map<Long, GraphNode> nodeMap;
    private Trie locations;
    /**
     * Example constructor shows how to create and start an XML parser.
     * @param db_path Path to the XML file to be parsed.
     */
    public GraphDB(String db_path) {
        nodeMap = new HashMap<>();
        locations = new Trie();
        try {
            File inputFile = new File(db_path);
            SAXParserFactory factory = SAXParserFactory.newInstance();
            SAXParser saxParser = factory.newSAXParser();
            // we pass this to the MapDBHandler, all the state records can be taken care at MapDBHandler
            MapDBHandler maphandler = new MapDBHandler(this);
            saxParser.parse(inputFile, maphandler);
        } catch (ParserConfigurationException | SAXException | IOException e) {
            e.printStackTrace();
        }
        clean();
    }

    // public helper method
    public void addNode(long id, GraphNode n) {
        this.nodeMap.put(id, n);
    }

    // add edge
    public void addEdge(long id1, long id2) {
        GraphNode n1 = nodeMap.get(id1);
        GraphNode n2 = nodeMap.get(id2);
        if (n1 == null || n2 == null) throw new IllegalStateException("Can't adding edge for non-exist node");
        GraphEdge e = new GraphEdge(n1, n2);  // all the work is done in the constructor
    }

    // get node
    public GraphNode getNode(long id) {
        return nodeMap.get(id);
    }

    public GraphNode getClosestNode(Position p) {
        double minDistance = Integer.MAX_VALUE;
        GraphNode target = null;
        for (Map.Entry<Long, GraphNode> entry : this.nodeMap.entrySet()) {
            double currentDistance = Position.euclideanDistance(entry.getValue().getPosition(), p);
            if (currentDistance < minDistance) {
                target = entry.getValue();
                minDistance = currentDistance;
            }
        }
        return target;
    }

    // insert by name to the locations (Trie), each TrieNode contains a list of GraphNodes
    public void insertByName(String name, GraphNode n) {
        this.locations.insert(name, n);
    }

    public List<String> getLocationsByPrefix(String prefix) {
        return this.locations.getNamesByPrefix(prefix);
    }

    public List<GraphNode> getListGraphNodeByName(String locationName) {
        TrieNode n = this.locations.searchNode(locationName);
        return n.getGraphNodes();
    }

    /**
     * Helper to process strings into their "cleaned" form, ignoring punctuation and capitalization.
     * @param s Input string.
     * @return Cleaned string.
     */
    static String cleanString(String s) {
        return s.replaceAll("[^a-zA-Z ]", "").toLowerCase();
    }

    /**
     *  Remove nodes with no connections from the graph.
     *  While this does not guarantee that any two nodes in the remaining graph are connected,
     *  we can reasonably assume this since typically roads are connected.
     */
    private void clean() {
        List<Long> toBeRemoved = new ArrayList<>();
        for (Map.Entry<Long, GraphNode> entry : this.nodeMap.entrySet()) {
            // if the GraphNode is isolated
            if (!entry.getValue().hasNeighbor()) {
                toBeRemoved.add(entry.getKey());
            }
        }
        for (long key : toBeRemoved) {
            this.nodeMap.remove(key);
        }
    }
}
