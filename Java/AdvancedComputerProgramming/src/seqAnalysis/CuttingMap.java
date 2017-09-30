package seqAnalysis;

import java.awt.Font;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/*************************************************************************
 * Name: Yulian Zhou
 * Email: zhouyulian17@gmail.com
 *
 * Compilation:  javac CuttingMap.java
 * Execution:    java CuttingMap cutter.txt DNAseq1.txt
 * Dependencies: StdDraw.java, Sequence.java, CutterTable.java
 *
 * Description:  Draw the cutting sites of cutters in cutter table against
 *               a DNA sequence in a graph.
 *
 *************************************************************************/

public class CuttingMap {
    
    // a list of cutting sites of each cutter in the table
    private List<List<Integer>> cutSites = new ArrayList<List<Integer>>();
    private List<Cutter> table = new ArrayList<Cutter>(); // a table of cutters
    private Sequence seq;                                 // a DNA sequence
    private final int DELAY = 400;                        // delay in miliseconds
    
    /**
     * Initializes a cutting map from a sequence and a cutter table.
     */
    public CuttingMap(Sequence s, CutterTable t) {
        seq = s;
        int[] array = seq.toIntArray();
        for (Cutter c : t.getCutters()) {
            table.add(c);
            cutSites.add(c.search(array));
        }
    }
    
    /**
     * Draws a cutting map with of all cutters against the sequence.
     */
    public void drawMap() {
        boolean[] included = new boolean[table.size()];
        drawTable(included);
        System.out.println("Please click the name of a cutter in the graph to show or hide its cutting site!");
        while (true) {
            if (StdDraw.isMousePressed()) {  // detected mouse click
                double x = StdDraw.mouseX(); // screen x-coordinates
                double y = StdDraw.mouseY(); // screen y-coordinates
                
                // convert to row i, column j, and index k
                int i = (int) ((seq.drawLength() * 0.55 - y) / (seq.drawLength() * 0.1));
                int j = (int) (x / (seq.drawLength() * 0.2));
                int k = i * 5 + j;
                boolean changed = false;
                if (k == included.length + 1) {
                    for (int l = 0; l < included.length; l++) {
                        if (included[l]) {
                            included[l] = false;
                            changed = true;
                        }
                    }
                }
                else if (k == included.length) {
                    for (int l = 0; l < included.length; l++) {
                        if (cutSites.get(l).size() != 0) {
                            if (!included[l]) {
                                included[l] = true;
                                changed = true;
                            }
                        }
                    }
                }
                else if (k > -1 && k < included.length) {
                    if (cutSites.get(k).size() != 0) {
                        included[k] = !included[k];
                        changed = true;
                    }
                }
                
                // update the cutter shown in the graph
                if (changed) {
                    drawTable(included);
                    drawCutter(included);
                    StdDraw.show(DELAY);
                }
            }
        }
    }
    
    // draw the name and the number of cutting sites in a graphic table,
    // draw the DNA sequence as a line
    private void drawTable(boolean[] included) {
        seq.draw();
        double length = seq.drawLength();
        StdDraw.setPenColor(StdDraw.BLUE);
        StdDraw.text(length * 0.5, length * 0.6, "Click the name to select or deselect a cutter!");
        int i;
        for (i = 0; i < table.size(); i++) {
            StdDraw.setPenColor(StdDraw.BLACK);
            String cutter = table.get(i).getCutter();
            String cut_num = Integer.toString(cutSites.get(i).size());
            StdDraw.rectangle(length * (i % 5 + 0.5) * 0.2, length * 0.5
                              * (1 - (i / 5) * 0.2), length * 0.1, length * 0.05);
            if (included[i]) StdDraw.setPenColor(StdDraw.RED);
            else StdDraw.setPenColor(StdDraw.BLACK);
            StdDraw.text(length * (i % 5 + 0.5) * 0.2, length * 0.5
                         * (1 - (i / 5) * 0.2), cutter + "(" + cut_num + ")");
        }
        StdDraw.setPenColor(StdDraw.BLACK);
        StdDraw.rectangle(length * (i % 5 + 0.5) * 0.2, length * 0.5 * (1 - (i / 5) * 0.2), length * 0.1,
                          length * 0.05);
        StdDraw.text(length * (i % 5 + 0.5) * 0.2, length * 0.5 * (1 - (i / 5) * 0.2), "All");
        i++;
        StdDraw.rectangle(length * (i % 5 + 0.5) * 0.2, length * 0.5 * (1 - (i / 5) * 0.2), length * 0.1,
                          length * 0.05);
        StdDraw.text(length * (i % 5 + 0.5) * 0.2, length * 0.5 * (1 - (i / 5) * 0.2), "None");
    }
    
    // draw sites of selected cutters against DNA sequence in the graph
    private void drawCutter(boolean[] included) {
        StdDraw.setPenColor(StdDraw.BLACK);
        for (int i = 0; i < included.length; i++) {
            if (included[i]) {
                table.get(i)
                .draw(cutSites.get(i), seq.size(), seq.drawLength());
            }
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
        CutterTable ct = new CutterTable(sc1);
        Sequence seq = new Sequence(sc2);
        CuttingMap cm = new CuttingMap(seq, ct);
        
        // draw a cutting map of cutters against a given DNA sequence
        // cutters are listed in table, and click to select or unselect a specific cutter.
        cm.drawMap();
    }
}