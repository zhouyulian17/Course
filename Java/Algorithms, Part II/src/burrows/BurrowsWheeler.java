package burrows;

import edu.princeton.cs.algs4.BinaryStdIn;
import edu.princeton.cs.algs4.BinaryStdOut;

/*************************************************************************
 * Name: Yulian Zhou
 * Email: zhouyulian17@gmail.com
 *
 * Compilation:  javac BurrowsWheeler.java
 * Execution:    java BurrowsWheeler
 * Dependencies: CircularSuffixArray.java
 *
 * Description:  A data type that performs Burrows-Wheeler encoding
 *               (transforms a string s to the last column in the sorted
 *               suffixes array t[], preceded by the row number in which
 *               the original string ends up) and decoder (inverts the
 *               Burrows-Wheeler encoding and recovers the original input
 *               string).
 * http://coursera.cs.princeton.edu/algs4/assignments/burrows.html
 *
 *************************************************************************/

public class BurrowsWheeler {
    
    private static final int R = 256;    // 256 extended ASCII characters
    
    /**
     * apply Burrows-Wheeler encoding, reading from standard input and writing to standard output
     */
    public static void encode() {
        String s = BinaryStdIn.readString();
        CircularSuffixArray CSA = new CircularSuffixArray(s);
        int first = -1;
        
        // the index of original string in the sorted suffix array
        for (int i = 0; i < s.length(); i++) {
            if (CSA.index(i) == 0) {
                first = i;
                break;
            }
        }
        System.out.println(first);
        BinaryStdOut.write(first);
        
        // the tail character of sorted suffixes
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt((CSA.index(i) + s.length() - 1) % s.length());
            BinaryStdOut.write(c);
        }
        BinaryStdOut.close();
    }
    
    /***
     * apply Burrows-Wheeler decoding, reading from standard input and writing to standard output
     */
    public static void decode() {
        int first = BinaryStdIn.readInt();
        char[] c = BinaryStdIn.readString().toCharArray();
        int[] next = new int[c.length];
        
        // construct next array
        sort(c, next);
        int i = first;
        for (int j = 0; j < c.length; j++) {
            BinaryStdOut.write(c[next[i]]);
            i = next[i];
        }
        BinaryStdOut.close();
    }
    
    // key-index counting: ith element in sorted order (head) is in next[i]
    // element in original input (tail)
    private static void sort(char[] c, int[] next) {
        int[] count = new int[R + 1];
        for (int i = 0; i < c.length; i++) {
            count[c[i] + 1]++;
        }
        for (int r = 0; r < R; r++) {
            count[r + 1] += count[r];
        }
        for (int i = 0; i < c.length; i++) {
            next[count[c[i]]++] = i;
        }
    }
    
    /**
     * Unit tests the BurrowsWheeler data type.
     */
    public static void main(String[] args) {
        
        // if args[0] is '-', apply Burrows-Wheeler encoding
        if (args[0].equals("-")) BurrowsWheeler.encode();
        
        // if args[0] is '+', apply Burrows-Wheeler decoding
        if (args[0].equals("+")) BurrowsWheeler.decode();
    }
}