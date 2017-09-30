package countComparisons;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

/*************************************************************************
 * Name: Yulian Zhou
 * Email: zhouyulian17@gmail.com
 *
 * Compilation:  javac QuickSortComp.java
 * Execution:    java QuickSortComp QuickSort.txt
 * Dependencies: None
 *
 * Description:  This class provides static methods for counting comparisons
 *               in an array using quicksort.
 *
 *************************************************************************/

public final class QuickSortComp {
    
    private static int count;    // count of comparisons
    private static int choice;   // method for pivot choice
    
    /**
     * Takes the name of file that stores an integer array, rearranges the array
     * in ascending order by quicksort using the natural order, and returns the
     * counts of comparisons in the array.
     */
    public static int quickSort(String file, int n) {
        
        // read the file and store in an array
        Scanner sc = null;
        try {
            sc = new Scanner(new File(file));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        int[] array = new int[10000];
        int i = 0;
        while (sc.hasNextInt()) {
            array[i++] = sc.nextInt();
        }
        choice = n;
        if (choice != 1 && choice != 2 && choice != 3) {
            throw new IllegalArgumentException("Illegal choice");
        }
        count = 0;
        
        // quick sort the array and calculate comparisons
        sort(array, 0, array.length - 1);
        assert isSorted(array);
        return count;
    }
    
    // quicksort array[lo, hi]
    private static void sort(int[] array, int lo, int hi) {
        if (lo > hi - 1) return;
        // pivot element is compared to each of the other elements in the array[lo, hi]
        count += hi - lo;
        int k = partition(array, lo, hi);
        sort(array, lo, k - 1);
        sort(array, k + 1, hi);
    }
    
    // partition array[lo, hi] so that array[lo, i - 1] <= array[i] <= array[i + 1, hi]
    private static int partition(int[] array, int lo, int hi) {
        int p = pivot(array, lo, hi);
        int i = lo + 1;
        for (int j = lo + 1; j < hi + 1; j++) {
            if (array[j] < p) exch(array, i++, j);
        }
        exch(array, lo, --i);
        return i;
    }
    
    // rules for choosing pivot:
    // if choice = 1, use the first element of the array as the pivot element
    // if choice = 2, use the last  element of the array as the pivot element
    // if choice = 3, use the median of first, last and middle element of the
    // array as the pivot element
    // exchange the pivot element with the first element and return the pivot
    private static int pivot(int[] array, int lo, int hi) {
        if (choice == 2) exch(array, lo, hi);
        else if (choice == 3) {
            int mid = (lo + hi) / 2;
            if (array[lo] > array[mid]) {
                if      (array[mid] > array[hi]) exch(array, lo, mid);
                else if (array[hi] < array[lo])  exch(array, lo, hi);
            }
            else {
                if      (array[mid] < array[hi]) exch(array, lo, mid);
                else if (array[lo] < array[hi])  exch(array, lo, hi);
            }
        }
        return array[lo];
    }
    
    // exchange array[i] with array[j]
    private static void exch(int[] array, int i, int j) {
        int k = array[i];
        array[i] = array[j];
        array[j] = k;
    }
    
    // check if array is sorted - useful for debugging
    private static boolean isSorted(int[] array) {
        for (int i = 1; i < array.length; i++) {
            if (array[i - 1] > array[i]) return false;
        }
        return true;
    }
    
    /**
     * Unit tests the QuickSortComp data type.
     */
    public static void main(String[] args) {
        System.out.println(QuickSortComp.quickSort(args[0], Integer.parseInt(args[1])));
    }
}