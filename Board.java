import java.util.ArrayList;
import java.util.List;

public class Board {
    private final boolean isGoal;
    private final int n;
    private int row0, col0;
    private int hamming = 0;
    private int manhattan = 0;
    private final int[][] boards;

    public Board(int[][] boards) {
        n = boards.length;
        this.boards = copyBoards(boards);
        // Calculate Hamming Distance
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (boards[i][j] != 0 && boards[i][j] != i * n + j + 1) {
                    hamming++;
                }
            }
        }
        // Check if this is goal
        isGoal = hamming == 0;
        // Calculate Manhattan distance
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (boards[i][j] != 0) {
                    int cell = boards[i][j] - 1;
                    int column = cell % n;
                    int row = (cell - column) / n;
                    manhattan += Math.abs(row - i) + Math.abs(column - j);
                } else {
                    row0 = i;
                    col0 = j;
                }
            }
        }
    }

    public int dimension() {
        return n;
    }

    public int hamming() {
        return hamming;
    }

    public int manhattan() {
        return manhattan;
    }

    public boolean isGoal() {
        return isGoal;
    }

    public Board twin() {
        int[][] newBoards = new int[n][n];
        for (int i = 0; i < n; i++) {
            System.arraycopy(boards[i], 0, newBoards[i], 0, n);
        }
        int i = 0, j = 0;
        if (newBoards[0][i] == 0) {
            i++;
        } else if (newBoards[1][j] == 0) {
            j++;
        }
        int temp = newBoards[0][i];
        newBoards[0][i] = boards[1][j];
        newBoards[1][j] = temp;
        return new Board(newBoards);
    }

    public boolean equals(Object y) {
        if (y == null) {
            return false;
        }
        if (y.getClass() != this.getClass()) {
            return false;
        }
        return toString().equals(y.toString());
    }

    public Iterable<Board> neighbors() {
        List<Board> res = new ArrayList<>();
        if (row0 > 0) {
            swap(boards, row0 - 1, col0, row0, col0);
            res.add(new Board(boards));
            swap(boards, row0 - 1, col0, row0, col0);
        }
        if (row0 < n - 1) {
            swap(boards, row0 + 1, col0, row0, col0);
            res.add(new Board(boards));
            swap(boards, row0 + 1, col0, row0, col0);
        }
        if (col0 > 0) {
            swap(boards, row0, col0 - 1, row0, col0);
            res.add(new Board(boards));
            swap(boards, row0, col0 - 1, row0, col0);
        }
        if (col0 < n - 1) {
            swap(boards, row0, col0 + 1, row0, col0);
            res.add(new Board(boards));
            swap(boards, row0, col0 + 1, row0, col0);
        }
        return res;
    }

    public String toString() {
        StringBuilder boardBuilder = new StringBuilder();
        boardBuilder.append(n);
        boardBuilder.append("\n");
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                boardBuilder.append(String.format("%6d", boards[i][j]));
            }
            boardBuilder.append("\n");
        }
        return boardBuilder.toString();
    }

    public static void main(String[] args) {
        int[][] b = {{1, 2, 3}, {4, 0, 5}, {6, 7, 8}};
        Board bb = new Board(b);
        Iterable<Board> bs = bb.neighbors();
        for (Board bbb: bs) {
            System.out.println(bbb);
        }
    }

    private void swap(int[][] board, int r1, int c1, int r2, int c2) {
        int temp = board[r1][c1];
        board[r1][c1] = board[r2][c2];
        board[r2][c2] = temp;
    }

    private int[][] copyBoards(int[][] oldBoards) {
        int[][] res = new int[n][n];
        for (int i = 0; i < n; i++) {
            System.arraycopy(oldBoards[i], 0, res[i], 0, n);
        }
        return res;
    }
}
