package allShortestPath;

/*************************************************************************
 * Name: Yulian Zhou
 * Email: zhouyulian17@gmail.com
 *
 * Compilation:  javac BellmanFord.java
 * Execution:    None
 * Dependencies: Edge.java, EdgeWeightedGraph.java
 *
 * Description:  Bellman-Ford shortest path algorithm. Computes the
 *               shortest path tree in edge-weighted digraph G from vertex
 *               s, or reports the existence of a negative cost cycle
 *               reachable from s.
 *
 *************************************************************************/

public class BellmanFord {
    
    private int[] distTo;       // distTo[v] = distance of shortest s->v path
    private int[] edgeTo;       // edgeTo[v] = second to last vertex on shortest s->v path
    private boolean[] preQ;     // preQ[v] = is v currently on the queue?
    private boolean[] postQ;    // postQ[v] = will v be on the queue?
    private boolean cycle;      // is there a negative cycle?
    
    /**
     * Computes a shortest-paths tree from the source vertex to every other
     * vertex in the edge-weighted digraph G.
     */
    public BellmanFord(EdgeWeightedGraph G, int s) {
        distTo = new int[G.V()];
        edgeTo = new int[G.V()];
        preQ = new boolean[G.V()];
        for (int i = 0; i < G.V(); i++) {
            distTo[i] = 1000000;
            edgeTo[i] = -1;
        }
        distTo[s] = 0;
        edgeTo[s] = s;
        preQ[s] = true;
        
        // Bellman-Ford algorithm
        for (int i = 0; i < G.V(); i++) {
            postQ = new boolean[G.V()];
            for (int j = 0; j < G.V(); j++) {
                if (!preQ[j]) continue;
                for (Edge e : G.adj(j)) relax(e);
            }
            preQ = postQ;
        }
        
        // check for negative cycle
        for (int i = 0; i < G.V(); i++) {
            if (preQ[i]) {
                cycle = true;
                return;
            }
        }
    }
    
    // relax edge e if changed
    private void relax(Edge e) {
        int v = e.v();
        int w = e.w();
        if (distTo[w] > distTo[v] + e.weight()) {
            distTo[w] = distTo[v] + e.weight();
            edgeTo[w] = v;
            postQ[w] = true;
        }
    }
    
    /**
     * Is there a negative cycle reachable from the source vertex s?
     */
    public boolean hasNegativeCycle() { return cycle; }
    
    /**
     * Returns the length of a shortest path from the source vertex to vertex v.
     */
    public int distTo(int v) {
        if (hasNegativeCycle())
            throw new UnsupportedOperationException("Negative cost cycle exists");
        return distTo[v];
    }
    
    /**
     * Returns the second to last vertex in a shortest path from the source
     * vertex to vertex v.
     */
    public int edgeTo(int v) {
        if (hasNegativeCycle())
            throw new UnsupportedOperationException("Negative cost cycle exists");
        return edgeTo[v];
    }
}