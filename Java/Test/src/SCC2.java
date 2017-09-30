import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Stack;

// StronglyConnectedComponents
public class SCC2 {
	private Vertex[] vertices;
	private boolean[] explored;
	private int[] times;
	private int n;
	private int v;
	private class Vertex {
		private ArrayList<Integer> tail;
		private ArrayList<Integer> head;
		public Vertex() {
			tail = new ArrayList<Integer>();
			head = new ArrayList<Integer>();
		}
		public void addTail(int n) {
			tail.add(n);
		}
		public void addHead(int n) {
			head.add(n);
		}
	}
	public SCC2(Scanner sc) {
		Scanner scanner = new Scanner(sc.nextLine());
		v = scanner.nextInt();
		n = v * 2;
		vertices = new Vertex[n + 1];
		times = new int[n + 1];
		explored = new boolean[n + 1];
		for (int i = n; i > 0; i--) {
			vertices[i] = new Vertex();
		}
		
		while (sc.hasNext()) {
			scanner = new Scanner(sc.nextLine());
			int n1 = scanner.nextInt();
			int n2 = scanner.nextInt();
			int n3;
			int n4;
			if (n1 < 0) {
				n3 = -n1;
				n1 = n3 + v;
			}
			else n3 = n1 + v;
			if (n2 < 0) {
				n4 = - n2;
				n2 = n4 + v;
			}
			else n4 = n2 + v;
			vertices[n3].addHead(n2);
			vertices[n2].addTail(n3);
			vertices[n4].addHead(n1);
			vertices[n1].addTail(n4);
		}
	}

	public void revDFS() {
		Stack<Integer> s = new Stack<Integer>();
		Stack<Integer> t = new Stack<Integer>();
		int m = 1;
		int k;
		int j = 1;
		while (m < n + 1) {
			if (!s.isEmpty()) {
				k = s.pop();
				t.push(k);
				if (!vertices[k].tail.isEmpty()) {
					for (Integer i: vertices[k].tail) {
						if (!explored[i]) {
							explored[i] = true;
							s.push(i);
						}
					}
				}
			}
			else {
				if (!explored[m]) {
					s.push(m);
					explored[m] = true;
					while(!t.isEmpty()) {
						times[j++] = t.pop();
					}
				}
				m++;
			}
		}
		if (!s.isEmpty()) {
			times[j++] = s.pop();
		}
		else {
			while (!t.isEmpty()) {
				times[j++] = t.pop();
			}
		}
	}
	
	public boolean DFS() {
		revDFS();
		explored = new boolean[n + 1];
		Stack<Integer> s = new Stack<Integer>();
		Stack<Integer> t = new Stack<Integer>();
		HashSet<Integer> set = new HashSet<Integer>();
		int m = n;
		int k;
		while (m > 0) {
			if (!s.isEmpty()) {
				k = s.pop();
				t.push(k);
				if (!vertices[k].head.isEmpty()) {
					for (Integer i: vertices[k].head) {
						if (!explored[i]) {
							explored[i] = true;
							s.push(i);
						}
					}
				}
			}
			else {
				if (!explored[times[m]]) {
					s.push(times[m]);
					explored[times[m]] = true;
					set.addAll(t);
					for (Integer key : set) {
						if (set.contains(key + v)) {
							System.out.printf("%d %d", key, key + v);
							return false;
						}
					}
					t = new Stack<Integer>();
					set = new HashSet<Integer>();
				}
				m--;
			}
		}
		//if (!s.isEmpty()) list.add(s.size());
		//else list.add(t.size());
		set.addAll(t);
		if (set.size() != 1) System.out.println(set.size());
		for (Integer key : set) {
			if (set.contains(key + v)) return false;
		}
		return true;
	}
	public static void main(String[] args) {
		  Scanner sc = null;
		  try {
		   sc = new Scanner(new File("/Users/yulianzhou/Desktop/course/Java/Algorithms Design and Analysis, Part 1/src/connectedcomponents/SCC.txt"));
		  } catch (FileNotFoundException e) {
		   // TODO Auto-generated catch block
		   e.printStackTrace();
		  }
		  //SSC ssc = new SSC(sc, 8);
		SCC2 s = new SCC2(sc);
		s.revDFS();
		s.DFS();
	}
}
