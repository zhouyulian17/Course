package eightPuzzle;

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Stack;

/*************************************************************************
 * Name: Yulian Zhou
 * Email: zhouyulian17@gmail.com
 *
 * Compilation:  javac Board.java
 * Execution:    java Board puzzle.txt
 * Dependencies: In.java, Stack.java
 *
 * Description:  N * N array containing the integers between 0 and n2 − 1,
 *               where 0 represents the blank square.
 * http://coursera.cs.princeton.edu/algs4/assignments/8puzzle.html
 *
 *************************************************************************/

public class Board {
    
    private int[] board;    // board[k] = block[k / n][k % n]
    private int n;          // dimension of block[][]
    private int x;          // x coordinate of blank square
    private int y;          // y coordinate of blank square
    
    /**
     * Constructs a board from an N-by-N array of blocks.
     * (where blocks[i][j] = block in row i, column j)
     */
    public Board(int[][] blocks) {
        n = blocks.length;
        board = new int[n * n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                board[i * n + j] = blocks[i][j];
                if (blocks[i][j] == 0) {
                    x = i;
                    y = j;
                }
            }
        }
    }
    
    /**
     * Returns board dimension N.
     */
    public int dimension() { return n; }
    
    /**
     * Returns number of blocks out of place.
     */
    public int hamming() {
        int count = 0;
        for (int i = 0; i < n * n - 1; i++) {
            if (board[i] != i + 1) count++;
        }
        return count;
    }
    
    /**
     * Returns sum of Manhattan distances between blocks and goal.
     */
    public int manhattan() {
        int count = 0;
        for (int i = 0; i < n * n - 1; i++) {
            for (int j = 0; j < n * n; j++) {
                if (i + 1 == board[j]) {
                    count += Math.abs(i / n - j / n) + Math.abs(i % n - j % n);
                }
            }
        }
        return count;
    }
    
    /**
     * Is this board the goal board?
     */
    public boolean isGoal() { return this.hamming() == 0; }
    
    /**
     * Returns a twin board by exchanging two adjacent blocks in the same row.
     * (1) Exactly one of a board and its twin are solvable.
     * (2) A board may have several possible twins and only one of them is returned.
     */
    public Board twin() {
        if (x == 0) return exch(copy(), 1, 0, 1, 1);
        else return exch(copy(), 0, 0, 0, 1);
    }
    
    /**
     * Does this board equal the other?
     */
    public boolean equals(Object other) {
        if (this == other) return true;
        if (other == null) return false;
        if (this.getClass() != other.getClass()) return false;
        Board that = (Board) other;
        if (this.n != that.n) return false;
        for (int i = 0; i < n * n; i++) {
            if (this.board[i] != that.board[i]) return false;
        }
        return true;
    }
    
    /**
     * Returns all neighboring boards of this board.
     */
    public Iterable<Board> neighbors() {
        Stack<Board> stack = new Stack<Board>();
        Board b;
        if (x > 0) {
            b = exch(copy(), x, y, x - 1, y);
            stack.push(b);
        }
        if (x < n - 1) {
            b = exch(copy(), x, y, x + 1, y);
            stack.push(b);
        }
        if (y > 0) {
            b = exch(copy(), x, y, x, y - 1);
            stack.push(b);
        }
        if (y < n - 1) {
            b = exch(copy(), x, y, x, y + 1);
            stack.push(b);
        }
        return stack;
    }
    
    // copy the block[][] of this board
    private int[][] copy() {
        int[][] temp = new int[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                temp[i][j] = board[i * n + j];
            }
        }
        return temp;
    }
    
    // a new board by exchanging block at (i1, j1) with block at (i2, j2)
    private Board exch(int[][] temp, int i1, int j1, int i2, int j2) {
        int k = temp[i1][j1];
        temp[i1][j1] = temp[i2][j2];
        temp[i2][j2] = k;
        return new Board(temp);
    }
    
    /**
     * Returns a string representation of this board (in the output format specified below).
     */
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(n + "\n");
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                sb.append(String.format(" %2d", board[i * n + j]));
            }
            sb.append("\n");
        }
        return sb.toString();
    }
    
    public static void main(String[] args) {
        
        // unit tests (not graded)
        In in = new In(args[0]);
        int n = in.readInt();
        int[][] block = new int[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                block[i][j] = in.readInt();
            }
        }
        Board b = new Board(block);
        System.out.println(b);
        System.out.println(b.hamming());
        System.out.println(b.manhattan());
        System.out.println();
        for (Board br : b.neighbors()) {
            System.out.println(br);
            System.out.println(br.hamming());
            System.out.println(br.manhattan());
            System.out.println();
        }
    }
}