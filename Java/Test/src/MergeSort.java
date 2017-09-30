import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;


public class MergeSort {
    private static long count;
    public MergeSort() {
    	count = 0;
    }
    public List<Integer> sort(List<Integer> list) {
        int N = list.size();
        if (N == 1) return list;
        List l1 = list.subList(0, N/2);
        List l2 = list.subList(N/2, N); 
        return merge(sort(l1), sort(l2));
    }
    private List merge(List<Integer> l1, List<Integer> l2) {
        int n1 = l1.size();
        int n2 = l2.size();
        int n =  n1 + n2;
        int i = 0;
        int j = 0;
        List<Integer> l12 = new ArrayList<Integer>();
        for(int k = 0; k < n; k++) {
            if(l1.get(i) < l2.get(j)) {
            	
                l12.add(l1.get(i));
                if(i < n1 - 1) i++;
                else {
                    l12.addAll(l2.subList(j, n2));
                    break;
                }
            }
            else {
                l12.add(l2.get(j));
                count += n1 - i;
                if(j < n2 - 1) j++;
                else {
                    l12.addAll(l1.subList(i, n1));
                    break;
                }
            }
        }
        //System.out.printf("%d ", count);
        return l12;        
    }
    public static void main(String[] args) {
        MergeSort ms = new MergeSort();
        List<Integer> list = new ArrayList<Integer>();
        Scanner scanner = null;
		try {
			scanner = new Scanner(new File("/Users/yulianzhou/Desktop/courses backup/java/Algs 2.1/IntegerArray.txt"));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        while(scanner.hasNextLine()) {
            list.add(scanner.nextInt());
        }
        ms.sort(list);
        System.out.println("");
        System.out.println(ms.count);
    }
    
}