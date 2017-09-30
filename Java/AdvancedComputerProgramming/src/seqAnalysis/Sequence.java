package seqAnalysis;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/*************************************************************************
 * Name: Yulian Zhou
 * Email: zhouyulian17@gmail.com
 *
 * Compilation:  javac Sequence.java
 * Execution:    java Sequence DNAseq1.txt
 * Dependencies: StdDraw.java
 *
 * Description:  A data type that represents a DNA sequence.
 *
 *************************************************************************/

public class Sequence {
    
    // the DNA sequence (A/a, T/t, G/g, C/c and N/any)
    private List<Character> seq = new ArrayList<Character>();
    private int printLength = 100;      // number of nucleotides printed per line by print()
    private double drawLength = 100.0;  // length of DNA drawn in graph by draw()
    
    /**
     * Initializes a sequence from a string.
     */
    public Sequence(String s) {
        for (char c : s.toCharArray()) {
            if (isValid(c)) seq.add(c);
        }
    }
    
    /**
     * Initializes a sequence from a scanner.
     */
    public Sequence(Scanner sc) {
        String s = sc.next();
        for (char c : s.toCharArray()) {
            if (isValid(c)) seq.add(c);
        }
    }
    
    // does c represent a valid nucleotide?
    private boolean isValid(char c) {
        if (c == 'A' || c == 'T' || c == 'G' || c == 'C' || c == 'N'
            || c == 'a' || c == 't' || c == 'g' || c == 'c') {
            return true;
        }
        return false;
    }
    
    /**
     * Returns the nucleotide sequence of this sequence.
     */
    public List<Character> getSeq() { return seq; }
    
    /**
     * Returns the number of nucleotides in this sequence.
     */
    public int size() { return seq.size(); }
    
    /**
     * Returns the number of nucleotides printed per line by print().
     */
    public int printLength() { return printLength; }
    
    /**
     * Returns the length of sequence drawn in graph by draw().
     */
    public double drawLength() {  return drawLength; }
    
    /**
     * Draws a line in graph that represents this sequence,
     * and labels the start and end number.
     */
    public void draw() {
        StdDraw.clear();
        StdDraw.setPenColor(StdDraw.BLACK);
        StdDraw.setXscale(-drawLength * 0.1, drawLength * 1.2);
        StdDraw.setYscale(-drawLength * 0.65, drawLength * 0.65);
        StdDraw.line(0, 0, drawLength, 0);
        StdDraw.text(-drawLength * 0.05, 0, "0");
        StdDraw.text(drawLength * 1.1, 0, Integer.toString(size()));
    }
    
    /**
     * Prints this sequence to the console in a specific format for visualization.
     */
    public void print() {
        int n = getSeq().size();
        int m = n / printLength;
        for (int i = 0; i < m; i++) {
            printLine(i * printLength, printLength);
        }
        printLine(n - n % printLength, n % printLength);
        System.out.println();
    }
    
    // print nucleotides starting from (n + 1) to (n + k) in this sequence
    private void printLine(int n, int k) {
        System.out.printf("%4d- ", n + 1);
        for (int i = 0; i < k; i++) {
            if (i % 10 == 0) System.out.printf(" ");
            System.out.printf("%c", seq.get(i + n));
        }
        for (int i = k; i < printLength; i++) {
            if (i % 10 == 0) System.out.printf(" ");
            System.out.printf(" ");
        }
        System.out.printf(" -%4d\n",  n + printLength);
    }
    
    /**
     * Adds string s to this sequence starting from site k.
     */
    public void insert(String s, int k) {
        assert(k > -1 && k < seq.size() + 1);
        ArrayList<Character> newSeq = new ArrayList<Character>();
        if (k == 0) {
            for (char c : s.toCharArray()) {
                if (isValid(c)) newSeq.add(c);
            }
        }
        else {
            for (int i = 0; i < k; i++) {
                newSeq.add(seq.get(i));
            }
            for (char c : s.toCharArray()) {
                if (isValid(c)) newSeq.add(c);
            }
        }
        for (int i = k; i < seq.size(); i++) {
            newSeq.add(seq.get(i));
        }
        seq = newSeq;
    }
    
