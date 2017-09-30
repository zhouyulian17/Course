package allShortestPath;

/*************************************************************************
 * Name: Yulian Zhou
 * Email: zhouyulian17@gmail.com
 *
 * Compilation:  javac Shortest.java
 * Execution:    java Shortest graph.txt
 * Dependencies: EdgeWeightedGraph.java, BellmanFord.java, Johnson.java
 *               FloydWarshall.java
 *
 * Description:  A data type that computes the "shortest shortest path"
 *               or reports the existence of a negative cycle in an
 *               edge-weighted digraph G from vertex by implementing
 *               Bellman-Ford, Johnson's or Floyd-Warshall algorithm.
 *
 *************************************************************************/

public class Shortest {
    
    private EdgeWeightedGraph G;    // an edge-weighted digraph
    
    /**
     * Initializes a Shortest data structure from file.
     */
    public Shortest(String file) {
        G = new EdgeWeightedGraph(file);
    }
    
    /**
     * Runs Bellman-Ford algorithm from every vertex as the source vertex and
     * finds the shortest path or a negative cycle.
     */
    public int bellmanFord() {
        int shortest = Integer.MAX_VALUE;
        BellmanFord bf;
        for (int i = 0; i < G.V(); i++) {
            bf = new BellmanFord(G, i);
            if (bf.hasNegativeCycle()) {
                System.out.println("Negative cycle.");
                return 0;
            }
            for (int j = 0; j < G.V(); j++) {
                if (shortest > bf.distTo(j)) shortest = bf.distTo(j);
            }
        }
        return shortest;
    }
    
    /**
     * Runs Johnson's algorithm on the edge-weighted graph G, and finds the
     * shortest path or a negative cycle.
     */
    public int johnson() {
        Johnson js = new Johnson(G);
        if (js.hasNegativeCycle()) {
            System.out.println("Negative cycle.");
            return 0;
        }
        int shortest = Integer.MAX_VALUE;
        for (int i = 0; i < G.V(); i++) {
            for (int j = 0; j < G.V(); j++) {
                if (shortest > js.distTo(i, j)) shortest = js.distTo(i, j);
            }
        }
        return shortest;
    }
    
    /**
     * Runs Floyd-Warshall algorithm on the edge-weighted graph G, and finds the
     * shortest path or a negative cycle.
     */
    public int floydWarshall() {
        FloydWarshall fw = new FloydWarshall(G);
        if (fw.hasNegativeCycle()) {
            System.out.println("Negative cycle.");
            return 0;
        }
        int shortest = Integer.MAX_VALUE;
        for (int i = 0; i < G.V(); i++) {
            for (int j = 0; j < G.V(); j++) {
                if (shortest > fw.distTo(i, j)) shortest = fw.distTo(i, j);
            }
        }
        return shortest;
    }
    
    /**
     * Unit tests the Shortest data type.
     */
    public static void main(String[] args) {
        String file = args[0];
        long time0 = System.currentTimeMillis();
        Shortest st = new Shortest(file);
        long time1 = System.currentTimeMillis();
        //System.out.println(st.bellmanFord());
        long time2 = System.currentTimeMillis();
        //System.out.println(time2 - time1);
        System.out.println(st.johnson());
        long time3 = System.currentTimeMillis();
        System.out.println(time3 - time2);
        System.out.println(st.floydWarshall());
        long time4 = System.currentTimeMillis();
        System.out.println(time4 - time3);
    }
}