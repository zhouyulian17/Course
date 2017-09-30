package queues;

import java.util.Iterator;
import java.util.NoSuchElementException;
import edu.princeton.cs.algs4.StdRandom;

/*************************************************************************
 * Name: Yulian Zhou
 * Email: zhouyulian17@gmail.com
 *
 * Compilation:  javac RandomizedQueue.java
 * Execution:    java RandomizedQueue
 * Dependencies: StdRandom.java
 *
 * Description:  A randomized queue where item is sampled/removed/iterated
 *               by random order.
 * http://coursera.cs.princeton.edu/algs4/assignments/queues.html
 *
 *************************************************************************/

public class RandomizedQueue<Item> implements Iterable<Item> {
    
    private int N = 0;       // number of items on queue
    private Item[] rdque;    // rdque[i] = the ith item on the randomized queue
    
    /**
     * Construct an empty randomized queue.
     */
    public RandomizedQueue() { rdque = (Item[]) new Object[1]; }
    
    /**
     * Returns true if queue is empty.
     */
    public boolean isEmpty() { return N == 0; }
    
    /**
     * Return the number of items on the queue.
     */
    public int size() { return N; }
    
    /**
     * Adds item to the queue, if full, increases size by 2 fold.
     */
    public void enqueue(Item item) {
        if (item == null) { throw new NullPointerException(); }
        if (N == rdque.length) resize(2 * rdque.length);
        rdque [N++] = item;
    }
    
    /**
     * Removes item from the queue, if 1/4 full, decreases size to 1/2.
     */
    public Item dequeue() {
        if (isEmpty()) { throw new NoSuchElementException(); }
        int r = StdRandom.uniform(N);
        Item item = rdque[r];
        rdque[r] = rdque[--N];
        rdque[N] = null;
        if (N > 0 && N == rdque.length/4) resize(rdque.length/2);
        return item;
    }
    
    // resize the queue
    private void resize(int capacity) {
        Item[] copy = (Item[]) new Object[capacity];
        for (int i = 0; i < N; i++) {
            copy[i] = rdque[i];
        }
        rdque = copy;
    }
    
    /**
     * Returns a random item from the queue.
     */
    public Item sample() {
        if (isEmpty()) { throw new NoSuchElementException(); }
        int r = StdRandom.uniform(N);
        return rdque[r];
    }
    
    /**
     * Returns an iterator that iterates over the items in this queue in random order.
     */
    public Iterator<Item> iterator() { return new RandomIterator(); }
    
    // an iterator
    private class RandomIterator implements Iterator<Item> {
        private Item[] temp;
        private int n;
        
        public RandomIterator() {
            n = N;
            temp = (Item[]) new Object[n];
            for (int i = 0; i < n; i++) {
                temp[i] = rdque[i];
            }
            StdRandom.shuffle(temp);
        }
        
        public boolean hasNext() { return n != 0; }
        public void remove() { throw new UnsupportedOperationException(); }
        
        public Item next() {
            if (hasNext()) return temp[--n];
            else { throw new NoSuchElementException(); }
        }
    }
    
    public static void main(String[] args) {
        
        // unit testing
        RandomizedQueue<String> rdque = new RandomizedQueue<String>();
        for (int i = 0; i < 10; i++) {
            rdque.enqueue(Integer.toString(i));
        }
        Iterator<String> it1 = rdque.iterator();
        //rdque.dequeue();
        Iterator<String> it2 = rdque.iterator();
        while (it1.hasNext()) {
            System.out.printf("%s %s \n", it1.next(), it2.next());
        }
    }
}