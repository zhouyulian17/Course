package eightPuzzle;

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.StdOut;

/*************************************************************************
 * Name: Yulian Zhou
 * Email: zhouyulian17@gmail.com
 *
 * Compilation:  javac Solver.java
 * Execution:    java Solver puzzle.txt
 * Dependencies: Board.java, In.java, MinPQ.java, Stack.java, StdOut.java
 *
 * Description:  Using two synchronized A* searches (priority queues) to
 *               solve a board and its twin for determining whether a board
 *               is solvable.
 * http://coursera.cs.princeton.edu/algs4/assignments/8puzzle.html
 *
 *************************************************************************/

public class Solver {
    
    private Stack<Board> stack;  // a sequence of boards from initial board to goal board
    private int moves;           // number of moves required to solve the initial board
    
    /**
     * helper linked list class that implements method compareTo.
     * The priority of Node is the sum of the Manhattan distances
     * plus the number of moves made so far to get to the search node.
     */
    private class Node implements Comparable<Node> {
        
        private Node previous;   // the previous node
        private Board board;     // the board
        private int priority;    // the priority of this node
        private int move;        // number of moves so far
        
        /**
         * Initializes a node containing a board with move.
         */
        public Node(Board board, int move) {
            this.board = board;
            this.move = move;
            this.priority = board.manhattan() + move;
        }
        
        /**
         * Compares two nodes by their priority.
         */
        @Override
        public int compareTo(Node that) {
            return this.priority - that.priority;
        }
    }
    
    /**
     * Solves the initial board. If solvable, calculates the number of moves and
     * stores the solution in a stack.
     */
    public Solver(Board initial) {
        
        // find a solution to the initial board (using the A* algorithm)
        stack = new Stack<Board>();
        
        // if the initial board is the goal board
        if (initial.isGoal()) {
            stack.push(initial);
            return;
        }
        
        // if the twin board is the goal board
        moves = -1;
        Board twin = initial.twin();
        if (twin.isGoal()) return;
        
        // insert the initial board and its neighbors onto a minimum priority queue
        MinPQ<Node> pq = new MinPQ<Node>();
        Node oldNd1 = new Node(initial, 0);
        for (Board b : oldNd1.board.neighbors()) {
            Node newNd1 = new Node(b, 1);
            pq.insert(newNd1);
            newNd1.previous = oldNd1;
        }
        
        // insert the twin board and its neighbors onto a different minimum priority queue
        MinPQ<Node> pqtwin = new MinPQ<Node>();
        Node oldNd2 = new Node(twin, 0);
        for (Board b : oldNd2.board.neighbors()) {
            Node newNd2 = new Node(b, 1);
            pqtwin.insert(newNd2);
            newNd2.previous = oldNd2;
        }
        
        Node searchNd1, searchNd2;
        int move;
        
        // start search from the board with lowest priority until initial board or its twin board is solved
        // avoid enqueuing a neighbor if its board is the same as the board of the previous search node
        while (true) {
            searchNd1 = pq.delMin();
            if (searchNd1.board.isGoal()) break;
            move = searchNd1.move + 1;
            for (Board b : searchNd1.board.neighbors()) {
                if (!b.equals(searchNd1.previous.board)) {
                    Node nd = new Node(b, move);
                    nd.previous = searchNd1;
                    pq.insert(nd);
                }
            }
            searchNd2 = pqtwin.delMin();
            if (searchNd2.board.isGoal()) break;
            move = searchNd2.move + 1;
            for (Board b : searchNd2.board.neighbors()) {
                if (!b.equals(searchNd2.previous.board)) {
                    Node nd = new Node(b, move);
                    nd.previous = searchNd2;
                    pqtwin.insert(nd);
                }
            }
        }
        
        // if the initial board is unsolvable
        if (!searchNd1.board.isGoal()) {
            return;
        }
        
        // otherwise, reconstruct the solution onto stack
        moves = searchNd1.move;
        while (searchNd1 != null) {
            stack.push(searchNd1.board);
            searchNd1 = searchNd1.previous;
        }
    }
    
    /**
     * Is the initial board solvable?
     */
    public boolean isSolvable() { return moves != -1; }
    
    /**
     * Returns min number of moves to solve initial board; -1 if unsolvable.
     */
    public int moves() { return moves; }
    
    /**
     * Returns sequence of boards in a shortest solution; null if unsolvable.
     */
    public Iterable<Board> solution() {
        if (moves == -1) return null;
        else return stack;
    }
    
    public static void main(String[] args) {
        
        // solve a slider puzzle (given below)
        // create initial board from file
        In in = new In(args[0]);
        int N = in.readInt();
        int[][] blocks = new int[N][N];
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                blocks[i][j] = in.readInt();
            }
        }
        Board initial = new Board(blocks);
        
        // solve the puzzle
        Solver solver = new Solver(initial);
        
        // print solution to standard output
        if (!solver.isSolvable())
            StdOut.println("No solution possible");
        else {
            StdOut.println("Minimum number of moves = " + solver.moves());
            for (Board board : solver.solution())
                StdOut.println(board);
        }
    }
}