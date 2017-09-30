package kdTrees;

import java.util.TreeSet;
import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.Stack;

/*************************************************************************
 * Name: Yulian Zhou
 * Email: zhouyulian17@gmail.com
 *
 * Compilation:  javac PointSET.java
 * Execution:    java PointSET
 * Dependencies: Point2D.java, RectHV.java, Stack.java
 *
 * Description:  A mutable data type that represents a set of points in
 *               the unit square and supports efficient range search and
 *               nearest neighbor search.
 * http://coursera.cs.princeton.edu/algs4/assignments/kdtree.html
 *
 *************************************************************************/

public class PointSET {
    
    // a red-black tree that stores points in the plane in order
    private TreeSet<Point2D> points;
    
    /**
     * Constructs an empty set of points.
     */
    public PointSET() { points = new TreeSet<Point2D>(); }
    
    /**
     * Is the set empty?
     */
    public boolean isEmpty() { return points.isEmpty(); } //
    
    /**
     * Returns the number of points in the set.
     */
    public int size() { return points.size(); } //
    
    /**
     * Adds the point to the set (if it is not already in the set).
     */
    public void insert(Point2D p) { points.add(p); }
    
    /**
     * Does the set contain point p?
     */
    public boolean contains(Point2D p) { return points.contains(p); }
    
    /**
     * Draws all points to standard draw
     */
    public void draw() {
        for (Point2D point : points) {
            point.draw();
        }
    }
    
    /**
     * Returns all points that are inside the rectangle.
     */
    public Iterable<Point2D> range(RectHV rect) {
        //
        Stack<Point2D> stack = new Stack<Point2D>();
        for (Point2D point : points) {
            if (point.y() < rect.ymin()) continue;
            if (point.y() > rect.ymax()) break;
            else {
                if (point.x() >= rect.xmin() && point.x() <= rect.xmax()) {
                    stack.push(point);
                }
            }
        }
        return stack;
    }
    
    /**
     * Returns a nearest neighbor in the set to point p; null if the set is empty.
     */
    public Point2D nearest(Point2D p) {
        //
        Point2D q = null;
        if (isEmpty()) return q;
        double min = Double.POSITIVE_INFINITY;
        for (Point2D point : points) {
            double d = point.distanceTo(p);
            if (d < min) {
                min = d;
                q = point;
            }
        }
        return q;
    }
    
    public static void main(String[] args) {
        
        // unit testing of the methods (optional)
        Point2D p1 = new Point2D(1, 1);
        Point2D p2 = new Point2D(5, 5);
        Point2D p3 = new Point2D(10, 0);
        Point2D p4 = new Point2D(10, 3);
        Point2D p5 = new Point2D(0.58588, 0.2925);
        PointSET p = new PointSET();
        p.insert(p1);
        p.insert(p2);
        p.insert(p3);
        p.insert(p4);
        p.insert(p5);
        System.out.println(p.contains(p1));
        System.out.println(p.nearest(p5));
        RectHV rect = new RectHV(0.19699, 0.2925, 0.74739, 0.88323);
        for (Point2D point: p.range(rect)) {
            System.out.println(point);
        }
    }
}