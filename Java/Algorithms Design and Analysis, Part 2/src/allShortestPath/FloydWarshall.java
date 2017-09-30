package allShortestPath;

/*************************************************************************
 * Name: Yulian Zhou
 * Email: zhouyulian17@gmail.com
 *
 * Compilation:  javac FloydWarshall.java
 * Execution:    None
 * Dependencies: Edge.java, EdgeWeightedGraph.java
 *
 * Description:  Floyd-Warshall all-pairs shortest path algorithm.
 *
 *************************************************************************/

public class FloydWarshall {
    
    private int[][] distTo;   // distTo[v] = distance of shortest s->v path
    private int[][] edgeTo;   // edgeTo[v] = second to last vertex on shortest s->v path
    private boolean cycle;    // is there a negative cycle?
    
    /**
     * Computes a shortest paths tree from each vertex to to every other vertex
     * in the edge-weighted digraph G. If no such shortest path exists for some
     * pair of vertices, it reports a negative cycle.
     */
    public FloydWarshall(EdgeWeightedGraph G) {
        
        // initialize distances to infinity
        distTo = new int[G.V()][G.V()];
        edgeTo = new int[G.V()][G.V()];
        for (int i = 0; i < G.V(); i++) {
            for (int j = 0; j < G.V(); j++) {
                distTo[i][j] = 1000000;
                edgeTo[i][j] = -1;
            }
        }
        // initialize distances using edge-weighted digraph's
        for (int v = 0; v < G.V(); v++) {
            for (Edge e : G.adj(v)) {
                distTo[v][e.w()] = e.weight();
                edgeTo[v][e.w()] = e.w();
            }
            // in case of self-loops
            if (distTo[v][v] > 0) {
                distTo[v][v] = 0;
                edgeTo[v][v] = v;
            }
        }
        
        // Floyd-Warshall updates
        for (int k = 0; k < G.V(); k++) {
            
            // compute shortest paths using only 0, 1, ..., k as intermediate vertices
            for (int i = 0; i < G.V(); i++) {
                if (edgeTo[i][k] == -1) continue;
                for (int j = 0; j < G.V(); j++) {
                    int t = distTo[i][k] + distTo[k][j];
                    if (distTo[i][j] > t) {
                        distTo[i][j] = t;
                        edgeTo[i][j] = k;
                    }
                }
                if (distTo[i][i] < 0) {
                    cycle = true;
                    return;
                }
            }
        }
    }
    
    /**
     * Is there a negative cycle in the graph?
     */
    public boolean hasNegativeCycle() { return cycle; }
    
    /**
     * Returns the length of a shortest path from vertex v to vertex w.
     */
    public int distTo(int v, int w) {
        if (hasNegativeCycle())
            throw new UnsupportedOperationException("Negative cost cycle exists");
        return distTo[v][w];
    }
    
    /**
     * Returns the second to last vertex in a shortest path from vertex v to
     * vertex w.
     */
    public int edgeTo(int v, int w) {
        if (hasNegativeCycle())
            throw new UnsupportedOperationException("Negative cost cycle exists");
        return edgeTo[v][w];
    }
}