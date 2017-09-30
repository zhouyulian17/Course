package sat2;

import java.util.HashSet;
import java.util.Set;
import java.util.Stack;

/*************************************************************************
 * Name: Yulian Zhou
 * Email: zhouyulian17@gmail.com
 *
 * Compilation:  javac SCC.java
 * Execution:    None
 * Dependencies: Digraph.java
 *
 * Description:  A data type that computes sizes of strongly connected
 *               components of a digraph using Kosaraju's algorithm.
 *
 *************************************************************************/

public class SCC {
    
    private Digraph G;         // a directed graph
    private boolean[] marked;  // marked[v] = has vertex v been visited?
    private int[] views;       // views[v] = number of visits at vertex v
    private int[] times;       // time[i] = the ith vertex that is finished last
    private int time;          // finish time for current vertex
    private Set<Integer> vertices;    // set of vertices on current SCC
    private boolean satisfiable;      // is this graph of instances satisfiable?
    
    /**
     * Computes the sizes of super connected components of the directed graph G
     * in ascending order.
     */
    public SCC(Digraph G) {
        this.G = G;
        revDfs();
        dfs();
    }
    
    /**
     * Are all v and ¬v in different super connected components?
     */
    public boolean satisfiable() { return satisfiable; }
    
    // reverse depth-first search that computes finish time (the earlier a
    // vertex is finished, the larger finish time it has) for all vertices in G
    private void revDfs() {
        marked = new boolean[G.V()];
        views = new int[G.V()];
        times = new int[G.V()];
        time = G.V();
        satisfiable = true;
        for (int i = 0; i < G.V(); i++) {
            if (!marked[i]) revDfs2(i);
        }
    }
    
    // reverse depth-first search from vertex v in G iteratively
    private void revDfs(int v) {
        Stack<Integer> stack = new Stack<Integer>();
        stack.push(v);
        marked[v] = true;
        boolean finished;
        while (!stack.isEmpty()) {
            v = stack.pop();
            finished = true;
            for (int i = views[v]; i < G.adjT(v).size(); i++) {
                views[v]++;
                int w = G.adjT(v).get(i);
                if (!marked[w]) {
                    stack.push(v);
                    stack.push(w);
                    marked[w] = true;
                    finished = false;
                    break;
                }
            }
            if (finished) times[--time] = v;
        }
    }
    
    // reverse depth-first search from vertex v in G recursively
    private void revDfs2(int v) {
        marked[v] = true;
        for (int w : G.adjT(v)) {
            if (!marked[w]) revDfs2(w);
        }
        times[--time] = v;
    }
    
    // depth-first search that computes sizes of super connected components in G
    private void dfs() {
        marked = new boolean[G.V()];
        for (int i = 0; i < G.V(); i++) {
            int v = times[i];
            if (!marked[v]) {
                vertices = new HashSet<Integer>();
                dfs2(v);
                if (!satisfiable) return;
            }
        }
    }
    
    // depth-first search from vertex v in G iteratively
    private void dfs(int v) {
        Stack<Integer> stack = new Stack<Integer>();
        stack.push(v);
        marked[v] = true;
        vertices.add(v);
        while (!stack.isEmpty()) {
            v = stack.pop();
            for (int w : G.adjH(v)) {
                if (!marked[w]) {
                    stack.push(w);
                    marked[w] = true;
                    if (vertices.contains(not(w))) {
                        satisfiable = false;
                        return;
                    }
                    vertices.add(v);
                }
            }
        }
    }
    
    // depth-first search from vertex v in G recursively
    private void dfs2(int v) {
        marked[v] = true;
        if (vertices.contains(not(v))) {
            satisfiable = false;
            return;
        }
        vertices.add(v);
        for (int w : G.adjH(v)) {
            if (!marked[w]) dfs2(w);
        }
    }
    
    // given v, return ¬v
    private int not(int v) {
        if (v < G.V() / 2) return v + G.V() / 2;
        return  v - G.V() / 2;
    }
}