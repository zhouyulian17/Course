package burrows;

import java.util.LinkedList;
import edu.princeton.cs.algs4.BinaryStdIn;
import edu.princeton.cs.algs4.BinaryStdOut;

/*************************************************************************
 * Name: Yulian Zhou
 * Email: zhouyulian17@gmail.com
 *
 * Compilation:  javac MoveToFront.java
 * Execution:    java MoveToFront -/+
 * Dependencies: BinaryStdIn.java, BinaryStdOut.java
 *
 * Description:  An data type that enables encoding sequences of characters
 *               to indices and decoding indices to sequences with a
 *               move-to-front queueing discipline.
 * http://coursera.cs.princeton.edu/algs4/assignments/burrows.html
 *
 *************************************************************************/

public class MoveToFront {
    
    private static final int R = 256;    // 256 extended ASCII characters
    
    /**
     * Apply move-to-front encoding:
     * (1) read character from standard input
     * (2) find index of the character in the sequence
     * (3) move the character to the front of sequence
     * (4) write the index to standard output
     */
    public static void encode() {
        LinkedList<Character> sequence = new LinkedList<Character>();
        for (char c = 0; c < R; c++) {
            sequence.add(c);
        }
        while (!BinaryStdIn.isEmpty()) {
            char c = BinaryStdIn.readChar();
            int index = sequence.indexOf(c);
            sequence.remove(index);
            sequence.addFirst(c);
            BinaryStdOut.write((char) index);
        }
        BinaryStdOut.close();
    }
    
    /**
     * Apply move-to-front decoding:
     * (1) read index from standard input
     * (2) find character of the index in the sequence
     * (3) move the character to the front of sequence
     * (4) write the character to standard output
     */
    public static void decode() {
        LinkedList<Character> sequence = new LinkedList<Character>();
        for (char c = 0; c < R; c++) {
            sequence.add(c);
        }
        while (!BinaryStdIn.isEmpty()) {
            int index = BinaryStdIn.readChar();
            char c = sequence.remove(index);
            sequence.addFirst(c);
            BinaryStdOut.write(c);
        }
        BinaryStdOut.close();
    }
    
    /**
     * Unit tests the MoveToFront data type.
     */
    public static void main(String[] args) {
        
        // if args[0] is '-', apply move-to-front encoding
        if (args[0].equals("-")) MoveToFront.encode();
        
        // if args[0] is '+', apply move-to-front decoding
        if (args[0].equals("+")) MoveToFront.decode();
    }
}