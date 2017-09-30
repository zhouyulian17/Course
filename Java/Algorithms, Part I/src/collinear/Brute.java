package collinear;

import java.util.Arrays;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;

/*************************************************************************
 * Name: Yulian Zhou
 * Email: zhouyulian17@gmail.com
 *
 * Compilation:  javac Brute.java
 * Execution:    java Brute input.txt
 * Dependencies: Point.java, In.java
 *
 * Description:  Find all lines that connect 4 or more of the points.
 *               duplicate lines are allowed.
 * http://coursera.cs.princeton.edu/algs4/assignments/collinear.html
 *
 *************************************************************************/

public class Brute {
    public static void main(String[] args) {
        
        // set x and y scale for drawing lines
        StdDraw.setXscale(0, 32768);
        StdDraw.setYscale(0, 32768);
        
        // read X, Y coordinates from file and store in Point[]
        In in = new In(args[0]);
        int n = in.readInt();
        Point[] points = new Point[n];
        for (int i = 0; i < n; i++) {
            points[i] = new Point(in.readInt(), in.readInt());
            points[i].draw();
        }
        
        // sort the array by Y, and breaking ties by X coordinates
        Arrays.sort(points);
        
        // iterate though all points and output lines that contains 4 points
        for (int i1 = 0; i1 < n -3; i1++) {
            for (int i2 = i1 + 1; i2 < n - 2; i2++) {
                for (int i3 = i2 + 1; i3 < n - 1; i3++) {
                    if (points[i1].slopeTo(points[i2]) != points[i1].slopeTo(points[i3])) continue;
                    for (int i4 = i3+ 1; i4 < n; i4++) {
                        if (points[i1].slopeTo(points[i2]) != points[i1].slopeTo(points[i4])) continue;
                        points[i1].drawTo(points[i4]);
                        String s1 = points[i1].toString();
                        String s2 = points[i2].toString();
                        String s3 = points[i3].toString();
                        String s4 = points[i4].toString();
                        System.out.printf("%s -> %s -> %s -> %s\n", s1, s2, s3, s4);
                    }
                }
            }
        }
        StdDraw.show(0);    // start to draw points
    }
}