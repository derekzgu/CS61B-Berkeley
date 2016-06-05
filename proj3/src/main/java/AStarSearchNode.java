import java.util.LinkedList;
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
    private LinkedList<GraphNode> route;

    public AStarSearchNode(GraphNode n, Double currentRouteLength, GraphNode target, LinkedList<GraphNode> parentRoute) {
        this.currentNode = n;
        this.currentRouteLength = currentRouteLength; // for UCS
        this.target = target;
        this.priority = Position.euclideanDistance(n.getPosition(), target.getPosition()) + currentRouteLength;
        if (parentRoute != null) {
            this.route = new LinkedList<>(parentRoute);
        } else {
            this.route = new LinkedList<>();
        }
        this.route.add(n);
    }

    public AStarSearchNode(GraphNode n, GraphNode target) {   // this constructor is used for root node
        this(n, 0.0, target, null);
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

    public LinkedList<GraphNode> getRoute() {
        return route;
    }

    @Override
    public int compareTo(AStarSearchNode n) {
        return this.getPriority().compareTo(n.getPriority());
    }

    @Override
    public String toString() {
        return "AStarSearchNode{" +
                "currentNode=" + currentNode +
                ", currentRouteLength=" + currentRouteLength +
                '}';
    }

    public Iterable<AStarSearchNode> getSuccessor() {
        Iterable<GraphNode> neighborNodes = this.currentNode.getNeighbors();
        List<AStarSearchNode> successors = new LinkedList<>();
        for (GraphNode n : neighborNodes) {
            double distance = Position.euclideanDistance(n.getPosition(), this.currentNode.getPosition());
            successors.add(new AStarSearchNode(n, this.getCurrentRouteLength() + distance, this.target, this.getRoute()));
        }
        return successors;
    }

    public boolean isGoal() {
        return currentNode.equals(target);
    }
}
