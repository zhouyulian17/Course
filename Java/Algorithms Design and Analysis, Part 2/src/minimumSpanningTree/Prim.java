package minimumSpanningTree;

import java.util.Comparator;
import java.util.PriorityQueue;

/*************************************************************************
 * Name: Yulian Zhou
 * Email: zhouyulian17@gmail.com
 *
 * Compilation:  javac Prim.java
 * Execution:    java Prim edges.txt
 * Dependencies: Edge.java, EdgeWeightedGraph.java
 *
 * Description:  A data type that computes a minimum spanning tree for a
 *               connected, edge-weighted graph using Prim's minimum
 *               spanning tree algorithm.
 *
 *************************************************************************/

public class Prim {
    
    private PriorityQueue<Integer> pq;    // priority queue of vertices
    private int[] distTo;     // distTo[v] = the weight of edge connecting v and its parent in the MST
    private int[] edgeTo;     // edgeTo[v] = the parent of vertex v in the MST
    private boolean[] marked; // marked[v] = true if v is included in the current tree
    
    /**
     * Computes a minimum spanning tree from a root vertex in the edge-weighted
     * digraph G.
     */
    public Prim(String file) {
        EdgeWeightedGraph G = new EdgeWeightedGraph(file);
        distTo = new int[G.V()];
        edgeTo = new int[G.V()];
        marked = new boolean[G.V()];
        
        for (int i = 0; i < G.V(); i++) {
            distTo[i] = Integer.MAX_VALUE;
            edgeTo[i] = -1;
        }
        int root = 0;
        distTo[root] = 0;
        edgeTo[root] = root;
        
        pq = new PriorityQueue<Integer>(G.V(), new Comparator<Integer>() {
            @Override
            public int compare(Integer o1, Integer o2) {
                return distTo[o1] - distTo[o2];
            }
        });
        pq.add(root);
        while (!pq.isEmpty()) {
            int v = pq.poll();
            marked[v] = true;
            for (Edge e : G.adj(v)) {
                relax(e, v);
            }
        }
    }
    
    // relax edge e and update pq if changed
    private void relax(Edge e, int v) {
        int w = e.other(v);
        if (marked[w]) return;
        int dvw = e.weight();
        if (distTo[w] > dvw) {
            distTo[w] = dvw;
            edgeTo[w] = v;
            pq.remove(w);
            pq.add(w);
        }
    }
    
    /**
     * Returns the overall cost of the minimum spanning tree.
     */
    public int MST() {
        int sum = 0;
        for (int v : distTo) {
            sum += v;
        }
        return sum;
    }
    
    /**
     * Returns the parent vertex of vertex v in the minimum spanning tree.
     */
    public int edgeTo(int v) { return edgeTo[v]; }
    
    /**
     * Unit tests the Prim data type.
     */
    public static void main(String[] args) {
        String file = args[0];
        
        // compute a MST
        Prim prim = new Prim(file);
        
        // print the total cost of the MST
        System.out.println(prim.MST());
    }
}