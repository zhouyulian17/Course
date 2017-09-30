package sum2;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashSet;
import java.util.Scanner;

/*************************************************************************
 * Name: Yulian Zhou
 * Email: zhouyulian17@gmail.com
 *
 * Compilation:  javac TwoSUM.java
 * Execution:    java TwoSUM algo1-programming_prob-2sum.txt lo hi
 * Dependencies: None
 *
 * Description:  A data type that computes the number of target values t in
 *               the interval [lo, hi] such that there are distinct numbers
 *               x, y in the input file that satisfy x + y = t.
 *
 *************************************************************************/

public class TwoSUM {
    
    private HashSet<Long> set;    // a set that stores unique keys
    
    /**
     * Stores unique values from a file in a set.
     */
    public TwoSUM(String file) {
        set = new HashSet<Long>();
        Scanner scanner = null;
        try {
            scanner = new Scanner(new File(file));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        while(scanner.hasNextLong()) {
            Long temp = scanner.nextLong();
            set.add(temp);
        }
    }
    
    /**
     * Counts number of distinct pairs in the set, the sum of which are within
     * [lo, hi].
     */
    public int count(int lo, int hi) {
        int count = 0;
        for (int t = lo; t < hi + 1; t++) {
            for (Long key: set) {
                Long k = t - key;
                if (k > key && set.contains(k)) {
                    count++;
                    break;
                }
            }
        }
        return count;
    }
    
    /**
     * Unit tests the TwoSUM data type.
     */
    public static void main(String[] args) {
        String file = args[0];
        int lo = Integer.parseInt(args[1]);
        int hi = Integer.parseInt(args[2]);
        TwoSUM ts = new TwoSUM(file);
        System.out.println(ts.count(lo, hi));
    }
}