import java.util.ArrayList;
import java.util.List;

class NQueens {
	
    public List<List<String>> solveNQueens(int n) {
        List<Integer> list = new ArrayList<Integer>();
        List<List<String>> boards = new ArrayList<List<String>>();
        char[] array = new char[n];
        for (int i = 0; i < n; i++) {
            array[i] = '.';
        }
        solveNQueens(boards, array, list, 0, n);
        return boards;
    }
    
    private void solveNQueens(List<List<String>> boards, char[] array, List<Integer> list, int m, int n) {
        if (m == n) {
            List<String> board = new ArrayList<String>();
            for (int i = 0; i < n; i++) {
                int k = list.get(i);
                array[k] = 'Q';
                String s = new String(array);
                board.add(s);
                array[k] = '.';
            }
            boards.add(board);
            return;
        }
        for (int i = 0; i < n; i++) {
            if (isSafe(list, m, i)) {
                list.add(i);
                solveNQueens(boards, array, list, m + 1, n);
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
    
    public static void main(String[] args) {
    	NQueens nq = new NQueens();
    	System.out.println(nq.solveNQueens(4));
    }
}