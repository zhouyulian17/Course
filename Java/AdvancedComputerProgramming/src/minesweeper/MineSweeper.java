package minesweeper;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.Timer;

/*************************************************************************
 * Name: Yulian Zhou
 * Email: zhouyulian17@gmail.com
 *
 * Compilation:  javac MineSweeper.java
 * Execution:    java MineSweeper
 * Dependencies: None
 *
 * Description:  This program implements the game - Minesweeper.
 * https://en.wikipedia.org/wiki/Minesweeper_(video_game)
 *
 *************************************************************************/

public class MineSweeper extends JFrame implements MouseListener, ActionListener, ItemListener {
    
    private int N1 = 9;             // number of rows
    private int N2 = 9;             // number of columns
    private int M = 10;             // number of total mines
    private MineSquare[][] buttons; // buttons[i][j] represents a square at row i and col j
    private int opened;             // total number of opened squares
    private int flagged;            // total number of flagged squares
    private boolean win;            // if win the game, win = true
    private boolean lose;           // if lose the game, lose = true
    private boolean inPlay;         // if the first square is opened, inPlay = true
    private boolean pressed;        // if the a square with number is being pressed
    private int rowPressed;         // the row of the square being pressed
    private int colPressed;         // the column of the square being pressed
    private int currentTime;        // time (sec) lapses after the first open
    
    private JPanel panel;           // top panel with mines left, level choice, restart button and time
    private JLabel minesLeft;       // number of mines left
    private JComboBox choice;       // choice of difficulty level
    private JButton btnStart;       // restart button
    private JLabel time;            // time lapsed
    private Timer timer;            // a timer fires an action event once per sec
    
    private final int SIZE = 20;    // default size of a square
    private final int DELTA = SIZE / 5;    // default offset to center number, mine and flag
    private final Font FONT = new Font("SansSerif", Font.BOLD, 15);    // default font
    private final Dimension DIM = new Dimension(SIZE * 2, SIZE);       // default button dimension
    private final int ALIN = JLabel.CENTER;                            // default alignment
    
    // colors for drawing
    private final Color BLACK = Color.black;
    private final Color GRAY = Color.gray;
    private final Color LIGHTGRAY = Color.lightGray;
    private final Color DARKGRAY = Color.darkGray;
    private final Color RED = Color.red;
    
    /**
     * Initiate a new minesweeper game with default settings.
     */
    public MineSweeper() {
        // set the frame
        setSize(N2 * SIZE + SIZE, N1 * SIZE + 7 * SIZE / 2);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        setTitle("Minesweeper");
        initTopPanel();                   // create the top panel
        initBoard();                      // create the mine board
        timer = new Timer(1000, this);    // create a new timer
        setVisible(true);
    }
    
    // create the top panel and add to the frame.
    private void initTopPanel() {
        panel = new JPanel();
        
        // add a JLabel showing "mines left"
        minesLeft = new JLabel("" + M);
        minesLeft.setFont(FONT);
        minesLeft.setPreferredSize(DIM);
        minesLeft.setHorizontalAlignment(ALIN);
        minesLeft.setForeground(Color.black);
        panel.add(minesLeft);
        
        // add a JComboBox for difficulty level selection
        String[] choices = { "", "Beginner", "Intermediate", "Expert" };
        choice = new JComboBox(choices);
        choice.setPreferredSize(DIM);
        choice.addItemListener(this);
        panel.add(choice);
        
        // add a JButton "restart"
        btnStart = new JButton("^_^");
        btnStart.setFont(FONT);
        btnStart.setPreferredSize(DIM);
        btnStart.setHorizontalAlignment(ALIN);
        btnStart.addActionListener(this);
        panel.add(btnStart);
        
        // add a JLabel showing time lapsed
        time = new JLabel("000");
        time.setFont(FONT);
        time.setPreferredSize(DIM);
        time.setHorizontalAlignment(ALIN);
        time.setBackground(Color.black);
        time.setOpaque(true);
        time.setForeground(Color.red);
        panel.add(time);
        
        // add the top panel to the frame
        add("North", panel);
    }
    
