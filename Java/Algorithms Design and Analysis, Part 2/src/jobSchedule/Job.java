package jobSchedule;

/*************************************************************************
 * Name: Yulian Zhou
 * Email: zhouyulian17@gmail.com
 *
 * Compilation:  javac Job.java
 * Execution:    None
 * Dependencies: None
 *
 * Description:  A weighted job.
 *
 *************************************************************************/

public class Job {
    private final int weight;         // the weight of this job
    private final int length;         // the length of this job
    private final int comp1;     // the difference (weight - length) of this job
    private final double comp2;  // the ratio (weight/length) of this job
    
    /**
     * Initializes a job with weight and length.
     */
    public Job(int w, int l) {
        weight = w;
        length = l;
        comp1 = w - l;
        comp2 = (double) w / l;
    }
    
    /**
     * Returns the weight of this job.
     */
    public int weight() { return weight; }
    
    /**
     * Returns the length of this job.
     */
    public int length() { return length; }
    
    /**
     * Returns the difference (weight - length) of this job.
     */
    public int comp1() { return comp1; }
    
    /**
     * Returns the ratio (weight/length) of this job.
     */
    public double comp2() { return comp2; }
}