import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.util.Scanner;

public class MinCut {
	private ArrayList<Edge> edges = new ArrayList<Edge>();
	private Vertex[] vertices;
	private int n;
	
	private class Vertex {
		private HashMap<Integer, Integer> adjacent;
		public Vertex() {
			adjacent = new HashMap<Integer, Integer>();
		}
		public void addConnect(int n) {
			if (adjacent.containsKey(n)) adjacent.put(n, adjacent.get(n) + 1);
			else adjacent.put(n, 1);
		}
		public void removeConnect(int n) {
			adjacent.remove(n);
		}
	}
	public class Edge {
		public int v1;
		public int v2;
		public Edge(int m, int n) {
			v1 = m;
			v2 = n;
		}
	}
	public MinCut(Scanner scanner, int v) {
		n = v;
		vertices = new Vertex[n + 1];
		for (int i = n; i > 0; i--) {
			vertices[i] = new Vertex();
		}
		Scanner sc = null;
		while (scanner.hasNextLine()) {
			sc = new Scanner(scanner.nextLine());
			int v1 = sc.nextInt();
			while(sc.hasNextInt()) {
				int v2 = sc.nextInt();
				vertices[v1].addConnect(v2);
				addEdge(v1, v2);
			}
		}
	}
	public void addEdge(int v1, int v2) {
		if (v1 > v2) return;
		edges.add(new Edge(v1, v2));
	}
	public Edge setEdge(int v1, int v2) {
		if (v1 > v2) {
			int temp = v1;
			v1 = v2;
			v2 = temp;
		}
		vertices[v1].addConnect(v2);
		vertices[v2].addConnect(v1);
		return new Edge(v1, v2);
	}
	public void removeEdge(int k) {
		int esize = edges.size();
		edges.set(k, edges.get(esize - 1));
		edges.remove(esize - 1);
	}
	public void mergeVertex(int k) {
		Edge e = edges.get(k);
		int v1 = e.v1;
		int v2 = e.v2;
		int i = edges.size();
		/*
		for (Edge edge : edges) {
			System.out.printf("%d %d, ", edge.v1, edge.v2);
		}
		System.out.println();
		System.out.printf("%d, %d, %d, %d", k, v1, v2, i);
		System.out.println();
		*/
		while (i-- > 0) {
			Edge edge = edges.get(i);
			if (edge.v1 == v2) {
				vertices[edge.v2].removeConnect(v2);
				edges.set(i, setEdge(v1, edge.v2));
			}
			else if (edge.v2 == v2) {
				vertices[edge.v1].removeConnect(v2);
				if (edge.v1 == v1) {
					removeEdge(i);
				}
				else {
					edges.set(i, setEdge(v1, edge.v1));
				}
			}
		}
	}
	public int findMinCut() {
		Random r = new Random();
		int size = n;
		while (size-- > 2) {
			int k = r.nextInt(edges.size());
			mergeVertex(k);
		}
		int k = 0;
		for (int key : vertices[1].adjacent.keySet()) {
			k = vertices[1].adjacent.get(key);
		}
		return k;
	}
	public static void main(String[] args) {
		int k = 100;
		int min = Integer.MAX_VALUE;
		Scanner sc = null;
		while (k-- > 0) {
			int n = 200;
			try {
				sc = new Scanner(new File("/Users/yulianzhou/Desktop/Course/java/Algs 2.1/kargerMinCut.txt"));
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
			MinCut mc = new MinCut(sc, n);
			int temp = mc.findMinCut();
			if (min > temp) min = temp;
		}
		System.out.println(min);
	}
}