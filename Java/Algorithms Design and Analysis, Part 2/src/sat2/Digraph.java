package sat2;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/*************************************************************************
 * Name: Yulian Zhou
 * Email: zhouyulian17@gmail.com
 *
 * Compilation:  javac Digraph.java
 * Execution:    None
 * Dependencies: None
 *
 * Description:  A graph, implemented using an array of lists.
 *
 *************************************************************************/

public class Digraph {
    
    private final int V;   // number of vertices in this digraph
    
    // adjHeads[v] = adjacency list of heads for vertex v
    private List<Integer>[] adjHeads;
    
    // adjHeads[v] = adjacency list of tails for vertex v
    private List<Integer>[] adjTails;
    
    /**
     * Initializes an empty digraph of n vertices.
     */
    public Digraph(int n) {
        V = n;
        adjHeads = (ArrayList<Integer>[]) new ArrayList[V];
        adjTails = (ArrayList<Integer>[]) new ArrayList[V];
        for (int i = 0; i < V; i++) {
            adjHeads[i] = new ArrayList<Integer>();
            adjTails[i] = new ArrayList<Integer>();
        }
    }
    
    /**
     * Initializes a digraph of n vertices from a file.
     */
    public Digraph(String file, int n) {
        V = n;
        adjHeads = (ArrayList<Integer>[]) new ArrayList[V];
        adjTails = (ArrayList<Integer>[]) new ArrayList[V];
        for (int i = 0; i < V; i++) {
            adjHeads[i] = new ArrayList<Integer>();
            adjTails[i] = new ArrayList<Integer>();
        }
        Scanner scanner = null;
        try {
            scanner = new Scanner(new File(file));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        Scanner sc;
        while (scanner.hasNextLine()) {
            sc = new Scanner(scanner.nextLine());
            int t = sc.nextInt() - 1;
            int h = sc.nextInt() - 1;
            addEdge(t, h);
        }
    }
    
    /**
     * Adds a directed edge t->h to this digraph.
     */
    public void addEdge(int t, int h) {
        adjHeads[t].add(h);
        adjTails[h].add(t);
    }
    
    /**
     * Returns the number of vertices in this digraph.
     */
    public int V() { return V; }
    
    /**
     * Returns the vertices adjacent from vertex v in this digraph.
     */
    public List<Integer> adjH(int v) { return adjHeads[v]; }
    
    /**
     * Returns the vertices adjacent to vertex v in this digraph.
     */
    public List<Integer> adjT(int v) { return adjTails[v]; }
}