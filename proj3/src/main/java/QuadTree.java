import java.util.ArrayDeque;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.Queue;

/**
 * Created by chizhang on 6/2/16.
 */
public class QuadTree {

    private QuadNode root;  // the root doesn't contain any information
    private final String rootImage = "root";
    private int maxDepth;

    public QuadTree(int maxDepth) {
        this.maxDepth = maxDepth;
        this.root = bfsConstructor();
    }

    // return a list of String representing imageNames which intersects with the params
    public List<QuadNode> respond(Map<String, Double> params) {
        // retrieve the information from params
        Position queryUpperLeft = new Position(params.get("ullon"), params.get("ullat"));
        Position queryLowerRight = new Position(params.get("lrlon"), params.get("lrlat"));
        double userWidthInPixel = params.get("w");
        // double userHeightInPixel = params.get("h");
        // calculate the query distance/pixel, which is the upper bound of the traverse
        double queryDistancePerPixel = (queryLowerRight.getLongitude() - queryUpperLeft.getLongitude()) / userWidthInPixel;
        // calculate the corresponding depth
        int depth, temp;
        for (depth = 0, temp = 1; depth < maxDepth; depth++, temp*=2) {
            double currentDistancePerPixel = (MapServer.ROOT_LRLON - MapServer.ROOT_ULLON) / MapServer.TILE_SIZE / temp;
            if (currentDistancePerPixel <= queryDistancePerPixel) break;
        }
        // given the depth, find all the tiles which are intersect with query
        return bfsTraverse(queryUpperLeft, queryLowerRight, depth);
    }

    // some helper methods
    // traverse the QuadTree and return the correct QuadNode given depth and query Position
    // when using dfs, the output sequence
    private List<QuadNode> bfsTraverse(Position queryUpperLeft, Position queryLowerRight, int depth) {
        if (depth > maxDepth) throw new IllegalArgumentException("depth can't exceed maxDepth/");
        Queue<QuadNode> frontier = new ArrayDeque<>();
        QuadNode rootNode = this.root;
        List<QuadNode> result = new ArrayList<>();
        frontier.add(rootNode);
        while (!frontier.isEmpty()) {
            QuadNode currentNode = frontier.remove();
            if (Position.isOverlap(queryUpperLeft, queryLowerRight, currentNode.getUpperLeftPosition(),
                    currentNode.getLowerRightPosition())) {
                if (currentNode.getDepth() == depth) {
                    result.add(currentNode);
                    continue;
                }
                for (int dir = 1; dir <= 4; dir++) {
                    QuadNode child = currentNode.getChild(dir);
                    if (Position.isOverlap(queryUpperLeft, queryLowerRight, child.getUpperLeftPosition(),
                            child.getLowerRightPosition())) {
                        frontier.add(child);   // add to frontier only overlapped
                    }
                }
            }
        }
        return result;
    }

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
            for (int dir = 1; dir <= 4; dir++) {
                QuadNode child = nodeConstructor(currentNode, dir);
                currentNode.setChild(child, dir);
                frontier.add(child);
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
