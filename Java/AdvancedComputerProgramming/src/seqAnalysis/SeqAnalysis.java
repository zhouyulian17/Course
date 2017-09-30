package seqAnalysis;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

/*************************************************************************
 * Name: Yulian Zhou
 * Email: zhouyulian17@gmail.com
 *
 * Compilation:  javac SeqAnalysis.java
 * Execution:    java SeqAnalysis codon.txt cutter.txt DNAseq1.txt
 *               DNAseq2.txt DNAseq3.txt DNAseq4.txt
 * Dependencies: Sequence.java, Alignment.java, CodonTable.java,
 *               CutterTable.java, CuttingMap.java
 *
 * Description:  This program provides a comprehensive sequence analysis.
 *
 *************************************************************************/

public class SeqAnalysis {
    
    /**
     * Unit tests the Sequence, Alignment, CondonTable, CutterTable and
     * CuttingMap data type.
     */
    public static void main(String[] args) throws FileNotFoundException {
        Scanner sc1 = null;
        Scanner sc2 = null;
        Scanner sc3 = null;
        Scanner sc4 = null;
        Scanner sc5 = null;
        Scanner sc6 = null;
        sc1 = new Scanner(new File(args[0]));
        sc2 = new Scanner(new File(args[1]));
        sc3 = new Scanner(new File(args[2]));
        sc4 = new Scanner(new File(args[3]));
        sc5 = new Scanner(new File(args[4]));
        sc6 = new Scanner(new File(args[5]));
        Sequence sequence1 = new Sequence(sc3);
        Sequence sequence2 = new Sequence(sc4);
        Sequence sequence3 = new Sequence(sc5);
        Sequence sequence4 = new Sequence(sc6);
        
        // test multiple functions in Sequence class
        sequence2.print();
        sequence4.print();
        sequence4.revComp();
        sequence4.print();
        sequence4.toLower();
        sequence4.print();
        sequence4.toUpper();
        sequence4.print();
        sequence4.insert("TG", 2);
        sequence4.print();
        
        // test align() function in Alignment class
        Alignment.align(sequence1, sequence2);  // case of match
        Alignment.align(sequence1, sequence3);  // case of match
        Alignment.align(sequence2, sequence3);  // case of not match
        
        // test translate() function in CodonTable class
        CodonTable codonTable = new CodonTable(sc1);
        codonTable.translate(sequence1);
        codonTable.translate(sequence4);
        
        // test digest() function in CutterTable class
        // output format is:
        // name (number of cuts) : sites of cuts in sequence.
        CutterTable cutterTable = new CutterTable(sc2);
        cutterTable.digest(sequence1);
        
        // test drawMap() function in CuttingMap class
        CuttingMap cuttingMap = new CuttingMap(sequence1, cutterTable);
        cuttingMap.drawMap();
    }
}