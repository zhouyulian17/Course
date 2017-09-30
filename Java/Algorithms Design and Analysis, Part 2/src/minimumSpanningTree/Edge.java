package minimumSpanningTree;

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
    private final int l;    // weight of the edge
    
    /**
     * Initializes an edge between vertex v to vertex w of weight l.
     */
    public Edge(int v, int w, int l) {
        this.v = v;
        this.w = w;
        this.l = l;
    }
    
    /**
     * Returns either endpoint of this edge.
     */
    public int either() { return v; }
    
    /**
     * Returns the endpoint of this edge that is different from the given
     * vertex.
     */
    public int other(int vertex) {
        if (vertex == v) return w;
        if (vertex == w) return v;
        throw new IllegalArgumentException("Illegal endpoint");
    }
    
    /**
     * Returns the weight of this edge.
     */
    public int weight() { return l; }
    
    /**
     * Returns a string representation of this edge.
     */
    public String toString() {
        return String.format("%d-%d %d", v, w, l);
    }
}