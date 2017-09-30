package queues;

import java.util.Iterator;
import java.util.NoSuchElementException;

/*************************************************************************
 * Name: Yulian Zhou
 * Email: zhouyulian17@gmail.com
 *
 * Compilation:  javac Deque.java
 * Execution:    java Deque
 * Dependencies: none
 *
 * Description:  A double-ended queue where item can be added to and
 *               removed from both ends.
 * http://coursera.cs.princeton.edu/algs4/assignments/queues.html
 *
 *************************************************************************/

public class Deque<Item> implements Iterable<Item> {
    
    private Node first, last;    // the beginning and end of deque
    private int size;            // number of items on deque
    
    // helper linked list class
    private class Node {
        private Item item;       // the item of this node
        private Node next;       // the next node
        private Node previous;   // the previous node
    }
    
    /**
     * Constructs an empty deque.
     */
    public Deque() {
        first = null;
        last = null;
        size = 0;
    }
    
    /**
     * Returns true if the deque is empty.
     */
    public boolean isEmpty() { return size == 0; }
    
    /**
     * Returns the number of items in this deque.
     */
    public int size() { return size; }
    
    /**
     * Adds the item to the front of this deque.
     */
    public void addFirst(Item item) {
        if (item == null) { throw new NullPointerException(); }
        size++;
        Node oldfirst = first;
        first = new Node();
        first.item = item;
        if (last == null) last = first;
        else {
            first.next = oldfirst;
            oldfirst.previous = first;
        }
    }
    
    /**
     * Adds the item to the end of this deque.
     */
    public void addLast(Item item) {
        if (item == null) { throw new NullPointerException(); }
        size++;
        Node oldlast = last;
        last = new Node();
        last.item = item;
        if (first == null) first = last;
        else {
            oldlast.next = last;
            last.previous = oldlast;
        }
    }
    
    /**
     * Removes and returns the item from the front of this deque.
     */
    public Item removeFirst() {
        if (isEmpty()) { throw new NoSuchElementException(); }
        size--;
        Item item = first.item;
        first = first.next;
        if (first == null) last = null;
        else first.previous = null;
        return item;
    }
    
    /**
     * Remove and returns the item from the end of this deque.
     */
    public Item removeLast() {
        if (isEmpty()) { throw new NoSuchElementException(); }
        size--;
        Item item = last.item;
        last = last.previous;
        if (last == null) first = null;
        else last.next = null;
        return item;
    }
    
    /**
     * Returns an iterator that iterates over the items in this queue in FIFO order.
     */
    public Iterator<Item> iterator() { return new ListIterator(); }
    
    // an iterator
    private class ListIterator implements Iterator<Item> {
        private Node current = first;
        
        public boolean hasNext() { return current != null; }
        public void remove() { throw new UnsupportedOperationException(); }
        
        public Item next() {
            if (hasNext()) {
                Item item = current.item;
                current = current.next;
                return item;
            }
            else { throw new NoSuchElementException(); }
        }
    }
    
    public static void main(String[] args) {
        
        // unit testing
        Deque<Integer> deque = new Deque<Integer>();
        deque.addFirst(5);
        System.out.println(deque.size());
        System.out.println(deque.removeLast());
        System.out.println(deque.size());
        deque.addFirst(2);
        deque.addFirst(3);
        deque.addFirst(4);
        deque.addLast(10);
        Iterator<Integer> it = deque.iterator();
        while (it.hasNext()) {
            int i = it.next();
            //it.remove();
            System.out.println(i);
        }
    }
}