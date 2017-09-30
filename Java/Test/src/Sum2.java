import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashSet;
import java.util.Scanner;

public class Sum2 {
	public static void main(String[] args) {
		long time1 = System.currentTimeMillis();
		HashSet<Long> set = new HashSet<Long>();
		Scanner sc = null;
		Long max = Long.MIN_VALUE;
		Long min = Long.MAX_VALUE;
		try {
			sc = new Scanner(new File("/Users/yulianzhou/Desktop/course/Java/Algorithms Design and Analysis, Part 1/src/sum2/algo1-programming_prob-2sum.txt"));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        while(sc.hasNextLong()) {
        	Long temp = sc.nextLong();
        	if (temp > max) max = temp;
        	if (temp < min) min = temp;
            set.add(temp);
        }
		System.out.println(set.size());
		int sum = 0;
		for (int t = -10000; t < 10001; t++) {
			for (Long key: set) {
				Long k = t - key;
				if (k > max || k < min) continue;
				if (Long.compare(key, k) == 1) {
					if (set.contains(k)) {
						sum++;
						System.out.printf("%d, %d\n", t, sum);
						break;
					}
				}
			}
		}
		System.out.println(sum);
		long time2 = System.currentTimeMillis();
		System.out.println(time2 - time1);
	}
}
