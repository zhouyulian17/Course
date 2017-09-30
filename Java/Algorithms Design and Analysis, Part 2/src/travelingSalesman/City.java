package travelingSalesman;

import java.util.Scanner;

/*************************************************************************
 * Name: Yulian Zhou
 * Email: zhouyulian17@gmail.com
 *
 * Compilation:  javac City.java
 * Execution:    None
 * Dependencies: None
 *
 * Description:  A City with x- and y- coordinate.
 *
 *************************************************************************/

public class City {
    
    private final double x;    // the x-coordinate
    private final double y;    // the y-coordinate
    
    /**
     * Initiates a new City with from scanner.
     */
    public City(Scanner sc) {
        x = sc.nextDouble();
        y = sc.nextDouble();
    }
    
    /**
     * Returns the x-coordinate of this city.
     */
    public double x() { return x; }
    
    /**
     * Returns the y-coordinate of this city.
     */
    public double y() { return y; }
    
    /**
     * Returns a string representation of this city.
     */
    public String toString() { return String.format("(%.5f, %.5f)", x, y); }
}