package hw4.puzzle;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

import edu.princeton.cs.algs4.MinPQ;

import java.util.*;

public class Solver {

    List<Board> solutionList;

    public Solver(Board initial) {
        this.solutionList = AStarSearch(initial);
    }

    public int moves() {
        return solutionList.size() - 1;
    }

    public Iterable<Board> solution() {
        return solutionList;
    }

    // DO NOT MODIFY MAIN METHOD
    // Uncomment this method once your Solver and Board classes are ready.
    public static void main(String[] args) {
        In in = new In(args[0]);
        int N = in.readInt();
        int[][] tiles = new int[N][N];
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                tiles[i][j] = in.readInt();
            }
        }
        Board initial = new Board(tiles);
        Solver solver = new Solver(initial);
        StdOut.println("Minimum number of moves = " + solver.moves());
        if (args.length > 1) {
            for (Board board : solver.solution()) {
                StdOut.println(board);
            }
        }
    }

    // helper method
    private List<Board> AStarSearch(Board initial) {
        MinPQ<SearchNode> helperPQ = new MinPQ<>();
        Set<Board> exploredSet = new HashSet<>();
        List<Board> solution = new ArrayList<>();
        solution.add(initial);
        int moves = 0;
        SearchNode initialNode = new SearchNode(initial, moves, solution);
        helperPQ.insert(initialNode);

        while (!helperPQ.isEmpty()) {
            SearchNode n = helperPQ.delMin();
            Board currentBoard = n.getBoard();
            moves = n.getMoves();
            if (currentBoard.isGoal()) {
                return n.getRoute();
            }
            if (exploredSet.contains(currentBoard)) {
                continue;
            }
            exploredSet.add(currentBoard);
            for (Board b : BoardUtils.neighbors(currentBoard)) {
                if (!exploredSet.contains(b)) {
                    List<Board> currentRoute = n.getCopyRoute();   // this function return a copy
                    currentRoute.add(b);
                    helperPQ.insert(new SearchNode(b, moves + 1, currentRoute));
                }
            }
        }
        return null;
    }

    // a solver node
    private class SearchNode implements Comparable<SearchNode> {
        private Board b;
        private int priority;
        private int moves;
        private List<Board> route;

        public SearchNode(Board b, int moves, List<Board> route) {
            this.b = b;
            priority = moves + b.manhattan();  // compute once and cache it
            this.moves = moves;
            this.route = route;  // route must be created on copy!!!
        }

        public Board getBoard() {
            return b;
        }

        public int getMoves() {
            return moves;
        }

        public int compareTo(SearchNode n) {
            return this.priority - n.priority;
        }

        // this method return a copy of current route
        public List<Board> getCopyRoute() {
            return new ArrayList<>(route);
        }

        public List<Board> getRoute() {
            return route;
        }

    }

}
