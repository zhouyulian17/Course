package boggle;

import java.util.HashSet;
import edu.princeton.cs.algs4.Bag;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

/*************************************************************************
 * Name: Yulian Zhou
 * Email: zhouyulian17@gmail.com
 *
 * Compilation:  javac BoggleSolver.java
 * Execution:    java BoggleSolver dictionary.txt board.txt
 * Dependencies: TrieDict.java, BoggleBoard.java, Bag.java
 *
 * Description:  An immutable data type that finds all valid words in a
 *               given Boggle board, using a given dictionary.
 * http://coursera.cs.princeton.edu/algs4/assignments/boggle.html
 *
 *************************************************************************/

public class BoggleSolver {
    
    private TrieDict dict;    // a dictionary implements trie
    
    /**
     * Initializes the data structure using the given array of strings as the dictionary.
     * Each word in the dictionary contains only the uppercase letters A through Z.
     */
    public BoggleSolver(String[] dictionary) {
        dict = new TrieDict();
        for (String s : dictionary) {
            dict.put(s);
        }
    }
    
    /**
     * Returns the set of all valid words in the given Boggle board, as an Iterable.
     */
    public Iterable<String> getAllValidWords(BoggleBoard board) {
        HashSet<String> wordList = new HashSet<String>();
        
        int m = board.rows();
        int n = board.cols();
        if (m == 0 || n == 0) return wordList;
        
        Bag<Integer>[] adj = (Bag<Integer>[]) new Bag[m * n];
        for (int k = 0; k < m * n; k++) {
            adj[k] = new Bag<Integer>();
        }
        buildAdj(adj, m, n);
        
        for (int k = 0; k < m * n; k++) {
            dict.searchBoard(board, adj, k, wordList);
        }
        return wordList;
    }
    
    // link all sites in the board with sites surrounding them
    private void buildAdj(Bag<Integer>[] adj, int m, int n) {
        for (int i = 0; i < m - 1; i++) {
            for (int j = 0; j < n - 1; j++) {
                
                // link site (i, j) with (i, j + 1)
                adj[i * n + j].add(i * n + j + 1);
                adj[i * n + j + 1].add(i * n + j);
                
                // link site (i, j) with (i + 1, j)
                adj[i * n + j].add((i + 1) * n + j);
                adj[(i + 1) * n + j].add(i * n + j);
                
                // link site (i, j) with (i + 1, j + 1)
                adj[i * n + j].add((i + 1) * n + j + 1);
                adj[(i + 1) * n + j + 1].add(i * n + j);
                
                // link site (i, j + 1) with (i + 1, j)
                adj[i * n + j + 1].add((i + 1) * n + j);
                adj[(i + 1) * n + j].add(i * n + j + 1);
            }
            
            // link site (i, j) with (i + 1, j) when j = n - 1
            adj[i * n + n - 1].add((i + 1) * n + n - 1);
            adj[(i + 1) * n + n - 1].add(i * n + n - 1);
        }
        
        // link site (i, j) with (i, j + 1) when i = m - 1
        for (int j = 0; j < n - 1; j++) {
            adj[(m - 1) * n + j].add((m - 1) * n + j + 1);
            adj[(m - 1) * n + j + 1].add((m - 1) * n + j);
        }
    }
    
    /**
     * Returns the score of the given word if it is in the dictionary, zero otherwise.
     * The word contains only the uppercase letters A through Z.
     */
    public int scoreOf(String word) {
        if (!dict.contains(word)) return 0;
        int l = word.length();
        if (l < 3) return 0;
        if (l < 5) return 1;
        if (l == 5) return 2;
        if (l == 6) return 3;
        if (l == 7) return 5;
        return 11;
    }
    
    /**
     * Unit tests the BoggleSolver data type.
     */
    public static void main(String[] args) {
        In in = new In(args[0]);
        String[] dictionary = in.readAllStrings();
        BoggleSolver solver = new BoggleSolver(dictionary);
        BoggleBoard board = new BoggleBoard(args[1]);
        solver.getAllValidWords(board);
        
        System.out.println(board);
        int score = 0;
        int len = 0;
        for (String word : solver.getAllValidWords(board))
        {
            StdOut.println(word);
            score += solver.scoreOf(word);
            len++;
        }
        StdOut.println("Score = " + score);
        StdOut.println(len);
    }
}