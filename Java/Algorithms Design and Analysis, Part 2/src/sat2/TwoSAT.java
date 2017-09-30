package sat2;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

/*************************************************************************
 * Name: Yulian Zhou
 * Email: zhouyulian17@gmail.com
 *
 * Compilation:  javac TwoSAT.java
 * Execution:    java TwoSAT 2sat.txt
 * Dependencies: Clause.java, Digraph.java, SCC.java
 *
 * Description:  A data type that determines the solvability of the
 *               2-Satisfiability instances based on the notion of strongly
 *               connected components.
 *
 *************************************************************************/

public class TwoSAT {
    
    private final int N;         // number of clause
    private Clause[] clauses;    // clauses[i] = the ith clause
    
    /**
     * Initializes a TwoSAT data structure from file.
     */
    public TwoSAT(String file) {
        Scanner scanner = null;
        try {
            scanner = new Scanner(new File(file));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        N = scanner.nextInt();
        clauses = new Clause[N];
        for (int i = 0; i < N; i++) {
            int n1 = scanner.nextInt();
            int n2 = scanner.nextInt();
            clauses[i] = new Clause(n1, n2);
        }
    }
    
    /**
     * Computes and returns the satisfiability of clauses using strongly
     * connected components.
     */
    public boolean scc() {
        Digraph G = new Digraph(2 * N);
        for (Clause c : clauses) {
            G.addEdge(not(c.m()) - 1, is(c.n()) - 1);
            G.addEdge(not(c.n()) - 1, is(c.m()) - 1);
        }
        SCC scc = new SCC(G);
        return scc.satisfiable();
    }
    
    // integer representation of ¬x
    private int not(int x) {
        if (x > 0) return x + N;
        return  -x;
    }
    
    // integer representation of x
    private int is(int x) {
        if (x > 0) return x;
        return -x + N;
    }
    
    /**
     * Unit tests the TwoSAT data type.
     */
    public static void main(String[] args) {
        TwoSAT tst = new TwoSAT(args[0]);
        System.out.println(tst.scc());
    }
}

/*
public class TwoSAT {
    
    private final int N;
    private Clause[] clauses;
    private boolean[] variables;
    
    public class Clause {
        private final int P;
        private boolean notP = false;
        private final int Q;
        private boolean notQ = false;
        public Clause(int p, int q) {
            if (p < 0) {
                notP = true;
                p = -p;
            }
            P = p - 1;
            if (q < 0) {
                notQ = true;
                q = -q;
            }
            Q = q - 1;
        }
        public int p() { return P; }
        public int q() { return Q; }
        public boolean satisfied(boolean b1, boolean b2) {
            if (notP) b1 = !b1;
            if (notQ) b2 = !b2;
            return b1 || b2;
        }
    }
    
    public TwoSAT(String file) {
        Scanner scanner = null;
        try {
            scanner = new Scanner(new File(file));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        N = scanner.nextInt();
        clauses = new Clause[N];
        
        for (int i = 0; i < N; i++) {
            int n1 = scanner.nextInt();
            int n2 = scanner.nextInt();
            clauses[i] = new Clause(n1, n2);
        }
    }
    
    public boolean papadimitriou() {
        for (int i = 0; i < Math.log(N) / Math.log(2); i++) {
            initVariables();
            for (long j = 0; j < 2 * Math.pow(N, 2); j++) {
                if (randomWalk()) return true;
            }
        }
        return false;
    }
    
    private void initVariables() {
        variables = new boolean[N];
        for (int i = 0; i < N; i++) {
            if (Math.random() < .5) variables[i] = true;
        }
    }
    
    private boolean randomWalk() {
        for (Clause c : clauses) {
            int p = c.p();
            boolean bp = variables[p];
            int q = c.q();
            boolean bq = variables[q];
            if (c.satisfied(bp, bq)) {
                if (Math.random() < .5) variables[p] = !variables[p];
                else variables[q] = !variables[q];
                return false;
            }
        }
        return true;
    }
    
    public boolean backtracking() {
        List<Integer> list = new ArrayList<Integer>();
        for (int i = 0; i < N; i++) {
            list.add(i);
        }
        Stack<List<Integer>> stack = new Stack<List<Integer>>();
        Stack<Integer> levels = new Stack<Integer>();
        stack.push(list);
        levels.push(0);
        while (!stack.isEmpty()) {
            list = stack.pop();
            int level = levels.pop();
            if (list.size() == 0) return true;
            List<Integer> listl = new ArrayList<Integer>();
            List<Integer> listr = new ArrayList<Integer>();
            boolean sat = true;
            for (int i : list) {
                int m = is(probs[i].m()) - 1;
                int notm = not(probs[i].m()) - 1;
                int n = is(probs[i].n()) - 1;
                int notn = not(probs[i].n()) - 1;
                if ((m < level || notm < level) && (n < level || notn < level)) {
                    sat = false;
                    break;
                }
                else if (m == level || n == level) listl.add(i);
                else if (notm == level || notn == level) listr.add(i);
                else {
                    listl.add(i);
                    listr.add(i);
                }
            }
            if (!sat) continue;
            if (listl.size() < listr.size()) {
                stack.push(listr);
                stack.push(listl);
            }
            else {
                stack.push(listl);
                stack.push(listr);
            }
            levels.push(level + 1);
            levels.push(level + 1);
        }
        return false;
    }
    
    public static void main(String[] args) {
        TwoSAT sat = new TwoSAT(args[0]);
        System.out.println(sat.papadimitriou());
        System.out.println(sat.backtracking());
    }
}
*/