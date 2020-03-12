/* *****************************************************************************
 *  Name: Abdelrahman Ali
 *  Date: March 11, 2020
 *  Description: solve puzzle
 **************************************************************************** */

import edu.princeton.cs.algs4.Queue;

public class Board {

    private final int n;
    private final int[][] myTiles;

    private final int calculatedManhhaten;
    private final int calculatedHamming;

    // create a board from an n-by-n array of tiles,
    // where tiles[row][col] = tile at (row, col)
    public Board(int[][] tiles) {
        this.n = tiles.length;
        myTiles = new int[tiles.length][tiles.length];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++)
                myTiles[i][j] = tiles[i][j];
        }

        // Calculate Manhhaten and Hamming for this board
        // and store them in order to Time Complexity
        calculatedManhhaten = getManhattan();
        calculatedHamming = getHamming();
    }

    // string representation of this board
    public String toString() {
        StringBuilder s = new StringBuilder();
        s.append(n + "\n");
        for (byte i = 0; i < n; i++) {
            for (byte j = 0; j < n; j++) {
                s.append(String.format("%2d ", myTiles[i][j]));
            }
            s.append("\n");
        }
        return s.toString();
    }

    // board dimension n
    public int dimension() {
        return n;
    }

    public int hamming() {
        return this.calculatedHamming;
    }

    // number of tiles out of place
    private int getHamming() {
        int count = -1;
        for (byte i = 0; i < n; i++)
            for (byte j = 0; j < n; j++)
                if (myTiles[i][j] != i * n + j + 1)
                    count++;

        return count;
    }

    public int manhattan() {
        return this.calculatedManhhaten;
    }

    // sum of Manhattan distances between tiles and goal
    private int getManhattan() {
        int count = 0;
        for (byte i = 0; i < n; i++)
            for (byte j = 0; j < n; j++)
                if (myTiles[i][j] != 0) {
                    int value = i * n + j + 1;
                    if (value != myTiles[i][j]) {
                        int row = (myTiles[i][j] - 1) / n;
                        int colum = (myTiles[i][j] - row * n) - 1;
                        count += Math.abs(row - i) + Math.abs(colum - j);
                    }

                }

        return count;
    }

    // is this board the goal board?
    public boolean isGoal() {
        return hamming() == 0;
    }

    // does this board equal y?
    public boolean equals(Object y) {
        if (y == this)
            return true;
        if (y == null)
            return false;
        if (y.getClass() != this.getClass())
            return false;
        Board that = (Board) y;

        if (this.dimension() != that.dimension())
            return false;

        for (int i = 0; i < myTiles.length; i++)
            for (int j = 0; j < myTiles.length; j++)
                if (that.myTiles[i][j] != this.myTiles[i][j])
                    return false;
        return true;
    }

    // all neighboring boards
    public Iterable<Board> neighbors() {

        byte[] indices = new byte[2];

        // pass array by reference to get the indices of the Zer0 position
        // hence We get the indices of the neighbor and do a move
        initiateIndices(indices);
        byte i = indices[0];
        byte j = indices[1];
        Queue<Board> output = new Queue<Board>();


        if (i - 1 >= 0) addBoard(output, i, j, i - 1, j);
        if (j - 1 >= 0) addBoard(output, i, j, i, j - 1);
        if (i + 1 < n) addBoard(output, i, j, i + 1, j);
        if (j + 1 < n) addBoard(output, i, j, i, j + 1);

        return output;
    }

    private void initiateIndices(byte[] output) {

        for (byte i = 0; i < myTiles.length; i++)
            for (byte j = 0; j < myTiles[i].length; j++) {
                if (myTiles[i][j] == 0) {
                    output[0] = i;
                    output[1] = j;
                    return;
                }
            }

    }


    // add a neighbor board
    private void addBoard(Queue<Board> output, int i, int j, int ii, int jj) {
        int[][] neighborBoard = new int[n][n];

        for (byte x = 0; x < n; x++)
            for (byte xx = 0; xx < n; xx++)
                neighborBoard[x][xx] = myTiles[x][xx];

        int temp = neighborBoard[i][j];
        neighborBoard[i][j] = neighborBoard[ii][jj];
        neighborBoard[ii][jj] = temp;
        // I need to check before adding that the borad is not repeated
        output.enqueue(new Board(neighborBoard));
    }

    // a board that is obtained by exchanging any pair of tiles
    public Board twin() {

        int[][] output = new int[this.n][this.n];
        for (byte i = 0; i < n; i++) {
            for (byte j = 0; j < n; j++) {
                output[i][j] = this.myTiles[i][j];
            }
        }

        // Check if one of them is not a tile (Zer0)
        if (output[0][1] != 0 && output[0][0] != 0) {
            int temp = output[0][1];
            output[0][1] = output[0][0];
            output[0][0] = temp;
        }

        else if (output[1][0] != 0 && output[0][0] != 0) {
            int temp = output[1][0];
            output[1][0] = output[0][0];
            output[0][0] = temp;
        }
        // Now, We are sure that the Zer0 is at position [0][0]
        else {
            int temp = output[1][0];
            output[1][0] = output[0][1];
            output[0][1] = temp;
        }
        return new Board(output);
    }


    // unit testing (not graded)
    public static void main(String[] args) {

        int[][] c = {
                { 2, 1, 3 },
                { 4, 6, 5 },
                { 7, 8, 0 }
        };
        Board x = new Board(c);


        for (Board borad : x.neighbors()) {
            System.out.println(borad.toString());
        }
        System.out.println("manhattan is: " + x.manhattan());
        System.out.println("hamming is:  " + x.hamming());
    }

}

// This is a test comment. I'm practicing with Git!
