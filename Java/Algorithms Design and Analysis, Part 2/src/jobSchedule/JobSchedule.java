package jobSchedule;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Scanner;

/*************************************************************************
 * Name: Yulian Zhou
 * Email: zhouyulian17@gmail.com
 *
 * Compilation:  javac JobSchedule.java
 * Execution:    java JobSchedule jobs.txt choice
 * Dependencies: Job.java
 *
 * Description:  A data type that minimizing the weighted sum of completion
 *               times using the greedy algorithm that schedules jobs in
 *               decreasing order of the difference (weight - length) or
 *               the ratio (weight/length).
 *
 *************************************************************************/

public class JobSchedule {
    
    private List<Job> jobs;    // a list of job
    
    // a comparator by the difference (weight - length) of jobs
    private final Comparator<Job> dif = new Comparator<Job>() {
        @Override
        public int compare(Job o1, Job o2) {
            if (o2.comp1() == o1.comp1()) return o2.weight() - o1.weight();
            return o2.comp1() - o1.comp1();
        }
    };
    
    // a comparator by the ratio (weight - length) of jobs
    private final Comparator<Job> rat = new Comparator<Job>() {
        @Override
        public int compare(Job o1, Job o2) {
            if (o2.comp2() > o1.comp2()) return 1;
            if (o2.comp2() < o1.comp2()) return -1;
            return 0;
        }
    };
    
    /**
     * Initializes a JobSchedule data structure from file.
     */
    public JobSchedule(String file) {
        Scanner scanner = null;
        try {
            scanner = new Scanner(new File(file));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        int n = Integer.parseInt(scanner.nextLine());
        jobs = new ArrayList<Job>(n);
        while (scanner.hasNextInt()) {
            Scanner sc = new Scanner(scanner.nextLine());
            int w = sc.nextInt();
            int l = sc.nextInt();
            jobs.add(new Job(w, l));
        }
    }
    
    /**
     * Computes and returns the weighted sum of completion times by scheduling
     * jobs in decreasing order of the difference or ratio.
     */
    public long weightedCompTime(int choice) {
        long WCT = 0;
        int time = 0;
        PriorityQueue<Job> pq = null;
        if (choice == 1) pq = new PriorityQueue<Job>(dif);
        else if (choice == 2) pq = new PriorityQueue<Job>(rat);
        else throw new IllegalArgumentException("Illegal choice");
        pq.addAll(jobs);
        while (!pq.isEmpty()) {
            Job job = pq.poll();
            time += job.length();
            WCT  += job.weight() * time;
        }
        return WCT;
    }
    
    /**
     * Unit tests the JobSchedule data type.
     */
    public static void main(String[] args) {
        String file = args[0];
        int choice = Integer.parseInt(args[1]);
        JobSchedule js = new JobSchedule(file);
        System.out.println(js.weightedCompTime(choice));
    }
}