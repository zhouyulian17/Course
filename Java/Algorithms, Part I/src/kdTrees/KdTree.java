package kdTrees;

import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.StdDraw;

/*************************************************************************
 * Name: Yulian Zhou
 * Email: zhouyulian17@gmail.com
 *
 * Compilation:  javac KdTreeST.java
 * Execution:    java KdTreeST
 * Dependencies: PointSET.java, Point2D.java, RectHV.java, Stack.java
 *               StdDraw.java
 *
 * Description:  A mutable data type that uses a 2d-tree to implement the
 *               same API (but replace PointSET with KdTree).
 * http://coursera.cs.princeton.edu/algs4/assignments/kdtree.html
 *
 *************************************************************************/

public class KdTree {
    
    private Node root;    // the root node of the 2d-tree
    private int n;        // number of nodes on the 2d-tree
    
    // helper tree node class
    private class Node {
        
        private Node left;      // the left  child of this node
        private Node right;     // the right child of this node
        private Point2D point;  // the point of this node
        private boolean color;  // the color of this node
        
        /**
         * Initializes a node with point p and color c.
         */
        public Node(Point2D p, boolean c) {
            point = p;
            color = c;
        }
        
        /**
         * Returns the point of the node.
         */
        public Point2D getPoint() { return point; }
        
        /**
         * Is this node at odd level?
         */
        public boolean getColor() { return color; }
    }
    
    /**
     * Initializes a 2d-tree with an empty set of points.
     */
    public KdTree() {
        root = null;
        n = 0;
    }
    
    /**
     * Is the set empty?
     */
    public boolean isEmpty() { return n == 0; }
    
    /**
     * Returns the number of points in the set.
     */
    public int size() { return n; }
    
    /**
     * Adds the point to the set (if it is not already in the set).
     */
    public void insert(Point2D p) { root = insert(p, root, true); }
    
    // insert node p into the set recursively
    private Node insert(Point2D p, Node h, boolean color) {
        // return a new node when it reaches a empty leaf of the 2d-tree
        if (h == null) {
            n++;
            return new Node(p, color);
        }
        
        // if point p has same (x, y) coordinates with point h in the tree
        if (p.equals(h.getPoint())) return h;
        
        // at odd level, insert point p in the tree according to x value
        else if (h.getColor()) {
            if (Point2D.X_ORDER.compare(h.getPoint(), p) > 0) h.left = insert(p, h.left, !color);
            else h.right = insert(p, h.right, !color);
        }
        
        // at even level, insert point p in the tree according to y value
        else {
            if (Point2D.Y_ORDER.compare(h.getPoint(), p) > 0) h.left = insert(p, h.left, !color);
            else h.right = insert(p, h.right, !color);
        }
        
        return h;
    }
    
    /**
     * Does the set contain point p?
     */
    public boolean contains(Point2D p) { return contains(p, root); }
    
    // search node p in the set recursively
    private boolean contains(Point2D p, Node h) {
        // return false if no more node to search
        if (h == null) return false;
        
        // return true if point p is equal to node point
        if (p.equals(h.getPoint())) return true;
        
        // at odd level, if the point to be inserted has a smaller x-coordinate
        // than the point at the root, go left; otherwise go right
        if (h.getColor()) {
            if (Point2D.X_ORDER.compare(h.getPoint(), p) > 0) return contains(p, h.left);
            else return contains(p, h.right);
        }
        
        // at even level, if the point to be inserted has a smaller y-coordinate
        // than the point at the root, go left; otherwise go right
        else {
            if (Point2D.Y_ORDER.compare(h.getPoint(), p) > 0) return contains(p, h.left);
            else return contains(p, h.right);
        }
    }
    
    /**
     * Draws all points to standard draw.
     */
    public void draw() { draw(root, 0, 1, 0, 1); }
    
    // draw points and lines within the square[xmin, xmax][ymin, ymax] recursively
    private void draw(Node h, double xmin, double xmax, double ymin, double ymax) {
        // stop if no more node to draw
        if (h == null) return;
        
        // draw the point in black
        StdDraw.setPenColor(StdDraw.BLACK);
        StdDraw.setPenRadius(.01);
        h.getPoint().draw();
        StdDraw.setPenRadius();
        
        // draw vertical splits in red and horizontal splits in blue
        if (h.getColor()) {
            StdDraw.setPenColor(StdDraw.RED);
            StdDraw.line(h.getPoint().x(), ymin, h.getPoint().x(), ymax);
            draw(h.left, xmin, h.getPoint().x(), ymin, ymax);
            draw(h.right, h.getPoint().x(), xmax, ymin, ymax);
        }
        else {
            StdDraw.setPenColor(StdDraw.BLUE);
            StdDraw.line(xmin, h.getPoint().y(), xmax, h.getPoint().y());
            draw(h.left, xmin, xmax, ymin, h.getPoint().y());
            draw(h.right, xmin, xmax, h.getPoint().y(), ymax);
        }
    }
    
