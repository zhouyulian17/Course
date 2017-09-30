package clustering;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Scanner;

/*************************************************************************
 * Name: Yulian Zhou
 * Email: zhouyulian17@gmail.com
 *
 * Compilation:  javac MaxSpacingClustering.java
 * Execution:    java MaxSpacingClustering clustering1.txt k
 * Dependencies: Edge.java, WUF.java
 *
 * Description:  A data type that computes a maximum spacing of a
 *               k-clustering by the greedy clustering algorithm.
 *
 *************************************************************************/

public class MaxSpacingClustering {
    
    private List<Edge> edges;    // a list of edges
    private int V;               // number of vertices
    
    /**
     * Initializes a MaxSpacingClustering data structure from file.
     */
    public MaxSpacingClustering(String file) {
        Scanner scanner = null;
        try {
            scanner = new Scanner(new File(file));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        Scanner sc;
        sc = new Scanner(scanner.nextLine());
        V = sc.nextInt();
        edges = new ArrayList<Edge>();
        while (scanner.hasNextLine()) {
            sc = new Scanner(scanner.nextLine());
            int v = sc.nextInt() - 1;
            int w = sc.nextInt() - 1;
            int l = sc.nextInt();
            edges.add(new Edge(v, w, l));
        }
    }
    
    /**
     * Computes and returns the maximum spacing of a k-clustering.
     */
    public int maxSpacing(int k) {
        if (k < 0) throw new IllegalArgumentException("Illegal number");
        PriorityQueue<Edge> pq = new PriorityQueue<Edge>(new Comparator<Edge>() {
            @Override
            public int compare(Edge o1, Edge o2) {
                return o1.weight() - o2.weight();
            }
            
        });
        pq.addAll(edges);
        WUF wuf = new WUF(V);
        
        // form k clusters with maximum space
        while (wuf.count() > k) {
            Edge e = pq.poll();
            wuf.union(e.v(), e.w());
        }
        
        // compute the space as the shortest edge across clusters
        while (!pq.isEmpty()) {
            Edge e = pq.poll();
            if (!wuf.connected(e.v(), e.w())) return e.weight();
        }
        return 0;
    }
    
    /**
     * Unit tests the MaxSpacingClustering data type.
     */
    public static void main(String[] args) {
        String file = args[0];
        int k = Integer.parseInt(args[1]);
        MaxSpacingClustering msc = new MaxSpacingClustering(file);
        System.out.println(msc.maxSpacing(k));
    }
}