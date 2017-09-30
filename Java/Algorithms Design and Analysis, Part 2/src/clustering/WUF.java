package clustering;

/*************************************************************************
 * Name: Yulian Zhou
 * Email: zhouyulian17@gmail.com
 *
 * Compilation:  javac WUF.java
 * Execution:    None
 * Dependencies: None
 *
 * Description:  Weighted quick-union (without path compression).
 *
 *************************************************************************/

public class WUF {
    
    private int[] parent;    // parent[i] = parent of i
    private int[] size;      // size[i] = number of sites in subtree rooted at i
    private int count;       // number of components
    
    /**
     * Initializes an empty union-find data structure with n sites (0 to n-1).
     */
    public WUF(int n) {
        count = n;
        parent = new int[n];
        for (int i = 0; i < n; i++) {
            parent[i] = i;
        }
        size = new int[n];
    }
    
    /**
     * Returns the number of components.
     */
    public int count() { return count; }
    
    /**
     * Returns true if the two sites are in the same component.
     */
    public boolean connected(int p, int q) {
        return find(p) == find(q);
    }
    
    /**
     * Merges the component containing site p with the component containing site
     * q.
     */
    public void union(int p, int q) {
        int rootP = find(p);
        int rootQ = find(q);
        if (rootP == rootQ) return;
        if (size[rootP] < size[rootQ]) {
            parent[rootP] = rootQ;
            size[rootQ] += size[rootP];
        }
        else {
            parent[rootQ] = rootP;
            size[rootP] += size[rootQ];
        }
        count--;
    }
    
    /**
     * Returns the component identifier for the component containing site p.
     */
    public int find(int p) {
        while (p != parent[p]) {
            p = parent[p];
        }
        return p;
    }
}