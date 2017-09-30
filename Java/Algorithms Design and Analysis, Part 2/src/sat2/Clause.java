package sat2;

/*************************************************************************
 * Name: Yulian Zhou
 * Email: zhouyulian17@gmail.com
 *
 * Compilation:  javac Clause.java
 * Execution:    None
 * Dependencies: None
 *
 * Description:  A Clause with two variables.
 *
 *************************************************************************/

public class Clause {
    
    private int M;    // variable 1 of this clause
    private int N;    // variable 2 of this clause
    
    /**
     * Initiates a new Clause with variable M and N.
     */
    public Clause(int m, int n) {
        M = m;
        N = n;
    }
    
    /**
     * Returns variable 1 of this clause.
     */
    public int m() { return M; }
    
    /**
     * Returns variable 2 of this clause.
     */
    public int n() { return N; }
}