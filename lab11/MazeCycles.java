import java.util.Observable;

import edu.princeton.cs.algs4.Queue;

/**
 * @author Josh Hug
 */

public class MazeCycles extends MazeExplorer {
    /* Inherits public fields: 
    public int[] distTo;
    public int[] edgeTo;
    public boolean[] marked;
    */
    private boolean containCycle = false;

    public MazeCycles(Maze m) {
        super(m);
    }

    public int getFirstUnmarked() {
        for (int i = 0; i < maze.V(); i++) {
            if (!marked[i]) return i;
        }
        return -1;
    }


    public void bfsSolve() {
        /* Your code here. */
        // also work for unconnected graph
        int s = getFirstUnmarked();
        while (s != -1) {
            Queue<Integer> frontier = new Queue<>();
            frontier.enqueue(s);
            edgeTo[s] = s;
            distTo[s] = 0;
            while (!frontier.isEmpty()) {
                int v = frontier.dequeue();
                marked[v] = true;
                announce();
                for (int w : maze.adj(v)) {
                    if (marked[w] && edgeTo[v] != w) {
                        containCycle = true;
                        return;
                    }
                    if (!marked[w]) {
                        edgeTo[w] = v;
                        announce();
                        distTo[w] = distTo[v] + 1;
                        frontier.enqueue(w);
                    }
                }
            }
            // if there is no cycle in one connected area, choose another start point
            s = getFirstUnmarked();
        }

    }

    @Override
    public void solve() {
        bfsSolve();
        System.out.println("Whether the graph has cycle: " + containCycle);
    }
} 

