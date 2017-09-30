import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class MWIS {
	
	private int N;
	private int[] weights;
	boolean[] included;
	
	public MWIS(String file) {
		Scanner scanner = null;
		try {
			scanner = new Scanner(new File(file));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		N = scanner.nextInt();
		weights = new int[N];
		for (int i = 0; i < N; i++) {
			weights[i] = scanner.nextInt();
			//System.out.println(weights[i]);
		}
		long[] choice = new long[N];
		choice[0] = weights[0];
		choice[1] = weights[1];
		for (int i = 2; i < N; i++) {
			long temp = choice[i - 2] + weights[i];
			choice[i] = (temp > choice[i - 1]) ? temp : choice[i - 1];
			System.out.println(choice[i]);
		}
		included = new boolean[N];
		int i = N - 1;
		for (; i > 0; i--) {
			if (choice[i] != choice[i - 1]) included[i--] = true;
		}
		if (i == 0) included[0] = true;
	}
	
	public static void main(String[] args) {
		MWIS mwis = new MWIS(args[0]);
		int[] array = {1, 2, 3, 4, 17, 117, 517, 997};
		for (int i = 0; i < array.length; i++) {
			if (mwis.included[array[i] - 1]) System.out.printf("%d", 1);
			else System.out.printf("%d", 0);
		}
	}
}
