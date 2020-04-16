import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

public class PercolationStats {

    private static final double CONFIDENCE_95 = 1.96;
    private final double mean;
    private final double std;
    private final int n;

    // perform independent trials on an n-by-n grid
    public PercolationStats(int n, int trials)
    {
        if (n <= 0 || trials <= 0) {
            throw new IllegalArgumentException("n and trials should be bigger than 0");
        }

        this.n = n;
        double[] numberOfTrials = new double[trials];

        // trials' value is a test number
        for (int i = 0; i < trials; i++)
            numberOfTrials[i] = test(n);

        // if trials' value is 1, then there is no way to calculate standard derivation.
        if (trials == 1) {
            mean = numberOfTrials[0];
            std = Double.NaN;
        }
        // otherwise, it is possible.
        else {
            mean = StdStats.mean(numberOfTrials);
            std = StdStats.stddev(numberOfTrials);
        }
    }

    // getting average from percolation.
    private double test(int num) {

        // create a percolation
        Percolation a = new Percolation(num);
        int counter = 0;

        // while not percolates, open new sites and keep track of the number of open sites.
        while (!a.percolates()) {
            // getting random number for row and col inside of the (1)<->(n+1)
            int row = StdRandom.uniform(1, num+1);
            int col = StdRandom.uniform(1, num+1);
            if (!a.isOpen(row, col)) {
                a.open(row, col);
                counter++;
            }
        }
        return counter * 1.0 / (num*num);
    }

    // sample mean of percolation threshold
    public double mean() { return mean; }

    // sample standard deviation of percolation threshold
    public double stddev() { return std; }

    // low endpoint of 95% confidence interval
    public double confidenceLo() { return mean - ((CONFIDENCE_95 * std) / Math.sqrt(n)); }

    // high endpoint of 95% confidence interval
    public double confidenceHi() { return mean + ((CONFIDENCE_95 * std) / Math.sqrt(n)); }

    // test client (see below)
    public static void main(String[] args)
    {
        int n = Integer.parseInt(args[0]);
        int trials = Integer.parseInt(args[1]);
        // creating PercolationStats to get stats from Percolation
        PercolationStats end = new PercolationStats(n, trials);
        // printing
        StdOut.println("mean                    = " + end.mean());
        StdOut.println("stddev                  = " + end.stddev());
        StdOut.println("95% confidence interval = " + "[" + end.confidenceLo() + ", " + end.confidenceHi() + "]");

    }

}