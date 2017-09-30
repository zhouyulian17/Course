package travelingSalesman;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Scanner;

/*************************************************************************
 * Name: Yulian Zhou
 * Email: zhouyulian17@gmail.com
 *
 * Compilation:  javac City.java
 * Execution:    java TSP tsp.txt
 * Dependencies: City.java, Set.java
 *
 * Description:  A data type that implements dynamic programming for the
 *               traveling salesman problem.
 *
 *************************************************************************/

public class TSP {
    
    private final int N;      // number of cities
    private City[] cities;    // cities[i] = the ith city
    
    /**
     * Initializes a TSP data structure from file.
     */
    public TSP(String file) {
        Scanner scanner = null;
        try {
            scanner = new Scanner(new File(file));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        Scanner sc;
        sc = new Scanner(scanner.nextLine());
        N = sc.nextInt();
        cities = new City[N];
        for (int i = 0; i < N; i++) {
            sc = new Scanner(scanner.nextLine());
            cities[i] = new City(sc);
        }
    }
    
    /**
     * Computes and returns minimum cost of a traveling salesman tour that
     * visits each city exactly once (except the source city).
     */
    public double tsp() {
        
        // initializes with one city per set
        HashMap<Integer, Set> pre = new HashMap<Integer, Set>();
        for (int i = 0; i < N - 1; i++) {
            int key = 1 << i;
            Set set = new Set(key, N - 1);
            set.update(i, dist(cities[N - 1], cities[i]));
            pre.put(key, set);
        }
        
        // iteratively calculate the minimal distances in sets containing n+1
        // cities from sets containing n cities
        while (pre.size() > 1) pre = next(pre);
        
        // find the minimal cost tour back to the source city
        double minD = Double.POSITIVE_INFINITY;
        Set set = pre.get((1 << N - 1) - 1);
        for (int i = 0; i < N - 1; i++) {
            double d = set.distTo(i) + dist(cities[i], cities[N - 1]);
            if (minD > d) minD = d;
        }
        return minD;
    }
    
    // the minimal distances from sets with n cities to sets with n+1 cities
    private HashMap<Integer, Set> next(HashMap<Integer, Set> pre) {
        HashMap<Integer, Set> post = new HashMap<Integer, Set>();
        for (int preKey : pre.keySet()) {
            Set preSet = pre.get(preKey);
            for (int i = 0; i < N - 1; i++) {
                if (preSet.added(i)) {
                    for (int j = 0; j < N - 1; j++) {
                        if (!preSet.added(j)) {
                            int postKey = preKey + (1 << j);
                            Set postSet;
                            if (post.containsKey(postKey)) {
                                postSet = post.get(postKey);
                            }
                            else {
                                postSet = new Set(postKey, N - 1);
                                post.put(postKey, postSet);
                            }
                            double preD = postSet.distTo(j);
                            double curD = preSet.distTo(i) + dist(cities[i], cities[j]);
                            if (preD > curD)
                                postSet.update(j, curD);
                        }
                    }
                }
            }
        }
        return post;
    }
    
    // the Euclidean distance between src city and dest city
    private double dist(City src, City dest) {
        return Math.sqrt(Math.pow((src.x() - dest.x()), 2) + Math.pow((src.y() - dest.y()), 2));
    }
    
    /**
     * Unit tests the TSP data type.
     */
    public static void main(String[] args) {
        TSP tsp = new TSP(args[0]);
        System.out.println(tsp.tsp());
    }
}