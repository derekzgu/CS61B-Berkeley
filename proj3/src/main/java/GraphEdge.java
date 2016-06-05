/**
 * Created by chizhang on 6/4/16.
 */
public class GraphEdge {  // only constructed when encounter ways, we assume it is a indirected graph
    GraphNode n1, n2;

    // it seems that we don't need to track the edge id
    public GraphEdge(GraphNode n1, GraphNode n2) {
        this.n1 = n1;
        this.n2 = n2;
        n1.addEdge(this);
        n2.addEdge(this);
    }

    public double getDistance() {
        return Position.euclideanDistance(n1.getPosition(), n2.getPosition());
    }

    public GraphNode getOtherNode(GraphNode n) {
        if (n.equals(n1)) return n2;
        else if (n.equals(n2)) return n1;
        else return null;
    }
}
