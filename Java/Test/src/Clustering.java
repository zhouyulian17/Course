import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.PriorityQueue;
import java.util.Scanner;

public class Clustering {
	public ArrayList<Edge> edges;
	public PriorityQueue<Edge> pq;
	public UF uf;
	private class UF {
		int[] rank;
		int[] vertices;
		int num;
		public UF(int n) {
			num = n;
			vertices = new int[n];
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
		public void union(int rooti, int rootj) {
			num--;
			//System.out.printf("%d, %d, %d,", rooti, rootj, num);
			if (rank[rooti] > rank[rootj]) vertices[rootj] = rooti;
			else if (rank[rootj] > rank[rooti]) vertices[rooti] = rootj;
			else {
				vertices[rootj] = rooti;
				//System.out.printf("%d %d %d ",rank[rooti], rank[rootj], rank[rooti] + 1);
				//System.out.println();
				rank[rooti] = rank[rooti] + 1;
			}
		}
	}
	private class Edge implements Comparable<Edge> {
		private int len;
		private int nd1;
		private int nd2;
		public Edge(int m, int n, int l) {
			nd1 = m;
			nd2 = n;
			len = l;
		}
		public int compareTo(Edge o) {
			return this.len - o.len;
		}
	}
	public Clustering(Scanner sc) {
		uf = new UF(Integer.parseInt(sc.nextLine()));
		pq = new PriorityQueue<Edge>();
		edges = new ArrayList<Edge>();
		Scanner scanner;
		while(sc.hasNextInt()) {
			scanner = new Scanner(sc.nextLine());
			int m = scanner.nextInt();
			int n = scanner.nextInt();
			int l = scanner.nextInt();
			edges.add(new Edge(m, n, l));
		}
		pq.addAll(edges);
	}
	public int kCluster(int k) {
		while(uf.num > k) {
			Edge temp = pq.poll();
			int i = temp.nd1 - 1;
			int j = temp.nd2 - 1;
			int rooti = uf.find(i);
			int rootj = uf.find(j);
			if (rooti != rootj) {
				uf.union(rooti, rootj);
			}
		}
		int l = 0;
		while(!pq.isEmpty()) {
			Edge temp = pq.poll();
			int i = temp.nd1 - 1;
			int j = temp.nd2 - 1;
			int rooti = uf.find(i);
			int rootj = uf.find(j);
			if (rooti != rootj) {
				l = temp.len;
				break;
			}
			//System.out.println(temp.len);
		}
		return l;
	}
	public static void main(String[] args) {
		Scanner sc = null;
		try {
			sc = new Scanner(new File("/Users/yulianzhou/Desktop/course/Java/Algorithms Design and Analysis, Part 2/src/clustering/clustering1.txt"));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		double t1 = System.currentTimeMillis();
		Clustering cl = new Clustering(sc);
		System.out.println(cl.kCluster(4));
		System.out.println();
		double t2 = System.currentTimeMillis();
		System.out.println(t2 - t1);
		/*
		HashSet<Integer> set = new HashSet<Integer>();
		for (int i = 0; i < 500; i++) {
			int temp = cl.uf.find(i);
			if(!set.contains(temp)) {
				set.add(temp);
			}
		}
		for (Integer i : set) {
			System.out.println(i);
		}
		*/
	}
}

/*
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.PriorityQueue;
import java.util.Scanner;

public class Clustering {
	private int V;
	private PriorityQueue<Edge> edges;
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
	private class Edge implements Comparable<Edge>{
		private int p;
		private int q;
		private int l;
		public Edge(int n1, int n2, int n3) {
			p = n1;
			q = n2;
			l = n3;
		}
		public int compareTo(Edge that) {
			return Integer.compare(this.l, that.l);
		}
	}
	public Clustering(Scanner sc) {
		Scanner scanner = new Scanner(sc.nextLine());
		V = scanner.nextInt();
		uf = new UF(V);
		ArrayList<Edge> temp_edges = new ArrayList<Edge>();
		while (sc.hasNextInt()) {
			scanner = new Scanner(sc.nextLine());
			int n1 = scanner.nextInt();
			int n2 = scanner.nextInt();
			int n3 = scanner.nextInt();
			temp_edges.add(new Edge(n1 - 1, n2 - 1, n3));
		}
		edges = new PriorityQueue<Edge>();
		edges.addAll(temp_edges);
	}
	
	public int kCluster(int n) {
		int num = V;
		while (num != n) {
			Edge e = edges.poll();
			boolean b = uf.union(e.p, e.q);
			if (b) continue;
			else num--;
		}
		int l = 0;
		while (!edges.isEmpty()) {
			Edge e = edges.poll();
			boolean b = uf.union(e.p, e.q);
			if (!b) {
				l = e.l;
				break;
			}
		}
		return l;
	}
	public static void main(String[] args) {
		Scanner sc = null;
		try {
			sc = new Scanner(new File("/Users/yulianzhou/Desktop/Course/java/Algs 2.2/clustering1.txt"));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		double t1 = System.currentTimeMillis();
		Clustering cl = new Clustering(sc);
		System.out.println(cl.kCluster(4));
		System.out.println();
		double t2 = System.currentTimeMillis();
		System.out.println(t2 - t1);
	}
}

 */
