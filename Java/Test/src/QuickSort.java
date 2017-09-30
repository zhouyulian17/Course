import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;


public final class QuickSort {
    private int count;
    public QuickSort() {
        count = 0;
    }
    public void quickSort(int[] nums) {
        int n = nums.length;
        partition(nums, 0, n);
    }
    public void partition(int[] nums, int lo, int hi) {
        if (lo >= hi - 1) return;
        
        int mid = (hi + lo - 1) / 2;
        
        if (nums[hi - 1] > nums[lo]) {
            if (nums[mid] > nums[lo]) {
                if (nums[hi - 1] > nums[mid]) {
                    exch(nums, lo, mid);
                }
                else {
                    exch(nums, lo, hi - 1);
                }
            }
        }
        else {
            if (nums[lo] > nums[mid]) {
                if (nums[hi - 1] > nums[mid]) {
                    exch(nums, lo, hi - 1);
                }
                else {
                    exch(nums, lo, mid);
                }
            }
        }
        
        count += hi - lo - 1;
        
        //exch(nums, lo, hi - 1);
        int k = nums[lo];
        int i = lo + 1;
        int j = lo + 1;
        while (true) {
            while (j < hi && nums[j] > k) {
                j++;
            }
            if (j == hi) break;
            exch(nums, i, j);
            i++;
            j++;
        }
        exch(nums, lo, i - 1);
        partition(nums, lo, i - 1);
        partition(nums, i, hi);
    }
    public void exch(int[] nums, int i, int j) {
        int k = nums[i];
        nums[i] = nums[j];
        nums[j] = k;
    }
    
    public static void main(String[] args) {
        
        Scanner sc = null;
        try {
            sc = new Scanner(new File("/Users/yulianzhou/Desktop/courses backup/java/Algs 2.1/QuickSort.txt"));
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        int[] nums = new int[10000];
        int i = 0;
        while (sc.hasNextInt()) {
            nums[i] = sc.nextInt();
            i++;
        }
        //System.out.println(nums[0]);
        QuickSort qs = new QuickSort();
        qs.quickSort(nums);
        int k = 0;
        for (i = 1; i< 10000; i++) {
            if (nums[i] > nums[i - 1]) k++;
        }
        System.out.println(qs.count);
        System.out.println(k);
        
    }
    
}
