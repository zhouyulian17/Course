package allShortestPath;

/*************************************************************************
 * Name: Yulian Zhou
 * Email: zhouyulian17@gmail.com
 *
 * Compilation:  javac Johnson.java
 * Execution:    None
 * Dependencies: Edge.java, EdgeWeightedGraph.java, BellmanFord.java
 *               Dijkstra.java
 *
 * Description:  Johnson's all-pairs shortest path algorithm.
 *
 *************************************************************************/

public class Johnson {
    
    private int[][] distTo;   // distTo[v] = distance of shortest s->v path
    private int[][] edgeTo;   // edgeTo[v] = second to last vertex on shortest s->v path
    private boolean cycle;    // is there a negative cycle?
    
    /**
     * Computes a shortest paths tree from each vertex to to every other vertex
     * in the edge-weighted digraph G. If no such shortest path exists for some
     * pair of vertices, it reports a negative cycle.
     */
    public Johnson(EdgeWeightedGraph G) {
        
        // Bellman-Ford algorithm on the modified graph with an extra vertex
        EdgeWeightedGraph G2 = new EdgeWeightedGraph(G);
        BellmanFord bf = new BellmanFord(G2, G.V());
        if (bf.hasNegativeCycle()) {
            cycle = true;
            return;
        }
        
        // reweight edges to be non-negative from Bellman-Ford computation
        for (int v = 0; v < G.V(); v++) {
            for (Edge e : G2.adj(v)) reweight(bf, e);
        }
        
        // Dijkstra's algorithm on every vertex
        distTo = new int[G.V()][G.V()];
        edgeTo = new int[G.V()][G.V()];
        for (int i = 0; i < G.V(); i++) {
            Dijkstra d = new Dijkstra(G2, i);
            for (int j = 0; j < G.V(); j++) {
                distTo[i][j] = d.distTo(j) + bf.distTo(j) - bf.distTo(i);
                edgeTo[i][j] = d.edgeTo(j);
            }
        }
    }
    
    // reweight the edges of the original graph using the values computed by the
    // Bellman–Ford algorithm.
    private void reweight(BellmanFord bf, Edge e) {
        int v = e.v();
        int w = e.w();
        int newl = e.weight() + bf.distTo(v) - bf.distTo(w);
        e.updateWeight(newl);
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