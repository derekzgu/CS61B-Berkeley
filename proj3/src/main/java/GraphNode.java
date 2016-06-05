import java.util.*;

/**
 * Created by chizhang on 6/4/16.
 */

/* We represent graph as an adjacency list, each node
 */
public class GraphNode {
    private long id;
    private Position p;
    private Set<GraphEdge> edges;  // a set of all the edges connected to this node
    private String name;

    public GraphNode(double longitude, double latitude, long id) {
        this.p = new Position(longitude, latitude);
        this.id = id;
        this.edges = new HashSet<>();
        this.name = "";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        GraphNode graphNode = (GraphNode) o;

        return id == graphNode.id;

    }

    @Override
    public int hashCode() {
        return (int) (id ^ (id >>> 32));
    }

    @Override
    public String toString() {
        return "GraphNode{" +
                "id=" + id +
                '}';
    }

    public void addEdge(GraphEdge e) {
        this.edges.add(e);
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean hasName() {
        return this.name.length() != 0;
    }

    public boolean hasNeighbor() {
        return this.edges.size() != 0;
    }

    // return an iterable containing all the neighbors of the current node
    public Iterable<GraphNode> getNeighbors() {
        List<GraphNode> neighborList = new ArrayList<>();
        for (GraphEdge e : edges) {
            neighborList.add(e.getOtherNode(this));
        }
        return neighborList;
    }

    public Position getPosition() {
        return this.p;
    }

    public long getId() {
        return id;
    }
}
