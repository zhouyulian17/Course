import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.PriorityQueue;
import java.util.Scanner;

public class Shortest {
	private Vertex[] vertices;
	int V;
	int E;
	
	private class Vertex {
		private HashMap<Integer, Integer> tails;
		private HashMap<Integer, Integer> heads;
		public Vertex() {
			tails = new HashMap<Integer, Integer>();
			heads = new HashMap<Integer, Integer>();
		}
		public void addTail(int t, int l) {
			tails.put(t, l);
		}
		public void addHead(int h, int l) {
			heads.put(h, l);
		}
	}
	private class Edge implements Comparable<Edge> {
		private int tail;
		private int head;
		private int len;
		public Edge(int t, int h, int l) {
			tail = t;
			head = h;
			len = l;
		}
		public int compareTo(Edge o) {
			return this.len - o.len;
		}
	}
	
	public Shortest(Scanner sc) {
		Scanner scanner = new Scanner(sc.nextLine());
		V = scanner.nextInt();
		E = scanner.nextInt();
		vertices = new Vertex[V + 1];
		for (int i = V; i > 0; i--) {
			vertices[i] = new Vertex();
		}
		while (sc.hasNextInt()) {
			scanner = new Scanner(sc.nextLine());
			int t = scanner.nextInt();
			int h = scanner.nextInt();
			int l = scanner.nextInt();
			vertices[h].addTail(t, l);
			vertices[t].addHead(h, l);
		}
	}
	private void BellmanFord(int[] temp1, int[] temp2) {
		for (int k = 1; k < V + 1; k++) {
			HashMap<Integer, Integer> map = vertices[k].tails;
			int min = temp1[k];
			for (Integer t : map.keySet()) {
				int val = temp1[t] + map.get(t);
				if (val < min) min = val;
			}
			temp2[k] = min;
		}
	}
	public int BellmanFord() {
		int s = Integer.MAX_VALUE;
		for (int i = 1; i < V + 1; i++) {
			int[] temp1 = new int[V + 1];
			for (int i2 = 1; i2 < V + 1; i2++) {
				temp1[i2] = 1000000;
			}
			temp1[i] = 0;
			int[] temp2 = new int[V + 1];
			for (int j = 1; j < V + 1; j++) {
				if (j % 2 == 1) BellmanFord(temp1, temp2);
				else BellmanFord(temp2, temp1);
			}
			int min = Integer.MAX_VALUE;
			for (int i2 = 1; i2 < V + 1; i2++) {
				if (temp1[i2] != temp2[i2]) {
					System.out.println(temp1[i2]);
					System.out.println(temp2[i2]);
					return Integer.MAX_VALUE;
				}
				if (min > temp1[i2]) min = temp1[i2];
			} 
			if (s > min) s = min;
			//System.out.printf("%d %d", i, s);
			//System.out.println();
		}
		return s;
	}
	private int FloydWarshall(int[][] temp1, int[][] temp2, int k) {
		int t = Integer.MAX_VALUE;
		for (int i = 1; i < V + 1; i++) {
			for (int j = 1; j < V + 1; j++) {
				int t1 = temp1[i][j];
				int t2 = temp1[i][k] + temp1[k][j];
				if (t2 < t1) t1 = t2;
				if (t > t1) t = t1;
				temp2[i][j] = t1;
			}
		}
		return t;
	}
	public int FloydWarshall() {
		//long time1 = System.currentTimeMillis();
		int[][] temp1 = new int[V + 1][V + 1];
		int[][] temp2 = new int[V + 1][V + 1];
		for (int i = 1; i < V + 1; i++) {
			for (int j = 1; j < V + 1; j++) {
				if (i == j) temp1[i][j] = 0;
				else {
					HashMap<Integer, Integer> map = vertices[j].tails;
					if (map.containsKey(i)) temp1[i][j] = map.get(i);
					else temp1[i][j] = 1000000;
				}
			}
		}
		//long time2 = System.currentTimeMillis();
		//System.out.println(time2 - time1);
		int min = Integer.MAX_VALUE;
		for (int k = 1; k < V + 1; k++) {
			int t;
			if (k % 2 == 1) t = FloydWarshall(temp1, temp2, k);
			else t = FloydWarshall(temp2, temp1, k);
			if (min > t) min = t;
		}
		return min;
	}
	
