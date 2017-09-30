import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;

public class TSP2 {
	private double length;
	private List<City> cities;
	private class City {
		private double x;
		private double y;
		private int id;
		public City(int n, double d1, double d2) {
			id = n;
			x = d1;
			y = d2;
		}
		public double x() { return x; }
		public double y() { return y; }
		public int id() { return id; }
		public double distTo(City other) {
			return Math.sqrt((x - other.x()) * (x - other.x()) + (y - other.y()) * (y - other.y()));
		}
	}
	
	public TSP2(String file) {
		Scanner scanner = null;
		try {
			scanner = new Scanner(new File(file));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		int V = scanner.nextInt();
		cities = new ArrayList<City>();
		for (int i = 0; i < V; i++) {
			City city = new City(scanner.nextInt(), scanner.nextDouble(), scanner.nextDouble());
			cities.add(city);
		}
		City start = cities.get(0);
		for (int i = 0; i < V - 1; i++) {
			//System.out.println(cities.get(0).id);
			Collections.sort(cities, new Comparator<City>() {
				@Override
				public int compare(City o1, City o2) {
					double d1 = o1.distTo(cities.get(0));
					double d2 = o2.distTo(cities.get(0));
					if (d1 == d2) return o1.id() - o2.id();
					if (d1 > d2) return 1;
					return -1;
				}
			});
			length += cities.get(0).distTo(cities.get(1));
			cities.set(0, cities.get(1));
			cities.set(1, cities.get(cities.size() - 1));
			cities.remove(cities.size() - 1);
		}
		System.out.printf("%d %d %f\n",cities.size(), start.id(), cities.get(0).distTo(start));
		length += cities.get(0).distTo(start);
		
	}
	private double length() { return length; }
	public static void main(String[] args) {
		TSP2 tsp = new TSP2(args[0]);
		System.out.println(tsp.length());
	}
}