    // create the mine board and add to the frame
    private void initBoard() {
        JPanel minePanel = new JPanel(new GridLayout(N1, N2, 1, 1));
        minePanel.setBackground(LIGHTGRAY);
        
        // create an array of squares (JPanel) and add to the mine panel
        // border is used to handle boundary cases
        buttons = new MineSquare[N1 + 2][N2 + 2];
        for (int i = 0; i < N1 + 2; i++) {
            for (int j = 0; j < N2 + 2; j++) {
                buttons[i][j] = new MineSquare(i, j);
                if (i == 0 || i == N1 + 1 || j == 0 || j == N2 + 1) {
                    buttons[i][j].open();      // open the squares on the border
                }
                else {
                    
                    // add inside squares to the mine panel
                    buttons[i][j].addMouseListener(this);
                    minePanel.add(buttons[i][j]);
                }
            }
        }
        setMine();
        countMine();
        add("Center", minePanel);    // add the mine panel to the frame
    }
    
    // set M mines randomly inside the board
    private void setMine() {
        for (int i = 0; i < M; i++) {
            int ri = (int) (Math.random() * (N1)) + 1;
            int rj = (int) (Math.random() * (N2)) + 1;
            if (!buttons[ri][rj].isMine()) buttons[ri][rj].setMine();
            else i--;
        }
    }
    
    // count the number of adjacent mines for each square (between 0 - 8)
    private void countMine() {
        for (int i = 1; i < N1 + 1; i++) {
            for (int j = 1; j < N2 + 1; j++) {
                if (buttons[i][j].isMine()) {
                    buttons[i][j].setMineCount(-1);
                    continue;
                }
                int k = 0;
                if (buttons[i - 1][j - 1].isMine()) k++;
                if (buttons[i - 1][j].isMine())     k++;
                if (buttons[i - 1][j + 1].isMine()) k++;
                if (buttons[i][j - 1].isMine())     k++;
                if (buttons[i][j + 1].isMine())     k++;
                if (buttons[i + 1][j - 1].isMine()) k++;
                if (buttons[i + 1][j].isMine())     k++;
                if (buttons[i + 1][j + 1].isMine()) k++;
                buttons[i][j].setMineCount(k);
            }
        }
    }
    
    /**
     * This method cannot be called directly (detects level choice and
     * starts a new game).
     */
    @Override
    public void itemStateChanged(ItemEvent e) {
        if (e.getSource() == choice) {
            int level = choice.getSelectedIndex();
            if (level == 0) return;
            setLevel(level);
            restart();
        }
    }
    
    // set the number of rows, cols and mines according to the level choice
    private void setLevel(int level) {
        if (level == 1) {
            N1 = 9;
            N2 = 9;
            M = 10;
        }
        else if (level == 2) {
            N1 = 16;
            N2 = 16;
            M = 40;
        }
        else {
            N1 = 16;
            N2 = 30;
            M = 99;
        }
    }
    
    // restart the game
    private void restart() {
        // clear the field and reset the size
        getContentPane().removeAll();
        setSize(N2 * SIZE + SIZE, N1 * SIZE + 7 * SIZE / 2);
        
        // resets parameters and labels
        opened = 0;
        flagged = 0;
        win = false;
        lose = false;
        inPlay = false;
        currentTime = 0;
        
        minesLeft.setText("" + M);
        btnStart.setText("^_^");
        time.setText("000");
        add("North", panel);              // update the top panel
        timer = new Timer(1000, this);    // create a new timer
        initBoard();                      // create a new mine board
        repaint();                        // repaint the frame
        setVisible(true);
    }
    
    /**
     * This method cannot be called directly (fires upon button and timer
     * clicks).
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        if (!inPlay) return;
        if (e.getSource() == btnStart)  restart();
        if (e.getSource() == timer) {
            currentTime++;
            time.setText(timerText());    // update the time label
        }
    }
    
    // convert timer counter to time text format
    private String timerText() {
        if (currentTime > 1000) return "999";
        String s = "";
        if      (currentTime < 10)  s = "00";
        else if (currentTime < 100) s = "0";
        return s + currentTime;
    }
    
    /**
     * This method cannot be called directly (detects mouse clicks on square).
     */
    @Override
    public void mousePressed(MouseEvent e) {
        if (win == true || lose == true) return;
        MineSquare ms = (MineSquare) e.getSource();
        int i = ms.getRow();
        int j = ms.getCol();
        
        // start the timer after the first click
        if (!inPlay) {
            inPlay = true;
            timer.start();
            currentTime++;
            time.setText("001");
        }
        
        if (e.getButton() == 1) {    // if left mouse button is clicked
            btnStart.setText("'o'");
            leftClick(i, j);
            System.out.printf("You left click at (%d, %d).\n", i, j);
        }
        else if (e.getButton() == 3) { // if right mouse button is clicked
            rightClick(i, j);
            System.out.printf("You right click at (%d, %d).\n", i, j);
        }
        
        // detects win - lose state after the click
        if (win) win();
        else if (lose) lose();
    }
    