	private int[] BellmanFord(int k) {
		int[] temp1 = new int[V + 1];
		for (int i = 1; i < V + 1; i++) {
			temp1[i] = 1000000;
		}
		int[] temp2 = new int[V + 1];
		for (int j = 0; j < V + 2; j++) {
			if (j % 2 == 1) BellmanFord(temp1, temp2);
			else BellmanFord(temp2, temp1);
		}
		return temp1;
	}
	public int Johnson() {
		//long time1 = System.currentTimeMillis();
		System.out.println("read");
		vertices[0] = new Vertex();
		for (int i = 1; i < V + 1; i++) {
			vertices[i].addTail(0, 0);
		}
		int[] p = BellmanFord(0);
		System.out.println("BF");
		for (int i = 1; i < V + 1; i++) {
			HashMap<Integer, Integer> map = vertices[i].heads;
			for (Integer k : map.keySet()) {
				int l = map.get(k) + p[i] - p[k];
				vertices[i].addHead(k, l);
			}
		}
		System.out.println("changed");
		
		//long time2 = System.currentTimeMillis();
		//System.out.println(time2 - time1);
		int min = Integer.MAX_VALUE;
		for (int i = 1; i < V + 1; i++) {
			boolean[] visited = new boolean[V + 1];
			int[] values = new int[V + 1];
			visited[i] = true;
			values[i] = 0;
			
			PriorityQueue<Edge> edges = new PriorityQueue<Edge>();
			HashMap<Integer, Integer> map = vertices[i].heads;
			for (Integer k : map.keySet()) {
				edges.add(new Edge(i, k, map.get(k)));
			}
			while (!edges.isEmpty()) {
				Edge e = edges.poll();
				if (!visited[e.head]) {
					visited[e.head] = true;
					values[e.head] = values[e.tail] + e.len;
					map = vertices[e.head].heads;
					for (Integer k : map.keySet()) {
						edges.add(new Edge(e.head, k, map.get(k)));
					}
				}
			}
			for (int j = 1; j < V + 1; j++) {
				values[j] = values[j] + p[j] - p[i];
				if (min > values[j]) min = values[j];
			}
			//System.out.printf("%d, %d", i, min);
			//System.out.println();
		}
		//long time3 = System.currentTimeMillis();
		//System.out.println(time3 - time2);
		return min;
	}

