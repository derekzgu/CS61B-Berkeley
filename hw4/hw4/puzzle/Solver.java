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
        return solutionList.size();
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
        for (Board board : solver.solution()) {
            StdOut.println(board);
       }
    }

    // helper method
    private List<Board> AStarSearch(Board initial) {
        List<Board> solution = new ArrayList<>();
        MinPQ<SearchNode> helperPQ = new MinPQ<>();
        Set<Board> exploredSet = new HashSet<>();

        int moves = 0;
        SearchNode initialNode = new SearchNode(initial, moves);
        helperPQ.insert(initialNode);

        while (!helperPQ.isEmpty()) {
            SearchNode n = helperPQ.delMin();
            Board currentBoard = n.getBoard();
            moves = n.getMoves();
            solution.add(currentBoard);
            if (currentBoard.isGoal()) {
                return solution;
            }
            if (exploredSet.contains(currentBoard)) {
                continue;
            }
            exploredSet.add(currentBoard);
            for (Board b : BoardUtils.neighbors(currentBoard)) {
                helperPQ.insert(new SearchNode(b, moves + 1));
            }
        }
        return null;
    }

    // a solver node
    private class SearchNode implements Comparator<SearchNode> {
        private Board b;
        private int priority;
        private int moves;

        public SearchNode(Board b, int moves) {
            this.b = b;
            priority = moves + b.manhattan();  // compute once and cache it
            this.moves = moves;
        }

        public Board getBoard() {
            return b;
        }

        public int getMoves() {
            return moves;
        }

        public int compare(SearchNode n1, SearchNode n2) {
            return n1.priority - n2.priority;
        }
    }

}
