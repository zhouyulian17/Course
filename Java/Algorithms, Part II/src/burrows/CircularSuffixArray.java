package burrows;

/*************************************************************************
 * Name: Yulian Zhou
 * Email: zhouyulian17@gmail.com
 *
 * Compilation:  javac CircularSuffixArray.java
 * Execution:    java CircularSuffixArray
 * Dependencies: None
 *
 * Description:  This class provides the index[] values of sorted suffixes
 *               in the original suffixes.
 * http://coursera.cs.princeton.edu/algs4/assignments/burrows.html
 *
 *************************************************************************/

public class CircularSuffixArray {
    
    // char array of string s
    private char[] array;
    
    // index[i] is index of the original suffix that appears ith in the sorted array
    private int[] index;
    
    /**
     * Initializes a circular suffix array from a string
     */
    public CircularSuffixArray(String s) {
        array = s.toCharArray();
        index = new int[length()];
        for (int i = 0; i < length(); i++) {
            index[i] = i;
        }
        sort(0, length() - 1, 0);
    }
    
    // 3-way quicksort of index array by comparing the dth character of the index in
    // the original string, and sort by the next character (d++) in case of a tie
    private void sort(int lo, int hi, int d) {
        if (hi <= lo) return;
        int lt = lo;
        int gt = hi;
        char v = getChar(lo, d);
        int i = lo + 1;
        while (i <= gt) {
            char w = getChar(i, d);
            if      (w < v) exch(lt++, i++);
            else if (w > v) exch(i, gt--);
            else            i++;
        }
        sort(lo, lt - 1, d);
        if (d < length()) sort(lt, gt, d + 1);
        sort(gt + 1, hi, d);
    }
    
    // the character of original string at (index[i] + d) % length
    private char getChar(int i, int d) {
        return array[(index(i) + d) % length()];
    }
    
    // exchange index[i] with index[j]
    private void exch(int i, int j) {
        int temp = index(i);
        index[i] = index(j);
        index[j] = temp;
    }
    
    /**
     * Returns the length of input string.
     */
    public int length() { return array.length; }
    
    /**
     * Returns the original index of ith sorted suffix.
     */
    public int index(int i) { return index[i]; }
    
    public static void main(String[] args) {
        
        // unit testing of the methods (optional)
        CircularSuffixArray c = new CircularSuffixArray("ABRACADABRA!");
        for (int i = 0; i < 12; i++) {
            System.out.println(c.index(i));
        }
    }
}