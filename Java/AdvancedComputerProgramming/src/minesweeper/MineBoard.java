package minesweeper;

/*************************************************************************
 * Name: Yulian Zhou
 * Email: zhouyulian17@gmail.com
 *
 * Compilation:  javac MineBoard.java
 * Execution:    java MineBoard
 * Dependencies: StdDraw.java
 *
 * Description:  The class represents a mine board.
 *
 *************************************************************************/

public class MineBoard {
    
    private int[] mine;         // cell i contains a mine if mine[i] = 1, otherwise mine[i] = 0
    private int[] mineCount;    // number of mines around cell i
    private int[] flag;         // cell i is flagged if flag[i] = 1, otherwise flag[i] = 0
    private int[] flagCount;    // number of flags around cell i
    private boolean[] open;     // cell i is open if open[i] = true, otherwise open[i] = false
    
    private int n1;             // number of rows + 2
    private int n2;             // number of columns + 2
    private int m;              // total number of mines
    private int flagged;        // number of flags
    private int opened;         // number of open cells
    private boolean win;        // if win the game, win = true
    private boolean lose;       // if lose the game, lose = true
    
    /**
     * Constructs a (N1 + 2) by (N2 + 2) grid with M mines within [1..N1+1][1..N2+1].
     */
    public MineBoard(int N1, int N2, int M) {
        
        // construct a (row + 2)-by-(col + 2) mine board represented in 1D array
        // border is used to handle boundary cases
        if (N1 < 0 || N2 < 1 || M < 0)
        throw new IllegalArgumentException();
        n1 = N1 + 2;
        n2 = N2 + 2;
        m = M;
        int size = n1 * n2;
        flag = new int[size];
        flagCount = new int[size];
        mine = new int[size];
        mineCount = new int[size];
        setMine();
        countMine();
        open = new boolean[size];
        
        // open the cells on the border
        for (int i = 0; i < n1; i++) {
            open[i * n2] = true;
            open[i * n2 + n2 - 1] = true;
        }
        for (int j = 1; j < n2 - 1; j++) {
            open[j] = true;
            open[(n1 - 1) * n2 + j] = true;
        }
    }
    
    // set M mines randomly inside the board
    private void setMine() {
        for (int i = 0; i < m; i++) {
            int r1 = (int) (Math.random() * (n1 - 2)) + 1;
            int r2 = (int) (Math.random() * (n2 - 2)) + 1;
            int n = xyTo1D(r1, r2);
            if (mine[n] == 0) mine[n] = 1;
            else i--;
        }
    }
    
    // count the number of adjacent mines for each cell (between 0 - 8)
    private void countMine() {
        for (int i = 1; i < n1 - 1; i++) {
            for (int j = 1; j < n2 - 1; j++) {
                if (isMine(i, j)) {
                    setMineCount(i, j, -1);
                    continue;
                }
                int k = mine[xyTo1D(i - 1, j - 1)];
                k += mine[xyTo1D(i - 1, j)];
                k += mine[xyTo1D(i - 1, j + 1)];
                k += mine[xyTo1D(i, j - 1)];
                k += mine[xyTo1D(i, j + 1)];
                k += mine[xyTo1D(i + 1, j - 1)];
                k += mine[xyTo1D(i + 1, j)];
                k += mine[xyTo1D(i + 1, j + 1)];
                setMineCount(i, j, k);
            }
        }
    }
    
    /**
     * Opens the cell at (i, j):
     * (1) If the cell is unopened and unflagged, opens the cell.
     * (2) If the cell is mine, state lose = true.
     * (3) If all none-mine cells are open, state win = true.
     * (4) If the cell has no mine around, opens cells around it.
     */
    public void open(int i, int j) {
        if (isOpen(i, j) || isFlagged(i, j)) return;
        open[xyTo1D(i, j)] = true;
        drawOpen(i, j);
        if (isMine(i, j)) {
            lose = true;
            return;
        }
        opened++;
        if (opened == (n1 - 2) * (n2 - 2) - m) {
            win = true;
            return;
        }
        if (getMineCount(i, j) == 0) openAround(i, j);
    }
    
    /**
     * Opens cells around the cell at (i, j).
     */
    public void openAround(int i, int j) {
        open(i - 1, j - 1);
        open(i - 1, j);
        open(i - 1, j + 1);
        open(i, j - 1);
        open(i, j + 1);
        open(i + 1, j - 1);
        open(i + 1, j);
        open(i + 1, j + 1);
    }
    
    /**
     * Flags the cell at (i, j), and draws a flag on the cell.
     */
    public void flag(int i, int j) {
        flag[xyTo1D(i, j)] = 1;
        flagged++;
        drawFlag(i, j);
        updateFlagCount(i, j, true);
    }
    
    /**
     * Unflags the cell at (i, j), and remove the flag from the cell.
     */
    public void unflag(int i, int j) {
        flag[xyTo1D(i, j)] = 0;
        flagged--;
        clearFlag(i, j);
        updateFlagCount(i, j, false);
    }
    
