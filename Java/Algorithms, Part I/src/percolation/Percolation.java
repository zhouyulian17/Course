package percolation;

import edu.princeton.cs.algs4.WeightedQuickUnionUF;

/*************************************************************************
 * Name: Yulian Zhou
 * Email: zhouyulian17@gmail.com
 *
 * Compilation:  javac Percolation.java
 * Execution:    java Percolation
 * Dependencies: WeightedQuickUnionUF.java
 *
 * Description:  Two N * N grid for percolation test.
 * http://coursera.cs.princeton.edu/algs4/assignments/percolation.html
 *
 *************************************************************************/

public class Percolation  {
    
    // WeightedQuickUnionUF object that contains both virtual top and bottom for percolates() test
    private WeightedQuickUnionUF quf1;
    
    // WeightedQuickUnionUF object that contains only virtual top for isFull() test
    private WeightedQuickUnionUF quf2;
    private boolean[] open;     // open[k] = true, if site k is open
    private int n;              // grid size
    
    /**
     * Constructs a N by N grid.
     */
    public Percolation(int N) {
        // construct a N-by-N percolation system with all sites closed
        if (N <= 0) throw new IllegalArgumentException();
        n = N;
        int size = N * N + 2;
        quf1 = new WeightedQuickUnionUF(size);
        quf2 = new WeightedQuickUnionUF(size - 1);
        open = new boolean[size];
        for (int i = 0; i < size - 2; i++) {
            open[i] = false;
        }
        
        // open the virtual top and bottom
        open[size - 2] = true;
        open[size - 1] = true;
        
        // connect the virtual top with sites on the first row
        // connect the virtual bottom with sites on the last row
        for (int i = 0; i < N; i++) {
            quf1.union(size - 2, i);
            quf2.union(size - 2, i);
            quf1.union(size - 1, size - 3 - i);
        }
    }
    
    /**
     * Open row i, column j and connect it to 4 adjacent sites if open.
     */
    public void open(int i, int j) {
        // open the site
        open[n * (i - 1) + (j - 1)] = true;
        
        // in quf1, connect the site to virtual top if on the first row
        if (i == 1) quf1.union(n * n, xyTo1D(i, j));
        
        // in quf1, connect the site to virtual bottom if on the last row
        if (i == n) quf1.union(n * n + 1, xyTo1D(i, j));
        
        // connect the site to adjacent sites if open
        if (i-1 > 0 && isOpen(i - 1, j)) {
            quf1.union(xyTo1D(i - 1, j), xyTo1D(i, j));
            quf2.union(xyTo1D(i - 1, j), xyTo1D(i, j));
        }
        if (i+1 <= n && isOpen(i + 1, j)) {
            quf1.union(xyTo1D(i + 1, j), xyTo1D(i, j));
            quf2.union(xyTo1D(i + 1, j), xyTo1D(i, j));
        }
        if (j-1 > 0 && isOpen(i, j - 1)) {
            quf1.union(xyTo1D(i, j - 1), xyTo1D(i, j));
            quf2.union(xyTo1D(i, j - 1), xyTo1D(i, j));
        }
        if (j+1 <= n && isOpen(i, j + 1)) {
            quf1.union(xyTo1D(i, j + 1), xyTo1D(i, j));
            quf2.union(xyTo1D(i, j + 1), xyTo1D(i, j));
        }
    }
    
    // convert 2D index (i, j) into 1D union-find index
    private int xyTo1D(int i, int j) {
        if (i > n || j > n || i < 1 || j < 1) {
            throw new IndexOutOfBoundsException();
        }
        return n * (i - 1) + (j - 1);
    }
    
    /**
     * Returns true if site (i j) is open.
     */
    public boolean isOpen(int i, int j) { return open[xyTo1D(i, j)]; }
    
    /**
     * Return true if site (i j) is full.
     */
    public boolean isFull(int i, int j) {
        if (i == 1) return open[xyTo1D(i, j)];
        return quf2.connected(xyTo1D(i, j), n * n);
    }
    
    /**
     * Return true if the grid is percolated.
     */
    public boolean percolates() {
        return quf1.find(n * n + 1) == quf1.find(n * n);
    }
    
    public static void main(String[] args) {
        
        // unit testing
        Percolation p = new Percolation(1);
        p.open(1, 1);
        System.out.println(p.percolates());
    }
}