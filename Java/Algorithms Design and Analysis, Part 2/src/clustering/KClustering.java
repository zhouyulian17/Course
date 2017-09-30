package clustering;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

/*************************************************************************
 * Name: Yulian Zhou
 * Email: zhouyulian17@gmail.com
 *
 * Compilation:  javac KClustering.java
 * Execution:    java KClustering clustering_big.txt d
 * Dependencies: Vertex.java, WUF.java
 *
 * Description:  A data type that computes the largest value of k such
 *               that there is a k-clustering with spacing at least d.
 *
 *************************************************************************/

public class KClustering {
    
    private Vertex[] vertices;    // vertices[i] = the ith vertex
    private int V;                // number of vertices
    
    /**
     * Initializes a KClustering data structure from file.
     */
    public KClustering(String file) {
        Scanner scanner = null;
        try {
            scanner = new Scanner(new File(file));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        Scanner sc;
        sc = new Scanner(scanner.nextLine());
        V = sc.nextInt();
        int n = sc.nextInt();
        vertices = new Vertex[V];
        for (int i = 0; i < V; i++) {
            sc = new Scanner(scanner.nextLine());
            vertices[i] = new Vertex(sc, n);
        }
    }
    
    /**
     * Computes and returns the largest value of k with spacing at least d.
     */
    public int clustering(int d) {
        if (d < 0) throw new IllegalArgumentException("Illegal distance");
        WUF wuf = new WUF(V);
        for (int i = 0; i < V - 1; i++) {
            for (int j = i + 1; j < V; j++) {
                if (vertices[i].hamming(vertices[j]) < d) wuf.union(i, j);;
            }
        }
        return wuf.count();
    }
    
    /**
     * Unit tests the KClustering data type.
     */
    public static void main(String[] args) {
        String file = args[0];
        int d = Integer.parseInt(args[1]);
        KClustering kc = new KClustering(file);
        System.out.println(kc.clustering(d));
    }
}