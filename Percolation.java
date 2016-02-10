import java.util.Arrays;

import edu.princeton.cs.algs4.StdIn;

public class Percolation {
    private int n; // width/height of the grid
    private int[] parent;
    private int[] size;
    private boolean[] isOpen;
    private boolean[] isFull;
    private boolean percolate = false;

    /**
     * create N-by-N grid, with all sites blocked
     * @param N size
     */
    public Percolation(int n) {
        if (n < 1) {
            throw new IllegalArgumentException();
        }
        this.n = n;
        int count = n * n;
        parent = new int[count];
        size = new int[count];
        isOpen = new boolean[count];
        isFull = new boolean[count];
        Arrays.fill(size, 1);
        for (int i = 0; i < n * n; i++) {
            parent[i] = i;
        }
    }

    /**
     * open site (row i, column j) if it is not open already
     */
    public void open(int i, int j) {
        validate(i, j);
        if (!isOpen(i, j)) {
            int site = getSite(i, j);
            isOpen[site] = true;
            if (i == 1) {
                isFull[site] = true;
            }
            // Union top
            if (i > 1 && isOpen(i - 1, j)) {
                union(site, getSite(i - 1, j));
            }
            // Union left
            if (j > 1 && isOpen(i, j - 1)) {
                union(site, getSite(i, j - 1));
            }
            // Union right
            if (j < n && isOpen(i, j + 1)) {
                union(site, getSite(i, j + 1));
            }
            // Union bottom
            if (i < n && isOpen(i + 1, j)) {
                union(site, getSite(i + 1, j));
            }
        }
    }

    /**
     * is site (row i, column j) open?
     */
    public boolean isOpen(int i, int j) {
        validate(i, j);
        return isOpen[getSite(i, j)];
    }

    /**
     * is site (row i, column j) full?
     */
    public boolean isFull(int i, int j) {
        validate(i, j);
        return isFull[find(getSite(i, j))];
    }

    /**
     * does the system percolate?
     */
    public boolean percolates() {
        if (!percolate) {
            for (int j = 1; j < n + 1; j++) {
                if (isFull(n, j)) {
                    percolate = true;
                    break;
                }
            }
        }
        return percolate;
    }

    /**
     * test client
     */
    public static void main(String[] args) {
        Percolation p = new Percolation(StdIn.readInt());
        while (!StdIn.isEmpty()) {
            int row = StdIn.readInt();
            int col = StdIn.readInt();
            p.open(row, col);
        }
        System.out.println(p.percolates());
    }

    private int getSite(int i, int j) {
        return (i - 1) * n + (j - 1);
    }

    private void validate(int i, int j) {
        if (i < 1 || i > n || j < 1 || j > n) {
            throw new IndexOutOfBoundsException();
        }
    }

    private int find(int p) {
        while (p != parent[p]) {
            p = parent[p];
        }
        return p;
    }

    private void union(int a, int b) {
        int rootA = find(a);
        int rootB = find(b);
        if (size[rootA] < size[rootB]) {
            merge(rootB, rootA);
        } else {
            merge(rootA, rootB);
        }
    }

    // Merge b into a
    private void merge(int a, int b) {
        parent[b] = parent[a];
        size[a] += size[b];
        if (isFull[b]) {
            isFull[a] = true;
        }
    }
}
