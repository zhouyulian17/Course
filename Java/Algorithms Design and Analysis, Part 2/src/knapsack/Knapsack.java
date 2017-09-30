package knapsack;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

/*************************************************************************
 * Name: Yulian Zhou
 * Email: zhouyulian17@gmail.com
 *
 * Compilation:  javac Knapsack.java
 * Execution:    java Knapsack Knapsack.txt big
 * Dependencies: Item.java
 *
 * Description:  A data type that computes the best value of items that
 *               can be fitted into a knapsack with a given weight.
 *
 *************************************************************************/

public class Knapsack {
    
    private final int W;     // capacity of knapsack
    private final int N;     // number of item
    private Item[] items;    // items[i] = the ith item
    
    /**
     * Initializes a Knapsack data structure from file.
     */
    public Knapsack(String file) {
        Scanner scanner = null;
        try {
            scanner = new Scanner(new File(file));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        Scanner sc;
        sc = new Scanner(scanner.nextLine());
        W = sc.nextInt();
        N = sc.nextInt();
        items = new Item[N];
        for (int i = 0; i < N; i++) {
            sc = new Scanner(scanner.nextLine());
            int v = sc.nextInt();
            int w = sc.nextInt();
            items[i] = new Item(v, w);
        }
    }
    
    /**
     * Computes and returns the maximum total value of items that can be fitted
     * into the knapsack of weight W.
     */
    public int maxVal(boolean big) {
        if (big) return maxVal2();
        return maxVal1();
    }
    
    // compute the best value of items [1..N] in knapsack of weight [0..W]
    // implementing an array that stores solution of all subproblems.
    private int maxVal1() {
        int[][] ks = new int[N + 1][W + 1];
        for (int i = 0; i < N; i++) {
            int w = items[i].weight();
            if (w > W) continue;
            int j = 0;
            while (++j < w) {
                ks[i + 1][j] = ks[i][j];
            }
            while (j < W + 1) {
                int x = ks[i][j];
                int y = ks[i][j - w] + items[i].value();
                ks[i + 1][j++] = (x > y) ? x : y;
            }
        }
        return ks[N][W];
    }
    
    // compute the best value of items [1..N] in knapsack of weight [0..W]
    // implementing an array that stores solution of current subproblems.
    private int maxVal2() {
        int[] pre = new int[W + 1];
        for (int i = 0; i < N; i++) {
            pre = maxVal2(pre, i);
        }
        return pre[W];
    }
    
    // compute the best value of items [1..i] in knapsack of weight [0..W]
    // given the best value of items [1..i-1] in knapsack of weight [0..W]
    private int[] maxVal2(int[] pre, int i) {
        int[] post = new int[W + 1];
        int w = items[i].weight();
        if (w > W) return pre;
        int j = 0;
        while (++j < w) {
            post[j] = pre[j];
        }
        while (j < W + 1) {
            int x = pre[j];
            int y = pre[j - w] + items[i].value();
            post[j++] = (x > y) ? x : y;
        }
        return post;
    }
    
    /**
     * Unit tests the Knapsack data type.
     */
    public static void main(String[] args) {
        String file = args[0];
        boolean big = Integer.parseInt(args[1]) == 1;
        Knapsack knapsack = new Knapsack(file);
        System.out.println(knapsack.maxVal(big));
    }
}