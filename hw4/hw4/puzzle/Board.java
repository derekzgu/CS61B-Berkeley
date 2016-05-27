package hw4.puzzle;
import edu.princeton.cs.algs4.In;

public class Board {

    private int[][] tiles;

    public Board(int[][] tiles) {
        // copy another tiles so that the board is not immutable
        if (tiles.length != tiles[0].length) throw new IllegalArgumentException("Invalid board");
        int boardSize = tiles.length;
        this.tiles = new int[boardSize][boardSize];
        for (int i = 0; i < boardSize; i++) {
            System.arraycopy(tiles[i], 0, this.tiles[i], 0, boardSize);
        }
    }

    public int tileAt(int i, int j) {
        return this.tiles[i][j];
    }

    public int size() {
        return tiles.length;
    }

    public int hamming() {
        int totalNumTilesInWrongPosition = 0;
        for (int i = 0; i < size(); i++) {
            for (int j = 0; j < size(); j++) {
                if (!isInCorrectPosition(i, j)) totalNumTilesInWrongPosition += 1;
            }
        }
        return totalNumTilesInWrongPosition;
    }

    public int manhattan() {
        int totalManhattanDistance = 0;
        for (int i = 0; i < size(); i++) {
            for (int j = 0; j < size(); j++) {
                if (tileAt(i, j) != 0) {
                    int correctX = goalPosX(tileAt(i, j));
                    int correctY = goalPosY(tileAt(i, j));
                    totalManhattanDistance += manhattanDistance(i, j, correctX, correctY);
                }
            }
        }
        return totalManhattanDistance;
    }

    public boolean isGoal() {
        return manhattan() == 0 || hamming() == 0;
    }

    public boolean equals(Object y) {
        if (y.getClass() != this.getClass()) return false;
        Board boardY = (Board) y;
        if (boardY.size() != this.size()) return false;
        for (int i = 0; i < size(); i++) {
            for (int j = 0; j < size(); j++) {
                if (boardY.tileAt(i, j) != this.tileAt(i, j)) return false;
            }
        }
        return true;
    }

    /** Returns the string representation of the board. 
      * Uncomment this method. */
    public String toString() {
        StringBuilder s = new StringBuilder();
        int N = size();
        s.append(N + "\n");
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                s.append(String.format("%2d ", tileAt(i,j)));
            }
            s.append("\n");
        }
        s.append("\n");
        return s.toString();
    }

    // used for Unit Test
    public static void main(String[] args) {
        In in = new In("../input/TestMaze.txt");
        int N = in.readInt();
        int[][] tiles = new int[N][N];
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                tiles[i][j] = in.readInt();
            }
        }
        Board initial = new Board(tiles);
        System.out.println(initial.hamming());
        System.out.println(initial.manhattan());
    }

    // private helper method
    // return the goal tile at (i, j)
    private int goalTile(int row, int col) {
        return row * size() + col + 1;
    }

    // return the goal position of num
    private int goalPosX(int num) {
        return (num - 1) / size();
    }

    private int goalPosY(int num) {
        return (num - 1) % size();
    }

    private boolean isInCorrectPosition(int row, int col) {
        return tileAt(row, col) == 0 || goalTile(row, col) == tileAt(row, col);
    }

    private int manhattanDistance(int x1, int y1, int x2, int y2) {
        return Math.abs(x1 - x2) + Math.abs(y1 - y2);
    }
}
