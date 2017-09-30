package countInversions;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/*************************************************************************
 * Name: Yulian Zhou
 * Email: zhouyulian17@gmail.com
 *
 * Compilation:  javac MergeSortCount.java
 * Execution:    java MergeSortCount IntegerArray.txt
 * Dependencies: None
 *
 * Description:  This class provides static methods for counting inversions
 *               in an array using mergesort.
 *
 *************************************************************************/

public class MergeSortCount {
    
    private static long count;    // count of inversions
    
    /**
     * Takes the name of file that stores an integer array, rearranges the array
     * in ascending order by mergesort using the natural order, and returns the
     * counts of inversions in the array.
     */
    public static long sort(String file) {
        
        // read the file and store in an array list
        Scanner scanner = null;
        try {
            scanner = new Scanner(new File(file));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        List<Integer> list = new ArrayList<Integer>();
        while(scanner.hasNextLine()) list.add(scanner.nextInt());
        count = 0;
        
        // merge sort the list and calculate inversions
        sort(list, 0, list.size());
        assert isSorted(list);
        return count;
    }
    
    // mergesort list[lo, hi)
    private static void sort(List<Integer> list, int lo, int hi) {
        if (lo == hi - 1) return;
        int mid = (lo + hi) / 2;
        sort(list, lo, mid);
        sort(list, mid, hi);
        merge(list, lo, mid, hi);
    }
    
    // stably merge list[lo, mid) with list[mid, hi) using newList[lo, hi) in sorted order
    private static void merge(List<Integer> list, int lo, int mid, int hi) {
        List<Integer> newList = new ArrayList<Integer>();
        int i = lo;
        int j = mid;
        for (int k = 0; k < hi - lo; k++) {
            if (list.get(i) < list.get(j)) {
                newList.add(list.get(i));
                if (i < mid - 1) i++;
                else {
                    newList.addAll(list.subList(j, hi));
                    break;
                }
            }
            else {
                newList.add(list.get(j));
                
                // count inversion when list(j) in [mid, hi) precedes elements in [i, mid)
                count += mid - i;
                if (j < hi - 1) j++;
                else {
                    newList.addAll(list.subList(i, mid));
                    break;
                }
            }
        }
        
        // copy newList back to list
        for (i = lo; i < hi; i++) {
            list.set(i, newList.get(i - lo));
        }
        assert(isSorted(list, lo, hi));
    }
    
    // check if list is sorted - useful for debugging
    private static boolean isSorted(List<Integer> list) {
        return isSorted(list, 0, list.size());
    }
    
    // check if list[lo, hi) is sorted
    private static boolean isSorted(List<Integer> list, int lo, int hi) {
        for (int i = lo + 1; i < hi; i++) {
            if (list.get(i - 1) > list.get(i)) return false;
        }
        return true;
    }
    
    /**
     * Unit tests the MergeSortCount data type.
     */
    public static void main(String[] args) {
        System.out.println(MergeSortCount.sort(args[0]));
    }
}