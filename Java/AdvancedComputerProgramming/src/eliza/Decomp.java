package eliza;

import java.util.ArrayList;
import java.util.List;

/*************************************************************************
 * Name:  Yulian Zhou, Zheng Kuang
 * Email: zhouyulian17@gmail.com, kuangzheng04@gmail.com
 *
 * Compilation:  javac Decomp.java
 * Execution:    none
 * Dependencies: none
 *
 * Description:  A data type that stores a decomposition pattern and its
 *               reassembly patterns.
 *
 *************************************************************************/

public class Decomp {
    
    private String pattern;        // pattern of decomp
    private int current = 0;       // index of current rule
    private List<String> reasmb;   // list of assembly rule
    
    /**
     * Initializes a new decomp with string pattern p.
     */
    public Decomp(String p) {
        pattern = p;
        reasmb = new ArrayList<String>();
    }
    
    /**
     * Returns pattern String.
     */
    public String getPattern() { return pattern; }
    
    /**
     * Returns current assembly rule.
     */
    public String currentRule() { return reasmb.get(current); }
    
    /**
     * Updates current rule.
     */
    public void nextRule() { if (++current == reasmb.size()) current = 0; }
    
    /**
     * Adds assembly rule to reasmb list.
     */
    public void addReasmb(String r) { reasmb.add(r); }
    
    /**
     * Returns a string representation of this decomp.
     */
    public String toString() {
        String s = "decompose pattern: " + pattern + ". ";
        for (String r : reasmb) {
            s += " reasmbl: " + r ;
        }
        return s;
    }
}