    /**
     * To find all points contained in a given query rectangle, start at the root and
     * recursively search for points in both subtrees using the following pruning rule:
     * if the query rectangle does not intersect the rectangle corresponding to a node,
     * there is no need to explore that node (or its subtrees).
     */
    public Iterable<Point2D> range(RectHV rect) {
        // all points that are inside the rectangle
        Stack<Point2D> stack = new Stack<Point2D>();
        stack = rangeSearch(rect, root, stack);
        return stack;
    }
    
    // add points onto the stack inside the rectangle recursively
    private Stack<Point2D> rangeSearch(RectHV rect, Node h, Stack<Point2D> s) {
        // stop if no more node to search
        if (h == null) return s;
        
        // determine the relative position of the point at the root with the rectangle,
        // and search the corresponding subtrees
        if (h.getColor()) {
            if (rect.xmin() > h.getPoint().x()) rangeSearch(rect, h.right, s);
            else if (h.getPoint().x() > rect.xmax()) rangeSearch(rect, h.left, s);
            else {
                if (rect.ymin() <= h.getPoint().y() && h.getPoint().y() <= rect.ymax()) {
                    s.push(h.getPoint());
                }
                rangeSearch(rect, h.left, s);
                rangeSearch(rect, h.right, s);
            }
        }
        else {
            if (rect.ymin() > h.getPoint().y()) rangeSearch(rect, h.right, s);
            else if (h.getPoint().y() > rect.ymax()) rangeSearch(rect, h.left, s);
            else {
                if (rect.xmin() <= h.getPoint().x() && h.getPoint().x() <= rect.xmax()) {
                    s.push(h.getPoint());
                }
                rangeSearch(rect, h.left, s);
                rangeSearch(rect, h.right, s);
            }
        }
        return s;
    }
    
    /**
     * To find a closest point to a given query point, start at the root and recursively
     * search in both subtrees using the following pruning rule: if the closest point discovered
     * so far is closer than the distance between the query point and the rectangle corresponding
     * to a node, there is no need to explore that node (or its subtrees).
     */
    public Point2D nearest(Point2D p) {
        // a nearest neighbor in the set to point p; null if the set is empty
        if (isEmpty()) return null;
        Point2D q = nearestSearch(p, root, root.getPoint());    // current nearest point
        return q;
    }
    
    // search nearest point of point p in the set recursively
    private Point2D nearestSearch(Point2D p, Node h, Point2D q) {
        // stop if no more node to search
        if (h == null) return q;
        
        // update the current nearest point q if the distance between
        // query point p and root point is smaller
        if (p.distanceToOrder().compare(q, h.getPoint()) > 0) q = h.point;
        
        // determine the relative position of the query point p with
        // root point, and search the corresponding subtrees
        if (h.getColor()) {
            if (Point2D.X_ORDER.compare(h.getPoint(), p) > 0) {
                q = nearestSearch(p, h.left, q);
                if (p.distanceTo(q) > h.getPoint().x() - p.x()) {
                    q = nearestSearch(p, h.right, q);
                }
            }
            else {
                q = nearestSearch(p, h.right, q);
                if (p.distanceTo(q) > p.x() - h.getPoint().x()) {
                    q = nearestSearch(p, h.left, q);
                }
            }
        }
        else {
            if (Point2D.Y_ORDER.compare(h.getPoint(), p) > 0) {
                q = nearestSearch(p, h.left, q);
                if (p.distanceTo(q) > h.getPoint().y() - p.y()) {
                    q = nearestSearch(p, h.right, q);
                }
            }
            else {
                q = nearestSearch(p, h.right, q);
                if (p.distanceTo(q) > p.y() - h.getPoint().y()) {
                    q = nearestSearch(p, h.left, q);
                }
            }
        }
        return q;
    }
    
    public static void main(String[] args) {
        
        // unit testing of the methods (optional)
        Point2D p1 = new Point2D(0.1, 0.1);
        Point2D p2 = new Point2D(0.5, 0.55);
        Point2D p3 = new Point2D(0.99, 0.15);
        Point2D p4 = new Point2D(0.4, 0.2);
        Point2D p5 = new Point2D(0.3, 0.11);
        Point2D p6 = new Point2D(2, 2);
        Point2D p7 = new Point2D(0.8, 3);
        KdTree p = new KdTree();
        p.insert(p2);
        System.out.println(p.size());
        p.insert(p1);
        p.insert(p3);
        p.insert(p4);
        p.insert(p5);
        p.insert(p7);
        p.insert(p7);
        System.out.println(p.size());
        System.out.println();
        Point2D p8 = new Point2D(1, 1);
        System.out.println(p.nearest(p6));
        System.out.println(p.contains(p8));
        RectHV rect = new RectHV(0, -1, 11, 3);
        for (Point2D point: p.range(rect)) {
            System.out.println(point);
        }
        p.draw();
    }
}