    /**
     * Deletes nucleotides between site k1 to k2 in this sequence.
     */
    public void delete(int k1, int k2) {
        assert(k1 > -1 && k1 < seq.size() + 1);
        assert(k2 > -1 && k2 < seq.size() + 1);
        if (k1 > k2) {
            int temp = k1;
            k1 = k2;
            k2 = temp;
        }
        ArrayList<Character> newSeq = new ArrayList<Character>();
        for (int i = 0; i < k1; i++) {
            newSeq.add(seq.get(i));
        }
        for (int i = k2; i < seq.size(); i++) {
            newSeq.add(seq.get(i));
        }
        seq = newSeq;
    }
    
    /**
     * Changes nucleotide in this sequence at site k to c.
     */
    public void mutate(int k, char c) {
        if (isValid(c)) seq.set(k, c);
    }
    
    /**
     * Reverses the order of this sequence.
     */
    public void reverse() {
        ArrayList<Character> newSeq = new ArrayList<Character>();
        for (int i = 0; i < seq.size(); i++) {
            newSeq.add(seq.get(seq.size() - 1 - i));
        }
        seq = newSeq;
    }
    
    /**
     * Changes this sequence to the complementary one.
     */
    public void complement() {
        ArrayList<Character> newSeq = new ArrayList<Character>();
        for (char c : seq) {
            newSeq.add(complement(c));
        }
        seq = newSeq;
    }
    
    // change nucleotide c to the complementary one
    private char complement(char c) {
        switch (c) {
            case 'A': c = 'T'; break;
            case 'T': c = 'A'; break;
            case 'G': c = 'C'; break;
            case 'C': c = 'G'; break;
            case 'a': c = 't'; break;
            case 't': c = 'a'; break;
            case 'g': c = 'c'; break;
            case 'c': c = 'g'; break;
            default:  c = 'N'; break;
        }
        return c;
    }
    
    /**
     * Changes this sequence to the reverse-complementary sequence.
     * (Convert sequence of one strand of the double-stranded
     *  DNA to the sequence of the other strand)
     */
    public void revComp() {
        ArrayList<Character> newSeq = new ArrayList<Character>();
        for (int i = 0; i < seq.size(); i++) {
            char c = seq.get(seq.size() - 1 - i);
            newSeq.add(complement(c));
        }
        seq = newSeq;
    }
    
    /**
     * Changes nucleotides to upper-case representation in this sequence.
     */
    public void toUpper() {
        ArrayList<Character> newSeq = new ArrayList<Character>();
        for (char c : seq) {
            newSeq.add(Character.toUpperCase(c));
        }
        seq = newSeq;
    }
    
    /**
     * Changes nucleotides to lower-case representation in this sequence.
     */
    public void toLower() {
        ArrayList<Character> newSeq = new ArrayList<Character>();
        for (char c : seq) {
            newSeq.add(Character.toLowerCase(c));
        }
        seq = newSeq;
    }
    
    /**
     * Numeric representation of this sequence for dfa array construction in string matching.
     */
    public int[] toIntArray(){
        int[] array = new int[getSeq().size()];
        for (int i = 0; i < array.length; i++) {
            char c = Character.toUpperCase(getSeq().get(i));
            array[i] = Sequence.convert(c);
        }
        return array;
    }
    
    /**
     * Converts a nucleotide character to a integer - used for string matching.
     */
    public static int convert(char c) {
        switch (c) {
            case 'A': return 0;
            case 'T': return 1;
            case 'G': return 2;
            case 'C': return 3;
            default:  return 4;
        }
    }
    
    public static void main(String[] args) {
        
        // unit test
        Scanner sc1 = null;
        try {
            sc1 = new Scanner(new File(args[0]));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        Sequence s = new Sequence(sc1);
        sc1.close();
        
        // show DNA sequence in a given format
        s.print();
    }
}