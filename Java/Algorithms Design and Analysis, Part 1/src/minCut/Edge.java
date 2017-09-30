package minCut;

/*************************************************************************
 * Name: Yulian Zhou
 * Email: zhouyulian17@gmail.com
 *
 * Compilation:  javac Edge.java
 * Execution:    None
 * Dependencies: None
 *
 * Description:  Immutable unweighted edge.
 *
 *************************************************************************/

public class Edge {
    
    public final int v1;    // vertex 1 of this edge
    public final int v2;    // vertex 2 of this edge
    
    /**
     * Initiates a new Edge ended at vertex v1 and v2
     */
    public Edge(int m, int n) {
        v1 = m;
        v2 = n;
    }
    
    public int getV1() { return v1; }    // returns vertex 1 of this edge
    
    public int getV2() { return v2; }    // returns vertex 2 of this edge
}