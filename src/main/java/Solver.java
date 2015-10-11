import edu.princeton.cs.algs4.MinPQ;

import java.util.ArrayList;
import java.util.List;

public class Solver {
    private boolean solvable;
    private int moves = -1;
    private Iterable<Board> solution;

    public Solver(Board initial) {
        MinPQ<Node> q = new MinPQ<>();
        MinPQ<Node> twinQ = new MinPQ<>();
        Node root = new Node(initial, null, 0);
        Node twinRoot = new Node(initial.twin(), null, 0);
        q.insert(root);
        twinQ.insert(twinRoot);
        while (!root.board().isGoal() && !twinRoot.board().isGoal()) {
            for (Board neighbor : root.board().neighbors()) {
                if (root.parent() == null || (root.parent() != null && !neighbor.equals(root.parent().board()))) {
                    q.insert(new Node(neighbor, root, root.moves() + 1));
                }
            }
            if (!q.isEmpty()) {
                root = q.delMin();
            }
            for (Board twinNeighbor : twinRoot.board().neighbors()) {
                if (twinRoot.parent() == null ||
                        (twinRoot.parent() != null && !twinNeighbor.equals(twinRoot.parent().board()))) {
                    twinQ.insert(new Node(twinNeighbor, twinRoot, twinRoot.moves() + 1));
                }
            }
            if (!twinQ.isEmpty()) {
                twinRoot = twinQ.delMin();
            }
        }
        solvable = root.board().isGoal();
        if (solvable) {
            moves = root.moves;
            List<Board> track = new ArrayList<>();
            while (root != null) {
                track.add(0, root.board());
                root = root.parent();
            }
            solution = track;
        }
    }

    public boolean isSolvable() {
        return solvable;
    }

    public int moves() {
        return moves;
    }

    public Iterable<Board> solution() {
        return solution;
    }

    public static void main(String[] args) {
        int[][] b = {{1, 2, 3}, {4, 5, 6}, {0, 7, 8}};
        Board bb = new Board(b);
        Solver s = new Solver(bb);
        System.out.println(s.moves());
        for (Board bs : s.solution()) {
            System.out.println(bs);
        }
    }

    private static class Node implements Comparable<Node> {
        private int moves, priority;
        private Board board;
        private Node parent;

        public Node(Board board, Node parent, int moves) {
            this.moves = moves;
            this.board = board;
            this.parent = parent;
            this.priority = board.manhattan() + moves;
        }

        public int moves() {
            return moves;
        }

        public int priority() {
            return priority;
        }

        public Board board() {
            return board;
        }

        public Node parent() {
            return parent;
        }

        @Override
        public int compareTo(Node o) {
            return Integer.compare(this.priority(), o.priority());
        }
    }
}
