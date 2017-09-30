package shortestPath;

import java.util.Comparator;
import java.util.PriorityQueue;

/*************************************************************************
 * Name: Yulian Zhou
 * Email: zhouyulian17@gmail.com
 *
 * Compilation:  javac Dijkstra.java
 * Execution:    java Dijkstra dijkstraData.txt V s
 * Dependencies: Edge.java, EdgeWeightedGraph.java
 *
 * Description:  A data type that computes shortest-path distances from a
 *               source vertex to every other vertex in an edge-weighted
 *               graph using Dijkstra's algorithm.
 *
 *************************************************************************/

public class Dijkstra {
    
    private PriorityQueue<Integer> pq;    // priority queue of vertices
    private int[] distTo;    // distTo[v] = distance of shortest s->v path
    private int[] edgeTo;    // edgeTo[v] = second to last vertex on shortest s->v path
    
    /**
     * Computes a shortest-paths tree from the source vertex to every other
     * vertex in the edge-weighted digraph G.
     */
    public Dijkstra(String file, int V, int s) {
        EdgeWeightedGraph G = new EdgeWeightedGraph(file, V);
        distTo = new int[V];
        edgeTo = new int[V];
        
        for (int i = 0; i < V; i++) {
            distTo[i] = 1000000;
            edgeTo[i] = -1;
        }
        distTo[s] = 0;
        edgeTo[s] = s;
        
        pq = new PriorityQueue<Integer>(V, new Comparator<Integer>() {
            @Override
            public int compare(Integer o1, Integer o2) {
                return distTo[o1] - distTo[o2];
            }
        });
        pq.add(s);
        while (!pq.isEmpty()) {
            int v = pq.poll();
            for (Edge e : G.adj(v)) {
                relax(e);
            }
        }
    }
    
    // relax edge e and update pq if changed
    private void relax(Edge e) {
        int v = e.v();
        int w = e.w();
        int dvw = distTo[v] + e.weight();
        if (distTo[w] > dvw) {
            distTo[w] = dvw;
            edgeTo[w] = v;
            pq.remove(w);
            pq.add(w);
        }
    }
    
    /**
     * Returns the length of a shortest path from the source vertex to vertex v.
     */
    public int distTo(int v) { return distTo[v]; }
    
    /**
     * Returns the second to last vertex in a shortest path from the source
     * vertex to vertex v.
     */
    public int edgeTo(int v) { return edgeTo[v]; }
    
    /**
     * Unit tests the Dijkstra data type.
     */
    public static void main(String[] args) {
        String file = args[0];
        int V = Integer.parseInt(args[1]);
        int s = Integer.parseInt(args[2]) - 1;
        
        // compute shortest paths
        Dijkstra d = new Dijkstra(file, V, s);
        
        // print shortest path to v in vertices[]
        int[] vertices = {7,37,59,82,99,115,133,165,188,197};
        for (int v : vertices) {
            System.out.printf("%d", v);
            int w = v - 1;
            while (w != s) {
                w = d.edgeTo(w);
                System.out.printf(" <- %d", w + 1);
            }
            System.out.println();
            System.out.printf("%d, %d\n", v, d.distTo(v - 1));
        }
    }
}