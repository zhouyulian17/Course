package allShortestPath;

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
    public Dijkstra(EdgeWeightedGraph G, int s) {
        distTo = new int[G.V()];
        edgeTo = new int[G.V()];
        
        for (int i = 0; i < G.V(); i++) {
            distTo[i] = 1000000;
            edgeTo[i] = -1;
        }
        distTo[s] = 0;
        edgeTo[s] = s;
        
        pq = new PriorityQueue<Integer>(G.V(), new Comparator<Integer>() {
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
}