import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Clustering2 {
	//public HashMap<Vertex, Integer> map;
	public Vertex[] v;
	public UF uf;
	int n;
	int V;
	private class UF {
		int[] rank;
		int[] vertices;
		//boolean[] visited;
		int num;
		public UF(int n) {
			num = n;
			vertices = new int[n];
			//visited = new boolean[n];
			for (int i = 0; i < n; i++) {
				vertices[i] = i;
			}
			rank = new int[n];
			for (int i = 0; i < n; i++) {
				rank[i] = 0;
			}
		}
		public int find(int i) {
			while (vertices[i] != i) {
				i = vertices[i];
			}
			return i;
		}
		public void union(int i, int j) {
			int rooti = find(i);
			int rootj = find(j);
			if (rooti == rootj) return;
			num--;
			if (rank[rooti] > rank[rootj]) vertices[rootj] = rooti;
			else if (rank[rootj] > rank[rooti]) vertices[rooti] = rootj;
			else {
				vertices[rootj] = rooti;
				rank[rooti] = rank[rooti] + 1;
			}
		}
	}
	private class Vertex {
		//private int value;
		private int[] bits;
		public Vertex(int n, Scanner sc) {
			//value = 0;
			bits = new int[n];
			for (int i = 0; i < n; i++) {
				bits[i] = sc.nextInt();
				//value *= 2;
				//value += bits[i];
			}
		}
		/*
		public Vertex(int n, int[] temp) {
			value = 0;
			bits = temp.clone();
			for (int i = 0; i < n; i++) {
				value *= 2;
				value += bits[i];
			}
		}
		public int hashCode() {
			return value;
		}
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			Vertex other = (Vertex) obj;
			if (value != other.value)
				return false;
			return true;
		}
		*/

	}
	public Clustering2(Scanner sc) {
		Scanner scanner = new Scanner(sc.nextLine());
		V = scanner.nextInt();
		n = scanner.nextInt();
		uf = new UF(V);
		v = new Vertex[V];
		int i = 0;
		while(sc.hasNextInt()) {
			scanner = new Scanner(sc.nextLine());
			v[i++] = new Vertex(n, scanner);
		}
		/*
		map = new HashMap<Vertex, Integer>();
		int i = 0;
		while(sc.hasNextInt()) {
			scanner = new Scanner(sc.nextLine());
			map.put(new Vertex(n, scanner), i++);
		}
		*/
	}
	public int clustering() {
		for (int i = 0; i < V; i++) {
			System.out.println(i);
			for (int j = i + 1; j < V; j++) {
				int temp = 0;
				for (int k = 0; k < n; k++) {
					temp += v[i].bits[k] ^ v[j].bits[k];
				}
				if (temp < 3) {
					uf.union(i, j);
				}
			}
		}
		return uf.num;
	}
	/*
	public void unionNeighbor(Vertex v, int m) {
		for (int i = 0; i < n; i++) {
			int[] temp = v.bits.clone();
			temp[i] ^= 1;
			Vertex v2 = new Vertex(n, temp);
			if (map.containsKey(v2)) {
				int value = map.get(v2);
				if (!uf.visited[value]) {
					uf.visited[value] = true;
					uf.vertices[value] = m;
					unionNeighbor(v2, m);
				}
			}
			for (int j = i + 1; j < n; j++) {
				int[] temp2 = temp.clone();
				temp2[j] ^= 1;
				v2 = new Vertex(n, temp2);
				if (map.containsKey(v2)) {
					int value = map.get(v2);
					if (!uf.visited[value]) {
						uf.visited[value] = true;
						uf.vertices[value] = m;
						unionNeighbor(v2, m);
					}
				}
			}
		}
	}
	public int clustering() {
		int count = V;
		for (Vertex v : map.keySet()) {
			int m = map.get(v);
			if(!uf.visited[m]) {
				uf.visited[m] = true;
				unionNeighbor(v, m);
			}
			else count--;
		}
		return count;
	}
	*/
	public static void main(String[] args) {
		Scanner sc = null;
		try {
			sc = new Scanner(new File("/Users/yulianzhou/Desktop/course/Java/Algorithms Design and Analysis, Part 2/src/clustering/clustering_big.txt"));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		double t1 = System.currentTimeMillis();
		Clustering2 cl = new Clustering2(sc);
		System.out.println(cl.clustering());
		double t2 = System.currentTimeMillis();
		System.out.println(t2 - t1);
		
		//6118
	}
}
/*
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.PriorityQueue;
import java.util.Scanner;

public class Clustering2 {
	private int V;
	private int k;
	private Vertex[] vertices;
	private UF uf;
	private class UF {
		private int[] rank;
		private int[] root;
		public UF(int n) {
			rank = new int[n];
			root = new int[n];
			for (int i = 0; i < n; i++) {
				root[i] = i;
			}
		}
		private int root(int p) {
			while (root[p] != p) {
				p = root[p];
			}
			return p;
		}
		private boolean union(int p, int q) {
			int rootp = root(p);
			int rootq = root(q);
			if (rootp == rootq) return true;
			if (rank[rootp] > rank[rootq]) root[rootq] = rootp;
			else if (rank[rootp] < rank[rootq]) root[rootp] = rootq;
			else {
				root[rootq] = rootp;
				rank[rootp] += 1;
			}
			return false;
		}
	}
	private class Vertex {
		private int[] bits;
		public Vertex(Scanner sc, int k) {
			bits = new int[k];
			int i = 0;
			while(sc.hasNextInt()) {
				bits[i++] = sc.nextInt();
			}
		}
	}
	public Clustering2(Scanner sc) {
		Scanner scanner = new Scanner(sc.nextLine());
		V = scanner.nextInt();
		k = scanner.nextInt();
		uf = new UF(V);
		vertices = new Vertex[V];
		for (int i = 0; i < V; i++) {
			scanner = new Scanner(sc.nextLine());
			vertices[i] = new Vertex(scanner, k);
		}
	}
	
	public int kCluster(int n) {
		int num = V;
		for (int i = 0; i < V; i++) {
			System.out.println(i);
			for (int j = i + 1; j < V; j++) {
				int dif = 0;
				Vertex v1 = vertices[i];
				Vertex v2 = vertices[j];
				for (int l = 0; l < k; l++) {
					dif += v1.bits[l] ^ v2.bits[l];
				}
				if (dif < n) {
					if (!uf.union(i, j)) num--;
				}
			}
		}
		return num;
	}
	public static void main(String[] args) {
		Scanner sc = null;
		try {
			sc = new Scanner(new File("/Users/yulianzhou/Desktop/Course/java/Algs 2.2/clustering_big.txt"));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		double t1 = System.currentTimeMillis();
		Clustering2 cl = new Clustering2(sc);
		System.out.println(cl.kCluster(3));
		double t2 = System.currentTimeMillis();
		System.out.println(t2 - t1);
		//6118
	}
}
*/