    /**
     * This method cannot be called directly (changes the label on the start
     * button after mouse is released).
     */
    @Override
    public void mouseReleased(MouseEvent e) {
        if (win) btnStart.setText("^o^");
        else if (lose) btnStart.setText("*_*");
        else {
            btnStart.setText("^_^");
            if (pressed) {
                pressed = false;
                pressAround(rowPressed, colPressed, pressed);
            }
        }
    }
    
    // perform left click operations on the square at (i, j)
    private void leftClick(int i, int j) {
        if (buttons[i][j].isOpen()) {
            if (buttons[i][j].getFlagCount() == buttons[i][j].getMineCount()) {
                openAround(i, j);
            }
            else {
                pressed = true;
                rowPressed = i;
                colPressed = j;
                pressAround(i, j, pressed);
            }
        }
        else open(i, j);
    }
    
    // open squares around the square at (i, j)
    private void openAround(int i, int j) {
        open(i - 1, j - 1);
        open(i - 1, j);
        open(i - 1, j + 1);
        open(i, j - 1);
        open(i, j + 1);
        open(i + 1, j - 1);
        open(i + 1, j);
        open(i + 1, j + 1);
    }
    
    // open the square at (i, j):
    // (1) If the square is unopened and unflagged, opens the square
    // (2) If the square is mine, state lose = true
    // (3) If all none-mine squares are open, state win = true
    // (4) If the square has no mine around, opens squares around it
    private void open(int i, int j) {
        if (buttons[i][j].isOpen() || buttons[i][j].isFlagged()) return;
        buttons[i][j].open();
        buttons[i][j].repaint();
        if (buttons[i][j].isMine()) {
            buttons[i][j].mist();
            lose = true;
            return;
        }
        opened++;
        if (opened == N1 * N2 - M) {
            win = true;
            return;
        }
        if (buttons[i][j].getMineCount() == 0) openAround(i, j);
    }
    
    // press squares around the square at (i, j)
    private void pressAround(int i, int j, boolean b) {
        updateShadow(i - 1, j - 1, b);
        updateShadow(i - 1, j, b);
        updateShadow(i - 1, j + 1, b);
        updateShadow(i, j - 1, b);
        updateShadow(i, j + 1, b);
        updateShadow(i + 1, j - 1, b);
        updateShadow(i + 1, j, b);
        updateShadow(i + 1, j + 1, b);
    }
    
    // If b = true, draw shadow on the square at (i, j)
    // otherwise, clear shadow on the square at (i, j)
    private void updateShadow(int i, int j, boolean b) {
        if (!buttons[i][j].isOpen() && !buttons[i][j].isFlagged()) {
            buttons[i][j].shadow(b);
            buttons[i][j].repaint();
        }
    }
    
    // perform right click operations on the square at (i, j)
    private void rightClick(int i, int j) {
        if (buttons[i][j].isOpen()) return;
        flag(i, j, !buttons[i][j].isFlagged());
    }
    
    // change the flag status on the square at (i, j)
    // reset the minesLeft label
    private void flag(int i, int j, boolean b) {
        if (b) flagged++;
        else flagged--;
        buttons[i][j].setFlag(b);
        buttons[i][j].repaint();
        minesLeft.setText(Integer.toString(M - flagged));
        updateFlagCount(i, j, b);
    }
    
    // update the flag counts on squares around the square at (i, j)
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
    
    // If b = true, increases flag counts on the square at (i, j) by 1
    // otherwise, decreases flag counts on the square at (i, j) by 1
    private void updateFlag(int i, int j, boolean b) {
        buttons[i][j].updateFlag(b);
    }
    
    // flag all mines and open the rest squares if game is won
    private void win() {
        timer.stop();
        for (int row = 1; row < N1 + 1; row++) {
            for (int col = 1; col < N2 + 1; col++) {
                if (buttons[row][col].isMine()) {
                    buttons[row][col].setFlag(true);
                    buttons[row][col].repaint();
                }
            }
        }
    }
    
