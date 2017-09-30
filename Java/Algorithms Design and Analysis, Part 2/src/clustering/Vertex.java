package clustering;

import java.util.Scanner;

/*************************************************************************
 * Name: Yulian Zhou
 * Email: zhouyulian17@gmail.com
 *
 * Compilation:  javac Vertex.java
 * Execution:    None
 * Dependencies: None
 *
 * Description:  A bit array representation of vertex.
 *
 *************************************************************************/

public class Vertex {
    
    private int[] v;        // v[i] = the ith bit of vertex
    private final int N;    // number of bits
    
    /**
     * Initializes a vertex with n bits from a scanner.
     */
    public Vertex(Scanner sc, int n) {
        N = n;
        v = new int[N];
        for (int i = 0; i < N; i++) {
            v[i] = sc.nextInt();
        }
    }
    
    /**
     * Returns the kth bit of this vertex.
     */
    public int bit(int k) { return v[k]; }
    
    /**
     * Returns the hamming distance of this vertex to other vertex.
     */
    public int hamming(Vertex other) {
        int d = 0;
        for (int i = 0; i < N; i++) {
            d += this.bit(i) ^ other.bit(i);
        }
        return d;
    }
}