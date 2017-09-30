package collinear;

import java.util.Arrays;
import java.util.HashSet;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;

/*************************************************************************
 * Name: Yulian Zhou
 * Email: zhouyulian17@gmail.com
 *
 * Compilation:  javac Fast.java
 * Execution:    java Fast input.txt
 * Dependencies: Point.java, In.java, StdDraw.java
 *
 * Description:  Find all unique lines that connect 4 or more of the points.
 * http://coursera.cs.princeton.edu/algs4/assignments/collinear.html
 *
 *************************************************************************/

public class Fast {
    public static void main(String[] args) {
        
        // set x and y scale for drawing lines
        StdDraw.setXscale(0, 32768);
        StdDraw.setYscale(0, 32768);
        
        //read X, Y coordinates from files and store in Point[]
        In in = new In(args[0]);
        int n = in.readInt();
        Point[] points = new Point[n];
        for (int i = 0; i < n; i++) {
            points[i] = new Point(in.readInt(), in.readInt());
            points[i].draw();
        }
        
        // build a new HashSet to eliminate duplicated lines
        HashSet<Point> set = new HashSet<Point>();
        
        for (int i = 0; i < n; i++) {
            // sort the array by Y, and breaking ties by X coordinates
            Arrays.sort(points);
            // sort the array by the Slope relative to ith point
            Arrays.sort(points, points[i].SLOPE_ORDER);
            // add the ith point (point 0 after sort) to the HashSet
            set.add(points[0]);
            
            // start to search 3 or more constitutive points that have same slope
            for (int j = 1; j < n - 2; j++) {
                // start to search points on the same line determined by point 0 and point j
                // count additional points as k and assume the line is unique
                double slope = points[0].slopeTo(points[j]);
                int k = 0;
                boolean unique = true;
                
                while (j + k + 1 < n) {
                    // stop searching when the point is not on the line
                    if (points[0].slopeTo(points[j + k + 1]) != slope) break;
                    // if the point is in the HashSet, the line is not unique
                    if (set.contains(points[j + k + 1])) unique = false;
                    k++;
                }
                if (set.contains(points[j])) unique = false;
                
                // draw the line if it is unique and contains 4 or more points
                if (unique && k > 1) {
                    System.out.printf("%s -> ", points[0].toString());
                    for (int m = 0; m < k; m++) {
                        System.out.printf("%s -> ", points[j + m].toString());
                    }
                    System.out.println(points[j + k].toString());
                    points[0].drawTo(points[j + k]);
                }
                
                // skip points on the same line with point0 and point j
                j += k;
            }
        }
        StdDraw.show(0);    // start to draw points
    }
}