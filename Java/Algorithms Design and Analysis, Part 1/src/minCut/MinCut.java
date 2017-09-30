package minCut;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

/*************************************************************************
 * Name: Yulian Zhou
 * Email: zhouyulian17@gmail.com
 *
 * Compilation:  javac MinCut.java
 * Execution:    java MinCut kargerMinCut.txt T
 * Dependencies: Edge.java, WUF.java
 *
 * Description:  A data type that computes min cut of a undirected graph
 *               using Karger's algorithm.
 *
 *************************************************************************/

public class MinCut {
    
    // a list of edges
    private ArrayList<Edge> edges = new ArrayList<Edge>();
    private final int N = 200;    // total number of vertices (1-200)

    /**
     * Initializes a MinCut data structure from file containing the adjacency
     * list representation of a simple undirected graph.
     */
    public MinCut(String file) {
        Scanner scanner = null;
        try {
            scanner = new Scanner(new File(file));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        Scanner sc = null;
        while (scanner.hasNextLine()) {
            sc = new Scanner(scanner.nextLine());
            int v1 = sc.nextInt();
            while(sc.hasNextInt()) {
                int v2 = sc.nextInt();
                addEdge(v1, v2);
            }
        }
    }
    
    // add a new Edge ending at v1 and v2 (v1 < v2) to the edges list
    private void addEdge(int v1, int v2) {
        if (v1 > v2) return;
        edges.add(new Edge(v1, v2));
    }
    
    /**
     * Runs t trials of randomized contraction algorithm and returns the
     * smallest min cut.
     */
    public int findMinCut(int t) {
        int minCut = Integer.MAX_VALUE;
        while (t-- > 0) {
            int count = oneMinCut();
            if (minCut > count) minCut = count;
        }
        return minCut;
    }
    
    // the min cut calculated from one trial of randomized contraction
    private int oneMinCut() {
        WUF wuf = new WUF(N);
        Random r = new Random();
        while (wuf.count() > 2) {
            int k = r.nextInt(edges.size());
            int v1 = edges.get(k).getV1();
            int v2 = edges.get(k).getV2();
            wuf.union(v1 - 1, v2 - 1);
        }
        int cut = 0;
        for (int i = 0; i < edges.size(); i++) {
            int v1 = edges.get(i).getV1();
            int v2 = edges.get(i).getV2();
            
            // increment cuts by 1 if a edge spans vertices in different components
            if (!wuf.connected(v1 - 1, v2 - 1)) cut++;
        }
        return cut;
    }
    
    /**
     * Unit tests the MinCut data type.
     */
    public static void main(String[] args) {
        String file = args[0];
        MinCut mc = new MinCut(file);
        int T = Integer.parseInt(args[1]);
        System.out.println(mc.findMinCut(T));
    }
}