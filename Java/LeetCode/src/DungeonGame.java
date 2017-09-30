class DungeonGame {

    public int calculateMinimumHP(int[][] dungeon) {
        if (dungeon.length == 0 || dungeon[0].length == 0) return 0;
        int m = dungeon.length;
        int n = dungeon[0].length;
        int[][] hp = new int[m][n];
        hp[m - 1][n - 1] = (dungeon[m - 1][n - 1] > -1) ? 1 : 1-dungeon[m - 1][n - 1];
        //System.out.printf("%d %d %d\n", m - 1, n - 1, hp[m - 1][n - 1]);
        for (int j = n - 2; j > -1; j--) {
            int k = dungeon[m - 1][j] - hp[m - 1][j + 1];
            hp[m - 1][j] = (k > -1) ? 1 : -k;
            //System.out.printf("%d %d %d\n", m - 1, j, hp[m - 1][j]);
        }
        for (int i = m - 2; i > -1; i--) {
            int k = dungeon[i][n - 1] - hp[i + 1][n - 1];
            hp[i][n - 1] = (k > -1) ? 1 : -k;
            //System.out.printf("%d %d %d\n", i, n - 1, hp[i][n - 1]);
            for (int j = n - 2; j > - 1; j--) {
                k = (hp[i + 1][j] > hp[i][j + 1]) ? hp[i][j + 1] : hp[i + 1][j];
                k = dungeon[i][j] - k;
                hp[i][j] = (k > -1) ? 1 : -k;
            }
        }
        return (hp[0][0] > 0) ? hp[0][0] : 1;
    }
    
    public static void main(String[] args) {
    	int[][] board = {{1 , -2, 3}, {2, -2, -2}};
    	DungeonGame dg = new DungeonGame();
    	System.out.println(dg.calculateMinimumHP(board));
    }
}