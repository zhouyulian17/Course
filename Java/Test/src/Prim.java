
/*import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.PriorityQueue;
import java.util.Scanner;

public class Prim {
	private ArrayList<Vertex> vertices;
	private PriorityQueue<Vertex> pq;
	private int V;
	private int E;
	private class Vertex implements Comparable<Vertex> {
		private HashMap<Vertex, Integer> adj;
		private int score;
		public Vertex() {
			adj = new HashMap<Vertex, Integer>();
			score = Integer.MAX_VALUE;
		}
		public void addAdj(Vertex v, int l) {
			adj.put(v, l);
		}
		public int compareTo(Vertex o) {
			return this.score - o.score;
		}
	}
	public Prim(Scanner sc) {
		Scanner scanner = new Scanner(sc.nextLine());
		V = scanner.nextInt();
		E = scanner.nextInt();
		vertices = new ArrayList<Vertex>(V + 1);
		int n = -1;
		while (++n < V) {
			vertices.add(new Vertex());
		}
		while (sc.hasNextInt()) {
			scanner = new Scanner(sc.nextLine());
			int i = scanner.nextInt() - 1;
			int j = scanner.nextInt() - 1;
			int l = scanner.nextInt();
			vertices.get(i).addAdj(vertices.get(j), l);
			vertices.get(j).addAdj(vertices.get(i), l);
		}
	}
	public int MST() {
		pq = new PriorityQueue<Vertex>();
		pq.addAll(vertices);
		Vertex v = pq.poll();
		for (Vertex vertex1 : v.adj.keySet()) {
			pq.remove(vertex1);
			int m = v.adj.get(vertex1);
			if (m < vertex1.score) vertex1.score = m;
			pq.add(vertex1);
		}
		int s = 0;
		while (!pq.isEmpty()) {
			v = pq.poll();
			s += v.score;
			for (Vertex vertex1 : v.adj.keySet()) {
				if (pq.contains(vertex1)) {
					pq.remove(vertex1);
					int m = v.adj.get(vertex1);
					if (m < vertex1.score) vertex1.score = m;
					pq.add(vertex1);
				}
			}
		}
		return s;
	}
	public static void main(String[] args) {
		Scanner sc = null;
		try {
			sc = new Scanner(new File("/Users/yulianzhou/Desktop/Course/java/Algs 2.2/edges.txt"));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Prim prim = new Prim(sc);
		System.out.println(prim.MST());
	}
}

*/

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.PriorityQueue;
import java.util.Scanner;

public class Prim {
	private int V;
	private int E;
	private boolean[] explored;
	private ArrayList<Vertex> vertices;
	private class Vertex implements Comparable<Vertex> {
		private int v;
		private int score;
		private HashMap<Vertex, Integer> adj;
		public Vertex(int n) {
			v = n;
			score = Integer.MAX_VALUE;
			adj = new HashMap<Vertex, Integer>();
		}
		private void addNeighbors(Vertex v, int l) {
			adj.put(v, l);
		}
		private void update(int s) {
			if (s < score) {
				score = s;
			}
		}
		public int compareTo(Vertex that) {
			return Integer.compare(this.score, that.score);
		}
	}

	public Prim(Scanner sc) {
		Scanner scanner = new Scanner(sc.nextLine());
		V = scanner.nextInt();
		E = scanner.nextInt();
		explored = new boolean[V + 1];
		vertices = new ArrayList<Vertex>(V);
		for (int i = 1; i < V + 1; i++) {
			Vertex vertex = new Vertex(i);
			vertices.add(vertex);
		}
		while (sc.hasNextInt()) {
			scanner = new Scanner(sc.nextLine());
			int n1 = scanner.nextInt();
			int n2 = scanner.nextInt();
			int n3 = scanner.nextInt();
			Vertex v1 = vertices.get(n1 - 1);
			Vertex v2 = vertices.get(n2 - 1);

			v1.addNeighbors(v2, n3);
			v2.addNeighbors(v1, n3);
		}
		
	}
	public int MST() {
		PriorityQueue<Vertex> pq = new PriorityQueue<Vertex>();
		vertices.get(0).update(0);
		pq.addAll(vertices);
		int mst = 0;
		while (!pq.isEmpty()) {
			Vertex v = pq.poll();
			explored[v.v] = true;
			mst += v.score;
			//System.out.printf("%d, %d\n", v.v, v.score);
			for (Vertex v2 : v.adj.keySet()) {
				pq.remove(v2);
				if (!explored[v2.v]) {
					//System.out.printf("%d, %d\n", v2.v, v.adj.get(v2));
					v2.update(v.adj.get(v2));
					pq.add(v2);
				}
			}
		}
		return mst;
	}
	public static void main(String[] args) {
		Scanner sc = null;
		try {
			sc = new Scanner(new File("/Users/yulianzhou/Desktop/course/Java/Algorithms Design and Analysis, Part 2/src/minimumSpanningTree/edges.txt"));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Prim prim = new Prim(sc);
		System.out.println(prim.MST());
	}
}
