package seqAnalysis;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

/*************************************************************************
 * Name: Yulian Zhou
 * Email: zhouyulian17@gmail.com
 *
 * Compilation:  javac CodonTable.java
 * Execution:    java CodonTable codon.txt DNAseq1.txt DNAseq2.txt
 * Dependencies: Sequence.java
 *
 * Description:  A data type that stores amino acids and their genetic
 * 			     code. It supports translating an input DNA sequence into
 *               amino acid sequence.
 *
 *************************************************************************/

public final class CodonTable {
    
    // 3-letter genetic code <Key> and amino acid <Value> pairs are stored in codonTable
    private HashMap<String, AminoAcid> codonTable = new HashMap<String, AminoAcid>();
    private int printLength = 100;    // number of amino acid printed per line by show()
    
    // helper class stores amino acid information
    private static class AminoAcid {
        
        private final String AA;      // the amino acid name
        private final char SLC;       // the single letter code
        private final double MW;      // the molecular weight in kilodalton
        
        /**
         * Initializes a new amino acid with name AA, single letter code SLC and
         * molecular weight MW.
         */
        public AminoAcid(String aa, char singleLetterCode, double molecularWeigth) {
            AA = aa;
            SLC = singleLetterCode;
            MW = molecularWeigth;
        }
        
        public char getSLC() { return SLC; }    // returns the single letter code
        
        public double getMW() { return MW; }    // returns the molecular weight
    }
    
    /**
     *  Initializes a codon table from a scanner.
     */
    public CodonTable(Scanner sc) {
        int n = Integer.parseInt(sc.nextLine());
        for (int i = 0; i < n; i++) {
            String[] s = sc.nextLine().split(" ");
            AminoAcid aa = new AminoAcid(s[0], s[1].toCharArray()[0], Double.parseDouble(s[2]));
            for (int j = 3; j < s.length; j++) {
                String code = s[j];
                codonTable.put(code, aa);
            }
        }
    }
    
    /**
     * Returns the codon table.
     */
    public HashMap<String, AminoAcid> getCodons() { return codonTable; }
    
    /**
     * Returns and shows the sequence of amino acids after translation of the
     * DNA sequence starting from site 0.
     */
    public String translate(Sequence seq) { return translate(seq, 0); }
    
    /**
     * Returns and shows the sequence of amino acids after translation of the
     * DNA sequence starting from site shift. An unknown amino acid (when DNA
     * sequence contains 'N') is translated as 'X'.
     */
    public String translate(Sequence seq, int shift) {
        String s = "";
        List<Character> sequence = seq.getSeq();
        int i = shift, j;
        for (; i < sequence.size() - 2; i += 3) {
            String t = "";
            for (j = i; j < i + 3; j++) {
                t += sequence.get(j);
            }
            if (j != i + 3) break;
            t = t.toUpperCase();
            if (getCodons().containsKey(t)) {
                s += getCodons().get(t).getSLC();
            }
            else s += "X";
        }
        show(s);
        return s;
    }
    
    // show the single letter code representation of amino acid sequence in console
    private void show(String s) {
        int i;
        for (i = 0; i < s.length() / printLength; i++) {
            for (int j = 0; j < printLength; j++) {
                System.out.printf("%c", s.charAt(i * printLength + j));
            }
            System.out.println();
        }
        for (int j = i * printLength; j < s.length(); j++) {
            System.out.printf("%c", s.charAt(j));
        }
        System.out.println();
        System.out.println();
    }
    
    public static void main(String[] args) {
        
        // unit test
        Scanner sc1 = null;
        Scanner sc2 = null;
        Scanner sc3 = null;
        try {
            sc1 = new Scanner(new File(args[0]));
            sc2 = new Scanner(new File(args[1]));
            sc3 = new Scanner(new File(args[2]));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        CodonTable ct = new CodonTable(sc1);
        Sequence seq2 = new Sequence(sc2);
        Sequence seq3 = new Sequence(sc3);
        sc1.close();
        sc2.close();
        sc3.close();
        
        // translate a DNA sequence into amino acid sequence
        ct.translate(seq2);
        
        // translate a DNA sequence from ith (1 to 3) nucleotide
        for (int i = 0; i < 3; i++) {
            ct.translate(seq3, i);
        }
    }
}