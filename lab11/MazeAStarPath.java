import java.util.Observable;
import java.util.PriorityQueue;

/**
 *  @author Josh Hug
 */

public class MazeAStarPath extends MazeExplorer {
    private int s;
    private int t;
    private boolean targetFound = false;
    private Maze maze;

    public MazeAStarPath(Maze m, int sourceX, int sourceY, int targetX, int targetY) {
        super(m);
        maze = m;
        s = maze.xyTo1D(sourceX, sourceY);
        t = maze.xyTo1D(targetX, targetY);
        distTo[s] = 0;
        edgeTo[s] = s;
    }

    /** Estimate of the distance from v to the target. */
    private int h(int v) {
        int sourceX = maze.toX(v);
        int sourceY = maze.toY(v);
        int targetX = maze.toX(t);
        int targetY = maze.toY(t);
        return Math.abs(sourceX - targetX) + Math.abs(sourceY - targetY);
    }

    /** Finds vertex estimated to be closest to target. */
    private int findMinimumUnmarked() {
        return -1;
        /* You do not have to use this method. */
    }

    /** Performs an astar search from vertex s. */
    private void astar(int s) {
        PriorityQueue<SearchNode> frontier = new PriorityQueue<>();
        frontier.add(new SearchNode(s, distTo[s]));
        while (!frontier.isEmpty()) {
            SearchNode n = frontier.remove();
            int v = n.getV();
            if (marked[v]) {
                continue;
            }
            marked[v] = true;
            announce();
            if (v == t) {
                targetFound = true;
                return;
            }
            for (int w : maze.adj(v)) {
                if (!marked[w]) {
                    edgeTo[w] = v;
                    announce();
                    distTo[w] = distTo[v] + 1;
                    frontier.add(new SearchNode(w, distTo[w]));
                }
            }
        }
    }

    @Override
    public void solve() {
        astar(s);
    }

    private class SearchNode implements Comparable<SearchNode> {
        private int v;
        private int priority;

        public SearchNode(int v, int dist) {
            this.v = v;
            this.priority = dist + h(v);
        }

        public int getPriority() {
            return priority;
        }

        public int getV() {
            return v;
        }

        public int compareTo(SearchNode n) {
            return priority - n.getPriority();
        }
    }

}

