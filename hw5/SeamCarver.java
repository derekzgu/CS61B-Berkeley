/**
 * Created by chizhang on 5/28/16.
 */
import edu.princeton.cs.algs4.Picture;

import java.awt.Color;
import java.util.Arrays;

public class SeamCarver {

    private Picture picture;
    private boolean transpose;
    private double[][] energyMatrix;

    public SeamCarver(Picture picture) {
        // copy a new picture as the private local variable
        this.picture = new Picture(picture);
        transpose = false;
        energyMatrix = new double[picture.width()][picture.height()];
        for (int col = 0; col < picture.width(); col++) {
            for (int row = 0; row < picture.height(); row++) {
                energyMatrix[col][row] = calculateEnergyAt(col, row);
            }
        }
    }

    // current picture
    public Picture picture() {
        return picture;
    }

    // width of current picture
    public int width() {
        if (transpose) {
            return picture.height();
        }
        return picture.width();
    }

    // height of current picture
    public int height() {
        if (transpose) {
            return picture.width();
        }
        return picture.height();
    }

    // energy of pixel at column x and row y
    public double energy(int x, int y) {
        if (transpose) {
            return energyMatrix[y][x];
        }
        return energyMatrix[x][y];
    }

    // sequence of indices for horizontal seam
    public int[] findHorizontalSeam() {
        transpose = true;
        int[] result = findVerticalSeam();
        transpose = false;
        return result;
    }

    // sequence of indices for vertical seam
    public int[] findVerticalSeam() {
        // initialize the frontier to be
        SeamSearchNode[] frontier = new SeamSearchNode[this.width()];
        for (int i = 0; i < width(); i++) {
            frontier[i] = new SeamSearchNode(energy(i, 0));
            frontier[i].setPath(i, 0);
        }

        for (int row = 1; row < height(); row++) {
            // it is very important that we create a new frontier each time
            SeamSearchNode[] newFrontier = new SeamSearchNode[this.width()];
            for (int col = 0; col < width(); col++) {
                int start, end;
                if (col > 0) {
                    start = col - 1;
                } else {
                    start = 0;
                }
                if (col < width() - 1) {
                    end = col + 1;
                } else {
                    end = width() - 1;
                }
                SeamSearchNode smallestParent = selectMinSeamSearchNode(frontier, start, end);
                newFrontier[col] = smallestParent.accumulate(col, row);
            }
            frontier = newFrontier;
        }
        // select the min from frontier
        SeamSearchNode smallestParent = selectMinSeamSearchNode(frontier, 0, width() - 1);
        return smallestParent.getPath();
    }

    // remove horizontal seam from picture
    public void removeHorizontalSeam(int[] seam) {
        picture = SeamRemover.removeHorizontalSeam(picture, seam);
    }

    // remove vertical seam from picture
    public void removeVerticalSeam(int[] seam) {
        picture = SeamRemover.removeVerticalSeam(picture, seam);
    }

    // private helper method
    private double calculateEnergyAt(int x, int y) {
        int leftOfX = x - 1;
        if (leftOfX < 0) {
            leftOfX = this.width() - 1;
        }
        int rightOfX = x + 1;
        if (rightOfX >= this.width()) {
            rightOfX = 0;
        }
        int upOfY = y - 1;
        if (upOfY < 0) {
            upOfY = this.height() - 1;
        }
        int bottomOfY = y + 1;
        if (bottomOfY >= this.height()) {
            bottomOfY = 0;
        }
        double deltaX = calculateColorGradient(picture.get(leftOfX, y), picture.get(rightOfX, y));
        double deltaY = calculateColorGradient(picture.get(x, upOfY), picture.get(x, bottomOfY));
        return deltaX + deltaY;
    }

    private double calculateColorGradient(Color c1, Color c2) {
        int bGradient = c1.getBlue() - c2.getBlue();
        int rGradient = c1.getRed() - c2.getRed();
        int gGradient = c1.getGreen() - c2.getGreen();
        return (double) (bGradient * bGradient + rGradient * rGradient + gGradient * gGradient);
    }

    private SeamSearchNode selectMinSeamSearchNode(SeamSearchNode[] frontier, int start, int end) {
        SeamSearchNode smallest = frontier[start];
        for (int i = start + 1; i <= end; i++) {
            if (smallest.compareTo(frontier[i]) > 0) {
                smallest = frontier[i];
            }
        }
        return smallest;
    }

    private class SeamSearchNode implements Comparable<SeamSearchNode> {
        private double accumulateEnergy;
        private int[] path;

        public SeamSearchNode(SeamSearchNode n) {
            accumulateEnergy = n.accumulateEnergy;
            this.path = Arrays.copyOf(n.path, n.path.length);
        }

        public SeamSearchNode(double initialEnergy) {
            accumulateEnergy = initialEnergy;
            this.path = new int[height()];
        }

        public void setPath(int col, int row) {
            path[row] = col;
        }

        public int[] getPath() {
            return path;
        }

        public double getEnergy() {
            return accumulateEnergy;
        }

        public int compareTo(SeamSearchNode n) {
            if (accumulateEnergy < n.getEnergy()) return -1;
            else if (accumulateEnergy > n.getEnergy()) return 1;
            else return 0;
        }

        // takes a node, return a new node which add the current col to the path
        public SeamSearchNode accumulate(int col, int row) {
            SeamSearchNode newNode = new SeamSearchNode(this);
            newNode.addEnergy(energy(col, row));
            newNode.setPath(col, row);
            return newNode;
        }

        private void addEnergy(double energy) {
            this.accumulateEnergy += energy;
        }

    }
}
