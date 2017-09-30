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
 * Compilation:  javac Alignment.java
 * Execution:    java Alignment DNAseq1.txt DNAseq2.txt
 * Dependencies: Sequence.java
 *
 * Description:  Alignment of two input DNA sequences by Needleman–Wunsch
 *               algorithm.
 *
 *************************************************************************/

public class Alignment {
    
    private static int intgap = 2;          // penalty scores for a internal gap
    private static int extgap = 0;          // penalty scores for a end gap
    private static int mismatch = 1;        // penalty scores for a mismatch
    private static int match = -1;          // decrease in penalty for a match
    private static char gap = (char) 126;   // '~' to represent a gap in the sequence
    private static char M = (char) 124;     // '|' to highlight a match
    private static char MM = (char) 35;     // '#' to highlight a mismatch
    private static char G = ' ';            // ' ' to indicate a gap
    private static int showLength = 100;    // number of nucleotides printed per line by show()
    
    /**
     * Aligns sequence seq1 and seq2 and shows the alignment result.
     */
    public static void align(Sequence seq1, Sequence seq2) {
        List<Character> l1 = seq1.getSeq();
        List<Character> l2 = seq2.getSeq();
        
        // sequence of seq1 after gap '~' insertion
        List<Character> newl1 = new ArrayList<Character>();
        
        // sequence of seq2 after gap '~' insertion
        List<Character> newl2 = new ArrayList<Character>();
        
        // indicators of gap, match, mismatch between seq1 and seq2 after alignment
        List<Character> temp = new ArrayList<Character>();
        
        int[][] penalty = encode(l1, l2);
        decode(l1, l2, penalty, newl1, newl2, temp);
        show(newl1, temp, newl2, l1.size(), l2.size());
    }
    
    // construct a minimal penalty grid by dynamic programming, and
    // the choice on penalty(i, j) depends upon the lowest penalty among:
    // (1) penalty of (i - 1, j - 1) and a match or mismatch between seq1 at i and seq2 at j.
    // (2) penalty of (i, j - 1) and a external or internal gap in seq1 at i.
    // (3) penalty of (i - 1, j) and a external or internal gap in seq2 at j.
    private static int[][] encode(List<Character> l1, List<Character> l2) {
        int[][] penalty = new int[l1.size() + 1][l2.size() + 1];
        for (int i = 0; i < penalty.length; i++) {
            penalty[i][0] = extgap * i;
        }
        for (int j = 0; j < penalty[0].length; j++) {
            penalty[0][j] = extgap * j;
        }
        for (int i = 1; i < penalty.length; i++) {
            for (int j = 1; j < penalty[0].length; j++) {
                int s1 = penalty[i - 1][j];
                if (j == 0 || j == penalty[0].length - 1) s1 += extgap;
                else s1 += intgap;
                int s2 = penalty[i][j - 1];
                if (i == 0 || i == penalty.length - 1) s2 += extgap;
                else s2 += intgap;;
                if (s1 > s2) s1 = s2;
                int s3 = penalty[i - 1][j - 1];
                if (l1.get(i - 1) != l2.get(j - 1)) {
                    if (l1.get(i - 1) != 'N' && l2.get(j - 1) != 'N') s3 += mismatch;
                }
                else s3 += match;
                if (s1 > s3) s1 = s3;
                penalty[i][j] = s1;
            }
        }
        return penalty;
    }
    
