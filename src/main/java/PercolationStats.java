import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

public class PercolationStats {
    private double mean;
    private double stddev;
    private double confidenceLo;
    private double confidenceHi;

    public PercolationStats(int N, int T) {
        if (N < 1 || T < 1) {
            throw new IllegalArgumentException();
        }
        double[] res = new double[T];
        for (int i = 0; i < T; i++) {
            Percolation p = new Percolation(N);
            int opened = 0;
            while (!p.percolates()) {
                int row = StdRandom.uniform(1, N + 1);
                int column = StdRandom.uniform(1, N + 1);
                if (!p.isOpen(row, column)) {
                    p.open(row, column);
                    opened++;
                }
            }
            res[i] = 1.0 * opened / (N * N);
        }
        mean = StdStats.mean(res);
        stddev = StdStats.stddev(res);
        confidenceLo = mean - 1.96 * stddev / Math.sqrt(T);
        confidenceHi = mean + 1.96 * stddev / Math.sqrt(T);
    }

    public double mean() {
        return mean;
    }

    public double stddev() {
        return stddev;
    }

    public double confidenceLo() {
        return confidenceLo;
    }

    public double confidenceHi() {
        return confidenceHi;
    }

    public static void main(String[] args) {
        int N = StdIn.readInt();
        int T = StdIn.readInt();
        PercolationStats stats = new PercolationStats(N, T);
        System.out.format("mean = %f%n", stats.mean());
        System.out.format("stddev = %f%n", stats.stddev());
        System.out.format("95%% confidence interval = %f, %f%n", stats.confidenceLo(), stats.confidenceHi());
    }
}
