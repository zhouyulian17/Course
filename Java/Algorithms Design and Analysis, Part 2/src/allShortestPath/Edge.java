package allShortestPath;

/*************************************************************************
 * Name: Yulian Zhou
 * Email: zhouyulian17@gmail.com
 *
 * Compilation:  javac Edge.java
 * Execution:    None
 * Dependencies: None
 *
 * Description:  Immutable weighted edge.
 *
 *************************************************************************/

public class Edge {
    
    private final int v;    // one vertex
    private final int w;    // the other vertex
    private int l;          // weight of the edge
    
    /**
     * Initializes an edge from vertex v to vertex w of weight l.
     */
    public Edge(int v, int w, int l) {
        this.v = v;
        this.w = w;
        this.l = l;
    }
    
    /**
     * Returns the first endpoint of this edge.
     */
    public int v() { return v; }
    
    /**
     * Returns the second endpoint of this edge.
     */
    public int w() { return w; }
    
    /**
     * Returns the weight of this edge.
     */
    public int weight() { return l; }
    
    /**
     * Updates the weight of this edge.
     */
    public void updateWeight(int newl) { l = newl; }
    
    /**
     * Returns a string representation of this edge.
     */
    public String toString() {
        return String.format("%d->%d %d", v, w, l);
    }
}