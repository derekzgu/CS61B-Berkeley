package hw2;                       

import edu.princeton.cs.algs4.WeightedQuickUnionUF;
import java.util.List;
import java.util.ArrayList;
import java.util.Set;
import java.util.HashSet;

public class Percolation {

    private WeightedQuickUnionUF disjointSet;
    // for disjoint set with virtual set, let N^2 to be the top virtual site and N^2+1 to be the bottom virtual set
    private WeightedQuickUnionUF disjointSetWithVirtualSite;
    private int dimension;
    private Set<Integer> openSiteSet;

    // create N-by-N grid, with all sites initially blocked
    public Percolation(int N) {
        dimension = N;
        disjointSet = new WeightedQuickUnionUF(N * N + 1);  // just have the top virtual set
        disjointSetWithVirtualSite = new WeightedQuickUnionUF(N * N + 2);  // used for percolated
        openSiteSet = new HashSet<>();
    }

    // open the site (row, col) if it is not open already
    public void open(int row, int col) {
        if (!isPosValid(row, col)) throw new IndexOutOfBoundsException("invalid row or col argument.");

        int currentPos = xyToInt(row, col);
        List<Integer> neighborList = getNeighbor(row, col);
        for (int neighbor : neighborList) {
            disjointSet.union(currentPos, neighbor);
            disjointSetWithVirtualSite.union(currentPos, neighbor);
        }
        if (row == 0) {
            disjointSet.union(currentPos, getTopVirtual());
            disjointSetWithVirtualSite.union(currentPos, getTopVirtual());
        }
        if (row == dimension - 1) {
            disjointSetWithVirtualSite.union(currentPos, getBottomVirtual());
        }
        openSiteSet.add(currentPos);
    }

    // is the site (row, col) open?
    public boolean isOpen(int row, int col) {
        if (!isPosValid(row, col)) throw new IndexOutOfBoundsException("invalid row or col argument.");
        return openSiteSet.contains(xyToInt(row, col));
    }

    // is the site (row, col) full?
    public boolean isFull(int row, int col) {
        if (!isPosValid(row, col)) throw new IndexOutOfBoundsException("invalid row or col argument.");
        return disjointSet.connected(xyToInt(row, col), getTopVirtual());
    }

    // number of open sites
    public int numberOfOpenSites() {
        return openSiteSet.size();
    }

    // does the system percolate?
    public boolean percolates() {
        return disjointSetWithVirtualSite.connected(getTopVirtual(), getBottomVirtual());
    }

    // unit testing (not required)
    public static void main(String[] args) {

    }



    //private helper method
    private int xyToInt(int row, int col) {
        return dimension * row + col;
    }

    private List<Integer> getNeighbor(int row, int col) {
        // up
        List<Integer> neighborList = new ArrayList<>();
        if (isPosValid(row, col + 1)) {
            neighborList.add(xyToInt(row, col + 1));
        }
        if (isPosValid(row, col - 1)) {
            neighborList.add(xyToInt(row, col - 1));
        }
        if (isPosValid(row + 1, col)) {
            neighborList.add(xyToInt(row + 1, col));
        }
        if (isPosValid(row - 1, col)) {
            neighborList.add(xyToInt(row - 1, col));
        }
        return neighborList;
    }

    private boolean isPosValid(int row, int col) {
        return (row >= 0 && row < dimension && col >= 0 && col < dimension);
    }

    private int getTopVirtual() {
        return dimension * dimension;
    }

    private int getBottomVirtual() {
        return dimension * dimension + 1;
    }

}