	public static void main(String[] args) {
		Scanner sc = null;
		try {
			sc = new Scanner(new File("/Users/yulianzhou/Desktop/course/Java/Algorithms Design and Analysis, Part 2/src/allShortestPath/g3.txt"));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Shortest st = new Shortest(sc);
		long time2 = System.currentTimeMillis();
		System.out.println(st.BellmanFord());
		//long time3 = System.currentTimeMillis();
		//System.out.println(st.FloydWarshall());
		//long time2 = System.currentTimeMillis();
		//System.out.println(st.Johnson());
		long time3 = System.currentTimeMillis();
		System.out.println(time3 - time2);
		//-6
		//18737077
	}
}



/*
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Comparator;
import java.util.HashMap;
import java.util.PriorityQueue;
import java.util.Scanner;

public class Shortest {
	private Vertex[] vertices;
	private int V;
	private int E;
	private boolean hasNegativeCycle = false;
	private class Vertex {
		private HashMap<Integer, Integer> tails;
		private HashMap<Integer, Integer> heads;
		public Vertex() {
			tails = new HashMap<Integer, Integer>();
			heads = new HashMap<Integer, Integer>();
		}
		private void addIndegree(int v, int l) {
			tails.put(v, l);
		}
		private void addOutdegree(int v, int l) {
			heads.put(v, l);
		}
		private void updateOutedges(HashMap<Integer, Integer> newedges) {
			heads = newedges;
		}
	}
	public Shortest(Scanner sc) {
		Scanner scanner = new Scanner(sc.nextLine());
		V = scanner.nextInt();
		E = scanner.nextInt();
		vertices = new Vertex[V + 1];
		for (int i = 1; i < V + 1; i++) {
			vertices[i] = new Vertex();
		}
		while (sc.hasNextInt()) {
			scanner = new Scanner(sc.nextLine());
			int v1 = scanner.nextInt();
			int v2 = scanner.nextInt();
			int l = scanner.nextInt();
			vertices[v1].addOutdegree(v2, l);
			vertices[v2].addIndegree(v1, l);
		}
		
	}
	private void bellmanFord(int[] p) {
		int[] temp = new int[V + 1];
		for (int i = 1; i < V + 1; i++) {
			p[i] = Integer.MAX_VALUE;
			temp[i] = p[i];
		}
		
		for (int i= 0; i < V + 1; i++) {
			for (int j = 0; j < V + 1; j++) {
				for (Integer v : vertices[j].tails.keySet()) {
					if (temp[v] != Integer.MAX_VALUE) {
						int l = temp[v] + vertices[j].tails.get(v);
						if (p[j] > l) p[j] = l;
					}
				}
			}
			for (int j = 0; j < V + 1; j++) {
				temp[j] = p[j];
			}
		}
		for (int j = 0; j < V + 1; j++) {
			for (Integer v : vertices[j].tails.keySet()) {
				if (temp[v] != Integer.MAX_VALUE) {
					int l = temp[v] + vertices[j].tails.get(v);
					if (p[j] > l) {
						hasNegativeCycle = true;
						break;
					}
				}
			}
		}
	}
	private int dijkstra(int v, int[] p) {
		int[] scores = new int[V + 1];
		for (int i = 1; i < V + 1; i++) {
			scores[i] = Integer.MAX_VALUE;
		}
		scores[v] = 0;

		PriorityQueue<Integer> pq = new PriorityQueue<Integer>(new Comparator<Integer>(){
			public int compare(Integer o1, Integer o2) {
				// TODO Auto-generated method stub
				return Integer.compare(scores[o1], scores[o2]);
			}
			
		});
		for (int i = 1; i < V + 1; i++) {
			pq.add(i);
		}
		
		boolean[] visited = new boolean[V + 1];
		
		while (!pq.isEmpty()) {
			int newv = pq.poll();
			visited[newv] = true;
			for (Integer head : vertices[newv].heads.keySet()) {
				if(!visited[head]) {
					int newscore = scores[newv] + vertices[newv].heads.get(head);
					if (scores[head] > newscore) {
						pq.remove(head);
						scores[head] = newscore;
						pq.add(head);
					}
				}
			}
		}
		int min = Integer.MAX_VALUE;
		for (int i = 1; i < V + 1; i++) {
			int pathlength = scores[i] + p[i];
			if (min > pathlength) min = pathlength;
		}
		return min - p[v];
	}
	private int johnson() {
		System.out.println("finish construction");
		vertices[0] = new Vertex();
		for (int i = 1; i < V + 1; i++) {
			vertices[i].addIndegree(0, 0);
		}
		int[] p = new int[V + 1];
		bellmanFord(p);
		System.out.println(hasNegativeCycle);
		if (hasNegativeCycle) {
			System.out.println("negative cycle");
			return Integer.MIN_VALUE;
		}
		System.out.println("finish bellmanford");
		
		for (int i = 1; i < V + 1; i++) {
			HashMap<Integer, Integer> newedges = new HashMap<Integer, Integer>();
			for (Integer head : vertices[i].heads.keySet()) {
				int l = vertices[i].heads.get(head);
				newedges.put(head, l + p[i] - p[head]);
			}
			vertices[i].updateOutedges(newedges);
		}
		System.out.println("finish update edges");
		
		int shortest = Integer.MAX_VALUE;
		for (int i = 1; i < V + 1; i++) {
			if (i % 1000 == 0) System.out.printf("%s %d\n", "dijkstra", i);
			int shortesti = dijkstra(i, p);
			if (shortest > shortesti) shortest = shortesti;
		}
		return shortest;
	}
	
	private int floydWarshall() {
		int[][] d1 = new int[V + 1][V + 1];
		int[][] d2 = new int[V + 1][V + 1];
		
		for (int i = 1; i < V + 1; i++) {
			for (int j = 1; j < V + 1; j++) {
				d1[i][j] = Integer.MAX_VALUE;
			}
		}
		for (int i = 1; i < V + 1; i++) {
			d1[i][i] = 0;
			for (Integer head : vertices[i].heads.keySet()) {
				d1[i][head] = vertices[i].heads.get(head);
			}
		}

		for (int k = 1; k < V + 1; k++) {
			d2 = d1.clone();
			for (int i = 1; i < V + 1; i++) {
				for (int j = 1; j < V + 1; j++) {
					if (d2[i][k] != Integer.MAX_VALUE && d2[k][j] != Integer.MAX_VALUE) {
						int dijk = d2[i][k] + d2[k][j];
						if (dijk < d2[i][j]) {
							if (i == j && dijk < 0) {
								System.out.println("negative cycle");
								return Integer.MIN_VALUE;
							}
							d1[i][j] = dijk;
						}
					}
				}				
			}
		}
		
		int shortest = Integer.MAX_VALUE;
		for (int i = 1; i < V + 1; i++) {
			for (int j = 1; j < V + 1; j++) {
				if (d1[i][j] < shortest) shortest = d1[i][j];
			}
		}
		return shortest;
	}

	public static void main(String[] args) {
		Scanner sc = null;
		try {
			sc = new Scanner(new File("/Users/yulianzhou/Desktop/Course/java/Algs 2.2/large.txt"));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Shortest st = new Shortest(sc);
		long time2 = System.currentTimeMillis();
		System.out.println(st.johnson());
		//System.out.println(st.floydWarshall());
		long time3 = System.currentTimeMillis();
		//System.out.println(st.FloydWarshall());
		//long time2 = System.currentTimeMillis();
		//System.out.println(st.Johnson());
		//long time3 = System.currentTimeMillis();
		System.out.println(time3 - time2);
		//-6
		//18737077
		
		
		//-6
		//59039977
	}
}

*/