package hw2;

import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

public class PercolationStats {

    double[] threshold;

    // perform T independent experiments on an N-by-N grid
    public PercolationStats(int N, int T) {
        if (T < 30) throw new IllegalArgumentException("T must be greater or equal to 30.");
        threshold = new double[T];
        for (int i = 0; i < T; i++) {
            threshold[i] = performExperiment(N);
        }
    }

    // sample mean of percolation threshold
    public double mean() {
        return StdStats.mean(threshold);
    }

    // sample standard deviation of percolation threshold
    public double stddev() {
        return StdStats.stddev(threshold);
    }

    // low  endpoint of 95% confidence interval
    public double confidenceLow() {
        return mean() - 1.96 * stddev() / threshold.length;
    }

    // high endpoint of 95% confidence interval
    public double confidenceHigh() {
        return mean() + 1.96 * stddev() / threshold.length;
    }

    // perform one experiment on N-by-N grid and return the threshold
    private double performExperiment(int N) {
        Percolation p = new Percolation(N);
        while (!p.percolates()) {
            int row = StdRandom.uniform(N);
            int col = StdRandom.uniform(N);
            while (p.isOpen(row, col)) {
                row = StdRandom.uniform(N);
                col = StdRandom.uniform(N);
            }
            p.open(row, col);
        }
        return (double) p.numberOfOpenSites() / (double) (N * N);
    }
}                       
