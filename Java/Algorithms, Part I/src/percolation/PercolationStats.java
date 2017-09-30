package percolation;

import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

/*************************************************************************
 * Name: Yulian Zhou
 * Email: zhouyulian17@gmail.com
 *
 * Compilation:  javac PercolationStats.java
 * Execution:    java PercolationStats N T
 * Dependencies: Percolation.java, StdRandom.java, StdStats.java
 *
 * Description:  Statistic test of percolation.
 * http://coursera.cs.princeton.edu/algs4/assignments/percolation.html
 *
 *************************************************************************/

public class PercolationStats {
    
    private double[] array;  // array[i] = percolation threshold in ith experiment
    private int T;           // number of independent experiments
    
    /**
     * Computes percolation threshold (counts of open sites when percolate /
     * total sites) in a N*N percolation system in T experiments with randomly
     * opening site, and stores the percolation threshold in array.
     */
    public PercolationStats(int N, int T) {
        if (N <= 0 || T <= 0) throw new IllegalArgumentException();
        array = new double[T];
        this.T = T;
        
        // run t independent test
        for (int k = 0; k < T; k++) {
            int count = 0;                      // number of total open sites
            Percolation p = new Percolation(N); // a new percolation system
            
            // open a random site until the system is percolated
            while (!p.percolates()) {
                int i = StdRandom.uniform(N) + 1;
                int j = StdRandom.uniform(N) + 1;
                if (!p.isOpen(i, j)) {
                    count++;
                    p.open(i, j);
                }
            }
            
            // the ratio of open sites to total sites
            array[k] = count / (double) (N * N);
        }
    }
    
    /**
     * Returns the mean of percolation threshold.
     */
    public double mean() { return StdStats.mean(array); }
    
    /**
     * Returns the standard deviation of percolation threshold.
     */
    public double stddev() { return StdStats.stddev(array); }
    
    /**
     * Returns the lower bound of CI.
     */
    public double confidenceLo() {
        return StdStats.mean(array) - 1.96 * StdStats.stddev(array) / Math.sqrt(T);
    }
    
    /**
     * Returns the upper bound of CI.
     */
    public double confidenceHi() {
        return StdStats.mean(array) + 1.96 * StdStats.stddev(array) / Math.sqrt(T);
    }
    
    /**
     * Unit tests the PercolationStats data type.
     */
    public static void main(String[] args) {
        PercolationStats ps;
        int N = Integer.parseInt(args[0]);
        int T = Integer.parseInt(args[1]);
        ps = new PercolationStats(N, T);
        System.out.println();
        System.out.printf("%% java PercolationStats %d %d\n", N, T);
        System.out.printf("mean                    = ");
        System.out.println(ps.mean());
        System.out.printf("stddev                  = ");
        System.out.println(ps.stddev());
        System.out.printf("95%% confidence interval = ");
        System.out.print(ps.confidenceLo());
        System.out.print(", ");
        System.out.print(ps.confidenceHi());
        System.out.println();
    }
}