import java.io.File;
import java.io.FileNotFoundException;
import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.Scanner;


public class Huffman {

	private final int N;
	private Node root;
	
	private class Node {
		private long weight;
		private Node left;
		private Node right;
		public Node(long k) {
			weight = k;
		}
	}
	public Huffman(String file) {
		Scanner scanner = null;
		try {
			scanner = new Scanner(new File(file));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		N = scanner.nextInt();
		PriorityQueue<Node> pq = new PriorityQueue<Node>(new Comparator<Node>() {
			public int compare(Node nd1, Node nd2) {
				return Long.compare(nd1.weight, nd2.weight);
			}
		});
		for (int i = 0; i < N; i++) {
			pq.add(new Node(scanner.nextInt()));
		}
		while (pq.size() > 1) {
			Node nd1 = pq.poll();
			Node nd2 = pq.poll();
			Node newnd = new Node(nd1.weight + nd2.weight);
			//System.out.printf("%d %d %d\n", nd1.weight, nd2.weight, newnd.weight);
			newnd.left = nd1;
			newnd.right = nd2;
			pq.add(newnd);
		}
		root = pq.poll();
	}
	
	public void huffman() {

	}
	
	public int maxLength() {
		return maxLength(root) - 1;
	}
	
	private int maxLength(Node nd) {
		if (nd == null) return 0;
		int l1 = maxLength(nd.left);
		int l2 = maxLength(nd.right);
		return 1 + ((l1 > l2) ? l1 : l2);
	}
	
	public int minLength() {
		return minLength(root) - 1;
	}
	
	private int minLength(Node nd) {
		if (nd == null) return 0;
		int l1 = minLength(nd.left);
		int l2 = minLength(nd.right);
		return 1 + ((l1 > l2) ? l2 : l1);
	}
	
	public static void main(String[] args) {
		Huffman hf = new Huffman(args[0]);
		System.out.println(hf.maxLength());
		System.out.println(hf.minLength());
	}
}
