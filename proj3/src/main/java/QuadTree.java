import java.util.ArrayDeque;
import java.util.Queue;

/**
 * Created by chizhang on 6/2/16.
 */
public class QuadTree {

    private QuadNode root;  // the root doesn't contain any information
    private final String rootImage = "root.png";
    private int maxDepth;

    public QuadTree(int maxDepth) {
        this.maxDepth = maxDepth;
        this.root = bfsConstructor();
    }


    // some helper methods

    // construct the QuadTree using bfs, return the root node
    private QuadNode bfsConstructor() {
        Queue<QuadNode> frontier = new ArrayDeque<>();
        QuadNode rootNode = new QuadNode(MapServer.ROOT_ULLON, MapServer.ROOT_ULLAT, MapServer.ROOT_LRLON, MapServer.ROOT_LRLAT,
                rootImage, 0);
        frontier.add(rootNode);
        while (!frontier.isEmpty()) {
            QuadNode currentNode = frontier.remove();
            // when reach the bottom of the QuadTree
            if (currentNode.getDepth() >= maxDepth) continue;
            for (int i = 1; i <= 4; i++) {
                frontier.add(nodeConstructor(currentNode, i));
            }
        }
        return rootNode;
    }

    // give a parent QuadNode, return the corresponding child QuadNode
    private QuadNode nodeConstructor(QuadNode parent, int direction) {
        Position helperPos;
        switch (direction) {
            case 1: helperPos = parent.getUpperLeftPosition(); break;
            case 2: helperPos = new Position(parent.getLowerRightPosition().getLongitude(),
                    parent.getUpperLeftPosition().getLatitude()); break;
            case 3: helperPos = new Position(parent.getUpperLeftPosition().getLongitude(),
                    parent.getLowerRightPosition().getLatitude()); break;
            case 4: helperPos = parent.getLowerRightPosition(); break;
            default: throw new IllegalArgumentException("illegal direction argument.");
        }
        Position newUpperLeft = Position.calculateMiddle(parent.getUpperLeftPosition(), helperPos);
        Position newLowerRight = Position.calculateMiddle(parent.getLowerRightPosition(), helperPos);
        String newPicture = getNextPicture(parent.getPicture(), direction);
        int newDepth = parent.getDepth() + 1;
        return new QuadNode(newUpperLeft, newLowerRight, newPicture, newDepth);
    }

    private String getNextPicture(String parentPic, int direction) {
        if (parentPic.equals(rootImage)) return Integer.toString(direction);
        else return parentPic + Integer.toString(direction);
    }

}
