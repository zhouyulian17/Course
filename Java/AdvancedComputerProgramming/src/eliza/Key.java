package eliza;

import java.util.ArrayList;
import java.util.List;

/*************************************************************************
 * Name:  Yulian Zhou, Zheng Kuang
 * Email: zhouyulian17@gmail.com, kuangzheng04@gmail.com
 *
 * Compilation:  javac Key.java
 * Execution:    none
 * Dependencies: none
 *
 * Description:  A data type that stores a key word and its decomposition
 *               patterns.
 *
 *************************************************************************/

public class Key implements Comparable<Key>{
    
    private String key;             // key word
    private int rank;               // rank of key
    private List<Decomp> decomp;    // list of decomp
    
    /**
     * Initializes a new key with key word w and rank n.
     */
    public Key(String w, int n) {
        key = w;
        rank = n;
        decomp = new ArrayList<Decomp>();
    }
    
    /**
     * Returns the key word of this key.
     */
    public String getKey() { return key; }
    
    /**
     * Returns all Decomps of this key.
     */
    public List<Decomp> getDecomp() { return decomp; }
    
    /**
     * Adds a Decomp d to decomp list.
     */
    public void addDecomp(Decomp d) { decomp.add(d); }
    
    @Override
    public int compareTo(Key other) { return other.rank - this.rank; }
    
    /**
     * Returns a string representation of this key.
     */
    public String toString() {
        String s = "key: " + key + " ";
        for (Decomp d : decomp) {
            s += d.toString();
        }
        return s;
    }
}