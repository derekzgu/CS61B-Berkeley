import java.util.ArrayDeque;
import java.util.Queue;

/**
 * Created by chizhang on 6/2/16.
 */
public class QuadTree {

    private QuadNode root;  // the root doesn't contain any information
    private final String rootImage = "root.png";
    private int maxDepth;

    public QuadTree(String imageRoot, int maxDepth) {
        this.maxDepth = maxDepth;

    }


    // some helper methods

    // construct the QuadTree using bfs
    private void bfsConstructor() {
        Queue<QuadNode> frontier = new ArrayDeque<>();
        root = new QuadNode(MapServer.ROOT_ULLON, MapServer.ROOT_ULLAT, MapServer.ROOT_LRLON, MapServer.ROOT_LRLAT,
                rootImage, 0);
        frontier.add(root);
        while (!frontier.isEmpty()) {
            QuadNode currentNode = frontier.remove();

        }
    }

}
