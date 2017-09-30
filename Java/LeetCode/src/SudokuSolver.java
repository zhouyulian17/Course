
public class SudokuSolver {
	
	boolean solvable = false;
    public void solveSudoku(char[][] board) {
        solveSudoku(board, 0);
    }
    
    private void solveSudoku(char[][] board, int k) {
    	int row = k / board.length;
    	if (row == board.length) {
    		solvable = true;
    		return;
    	}
    	int col = k % board.length;
    	if (board[row][col] == '.') {
        	for (int i = 1; i < 10; i++) {
        		board[row][col] = (char) (i + '0');
        		if (isValid(board, row, col)) {
        			solveSudoku(board, k + 1);
        			if (solvable) return;
        		}
        	}
        	board[row][col] = '.';
    	}
    	else solveSudoku(board, k + 1);
    }
    
    private boolean isValid(char[][] board, int row, int col) {
    	for (int i = 0; i < col; i++) {
    		if (board[row][i] == board[row][col]) return false;
    	}
    	for (int i = col + 1; i < board.length; i++) {
    		if (board[row][i] == board[row][col]) return false;
    	}
    	for (int i = 0; i < row; i++) {
    		if (board[i][col] == board[row][col]) return false;
    	}
    	for (int i = row + 1; i < board.length; i++) {
    		if (board[i][col] == board[row][col]) return false;
    	}
    	int r = row / 3;
    	int c = col / 3;
    	for (int i = 0; i < 3; i++) {
    		for (int j = 0; j < 3; j++) {
    			int nrow = r*3 + i;
    			int ncol = c*3 + j;
    			if (!(row == nrow && col == ncol) && board[row][col] == board[nrow][ncol]) {
    				return false;
    			}
    		}
    	}
    	return true;
    }
    
    public static void main(String[] args) {
    	char[][] board = {{'5','3','.','.','7','.','.','.','.'}, 
    					  {'6','.','.','1','9','5','.','.','.'}, 
    					  {'.','9','8','.','.','.','.','6','.'}, 
    					  {'8','.','.','.','6','.','.','.','3'}, 
    					  {'4','.','.','8','.','3','.','.','1'}, 
    					  {'7','.','.','.','2','.','.','.','6'}, 
    					  {'.','6','.','.','.','.','2','8','.'}, 
    					  {'.','.','.','4','1','9','.','.','5'}, 
    					  {'.','.','.','.','8','.','.','7','9'}};
    	SudokuSolver ss = new SudokuSolver();
    	ss.solveSudoku(board);
    	for (int i = 0; i < board.length; i++) {
    		for (int j = 0; j < board.length; j++) {
    			System.out.printf("%c ", board[i][j]);
    		}
    		System.out.println();
    	}
    }
}