    // perform back trace to obtain the alignment and modify seq1 and seq2 accordingly
    private static void decode(List<Character> l1, List<Character> l2, int[][] penalty,
                               List<Character> newl1, List<Character> newl2, List<Character> temp) {
        int i, j = 0;
        for (i = l1.size(), j = l2.size(); i > 0 && j > 0;) {
            int p = penalty[i][j];
            if (l1.get(i - 1) == l2.get(j - 1)
                && p == penalty[i - 1][j - 1] + match) {
                
                // seq1(i) matches with seq2(j)
                newl1.add(l1.get(--i));
                newl2.add(l2.get(--j));
                temp.add(M);
            }
            else if ((l1.get(i - 1) != l2.get(j - 1))
                     && (l1.get(i - 1) != 'N' && l2.get(j - 1) != 'N')
                     && (p == penalty[i - 1][j - 1] + mismatch)) {
                
                // seq1(i) mismatches with seq2(j)
                newl1.add(l1.get(--i));
                newl2.add(l2.get(--j));
                temp.add(MM);
            }
            else if ((l1.get(i - 1) != l2.get(j - 1))
                     && (l1.get(i - 1) == 'N' || l2.get(j - 1) == 'N')
                     && (p == penalty[i - 1][j - 1])) {
                
                // when seq1(i) or seq2(j) is any nucleotide 'N',
                // it does not count towards a match or mismatch.
                newl1.add(l1.get(--i));
                newl2.add(l2.get(--j));
                temp.add(MM);
            }
            else if ((j == 1 || j == l2.size())
                     && p == penalty[i - 1][j] + extgap) {
                
                // seq1(i) aligns to an external gap on seq2
                newl1.add(l1.get(--i));
                newl2.add(gap);
                temp.add(G);
            }
            else if (p == penalty[i - 1][j] + intgap) {
                
                // seq1(i) aligns to an internal gap at seq2(j)
                newl1.add(l1.get(--i));
                newl2.add(gap);
                temp.add(G);
            }
            else if ((i == 1 || i == l1.size())
                     && p == penalty[i][j - 1] + extgap) {
                
                // seq2(j) aligns to an external gap on seq1
                newl1.add(gap);
                newl2.add(l2.get(--j));
                temp.add(G);
            }
            else if (p == penalty[i][j - 1] + intgap) {
                
                // seq2(j) aligns to an internal gap at seq1(i)
                newl1.add(gap);
                newl2.add(l2.get(--j));
                temp.add(G);
            }
            else {
                try {
                    throw new Exception();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        
        // the end of seq2 aligns to gap on seq1
        for (int k = --i; k > -1; k--) {
            newl1.add(l1.get(k));
            newl2.add(gap);
            temp.add(G);
        }
        
        // the end of seq1 aligns to gap on seq2
        for (int k = --j; k > -1; k--) {
            newl1.add(gap);
            newl2.add(l2.get(k));
            temp.add(G);
        }
    }
    
    // show the alignment in console in a specific format for visualization
    private static void show(List<Character> l1, List<Character> l2,
                             List<Character> l3, int size1, int size2) {
        int n = l1.size();
        int m = n / showLength;
        int[] count = new int[3];
        for (int i = 0; i < m; i++) {
            showLines(l1, l2, l3, count, n - i * showLength - 1, showLength, size1, size2);
        }
        showLines(l1, l2, l3, count, n % showLength - 1, n % showLength, size1, size2);
    }
    
    // show one line of alignment of seq1, indicator and seq2
    private static void showLines(List<Character> l1, List<Character> l2,
                                  List<Character> l3, int[] count, int n, int k, int size1, int size2) {
        if (count[0] == size1) System.out.printf("%4d- ",  count[0]);
        else System.out.printf("%4d- ",  count[0] + 1);
        showLine(l1, count, 0, n, k);
        if (count[0] != 0) System.out.printf(" -%4d\n",  count[0]);
        else System.out.printf(" -%4d\n",  count[0] + 1);
        
        System.out.printf("      ");
        showLine(l2, count, 1, n, k);
        System.out.println("");
        
        if (count[2] == size2) System.out.printf("%4d- ",  count[2]);
        else System.out.printf("%4d- ",  count[2] + 1);
        showLine(l3, count, 2, n, k);
        if (count[2] != 0) System.out.printf(" -%4d\n",  count[2]);
        else System.out.printf(" -%4d\n",  count[2] + 1);
        System.out.println();
    }
    
    // show one line of seq1, indicator or seq2 in the alignment
    private static void showLine(List<Character> l, int[] count, int i, int n, int k) {
        for (int j = n; j > n - k; j--) {
            char c = l.get(j);
            if (c != gap) count[i]++;
            System.out.printf("%c", c);
        }
    }
    
    public static void main(String[] args) {
        
        // unit test
        Scanner sc1 = null;
        Scanner sc2 = null;
        try {
            sc1 = new Scanner(new File(args[0]));
            sc2 = new Scanner(new File(args[1]));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        Sequence seq1 = new Sequence(sc1);
        Sequence seq2 = new Sequence(sc2);
        
        // show sequence alignment between seq1 and seq2
        align(seq1, seq2);
    }
}