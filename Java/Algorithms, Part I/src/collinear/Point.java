package collinear;

import java.util.Comparator;
import edu.princeton.cs.algs4.StdDraw;

/*************************************************************************
 * Name: Yulian Zhou
 * Email: zhouyulian17@gmail.com
 *
 * Compilation:  javac Point.java
 * Execution:    java Point
 * Dependencies: StdDraw.java
 *
 * Description:  An immutable data type for points in the plane.
 * http://coursera.cs.princeton.edu/algs4/assignments/collinear.html
 *
 *************************************************************************/

public class Point implements Comparable<Point> {
    
    public final Comparator<Point> SLOPE_ORDER;    // compare points by slope
    private final int x;                           // x coordinate
    private final int y;                           // y coordinate
    
    // helper class implements a comparator
    private class SlopeOrder implements Comparator<Point> {
        public int compare(Point p1, Point p2) {
            return Double.compare(slopeTo(p1), slopeTo(p2));
        }
    }
    
    /**
     * Creates the point (x, y).
     */
    public Point(int x, int y) {
        /* DO NOT MODIFY */
        this.x = x;
        this.y = y;
        SLOPE_ORDER = new SlopeOrder();
    }
    
    /**
     * Plots this point to standard drawing.
     */
    public void draw() {
        /* DO NOT MODIFY */
        StdDraw.point(x, y);
    }
    
    /**
     * Draws line between this point and that point to standard drawing.
     */
    public void drawTo(Point that) {
        /* DO NOT MODIFY */
        StdDraw.line(this.x, this.y, that.x, that.y);
    }
    
    /**
     * Returns slope between this point and that point.
     */
    public double slopeTo(Point that) {
        if (that.y == this.y && that.x == this.x) return Double.NEGATIVE_INFINITY;
        if (that.y == this.y) return 0.0;
        if (that.x == this.x) return Double.POSITIVE_INFINITY;
        else return (double) (that.y - this.y)/ (that.x - this.x);
    }
    
    /**
     * Is this point lexicographically smaller than that one?
     * Compares y-coordinates and breaks ties by x-coordinates.
     */
    public int compareTo(Point that) {
        if (this.y < that.y) return -1;
        if (this.y > that.y) return +1;
        if (this.x < that.x) return -1;
        if (this.x > that.x) return +1;
        else return 0;
    }
    
    /**
     * Return string representation of this point.
     */
    public String toString() {
        /* DO NOT MODIFY */
        return "(" + x + ", " + y + ")";
    }
    
    // unit test
    public static void main(String[] args) {
        /* YOUR CODE HERE */
        // skip
    }
}