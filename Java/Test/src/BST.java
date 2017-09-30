import java.util.HashMap;

public class BST {
	private double[] frequencies;
	private int N;
	private class Tree {
		private int N;
		private boolean[] vertices;
		private double value;
		private int id;
		public Tree(int n) {
			N = n;
			vertices = new boolean[N];
			id = 0;
			value = 0;
		}
		private void addVertex(int v) {
			vertices[v] = true;
			id += 1 << v;
		}
		private Tree copy() {
			Tree newtree = new Tree(N);
			for (int i = 0; i < N; i++) {
				if (this.vertices[i]) newtree.addVertex(i);
			}
			return newtree;
		}
	}
	public BST(double[] nums) {
		frequencies = nums.clone();
		N = nums.length;
	}
	public double BSTconstruct() {
		
		HashMap<Integer, Tree> map = new HashMap<Integer, Tree>();
		HashMap<Integer, Tree> maptemp1 = new HashMap<Integer, Tree>();
		maptemp1.put(0,  new Tree(N));
		HashMap<Integer, Tree> maptemp2;
		
		for (int i = 0; i < N; i++) {
			map.putAll(maptemp1);
			maptemp2 = new HashMap<Integer, Tree>();
			//create set of trees with one size larger into maptemp2;
			for (Integer k : maptemp1.keySet()) {
				Tree t = maptemp1.get(k);
				for (int j = 0; j < N; j++) {
					if (!t.vertices[j]) {
						int newkey = t.id + (1 << j);
						if (!maptemp2.containsKey(newkey)) {
							Tree newt = t.copy();
							newt.addVertex(j);
							maptemp2.put(newt.id, newt);
						}
					}
				}
			}
			//calculate the value of trees in maptemp2;			
			maptemp1 = new HashMap<Integer, Tree>();
			for (Integer k : maptemp2.keySet()) {
				Tree t = maptemp2.get(k);
				double valuetemp = Double.POSITIVE_INFINITY;
				double sumv = 0;
				for (int j = 0; j < N; j++) {
					if (t.vertices[j]) {
						sumv += frequencies[j];
						//System.out.printf("%d %d\n", j, k);
						int kl = (k >>> (j + 1)) << (j + 1);
						int mask = (1 << j) - 1;
						int kr = k & mask;
						
						double valuel = map.get(kl).value;
						double valuer = map.get(kr).value;
						double value = valuel + valuer;
						
						if (value < valuetemp) valuetemp = value; 
					}
				}
				t.value = valuetemp + sumv; //
				maptemp1.put(k, t);
			}
		}
		
		//find minimal value of trees with all vertices
		double value = Double.POSITIVE_INFINITY;
		for (Integer k : maptemp1.keySet()) {
			double v = maptemp1.get(k).value;
			if (value > v) value = v;
		}
		return value;

	}
	public static void main(String[] args) {
		double[] nums = {0.2, 0.05, 0.17, 0.1, 0.2, 0.03, 0.25};
		BST bst = new BST(nums);

		System.out.println(bst.BSTconstruct());
		
	}
}