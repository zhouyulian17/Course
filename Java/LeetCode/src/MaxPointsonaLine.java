import java.util.Arrays;
import java.util.Comparator;

class Point {
    int x;
    int y;
	Point() { x = 0; y = 0; }
	Point(int a, int b) { x = a; y = b; }
}
class MaxPointsonaLine {

    public int maxPoints(Point[] points) {
    	if (points.length == 0 || points.length == 1 || points.length == 2) return points.length;
    	Point[] points_temp = new Point[points.length];
    	for (int i = 0; i < points.length; i++) {
    		points_temp[i] = points[i];
    	}
    	
    	int max = 2;
        for (int i = 0; i < points.length; i++) {
        	Point p = points[i];
            Arrays.sort(points_temp, new Comparator<Point>() {
				@Override
				public int compare(Point p1, Point p2) {
					double s1 = slopeTo(p, p1);
					double s2 = slopeTo(p, p2);
					return Double.compare(s1, s2);
				}
            });
            
            Point temp = points_temp[1];
            int count = 2;
            for (int j = 2; j < points.length; j++) {
            	Point temp2 = points_temp[j];
            	if (onLine(p, temp, temp2)) count++;
            	else {
            		if (max < count) max = count;
            		count = 2;
            	}
            	temp = temp2;
            }
    		if (max < count) max = count;
        }
        return max;
    }
    
	private double slopeTo(Point p, Point q) {
		if (p.y == q.y && p.x == q.x) return Double.NEGATIVE_INFINITY;
		if (p.y == q.y) return 0.0;
		if (p.x == q.x) return Double.POSITIVE_INFINITY;
		return (double) (p.y - q.y) / (p.x - q.x);
	}
	
	private boolean onLine(Point p, Point p1, Point p2) {
		return (long) (p.y - p1.y) * (p.x - p2.x) == (long) (p.y - p2.y) * (p.x - p1.x);
	}
	
	public static void main(String[] args) {
		Point p1 = new Point(0, 0);
		Point p2 = new Point(84,250);
		Point p3 = new Point(1,0);
		Point[] points = new Point[3];
		points[0] = p1;
		points[1] = p2;
		points[2] = p3;
		MaxPointsonaLine mpl = new MaxPointsonaLine();
		System.out.println(mpl.maxPoints(points));
	}
}