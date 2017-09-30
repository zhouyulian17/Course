import java.util.ArrayList;
import java.util.List;

public class NQueens2 {
	
    private int count = 0;
    
    public int totalNQueens(int n) {
        List<Integer> list = new ArrayList<Integer>();
        solveNQueens(list, 0, n);
        return count;
    }
    
    private void solveNQueens(List<Integer> list, int m, int n) {
        if (m == n) {
            count++;
            return;
        }
        for (int i = 0; i < n; i++) {
                if (isSafe(list, m, i)) {
                    list.add(i);
                    solveNQueens(list, m + 1, n);
                    list.remove(list.size() - 1);
                }
            }
    }
    
    private boolean isSafe(List<Integer> l, int m, int i) {
        for (int j = 0; j < m; j++) {
            int k = l.get(j);
            if (k == i || k - (m - j) == i || k + (m - j) == i) return false;
        }
        return true;
    }
}
