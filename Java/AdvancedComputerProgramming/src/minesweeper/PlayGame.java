package minesweeper;

/*************************************************************************
 * Name: Yulian Zhou
 * Email: zhouyulian17@gmail.com
 *
 * Compilation:  javac PlayGame.java
 * Execution:    java PlayGame N1 N2 M
 * Dependencies: MineBoard.java, StdDraw.java
 * (StdDraw.java has been modified to implements a timer and detect
 * left/right mouse clicks)
 *
 * Description:  The class takes mouse input and runs the Minesweeper game.
 * https://en.wikipedia.org/wiki/Minesweeper_(video_game)
 *
 *************************************************************************/

public class PlayGame {
    
    private int N1;                 // number of rows
    private int N2;                 // number of columns
    private int M;                  // number of total mines
    private MineBoard board;        // a mine board
    private final int DELAY = 150;  // number of milliseconds delay between detection of two sequential mouse events
    private boolean inPlay;         // if the first square is opened, inPlay = true
    
    /**
     * Starts a new minesweeper game with row = N1, col = N2, mine number = M.
     */
    public PlayGame(int n1, int n2, int m) {
        N1 = n1;
        N2 = n2;
        M = m;
        StdDraw.setCanvasSize(N2 * 32, N1 * 32);
        drawInit();                          // draw the initial mine board
        while (true) {
            if (StdDraw.isMousePressed()) {  // detect if mouse is pressed
                mouseInput();
                drawUpdate();                // draw text with mines left and game status after clicks
            }
        }
    }
    
    // generate a new mine board and draw the board
    private void drawInit() {
        board = new MineBoard(N1, N2, M);
        inPlay = false;
        
        // turns on animation mode
        StdDraw.show(0);
        StdDraw.clear();
        StdDraw.setXscale(-.05 * N2, 1.1 * N2);  // leaves a border to write text
        StdDraw.setYscale(-.05 * N1, 1.1 * N1);
        StdDraw.setPenColor(StdDraw.GRAY);       // draws grid of the board
        StdDraw.filledRectangle(N2 / 2.0, N1 / 2.0, N2 / 2.0, N1 / 2.0);
        StdDraw.setPenColor(StdDraw.BLACK);
        for (int row = 0; row < N2; row++) {
            for (int col = 0; col < N1; col++) {
                StdDraw.square(row + 0.5, col + 0.5, 0.46);
            }
        }
        
        // draws text with mines left, restart button and time lapsed
        StdDraw.setPenColor(StdDraw.BLACK);
        StdDraw.rectangle(0.2 * N2, N1 * 1.05, 1.2, 0.36);
        StdDraw.ellipse(0.5 * N2, N1 * 1.05, 1.2, 0.4);
        StdDraw.rectangle(0.8 * N2, N1 * 1.05, 1.2, 0.36);
        StdDraw.text(0.2 * N2, N1 * 1.05, Integer.toString(M));
        StdDraw.setPenColor(StdDraw.YELLOW);
        StdDraw.filledEllipse(0.5 * N2, N1 * 1.05, 1.18, 0.38);
        StdDraw.setPenColor(StdDraw.BLACK);
        StdDraw.text(0.5 * N2, N1 * 1.05, "New?");
        StdDraw.resetTimer(0.8 * N2, N1 * 1.05, 1.2, 0.36);
        StdDraw.drawTimer();
        StdDraw.show();
    }
    
