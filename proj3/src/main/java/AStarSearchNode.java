import java.util.ArrayList;
import java.util.List;

/**
 * Created by chizhang on 6/5/16.
 */
// private helper class
public class AStarSearchNode implements Comparable<AStarSearchNode> {
    private GraphNode currentNode;
    private Double currentRouteLength;
    private GraphNode target;
    private Double priority;

    public AStarSearchNode(GraphNode n, Double currentRouteLength, GraphNode target) {
        this.currentNode = n;
        this.currentRouteLength = currentRouteLength; // for UCS
        this.target = target;
        this.priority = Position.euclideanDistance(n.getPosition(), target.getPosition()) + currentRouteLength;
    }

    public AStarSearchNode(GraphNode n, GraphNode target) {   // this constructor is used for root node
        this(n, 0.0, target);
    }

    public GraphNode getNode() {
        return this.currentNode;
    }

    public Double getPriority() {
        return this.priority;
    }

    public Double getCurrentRouteLength() {
        return currentRouteLength;
    }

    @Override
    public int compareTo(AStarSearchNode n) {
        return this.getPriority().compareTo(n.getPriority());
    }

    public Iterable<AStarSearchNode> getSuccessor() {
        Iterable<GraphNode> neighborNodes = this.currentNode.getNeighbors();
        List<AStarSearchNode> successors = new ArrayList<>();
        for (GraphNode n : neighborNodes) {
            double distance = Position.euclideanDistance(n.getPosition(), this.currentNode.getPosition());
            successors.add(new AStarSearchNode(n, this.getCurrentRouteLength() + distance, this.target));
        }
        return successors;
    }

    public boolean isGoal() {
        return currentNode.equals(target);
    }
}
