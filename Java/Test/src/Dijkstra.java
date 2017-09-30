import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.PriorityQueue;
import java.util.Scanner;

public class Dijkstra {
	private Vertex[] vertices;
	private PriorityQueue<Edge> edges;
	private boolean[] visited;
	private int[] scores;
	private int n;
	
	private class Edge implements Comparable<Edge> {
		public int length;
		public int v1;
		public int v2;
		public Edge(int m, int n, int l) {
			length = l;
			v1 = m;
			v2 = n;
		}
		public int compareTo(Edge o) {
			return this.length + scores[this.v1] - (o.length + scores[o.v1]);
		}
	}
	
	private class Vertex {
		private HashMap<Integer, Integer> head;
		public Vertex() {
			head = new HashMap<Integer, Integer>();
		}
		public void addHead(int n, int l) {
			head.put(n, l);
		}
	}
	public Dijkstra(Scanner scanner, int v) {
		n = v;
		vertices = new Vertex[n + 1];
		for (int i = n; i > 0; i--) {
			vertices[i] = new Vertex();
		}
		Scanner sc = null;
		while (scanner.hasNextLine()) {
			sc = new Scanner(scanner.nextLine());
			int t = sc.nextInt();
			while(sc.hasNext()) {
				String[] s = sc.next().split(",");
				int h = Integer.parseInt(s[0]);
				int l = Integer.parseInt(s[1]);
				vertices[t].addHead(h, l);
			}
		}
	}
	
	public void calculateScore(int k) {
		visited = new boolean[n + 1];
		scores = new int[n + 1];
		edges = new PriorityQueue<Edge>();
		
		scores[k] = 0;
		addEdge(k);
		while (!edges.isEmpty()) {
			Edge temp = edges.poll();
			if (!visited[temp.v2]) {
				scores[temp.v2] = scores[temp.v1] + temp.length;
				addEdge(temp.v2);
			}
		}
	}

	private void addEdge(int k) {
		visited[k] = true;
		for (Integer i : vertices[k].head.keySet()) {
			edges.add(new Edge(k, i, vertices[k].head.get(i)));
		}
	}
	
	public static void main(String[] args) {
		Scanner scanner = null;
		try {
			scanner = new Scanner(new File("/Users/yulianzhou/Desktop/course/Java/Algorithms Design and Analysis, Part 1/src/shortestPath/dijkstraData.txt"));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Dijkstra d = new Dijkstra(scanner, 200);
		d.calculateScore(1);
		int[] k = {7,37,59,82,99,115,133,165,188,197};
		for (int i : k) {
			System.out.printf("%d, %d", i, d.scores[i]);
			System.out.println();
		}
		/*
		for (int i = 0 ; i < 200; i++) {
			System.out.printf("%d, %d", i + 1, d.scores[i+1]);
			System.out.println();
		}
		*/
	}
	
}