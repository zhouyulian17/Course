package queues;

import java.util.Iterator;
import edu.princeton.cs.algs4.StdIn;

/*************************************************************************
 * Name: Yulian Zhou
 * Email: zhouyulian17@gmail.com
 *
 * Compilation:  javac Subset.java
 * Execution:    java Subset k
 * Dependencies: RandomizedQueue.java, In.java
 *
 * Description:  The command-line takes a integer k, reads in a sequence
 *               of N strings from standard input, and randomly print out
 *               k of them.
 * http://coursera.cs.princeton.edu/algs4/assignments/queues.html
 *
 *************************************************************************/

public class Subset {
    public static void main(String[] args) {
        RandomizedQueue<String> rd = new RandomizedQueue<String>();
        while (!StdIn.isEmpty()) {
            rd.enqueue(StdIn.readString());
        }
        Iterator<String> it = rd.iterator();
        int k = Integer.parseInt(args[0]);
        for (int i = 0; i < k && it.hasNext(); i++) {
            System.out.println(it.next());
        }
    }
}