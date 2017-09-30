package knapsack;

/*************************************************************************
 * Name: Yulian Zhou
 * Email: zhouyulian17@gmail.com
 *
 * Compilation:  javac Item.java
 * Execution:    None
 * Dependencies: None
 *
 * Description:  An item with a weight and a value.
 *
 *************************************************************************/

public class Item {
    
    private int value;    // the value
    private int weight;   // the weight
    
    /**
     * Initializes an item of value v and weight w.
     */
    public Item(int v, int w) {
        value = v;
        weight = w;
    }
    
    /**
     * Returns the value of this item.
     */
    public int value() { return value; }
    
    /**
     * Returns the weight of this item.
     */
    public int weight() { return weight; }
}