package seqAnalysis;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.Set;
import java.util.TreeSet;

/*************************************************************************
 * Name: Yulian Zhou
 * Email: zhouyulian17@gmail.com
 *
 * Compilation:  javac CutterTable.java
 * Execution:    java CutterTable cutter.txt DNAseq1.txt
 * Dependencies: Cutter.java, Sequence.java
 *
 * Description:  A data type that stores a set of cutters.
 *
 *************************************************************************/

public class CutterTable {
    
    Set<Cutter> cutterTable = new TreeSet<Cutter>();    // a set contains cutters
    
    /**
     * Initializes a cutter table from a scanner.
     */
    public CutterTable(Scanner sc) {
        int n = Integer.parseInt(sc.nextLine());
        for (int i = 0; i < n; i++) {
            String[] s = sc.nextLine().split(" ");
            cutterTable.add(new Cutter(s[0], s[1]));
        }
    }
    
    /**
     * Returns the cutter table.
     */
    public Set<Cutter> getCutters() { return cutterTable; }
    
    /**
     * Adds cutter c to the table.
     */
    public void addCutter(Cutter c) { cutterTable.add(c); }
    
    /**
     * Adds a cutter with a name and a recognition sequence.
     */
    public void addCutter(String name, String seq) {
        addCutter(new Cutter(name, seq));
    }
    
    /**
     * Returns the number of cutters in cutter table.
     */
    public int size() { return cutterTable.size(); }
    
    /**
     * Prints all cutters in table to the console.
     */
    public void showCutter() {
        for (Cutter c : cutterTable) {
            c.print();
        }
        System.out.println();
    }
    
    /**
     * Searches all cutters against a given DNA sequence, and output the results
     * in a specific format: name (number of cuts) : sites of cuts in sequence.
     */
    public void digest(Sequence seq) {
        List<Integer> list = new ArrayList<Integer>();
        int[] array = seq.toIntArray();
        for (Cutter c : cutterTable) {
            list = c.search(array);
            System.out.printf("%8s (%d): ", c.getCutter(), list.size());
            for (int n : list) {
                System.out.printf("%4d ", n);
            }
            System.out.println();
        }
        System.out.println();
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
        CutterTable cutters = new CutterTable(sc1);
        Sequence seq = new Sequence(sc2);
        sc1.close();
        sc2.close();
        
        // show all cutters in the cutter table
        cutters.showCutter();
        
        // show DNA sequence
        seq.print();
        
        // show cutting sites of cutters against a DNA sequence
        cutters.digest(seq);
    }
}