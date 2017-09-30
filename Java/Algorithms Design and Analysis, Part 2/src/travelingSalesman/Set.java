package travelingSalesman;

/*************************************************************************
 * Name: Yulian Zhou
 * Email: zhouyulian17@gmail.com
 *
 * Compilation:  javac Set.java
 * Execution:    None
 * Dependencies: None
 *
 * Description:  A Set that stores the minimal distance from a source city
 *               to any destiny cities visiting all cities exactly once.
 *
 *************************************************************************/

public class Set {
    
    private boolean[] added;    // added[i] = is the ith city in this set?
    private double[] distTo;    // distTo[i] = the minimal distance to ith city
    
    /**
     * Initiates a set of cities with distance to any city as infinity.
     */
    public Set(int k, int n) {
        added = new boolean[n];
        distTo = new double[n];
        for (int j = 0; j < n; j++) {
            added[j] = (k & 1) == 1;
            distTo[j] = Double.POSITIVE_INFINITY;
            k = k >>> 1;
        }
    }
    
    /**
     * Is the ith city in this set?
     */
    public boolean added(int i) { return added[i]; }
    
    /**
     * Returns the distance to ith city.
     */
    public double distTo(int i) { return distTo[i]; }
    
    /**
     * Updates the distance to ith city as d.
     */
    public void update(int i, double d) { distTo[i] = d; }
}