    // detect mouse input and update the mine board
    private void mouseInput() {
        double x = StdDraw.mouseX();
        double y = StdDraw.mouseY();
        
        // k = 0, if mouse click is within the start button (an eclipse)
        int k = (int) ((x - 0.5 * N2) * (x - 0.5 * N2) / 1.18 / 1.18 +
                       (y - 1.05 * N1) * (y - 1.05 * N1) / 0.38 / 0.38);
        if (k == 0) drawInit();
        
        // do not update the board if board is in the win or lose state
        if (board.isWin() || board.isLose()) return;
        
        // convert to row i, column j
        int i = (int) Math.floor(y) + 1;
        int j = (int) Math.floor(x) + 1;
        
        if (i > 0 && i < N1 + 1 && j > 0 && j < N2 + 1) {
            
            // start the timer after the first click
            if (!inPlay) {
                inPlay = true;
                StdDraw.startTimer();
            }
            
            // if left mouse button is clicked
            if (StdDraw.isLeftClick()) {
                StdDraw.setPenColor(StdDraw.YELLOW);
                StdDraw.filledEllipse(0.5 * N2, 1.05 * N1, 1.18, 0.38);
                leftClick(i, j);
                System.out.printf("You left click at (%d, %d).\n", i, j);
            }
            
            // if right mouse button is clicked
            else if (StdDraw.isRightClick()) {
                rightClick(i, j);
                System.out.printf("You right click at (%d, %d).\n", i, j);
            }
            
        }
    }
    
    // draw updated game status
    private void drawUpdate() {
        StdDraw.show(DELAY);
        
        // clear the previous drawing
        StdDraw.setPenColor(StdDraw.WHITE);
        StdDraw.filledRectangle(0.2 * N2, N1 * 1.05, 1.2, 0.44);
        StdDraw.filledEllipse(0.5 * N2, N1 * 1.05, 1.2, 0.44);
        
        // draw new mines left count
        StdDraw.setPenColor(StdDraw.BLACK);
        StdDraw.rectangle(0.2 * N2, N1 * 1.05, 1.2, 0.36);
        StdDraw.text(0.2 * N2, N1 * 1.05, Integer.toString(board.getMineNum() - board.getFlagged()));
        
        // draw start button with game status
        StdDraw.ellipse(0.5 * N2, N1 * 1.05, 1.2, 0.4);
        String s;
        if (board.isLose()) {
            StdDraw.setPenColor(StdDraw.BOOK_RED);
            s = "Lose";
        }
        else if (board.isWin()) {
            StdDraw.setPenColor(StdDraw.GREEN);
            s = "Win!";
        }
        else {
            StdDraw.setPenColor(StdDraw.YELLOW);
            s = "New?";
        }
        StdDraw.filledEllipse(0.5 * N2, 1.05 * N1, 1.18, 0.38);
        StdDraw.setPenColor(StdDraw.BLACK);
        StdDraw.text(0.5 * N2, 1.05 * N1, s);
        StdDraw.show();
    }
    
    // perform left click operations on the cell at (i, j)
    // detect win - lose state after the click
    private void leftClick(int i, int j) {
        if (board.isOpen(i, j)) {
            if (board.getFlagCount(i, j) == board.getMineCount(i, j)) {
                board.openAround(i, j);
            }
        }
        else board.open(i, j);
        if (board.isWin()) win(board);
        if (board.isLose()) lose(board);
    }
    
    // perform right click operations on the cell at (i, j)
    private void rightClick(int i, int j) {
        if (board.isOpen(i, j)) return;
        if (!board.isFlagged(i, j)) board.flag(i, j);
        else board.unflag(i, j);
    }
    
    // flag all mines if game is won
    private void win(MineBoard board) {
        StdDraw.stopTimer();
        for (int row = 1; row < N1 + 1; row++) {
            for (int col = 1; col < N2 + 1; col++) {
                if (board.isMine(row, col) && !board.isFlagged(row, col)) {
                    board.drawFlag(row, col);
                }
            }
        }
    }
    
    // open all squares containing mine if game is lost
    private void lose(MineBoard board) {
        StdDraw.stopTimer();
        for (int row = 1; row < N1 + 1; row++) {
            for (int col = 1; col < N2 + 1; col++) {
                if (board.isMine(row, col)) {
                    board.drawOpen(row, col);
                    if (board.isOpen(row, col)) board.drawMist(row, col);
                    board.drawMine(row, col);
                    if (board.isFlagged(row, col)) board.drawFlag(row, col);
                }
            }
        }
    }
    
    /**
     * Play the minesweeper game.
     */
    public static void main(String[] args) {
        int N1 = Integer.parseInt(args[0]);
        int N2 = Integer.parseInt(args[1]);
        int M = Integer.parseInt(args[2]);
        new PlayGame(N1, N2, M);
    }
}