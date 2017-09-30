import java.io.File;
import java.io.FileNotFoundException;
import java.util.PriorityQueue;
import java.util.Scanner;

public class Jobs {
	PriorityQueue<Job> jobs;
	private class Job implements Comparable<Job> {
		int weight;
		int length;
		double c;
		public Job(Scanner sc) {
			weight = sc.nextInt();
			length = sc.nextInt();
			c = weight - length;
			//c = (double) weight / length;
		}
		/*
		public int compareTo(Job o) {
			if (this.c < o.c) return 1;
			if (this.c > o.c) return -1;
			if (this.weight < o.weight) return 1;
			if (this.weight > o.weight) return -1;
			else return 0;
		}	
		*/
		public int compareTo(Job o) {
			// TODO Auto-generated method stub
			if (this.c < o.c) return 1;
			if (this.c > o.c) return -1;
			return 0;
		}
		
	}
	public Jobs(Scanner sc) {
		int n = Integer.parseInt(sc.nextLine());
		jobs = new PriorityQueue<Job>(n);
		while (sc.hasNextInt()) {
			Scanner scanner = new Scanner(sc.nextLine());
			jobs.add(new Job(scanner));
		}
	}
	public long calculateWeight() {
		long n = 0L;
		int c = 0;
		while (!jobs.isEmpty()) {
			Job job = jobs.poll();
			c += job.length;
			n += job.weight * c;
		}
		return n;
	}
	public static void main(String[] args) {
		Scanner sc = null;
		try {
			sc = new Scanner(new File("/Users/yulianzhou/Desktop/course/Java/Algorithms Design and Analysis, Part 2/src/jobSchedule/jobs.txt"));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Jobs jb = new Jobs(sc);
		System.out.println(jb.calculateWeight());
	}
}
/*
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

public class Jobs {
	private ArrayList<Job> jobs;
	private class Job implements Comparable<Job> {
		private int w;
		private int l;
		private double dif;
		public Job (Scanner sc) {
			w = sc.nextInt();
			l = sc.nextInt();
			dif = w - l;
			//dif = (double) w / l;
		}
		public int compareTo(Job that) {
			if (this.dif > that.dif) return -1;
			if (this.dif < that.dif) return 1;
			return that.w - this.w;
		}
		
	}
	public Jobs(Scanner sc) {
		int n = Integer.parseInt(sc.nextLine());
		jobs = new ArrayList<Job>(n);
		while (sc.hasNextInt()) {
			Scanner scanner = new Scanner(sc.nextLine());
			jobs.add(new Job(scanner));
		}
		Collections.sort(jobs);
		
	}
	public long calculateWeight() {
		int time = 0;
		long weightedtime = 0L;
		for (Job job : jobs) {
			time += job.l;
			weightedtime += time * job.w;
		}
		return weightedtime;
	}
	public static void main(String[] args) {
		Scanner sc = null;
		try {
			sc = new Scanner(new File("/Users/yulianzhou/Desktop/Course/java/Algs 2.2/jobs.txt"));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Jobs jb = new Jobs(sc);
		System.out.println(jb.calculateWeight());
	}
}
*/