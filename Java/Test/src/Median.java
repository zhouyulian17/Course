import java.io.File;
import java.io.FileNotFoundException;
import java.util.Collections;
import java.util.PriorityQueue;
import java.util.Scanner;

public class Median {
	private PriorityQueue<Integer> pq1;
	private PriorityQueue<Integer> pq2;
	
	public Median() {
		pq1 = new PriorityQueue<Integer>(5000, Collections.reverseOrder());
		pq2 = new PriorityQueue<Integer>(5000);
	}
	
	public int median(int n) {
		if (pq1.size() == 0) {
			pq1.add(n);
			return n;
		}
		if(pq1.size() > pq2.size()) {
			int p1 = pq1.peek();
			if (n < p1) {
				pq1.poll();
				pq1.add(n);
				pq2.add(p1);
			}
			else pq2.add(n);
		}
		else {
			int p2 = pq2.peek();
			if (n > p2) {
				pq2.poll();
				pq1.add(p2);
				pq2.add(n);
			}
			else pq1.add(n);
		}
		return pq1.peek();
	}
	public static void main(String[] args) {
		Median m = new Median();
		int sum = 0;
		int i = 0;
		Scanner sc = null;
		try {
			sc = new Scanner(new File("/Users/yulianzhou/Desktop/course/Java/Algorithms Design and Analysis, Part 1/src/medianMaintenance/Median.txt"));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		while (sc.hasNextInt()) {
			int n = m.median(sc.nextInt());
			sum += n;
			i++;
			//System.out.println(n);
		}
		System.out.printf("%d, %d, %d", i, sum, sum % 10000);
	}
}
