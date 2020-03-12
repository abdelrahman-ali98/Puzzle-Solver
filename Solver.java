/* *****************************************************************************
 *  Name: Abdelrahman Ali
 *  Date: March 11, 2020
 *  Description: Solve n*n-1 puzzle using A* Algorithm (using Piority Queues)
 * 1 < n <= 128
 **************************************************************************** */

import edu.princeton.cs.algs4.MinPQ;

import java.util.ArrayList;
import java.util.Collections;

public class Solver {

    private SearchNode result;
    private int moves;
    private boolean isSolvable;

    // If the initial board is solvable, hence all neighbors are unsolvable
    // If the initial board is unsolvable, hence all neighbors are solvable

    // Our strategy is to solve the initial board in parallel to a twin
    // Once we reach a solution, We check if it is the intial board or a Twin...


    // find a solution to the initial board (using the A* algorithm)
    public Solver(Board initial) {

        if (initial == null)
            throw new IllegalArgumentException();

        MinPQ<SearchNode> minPQ = new MinPQ<SearchNode>();
        minPQ.insert(new SearchNode(null, initial));

        MinPQ<SearchNode> minPQTwin = new MinPQ<SearchNode>();
        minPQTwin.insert(new SearchNode(null, initial.twin()));

        while (true) {

            SearchNode searchNode = minPQ.delMin();
            SearchNode searchNodeTwin = minPQTwin.delMin();

            // We consider the goal when it dequeued not enqueued according to A* Algorithm
            if (searchNode.board.isGoal()) {
                result = searchNode;
                moves = result.moves;
                isSolvable = true;
                return;
            }
            else if (searchNodeTwin.board.isGoal()) {
                isSolvable = false;

                return;
            }
            for (Board board : searchNode.board.neighbors())
                // make sure the previous node is not null before calling it in the next if statement
                if (searchNode.previous == null)
                    minPQ.insert(new SearchNode(searchNode, board));
                else if (!board.equals(searchNode.previous.board))
                    minPQ.insert(new SearchNode(searchNode, board));

            for (Board board : searchNodeTwin.board.neighbors())
                if (searchNodeTwin.previous == null)
                    minPQTwin.insert(new SearchNode(searchNodeTwin, board));
                else if (!board.equals(searchNodeTwin.previous.board))
                    minPQTwin.insert(new SearchNode(searchNodeTwin, board));

        }

    }

    // is the initial board solvable?
    public boolean isSolvable() {
        return isSolvable;
    }

    // min number of moves to solve initial board
    public int moves() {
        return isSolvable ? moves : -1;
    }

    // sequence of boards in a shortest solution
    public Iterable<Board> solution() {

        if (!isSolvable) return null;
        ArrayList<Board> finalResult = new ArrayList<Board>();

        // We initiate a refernce to the solution node
        // to keep track the root, in case several calls
        SearchNode node = result;
        while (node != null) {
            finalResult.add(node.board);
            node = node.previous;
        }

        // the list contains the boards in reversed order
        // since the Nodes refer to the previous node , not the next node
        Collections.reverse(finalResult);
        return finalResult;
    }

    // test client (see below)
    public static void main(String[] args) {

        // simple test case
        int[][] c = {
                { 4, 2, 3 },
                { 1, 6, 5 },
                { 8, 7, 0 }
        };

        Solver x = new Solver(new Board(c));

        for (Board borad : x.solution())
            System.out.println(borad.toString());

    }

    private class SearchNode implements Comparable<SearchNode> {

        private final SearchNode previous;
        private final Board board;

        // number of moves so far to reach this board
        private final int moves;
        private final int piority;

        public SearchNode(SearchNode previous, Board board) {
            this.previous = previous;
            this.board = board;
            if (previous != null)
                moves = previous.moves + 1;
            else moves = 0;
            this.piority = moves + board.manhattan();
        }

        public int compareTo(SearchNode searchNode) {
            return this.piority - searchNode.piority;
        }

    }
}
