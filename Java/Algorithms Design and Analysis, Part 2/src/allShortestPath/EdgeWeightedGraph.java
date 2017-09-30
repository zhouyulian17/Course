package allShortestPath;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/*************************************************************************
 * Name: Yulian Zhou
 * Email: zhouyulian17@gmail.com
 *
 * Compilation:  javac EdgeWeightedGraph.java
 * Execution:    None
 * Dependencies: Edge.java
 *
 * Description:  An edge-weighted undirected graph, implemented using
 *               adjacency lists.
 *
 *************************************************************************/

public class EdgeWeightedGraph {
    
    private final int V;       // number of vertices in this graph
    private final int E;       // number of edges in this graph
    private List<Edge>[] adj;  // adj[v] = adjacency list for vertex v
    
    /**
     * Initializes an edge-weighted graph of V vertices from a file.
     */
    public EdgeWeightedGraph(String file) {
        Scanner scanner = null;
        try {
            scanner = new Scanner(new File(file));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        Scanner sc;
        sc = new Scanner(scanner.nextLine());
        V = sc.nextInt();
        E = sc.nextInt();
        adj = (ArrayList<Edge>[]) new ArrayList[V];
        for (int i = 0; i < V; i++) {
            adj[i] = new ArrayList<Edge>();
        }
        for (int i = 0; i < E; i++) {
            sc = new Scanner(scanner.nextLine());
            int v = sc.nextInt() - 1;
            int w = sc.nextInt() - 1;
            int l = sc.nextInt();
            addEdge(v, w, l);
        }
    }
    
    /**
     * Initializes an edge-weighted graph from a graph with an extra virtual
     * vertex and 0-weighted edges pointing from the virtual vertex to every
     * other vertex for Johnson's algorithm.
     */
    public EdgeWeightedGraph(EdgeWeightedGraph G) {
        V = G.V() + 1;
        E = G.E() + G.V();
        adj = (ArrayList<Edge>[]) new ArrayList[V];
        for (int i = 0; i < V; i++) {
            adj[i] = new ArrayList<Edge>();
        }
        for (int v = 0; v < G.V(); v++) {
            addEdge(G.V(), v, 0);
            for (Edge e : G.adj(v)) {
                addEdge(v, e.w(), e.weight());
            }
        }
    }
    
    /**
     * Adds a directed edge v->w to this graph.
     */
    public void addEdge(int v, int w, int l) {
        Edge e = new Edge(v, w, l);
        adj[v].add(e);
    }
    
    /**
     * Returns the number of vertices in this graph.
     */
    public int V() { return V; }
    
    /**
     * Returns the number of edges in this graph.
     */
    public int E() { return E; }
    
    /**
     * Returns the vertices adjacent from vertex v in this graph.
     */
    public List<Edge> adj(int v) { return adj[v]; }
}