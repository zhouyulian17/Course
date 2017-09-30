package medianMaintenance;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Scanner;

/*************************************************************************
 * Name: Yulian Zhou
 * Email: zhouyulian17@gmail.com
 *
 * Compilation:  javac Medians.java
 * Execution:    java Medians Median.txt mod
 * Dependencies: None
 *
 * Description:  A data type that computes the modular value of sum of
 *               medians from a stream of numbers.
 *
 *************************************************************************/

public class Medians {
    
    // medians.get(k) = the kth median of the numbers [x1..xk]
    private List<Integer> medians = new ArrayList<Integer>();
    
    /**
     * Reads file, calculates and stores medians after each input.
     */
    public Medians(String file) {
        
        // a max priority queue that stores half of values <= median
        PriorityQueue<Integer> lo = new PriorityQueue<Integer>(new Comparator<Integer>() {
            @Override
            public int compare(Integer n1, Integer n2) {
                return n2 - n1;
            }
        });
        
        // a min priority queue that stores half of values > median
        PriorityQueue<Integer> hi = new PriorityQueue<Integer>();
        Scanner scanner = null;
        try {
            scanner = new Scanner(new File(file));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        
        lo.add(scanner.nextInt());
        medians.add(lo.peek());
        while (scanner.hasNextInt()) {
            int t = scanner.nextInt();
            if (lo.size() == hi.size()) {
                if (t > hi.peek()) {
                    lo.add(hi.poll());
                    hi.add(t);
                }
                else lo.add(t);
            }
            else {
                if (t < lo.peek()) {
                    hi.add(lo.poll());
                    lo.add(t);
                }
                else hi.add(t);
            }
            medians.add(lo.peek());
        }
    }
    
    /**
     * Returns the sum of medians modulo mod.
     */
    public int mediansMod(int mod) {
        int sum = 0;
        for (int mid : medians) {
            sum += mid;
        }
        return sum % mod;
    }
    
    /**
     * Unit tests the Medians data type.
     */
    public static void main(String[] args) {
        String file = args[0];
        int mod = Integer.parseInt(args[1]);
        Medians md = new Medians(file);
        System.out.println(md.mediansMod(mod));
    }
}