    // open all squares with mine if game is lost
    private void lose() {
        timer.stop();
        for (int row = 1; row < N1 + 1; row++) {
            for (int col = 1; col < N2 + 1; col++) {
                if (buttons[row][col].isMine()) {
                    buttons[row][col].open();
                    buttons[row][col].repaint();
                }
            }
        }
    }
    
    // class represents a square on mine board
    private class MineSquare extends JPanel {
        
        private int row;              // row index of the square on the board
        private int col;              // column index of the square on the board
        private boolean mine = false; // if the square contains a mine, mine = true
        private boolean flag = false; // if the square has been flagged, flag = true
        private boolean open = false; // if the square is opened, open = true
        private boolean mist = false; // if the square is a mine and has been opened by player, mist == true
        private boolean shadow = false;
        private int mineCount = 0;    // number of mines around the square
        private int flagCount = 0;    // number of flags around the square
        
        /**
         * Initializes a new square at (i, j).
         */
        public MineSquare(int i, int j) {
            row = i;
            col = j;
        }
        
        public int getRow() { return row; }                // returns row index
        
        public int getCol() { return col; }                // returns col index
        
        public boolean isMine() { return mine; }           // returns true if it contains a mine
        
        public void setMine() { mine = true; }             // set the square with a mine
        
        public boolean isFlagged() { return flag; }        // returns true if it has been flagged
        
        public void setFlag(boolean b) { flag = b; }       // flag or unflag the square
        
        public int getMineCount() { return mineCount; }    // returns number of mines around the square
        
        public void setMineCount(int k) { mineCount = k; } // sets number of mines around the square
        
        public int getFlagCount() { return flagCount; }    // returns number of flags around the square
        
        private void updateFlag(boolean b) {               // update the count of flags around the square
            if (b) flagCount++;
            else   flagCount--;
        }
        
        public boolean isOpen() { return open; }           // returns true if it is open
        
        public void open() { open = true; }                // opens the square
        
        public boolean isMist() { return mist; }           // returns true if it is a mistake (opened mine)
        
        public void mist() { mist = true; }                // sets the square as a mistake
        
        private boolean isShadow() { return shadow; }      // returns true if it has been shadowed
        
        private void shadow(boolean b) { shadow = b; }     // shadow or unshadow the square
        
        /**
         * This method cannot be called directly (invoked by repaint() outside the class).
         */
        @Override
        public void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D drawImage = (Graphics2D) g;
            drawImage.setFont(FONT);
            
            if (isShadow()) {
                drawImage.setColor(DARKGRAY);
                drawImage.fillRect(0, 0, SIZE, SIZE);
                return;
            }
            
            // draw a covered square if unopen
            if (!isOpen()) {
                drawImage.setColor(GRAY);
                drawImage.fillRect(0, 0, SIZE, SIZE);
            }
            
            else if (isMine()) {
                
                // draw a mistake square if a mine has been opened by the player
                if (isMist()) {
                    drawImage.setColor(RED);
                    drawImage.fillRect(0, 0, SIZE, SIZE);
                }
                
                // draw a mine if a mine is open (when game state lose = true)
                drawImage.setColor(BLACK);
                drawImage.fillOval(DELTA, DELTA, SIZE - DELTA * 2, SIZE - DELTA * 2);
            }
            
            // draw mine counts of surrounding squares
            else if (getMineCount() > 0) {
                drawImage.setColor(getNumCol(mineCount));
                drawImage.drawString("" + mineCount, DELTA, SIZE - DELTA);
            }
            
            // draw a flag if square is flagged
            if (flag) {
                int[] x = { DELTA, DELTA, SIZE - DELTA };
                int[] y = { DELTA, SIZE - DELTA, SIZE / 2 };
                drawImage.setColor(RED);
                drawImage.fillPolygon(x, y, 3);
            }
        }
    }
    
    // determine the color of the number label given the number
    private Color getNumCol(int k) {
        switch (k) {
            case 1: return Color.blue;
            case 2: return Color.green;
            case 3: return Color.red;
            case 4: return Color.orange;
            case 5: return Color.magenta;
            case 6: return Color.cyan;
            case 7: return Color.black;
            case 8: return Color.darkGray;
            default: return null;
        }
    }
    
    @Override
    public void mouseClicked(MouseEvent e) { };
    
    @Override
    public void mouseEntered(MouseEvent e) { };
    
    @Override
    public void mouseExited(MouseEvent e) { };
    
    /**
     * Start the minesweeper game.
     */
    public static void main(String[] args) {
        new MineSweeper();
    }
}