package wordNet;

import java.util.ArrayList;
import java.util.List;
import edu.princeton.cs.algs4.BreadthFirstDirectedPaths;
import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

/*************************************************************************
 * Name: Yulian Zhou
 * Email: zhouyulian17@gmail.com
 *
 * Compilation:  javac SAP.java
 * Execution:    java SAP digraph.txt
 * Dependencies: BreadthFirstDirectedPaths.java, Digraph.java, In.java
 *
 * Description:  A immutable data type for finding shortest ancestral path
 *               between two (groups of) vertices in a digraph.
 * http://coursera.cs.princeton.edu/algs4/assignments/wordnet.html
 *
 *************************************************************************/

public class SAP {
    
    private Digraph G;    // a directed graph
    
    /**
     * Constructor that takes a digraph (not necessarily a DAG)
     */
    public SAP(Digraph G) {
        this.G = new Digraph(G);
    }
    
    /**
     * Returns length of shortest ancestral path between v and w; -1 if no such path
     */
    public int length(int v, int w) {
        if (v < 0 || v > G.V() - 1 || w < 0 || w > G.V() - 1) {
            throw new IndexOutOfBoundsException();
        }
        
        BreadthFirstDirectedPaths bfs1 = new BreadthFirstDirectedPaths(G, v);
        BreadthFirstDirectedPaths bfs2 = new BreadthFirstDirectedPaths(G, w);
        int shortest = Integer.MAX_VALUE;
        boolean has_shortest = false;
        for (int vetex = 0; vetex < G.V(); vetex++) {
            if (bfs1.hasPathTo(vetex) && bfs2.hasPathTo(vetex)) {
                has_shortest = true;
                int path_length = bfs1.distTo(vetex) + bfs2.distTo(vetex);
                if (path_length < shortest) shortest = path_length;
            }
        }
        if (!has_shortest) return -1;
        return shortest;
    }
    
    /**
     * Returns a common ancestor of v and w that participates in a shortest ancestral path; -1 if no such path
     */
    public int ancestor(int v, int w) {
        if (v < 0 || v > G.V() - 1 || w < 0 || w > G.V() - 1) {
            throw new IndexOutOfBoundsException();
        }
        
        BreadthFirstDirectedPaths bfs1 = new BreadthFirstDirectedPaths(G, v);
        BreadthFirstDirectedPaths bfs2 = new BreadthFirstDirectedPaths(G, w);
        int shortest = Integer.MAX_VALUE;
        int ancestor = -1;
        for (int vertex = 0; vertex < G.V(); vertex++) {
            if (bfs1.hasPathTo(vertex) && bfs2.hasPathTo(vertex)) {
                int path_length = bfs1.distTo(vertex) + bfs2.distTo(vertex);
                if (path_length < shortest) {
                    shortest = path_length;
                    ancestor = vertex;
                }
            }
        }
        return ancestor;
    }
    
    /**
     * Returns length of shortest ancestral path between any vertex in v and any vertex in w; -1 if no such path
     */
    public int length(Iterable<Integer> v, Iterable<Integer> w) {
        // if v or w is null
        if (v == null || w == null) {
            throw new NullPointerException();
        }
        // if v or w is empty
        if (!v.iterator().hasNext() || !w.iterator().hasNext()) return -1;
        
        BreadthFirstDirectedPaths bfs1 = new BreadthFirstDirectedPaths(G, v);
        BreadthFirstDirectedPaths bfs2 = new BreadthFirstDirectedPaths(G, w);
        int shortest = Integer.MAX_VALUE;
        boolean has_shortest = false;
        for (int vertex = 0; vertex < G.V(); vertex++) {
            if (bfs1.hasPathTo(vertex) && bfs2.hasPathTo(vertex)) {
                has_shortest = true;
                int path_length = bfs1.distTo(vertex) + bfs2.distTo(vertex);
                if (path_length < shortest) shortest = path_length;
            }
        }
        if (!has_shortest) return -1;
        return shortest;
    }
    
    /**
     * Returns a common ancestor that participates in shortest ancestral path; -1 if no such path
     */
    public int ancestor(Iterable<Integer> v, Iterable<Integer> w) {
        if (v == null || w == null) {
            throw new NullPointerException();
        }
        if (!v.iterator().hasNext() || !w.iterator().hasNext()) return -1;
        
        BreadthFirstDirectedPaths bfs1 = new BreadthFirstDirectedPaths(G, v);
        BreadthFirstDirectedPaths bfs2 = new BreadthFirstDirectedPaths(G, w);
        int shortest = Integer.MAX_VALUE;
        int ancestor = -1;
        for (int vertex = 0; vertex < G.V(); vertex++) {
            if (bfs1.hasPathTo(vertex) && bfs2.hasPathTo(vertex)) {
                int path_length = bfs1.distTo(vertex) + bfs2.distTo(vertex);
                if (path_length < shortest) {
                    shortest = path_length;
                    ancestor = vertex;
                }
            }
        }
        return ancestor;
    }
    
    // do unit testing of this class
    public static void main(String[] args) {
        In in = new In(args[0]);
        Digraph G = new Digraph(in);
        SAP sap = new SAP(G);
        List<Integer> v1 = new ArrayList<Integer>();
        List<Integer> w1 = new ArrayList<Integer>();
        v1.add(1);
        StdOut.println(sap.ancestor(v1, w1));
        while (!StdIn.isEmpty()) {
            int v = StdIn.readInt();
            int w = StdIn.readInt();
            int length   = sap.length(v, w);
            int ancestor = sap.ancestor(v, w);
            StdOut.printf("length = %d, ancestor = %d\n", length, ancestor);
        }
    }
}