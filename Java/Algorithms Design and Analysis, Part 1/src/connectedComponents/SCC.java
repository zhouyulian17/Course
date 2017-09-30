package connectedComponents;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Stack;

/*************************************************************************
 * Name: Yulian Zhou
 * Email: zhouyulian17@gmail.com
 *
 * Compilation:  javac SCC.java
 * Execution:    java SCC SCC.txt n
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
    private int count;         // size of current SCC
    private List<Integer> counts;    // sizes of all SCC in G
    
    /**
     * Computes the sizes of super connected components of the directed graph G
     * in ascending order.
     */
    public SCC(String file, int V) {
        G = new Digraph(file, V);
        System.out.println("read completed");
        revDfs();
        dfs();
        Collections.sort(counts);
    }
    
    /**
     * Outputs the sizes of all super connected components in G.
     */
    public void counts() {
        for (int n : counts) {
            System.out.println(n);
        }
    }
    
    // reverse depth-first search that computes finish time (the earlier a
    // vertex is finished, the larger finish time it has) for all vertices in G
    private void revDfs() {
        marked = new boolean[G.V()];
        views = new int[G.V()];
        times = new int[G.V()];
        time = G.V();
        for (int i = 0; i < G.V(); i++) {
            if (!marked[i]) revDfs(i);
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
        counts = new ArrayList<Integer>();
        for (int i = 0; i < G.V(); i++) {
            int v = times[i];
            if (!marked[v]) {
                count = 0;
                dfs(v);
                counts.add(count);
            }
        }
    }
    
    // depth-first search from vertex v in G iteratively
    private void dfs(int v) {
        Stack<Integer> stack = new Stack<Integer>();
        stack.push(v);
        marked[v] = true;
        count++;
        while (!stack.isEmpty()) {
            v = stack.pop();
            for (int w : G.adjH(v)) {
                if (!marked[w]) {
                    stack.push(w);
                    marked[w] = true;
                    count++;
                }
            }
        }
    }
    
    // depth-first search from vertex v in G recursively
    private void dfs2(int v) {
        marked[v] = true;
        count++;
        for (int w : G.adjH(v)) {
            if (!marked[w]) dfs2(w);
        }
    }
    
    /**
     * Unit tests the SCC data type.
     */
    public static void main(String[] args) {
        String file = args[0];
        int n = Integer.parseInt(args[1]);
        SCC scc = new SCC(file, n);
        scc.counts();
    }
}