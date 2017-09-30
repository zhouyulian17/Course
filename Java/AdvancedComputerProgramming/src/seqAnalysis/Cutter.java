package seqAnalysis;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/*************************************************************************
 * Name: Yulian Zhou
 * Email: zhouyulian17@gmail.com
 *
 * Compilation:  javac Cutter.java
 * Execution:    java Cutter
 * Dependencies: StdDraw.java, Sequence.java
 *
 * Description:  An immutable data type that represents a cutter. It
 *               supports finding all recognition sites of the cutter in
 *               an input DNA sequence by Knuth–Morris–Pratt string
 *               searching algorithm.
 *
 *************************************************************************/

public class Cutter implements Comparable<Cutter> {
    
    private String cutter;    // name of the cutter
    private String recSeq;    // recognition DNA sequence of the cutter
    private int[] seqToInt;   // numeric representation of recSeq
    private int[][] dfa;      // deterministic finite automaton array from recSeq pattern
    
    /**
     *  Initializes a cutter with a name and a recognition DNA sequence.
     */
    public Cutter(String name, String seq) {
        cutter = name;
        recSeq = seq;
        seqToInt = new int[seq.length()];
        convert(seq, seqToInt);
        dfa = new int[5][seq.length()];
        KMP();                           // construct a dfa
    }
    
    /**
     * Returns the name of cutter.
     */
    public String getCutter() { return cutter; }
    
    /**
     * Prints the name and recognition sites of cutter to console.
     */
    public void print() { System.out.printf("%8s %8s\n", cutter, recSeq); }
    
    // convert character in sequence to integer for dfa array construction
    private void convert(String s, int[] array) {
        assert(s.length() == array.length);
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            array[i] = Sequence.convert(c);
        }
    }
    
    // construct dfa array:
    // (1) Includes one state for each character in pattern (plus accept state)
    // (2) If in state i and next int j == seqToInt[i] (match case), go to i+1
    // (3) Maintains state X and copy dfa[][X] to dfa[][j] for mismatch case
    // (4) Updates X to dfa[seqToInt[i]][X]
    private void KMP() {
        int X = 0;
        for (int j = 0; j < dfa.length; j++) {
            dfa[j][X] = 0;
        }
        dfa[seqToInt[0]][X] = 1;
        for (int i = 1; i < dfa[0].length; i++) {
            for (int j = 0; j < dfa.length; j++) {
                dfa[j][i] = dfa[j][X];
            }
            dfa[seqToInt[i]][i] = i + 1;
            X = dfa[seqToInt[i]][X];
        }
    }
    
    /**
     * Searches recSeq of cutter in DNA sequence.
     */
    public List<Integer> search(Sequence seq) {
        int[] array = seq.toIntArray();
        return search(array);
    }
    
    /**
     * Performs string matching using dfa:
     * (1) At state i, reads the integer array[j] in query sequence.
     * (2) Updates i to state dfa[array[j]][i].
     * (3) If i == length of recSeq, records the start site of the match.
     * (4) Starts a new search from start site + 1.
     */
    public List<Integer> search(int[] array) {
        List<Integer> list = new ArrayList<Integer>();
        for (int j = 0, i = 0; j < array.length; j++) {
            i = dfa[array[j]][i];
            if (i == dfa[0].length) {
                j -= dfa[0].length - 1;
                i = 0;
                list.add(j + 1);
            }
        }
        return list;
    }
    
    /**
     * Draws sites of cutter against a given DNA sequence in a simple graph.
     */
    public void draw(Sequence seq) {
        List<Integer> list = search(seq);
        draw(list, seq.size(), seq.drawLength());
    }
    
    /**
     * Draws sites in a list against a DNA sequence with n nucleotides.
     */
    public void draw(List<Integer> list, int n, double length) {
        for (int i : list) {
            double x = length * i / n;
            Random rand = new Random();
            double y = -rand.nextInt((int) (length * 0.2)) - 1;
            StdDraw.line(x, 0, x, y);
            StdDraw.text(x, y - length * 0.05,
                         getCutter() + " " + Integer.toString(i));
        }
    }
    
    /**
     * Compares two cutters by their name.
     */
    @Override
    public int compareTo(Cutter that) {
        return this.getCutter().compareTo(that.getCutter());
    }
    
    public static void main(String[] args) {
        
        // unit test
        Sequence seq = new Sequence("atcggatcggcgcgccatcgatggcgcgccaat");
        Cutter cutter = new Cutter("X", "GGCGCGCC");
        List<Integer> l = cutter.search(seq);
        for (int i : l) {
            System.out.println(i);
        }
        
        // draw a DNA sequence as a line and highlight a cutter site against it
        seq.draw();
        cutter.draw(l, seq.size(), seq.drawLength());
    }
}