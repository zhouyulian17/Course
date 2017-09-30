import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Scanner;


public class TSP {
	private Vertex[] vertices;
	private Edge[][] edges;
	private HashMap<Integer, Set> map;
	int V;
	private class Vertex {
		private double x;
		private double y;
		public Vertex(Scanner sc) {
			x = sc.nextDouble();
			y = sc.nextDouble();
		}
	}
	private class Edge {
		private double len;
		public Edge(Vertex v1, Vertex v2) {
			len = calcLength(v1, v2);
		}
		private double calcLength(Vertex v1, Vertex v2) {
			return Math.sqrt(Math.pow(v1.x - v2.x, 2) + Math.pow(v1.y - v2.y, 2));
		}
	}
	private class Set {
		private int id;
		private boolean[] added;
		private double[] values;
		public Set() {
			id = 0;
			added = new boolean[V];
			values = new double[V];
		}
		public void addVertex(int n, double m) {
			added[n] = true;
			values[n] = m;
			id += Math.pow(2, n);
		}
		public void addVertex(int n) {
			added[n] = true;
			id += Math.pow(2, n);
		}
	}
	/*
	private class Set {
		private int id;
		private HashMap<Integer, Double> values;
		public Set() {
			id = 0;
			values = new HashMap<Integer, Double>(25);
		}
		public void addVertex(int n, double m) {
			values.put(n, m);
			id += Math.pow(2, n);
		}
		public void addVertex(int n) {
			values.put(n, null);
			id += Math.pow(2, n);
		}
	}
	*/
	public TSP(Scanner sc) {
		Scanner scanner = new Scanner(sc.nextLine());
		V = scanner.nextInt();
		vertices = new Vertex[V];
		edges = new Edge[V][V];
		
		scanner = new Scanner(sc.nextLine());
		vertices[0] = new Vertex(scanner);
		edges[0][0] = new Edge(vertices[0], vertices[0]);
		int i = 1;
		
		map = new LinkedHashMap<Integer, Set>();
		while (sc.hasNextDouble()) {
			scanner = new Scanner(sc.nextLine());
			vertices[i] = new Vertex(scanner);
			for (int j = i - 1; j > -1; j--) {
				edges[j][i] = new Edge(vertices[j], vertices[i]);
				edges[i][j] = edges[j][i];
			}
			Set set = new Set();
			set.addVertex(i, edges[i][0].len);
			map.put(set.id, set);
			i++;
		}
		/*
		for (Integer k : map.keySet()) {
			System.out.printf("%d %d\n",k, map.get(k).id); 
			for (int j = 1; j < V; j++) {
				if (map.get(k).added[j]) System.out.printf("%d %f ", j, map.get(k).values[j]);
			}
			System.out.println();
		}
		
		for (Integer k : map.keySet()) {
			System.out.printf("%d %d\n",k, map.get(k).id); 
			for (Integer j : map.get(k).values.keySet()) {
				System.out.print(j);
			}
			System.out.println();
		}
		*/
	}
	private HashMap<Integer, Set> TSP(HashMap<Integer, Set> map, int i) {
		HashMap<Integer, Set> newMap = new HashMap<Integer, Set>();
		for (Integer k : map.keySet()) {
			for (int j = i; j < V; j++) {

				int key = (int) (k + Math.pow(2, j));
				if (newMap.containsKey(key)) continue;

				Set oldSetk = map.get(k);
				if (oldSetk.added[j]) continue;
				
				Set newSetj = new Set();
				for (int k2 = 1; k2 < V; k2++) {
					if (oldSetk.added[k2]) newSetj.addVertex(k2);
				}
				newSetj.addVertex(j);
				for (int k2 = 1; k2 < V; k2++) {
					if (!newSetj.added[k2]) continue;
					int h = (int) (newSetj.id - Math.pow(2, k2));
					Set oldSeth = map.get(h);
					Double min = Double.POSITIVE_INFINITY;
					for (int k3 = 1; k3 < V; k3++) {
						if (oldSeth.added[k3]) {
							double t = oldSeth.values[k3] + edges[k3][k2].len;
							if (min > t) min = t;
						}
					}
					newSetj.values[k2] = min;
				}
				newMap.put(newSetj.id, newSetj);
			}
		}
		/*
		for (Integer k : map.keySet()) {
			System.out.printf("%d %d\n",k, map.get(k).id); 
			for (int j = 1; j < V; j++) {
				if (map.get(k).added[j]) System.out.printf("%d %f ", j, map.get(k).values[j]);
			}
			System.out.println();
		}
		*/
		return newMap;
	}
	public double newTSP() {
		for (int i = 2; i < V; i++) {
			double time1 = System.currentTimeMillis();
			map = TSP(map, i);
			double time2 = System.currentTimeMillis();
			System.out.printf("%d %.1f\n", i, time2 - time1);
		}
		
		Double min = Double.POSITIVE_INFINITY;
		int k = 0;
		for (int i = 1; i < V; i++) {
			k += Math.pow(2, i);
		}
		Set temp = map.get(k);
		for (int i = 1; i < V; i++) {
			double t = temp.values[i] + edges[0][i].len;
			if (min > t) min = t;
		}
		/*
		HashMap<Integer, Double> temp1 = temp.values;
		for (Integer j : temp1.keySet()) {
			double t = temp1.get(j) + edges[0][j].len;
			if (min > t) min = t;
		}
		*/
		return min;
	}
	public static void main(String[] args) {

		long t1 = System.currentTimeMillis();
		Scanner sc = null;
		try {
			sc = new Scanner(new File("/Users/yulianzhou/Desktop/course/Java/Algorithms Design and Analysis, Part 2/src/travelingSalesman/tsp.txt"));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		TSP tsp = new TSP(sc);
		
		
		System.out.println(tsp.newTSP());
		long t2 = System.currentTimeMillis();
		System.out.println(t2 - t1);
	}
}
/*
import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Scanner;

public class TSP {
	private int N;
	private Vertex[] vertices;
	private double[][] edges;
	
	private class Vertex {
		private double x;
		private double y;
		public Vertex(Scanner sc) {
			x = sc.nextDouble();
			y = sc.nextDouble();
		}
		private double disTo(Vertex that) {
			return Math.pow((Math.pow(this.x - that.x, 2) + Math.pow(this.y - that.y, 2)), 0.5);
		}
	}

	private class Set {
		private int id;
		private boolean[] added;
		private double[] values;
		public Set() {
			id = 0; 
			added = new boolean[N];
			values = new double[N];
		}
		private void addVertex(int v, double l) {
			id += 1 << v;
			added[v] = true;
			values[v] = l;
		}
		private void addVertex(int v) {
			id += 1 << v;
			added[v] = true;
		}
		private Set copy() {
			Set newset = new Set();
			for (int i = 0; i < N; i++) {
				if (added[i]) {
					newset.addVertex(i);
				}
			}
			return newset;
		}
	}
	
	public TSP(Scanner sc) {
		N = Integer.parseInt(sc.nextLine());
		vertices = new Vertex[N];
		
		for (int i = 0; i < N; i++) {
			Scanner scanner = new Scanner(sc.nextLine());
			vertices[i] = new Vertex(scanner);
		} 
		edges = new double[N][N];
		for (int i = 0; i < N; i++) {
			for (int j = i; j < N; j++) {
				edges[i][j] = vertices[i].disTo(vertices[j]);
				edges[j][i] = edges[i][j];
			}
		}

	}
	
	private HashMap<Integer, Set> setIncrease(HashMap<Integer, Set> map) {
		HashMap<Integer, Set> newmap = new HashMap<Integer, Set>();
		for (int key : map.keySet()) {
			Set set = map.get(key);
			for (int i = 1; i < N; i++) {
				if (!set.added[i]) {
					int newkey = key + (1 << i);
					if (!newmap.containsKey(newkey)) {
						Set newset = set.copy();
						newset.addVertex(i);
						newmap.put(newset.id, newset);
					}
				}
			}
		}
		
		for (int key : newmap.keySet()) {
			Set set = newmap.get(key);
			for (int i = 1; i < N; i++) {
				if (set.added[i]) {
					int oldkey = key - (1 << i);
					Set oldset = map.get(oldkey);
					double min = Double.POSITIVE_INFINITY;
					for (int j = 1; j < N; j++) {
						if (oldset.added[j]) {
							double temp = oldset.values[j] + edges[i][j];
							if (temp < min) min = temp;
						}
					}
					set.values[i] = min;
				}
			}
		}
		map = null;
		return newmap;
		
	}
	public double TSP() {
		HashMap<Integer, Set> map = new HashMap<Integer, Set>();
		for (int i = 1; i < N; i++) {
			Set set = new Set();
			set.addVertex(0);
			set.addVertex(i, edges[0][i]);
			map.put(set.id, set);
		}
		
		for (int i = 2; i < N; i++) {
			double time1 = System.currentTimeMillis();
			map = setIncrease(map);
			double time2 = System.currentTimeMillis();
			System.out.printf("%d %.1f\n", i, time2 - time1);
		}
		int k = 0;
		for (int key : map.keySet()) {
			k = key;
		}
		double min = Double.POSITIVE_INFINITY;
		Set set = map.get(k);
		for (int i = 1; i < N; i++) {
			double temp = set.values[i] + edges[i][0];
			if (temp < min) min = temp;
		}
		return min;
	}
	
	public static void main(String[] args) {

		
		Scanner sc = null;
		try {
			sc = new Scanner(new File("/Users/yulianzhou/Desktop/Course/java/Algs 2.2/tsp.txt"));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		TSP tsp = new TSP(sc);
		
		
		System.out.println(tsp.TSP());
		//26442.73030895475
	}
}
*/