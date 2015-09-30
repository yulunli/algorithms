import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {
    private int size;
    private int[] status;
    private WeightedQuickUnionUF uf;

    private boolean percolate = false;

    /**
     * create N-by-N grid, with all sites blocked
     * @param N size
     */
    public Percolation(int N) {
        if (N < 1) {
            throw new IllegalArgumentException();
        }
        size = N;
        status = new int[N * N];
        uf = new WeightedQuickUnionUF(N * N + 2);
    }

    /**
     * open site (row i, column j) if it is not open already
     * @param i row
     * @param j column
     */
    public void open(int i, int j) {
        if (i < 1 || i > size || j < 1 || j > size) {
            throw new IndexOutOfBoundsException();
        }
        if (!isOpen(i, j)) {
            int site = (i - 1) * size + (j - 1);
//            open[site] = true;
            // Union top
            if (i == 1) {
//                uf.union(site, vTop);
            } else if (isOpen(i - 1, j)) {
                uf.union(site, (i - 2) * size + (j - 1));
            }
            // Union left
            if (j > 1 && isOpen(i, j - 1)) {
                uf.union(site, (i - 1) * size + (j - 2));
            }
            // Union right
            if (j < size && isOpen(i, j + 1)) {
                uf.union(site, (i - 1) * size + j);
            }
            // Union bottom
//            if (i == size && uf.connected(site, vTop)) {
//                uf.union(site, vBottom);
//            } else if (i < size && isOpen(i + 1, j)) {
//                uf.union(site, i * size + (j - 1));
//            }
        }
    }

    /**
     * is site (row i, column j) open?
     * @param i row
     * @param j column
     */
    public boolean isOpen(int i, int j) {
        if (i < 1 || i > size || j < 1 || j > size) {
            throw new IndexOutOfBoundsException();
        }
        int site = (i - 1) * size + (j - 1);
//        return open[site];
        return false;
    }

    /**
     * is site (row i, column j) full?
     */
    public boolean isFull(int i, int j) {
        if (i < 1 || i > size || j < 1 || j > size) {
            throw new IndexOutOfBoundsException();
        }
        int site = (i - 1) * size + (j - 1);
//        return uf.connected(site, vTop);
        return false;
    }

    /**
     * does the system percolate?
     * @return result
     */
    public boolean percolates() {
        return percolate;
    }

    /**
     * test client
     */
    public static void main(String[] args) {
        Percolation p = new Percolation(StdIn.readInt());
        int numOps = StdIn.readInt();
        for (int i = 0; i < numOps; i++) {
            int row = StdIn.readInt();
            int col = StdIn.readInt();
            p.open(row, col);
        }
        System.out.println(p.percolates());
    }
}