    // update the flag counts on cells around the cell at (i, j)
    private void updateFlagCount(int i, int j, boolean b) {
        updateFlag(i - 1, j - 1, b);
        updateFlag(i - 1, j, b);
        updateFlag(i - 1, j + 1, b);
        updateFlag(i, j - 1, b);
        updateFlag(i, j + 1, b);
        updateFlag(i + 1, j - 1, b);
        updateFlag(i + 1, j, b);
        updateFlag(i + 1, j + 1, b);
    }
    
    // updates the count of flags around the cell at (i, j)
    private void updateFlag(int i, int j, boolean b) {
        if (b) flagCount[xyTo1D(i, j)]++;
        else flagCount[xyTo1D(i, j)]--;
    }
    
    // convert 2D index (i, j) into 1D index
    private int xyTo1D(int i, int j) {
        return i * n2 + j;
    }
    
    /**
     * Returns true if the cell at (i, j) contains a mine.
     */
    public boolean isMine(int i, int j) {
        return mine[xyTo1D(i, j)] == 1;
    }
    
    /**
     * Returns the number of mines surrounding the cell at (i, j).
     */
    public int getMineCount(int i, int j) {
        return mineCount[xyTo1D(i, j)];
    }
    
    /**
     * Sets the number of mines surrounding the cell at (i, j).
     */
    public void setMineCount(int i, int j,  int k) {
        mineCount[xyTo1D(i, j)] = k;
    }
    
    /**
     * Returns true if the cell at (i, j) contains a flag.
     */
    public boolean isFlagged(int i, int j) {
        return flag[xyTo1D(i, j)] == 1;
    }
    
    /**
     * Returns the number of flags surrounding the cell at (i, j).
     */
    public int getFlagCount(int i, int j) {
        return flagCount[xyTo1D(i, j)];
    }
    
    /**
     * Returns true if the cell at (i, j) is open.
     */
    public boolean isOpen(int i, int j) {
        return open[xyTo1D(i, j)];
    }
    
    /**
     * Returns the total number of flags on board.
     */
    public int getFlagged() {
        return flagged;
    }
    
    /**
     * Returns the total number of mines on board.
     */
    public int getMineNum() {
        return m;
    }
    
    /**
     * Returns true if game is won.
     */
    public boolean isWin() {
        return win;
    }
    
    /**
     * Returns true if game is lost.
     */
    public boolean isLose() {
        return lose;
    }
    
    /**
     * Draws a open cell at (i, j) showing mineCount number.
     */
    public void drawOpen(int i, int j) {
        StdDraw.setPenColor(StdDraw.WHITE);
        StdDraw.filledSquare(j - 0.5, i - 0.5, 0.46);
        int k = getMineCount(i, j);
        if (k != 0) {
            StdDraw.setPenColor(StdDraw.BLACK);
            StdDraw.text(j - 0.5, i - 0.5, Integer.toString(k));
        }
    }
    
    /**
     * Draws a blue flag on the cell at (i, j).
     */
    public void drawFlag(int i, int j) {
        StdDraw.setPenColor(StdDraw.BOOK_BLUE);
        double[] x = {j - 0.8, j - 0.8, j - 0.1};
        double[] y = {i - 0.8, i - 0.2, i - 0.5};
        StdDraw.filledPolygon(x, y);
    }
    
    /**
     * Removes the flag from the cell at (i, j).
     */
    public void clearFlag(int i, int j) {
        StdDraw.setPenColor(StdDraw.GRAY);
        StdDraw.filledSquare(j - 0.5, i - 0.5, 0.4);
    }
    
    /**
     * Draws the cell at (i, j) indicating a mistake open (open mine).
     */
    public void drawMist(int i, int j) {
        StdDraw.setPenColor(StdDraw.RED);
        StdDraw.filledSquare(j - 0.5, i - 0.5, 0.46);
    }
    
    /**
     * Draws a black mine on the cell at (i, j).
     */
    public void drawMine(int i, int j) {
        StdDraw.setPenColor(StdDraw.BLACK);
        StdDraw.filledCircle(j - 0.5, i - 0.5, 0.4);
    }
    
    public static void main(String[] args) {
        
        // unit testing
        MineBoard ms = new MineBoard(9, 9, 10);
        for (int i = 0; i < 11; i++) {
            for (int j = 0; j < 11; j++) {
                if (ms.isMine(i, j)) System.out.printf("%2d ", 1);
                else System.out.printf("%2d ", 0);
            }
            System.out.println();
        }
        System.out.println();
        for (int i = 0; i < 11; i++) {
            for (int j = 0; j < 11; j++) {
                //if (!ms.isOpen(i, j))
                System.out.printf("%2d ", ms.getMineCount(i, j));
            }
            System.out.println();
        }
    }
}