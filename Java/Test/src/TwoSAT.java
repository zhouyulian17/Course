import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Random;
import java.util.Scanner;

public class TwoSAT {
	private int n;
	private int n2;
	private int logn;
	private HashMap<Integer, Integer> variables;
	private Clause[] clauses;
	private class Clause {
		boolean notm;
		int m;
		boolean notn;
		int n;
		public Clause (int n1, int n2) {
			notm = n1 < 0;
			if (notm) m = -n1;
			else m = n1;
			notn = n2 < 0;
			if (notn) n = -n2;
			else n = n2;
		}
		public boolean satisfied(int m1, int m2) {
			boolean b1;
			boolean b2;
			if (notm) b1 = m1 == 0;
			else b1 = m1 == 1;
			if (notn) b2 = m2 == 0;
			else b2 = m2 == 1;
			return b1 || b2;
		}
	}
	public TwoSAT(Scanner sc) {
		Scanner scanner = new Scanner(sc.nextLine());
		n = scanner.nextInt();
		n2 = (int) (2 * Math.pow(n, 2));
		logn = (int) (Math.log(n) / Math.log(2));
		clauses = new Clause[n];
		variables = new HashMap<Integer, Integer>(n);
		int i = 0;
		while (sc.hasNext()) {
			scanner = new Scanner(sc.nextLine());
			int n1 = scanner.nextInt();
			int n2 = scanner.nextInt();
			//System.out.printf("%d %d \n", n1, n2);
			clauses[i] = new Clause(n1, n2);
			variables.put(clauses[i].m, 0);
			variables.put(clauses[i].n, 0);
			//System.out.printf("%d %d \n", clauses[i].m, clauses[i].n);
			i++;
		}

		//System.out.println(variables.size());
	}
	public boolean TwoSAT() {
		for (int i = 0; i < logn; i++) {
			System.out.println(i);
			System.out.println(n2);
			init();
			if (randomWalk() == true) return true;
		}
		return false;
	}
	public boolean randomWalk() {
		Random r = new Random();
		boolean satisfied = true;
		for (int i = 0; i < n2; i++) {
			satisfied = true;
			for (Clause c : clauses) {
				int m1 = variables.get(c.m);
				int m2 = variables.get(c.n);
				if (!c.satisfied(m1, m2)) {
					int n = r.nextInt(2);
					if (n == 0) variables.put(c.m, m1 ^ 1);
					else variables.put(c.n, m2 ^ 1);
					satisfied = false;
					break;
				}
			}
		}
		return satisfied;
	}
	public void init() {
		Random r = new Random();
		for (Integer k : variables.keySet()) {
			variables.put(k, r.nextInt(2));
		}
	}
	public static void main(String[] args) {
		Scanner sc = null;
		try {
			sc = new Scanner(new File("/Users/yulianzhou/Desktop/course/Java/Algorithms Design and Analysis, Part 2/src/sat2/2sat1.txt"));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		TwoSAT ts = new TwoSAT(sc);
		System.out.println(ts.TwoSAT());
	}
}

/*
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;

public class TwoSAT {
	private int N;
	private Clause[] set;
	private HashSet<Integer> variables;
	private class Clause {
		private int x;
		private int y;
		private boolean b1;
		private boolean b2;
		public Clause(Scanner sc) {
			x = sc.nextInt();
			if (x < 0) {
				x = -x;
				b1 = false;
			}
			else b1 = true;
			y = sc.nextInt();
			if (y < 0) {
				y = -y;
				b2 = false;
			}
			else b2 = true;
		}
		private boolean satisfied(boolean b1, boolean b2) {
			if (this.b1 != b1 && this.b2 != b2) return false;
			else return true;
		}
	}
	public TwoSAT(Scanner sc) {
		N = Integer.parseInt(sc.nextLine());
		set = new Clause[N];
		variables = new HashSet<Integer>();
		for (int i = 0; i < N; i++) {
			Scanner scanner = new Scanner(sc.nextLine());
			set[i] = new Clause(scanner);
			variables.add(set[i].x);
			variables.add(set[i].y);
		}
	}
	private HashMap<Integer, Boolean> init(HashSet<Integer> variables) {
		HashMap<Integer, Boolean> values = new HashMap<Integer, Boolean>();
		for (Integer k : variables) {
			values.put(k, Math.random() < 0.5);
		}
		return values;
	}
	private boolean satisfiable() {
		for (int i = 0; i < Math.log(N) / Math.log(2); i++) {
			HashMap<Integer, Boolean> values = init(variables);
			System.out.println(i);
			for (long j = 0L; j < 2 * Math.pow(N, 2); j++) {
				//System.out.printf("%d %d\n", i, j);
				List<Integer> unsatisfied = new ArrayList<Integer>(); 
				
				for (int k = 0; k < N; k++) {
					int x = set[k].x;
					int y = set[k].y;
					boolean b1 = values.get(x);
					boolean b2 = values.get(y);
					if (!set[k].satisfied(b1, b2)) {
						unsatisfied.add(k);
					}
				}
				if (unsatisfied.isEmpty()) return true;
				int n = unsatisfied.size();
				int pickclause = (int) (Math.random() * n);
				int clause = unsatisfied.get(pickclause);
				int variable;
				if (Math.random() < 0.5) variable = set[clause].x;
				else variable = set[clause].y;
				values.put(variable, !values.get(variable));
			}
		}
		return false;
	}
	public static void main(String[] args) {
		Scanner sc = null;
		try {
			sc = new Scanner(new File("/Users/yulianzhou/Desktop/Course/java/Algs 2.2/2sat1.txt"));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		TwoSAT ts = new TwoSAT(sc);
		System.out.println(ts.satisfiable());
	}
}
*/