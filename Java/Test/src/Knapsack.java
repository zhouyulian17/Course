import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class Knapsack {
	private int capacity;
	private int num;
	private ArrayList<Item> items;
	private int[][] values;
	private HashMap<Integer,HashMap<Integer, Integer>> map;
	
	private class Item {
		int value;
		int weight;
		public Item(Scanner sc) {
			value = sc.nextInt();
			weight = sc.nextInt();
		}
	}
	public Knapsack(Scanner sc) {
		Scanner scanner = new Scanner(sc.nextLine());
		capacity = scanner.nextInt();
		num = scanner.nextInt();
		items = new ArrayList<Item>();
		while (sc.hasNextInt()) {
			scanner = new Scanner(sc.nextLine());
			items.add(new Item(scanner));
		}
		map = new HashMap<Integer,HashMap<Integer, Integer>>();
	}
	public int maxValue(int i, int j) {
		if (i == 0 || j == 0) return 0;
		Item temp = items.get(i);
		HashMap<Integer, Integer> temp3 = new HashMap<Integer, Integer>();;
		int temp1 = 0;
		if (j >= temp.weight) {
			if (!map.containsKey(i)) {
				temp3.put(j - temp.weight,  maxValue(i - 1, j - temp.weight));
				map.put(i, temp3);
			}
			else {
				temp3 = map.get(i);
				if (!temp3.containsKey(j - temp.weight)) {
					temp3.put(j - temp.weight,  maxValue(i - 1, j - temp.weight));
					map.put(i, temp3);
				}
			}
			temp1 = temp.value + map.get(i).get(j - temp.weight);
		}
		if (!map.containsKey(i)) {
			temp3.put(j, maxValue(i - 1, j));
			map.put(i, temp3);
		}
		else {
			temp3 = map.get(i);
			if (!temp3.containsKey(j)) {
				temp3.put(j,  maxValue(i - 1, j));
				map.put(i, temp3);
			}
		}
		int temp2 =  map.get(i).get(j);
		if (temp1 > temp2) return temp1;
		else return temp2;
	}
	public int maxValue() {
		values = new int[num + 1][capacity + 1];
		for (int i = 1; i < num + 1; i++) {
			for (int j = 0; j < capacity + 1; j++) {
				Item temp = items.get(i - 1);
				if (temp.weight > j) {
					values[i][j] = values[i - 1][j];
				}
				else {
					int temp1 = temp.value + values[i - 1][j - temp.weight];
					int temp2 = values[i - 1][j];
					if (temp1 > temp2) values[i][j] = temp1;
					else values[i][j] = temp2;
				}
			}
		}
		/*
		ArrayList<Item> added = new ArrayList<Item>();
		int j = capacity;
		for (int i = num; i > 0; i--) {
			if (values[i][j] > values[i - 1][j]) {
				Item temp = items.get(i - 1);
				added.add(temp);
				j -= temp.weight;
			}
		}
		for (Item i : added) {
			System.out.printf("%d %d, ", i.value, i.weight);
		}
		System.out.println();
		*/
		return values[num][capacity];
	}
	public static void main(String[] args) {
		Scanner sc = null;
		try {
			sc = new Scanner(new File("/Users/yulianzhou/Desktop/course/Java/Algorithms Design and Analysis, Part 2/src/knapsack/knapsack_big.txt"));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Knapsack k = new Knapsack(sc);
		System.out.println(k.maxValue(k.num - 1, k.capacity));
		System.out.println(k.maxValue());
		//2493893
		//4243395
	}
}
/*
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class Knapsack {
	private int size;
	private int num;
	private Item[] items;
	private class Item {
		private int weight;
		private int value;
		public Item(Scanner sc) {
			value = sc.nextInt();
			weight = sc.nextInt();
		}
	}
	public Knapsack(Scanner sc) {
		Scanner scanner = new Scanner(sc.nextLine());
		size = scanner.nextInt();
		num = scanner.nextInt();
		items = new Item[num + 1];
		int k = 0;
		while (sc.hasNextInt()) {
			scanner = new Scanner(sc.nextLine());
			items[++k] = new Item(scanner);
		}
	}
	public int maxValue() {
		int[][] knapsack = new int[num + 1][size + 1];
		for (int i = 1; i < num + 1; i++) {
			for (int j = 1; j < size + 1; j++) {
				int exclude_i = knapsack[i - 1][j];
				int include_i = 0;
				int weight_i = items[i].weight;
				if (j >= weight_i) {
					include_i = items[i].value + knapsack[i - 1][j - weight_i];
				}
				if (exclude_i > include_i) knapsack[i][j] = exclude_i;
				else knapsack[i][j] = include_i;
			}
		}
		return knapsack[num][size];
	}
	public int maxValue_big() {
		HashMap<Integer, Integer> knapsack = new HashMap<Integer, Integer>();
		knapsack.put(0,  0);
		for (int i = 1; i < num + 1; i++) {
			int weight_i = items[i].weight;
			int value_i = items[i].value;
			HashMap<Integer, Integer> knapsack_i = new HashMap<Integer, Integer>();
			for (Integer weight : knapsack.keySet()) {
				int weight_temp = weight + weight_i;
				if (weight_temp < size) {
					int value_temp = knapsack.get(weight) + value_i;
					if (knapsack.containsKey(weight_temp)) {
						int value_temp2 = knapsack.get(weight_temp);
						if (value_temp2 > value_temp) continue;
					}
					knapsack_i.put(weight_temp, value_temp);
				}
			}
			knapsack.putAll(knapsack_i);
		}
		int max_value = 0;
		for (Integer weight : knapsack.keySet()) {
			int value = knapsack.get(weight);
			if (value > max_value) max_value = value;
		}
		return max_value;
	}
	public static void main(String[] args) {
		Scanner sc = null;
		try {
			sc = new Scanner(new File("/Users/yulianzhou/Desktop/Course/java/Algs 2.2/knapsack_big.txt"));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Knapsack k = new Knapsack(sc);
		System.out.println(k.maxValue_big());
		//2493893
		//4243395
	}
}

*/