import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {

    private final boolean[][] grid;
    private final int n;
    private final WeightedQuickUnionUF quick;
    private final int top, bot;
    private int counter = 0;

    // creates n-by-n grid, with all sites initially blocked
    public Percolation(int n)
    {
        if (n <= 0) {
            throw new IllegalArgumentException("n should be bigger than 0");
        }

        this.n = n;
        // blocked site = 0
        // open site = 1
        this.grid = new boolean[n][n];
        // extra 2 for virtual elements.
        this.quick = new WeightedQuickUnionUF(n * n + 2);

        // virtual elements are created.
        this.top = 0;
        this.bot = n * n + 1;

        if (n > 1)
            for (int i = 1; i < n; i++) {
                this.quick.union(getValue(1, i), top);
                this.quick.union(getValue(n, i), bot);

            }
    }

    // opens the site (row, col) if it is not open already
    public void open(int row, int col)
    {
        if (row <= 0 || col <= 0 || row > n || col > n)
            throw new IllegalArgumentException("Coordinates start from (1,1)");
        if (grid[row - 1][col - 1]) {
            return;
        }
        if (row == 1) {
            this.quick.union(getValue(row, col), top);
        }
        if (row == n) {
            this.quick.union(getValue(row, col), bot);
        }

        grid[row - 1][col - 1] = true;

        if (n == 1) {
            this.quick.union(getValue(row, col), top);
            this.quick.union(getValue(row, col), bot);
        }
        int value = getValue(row, col);
        // union with open neighbor sites.
        neighborUnion(value, row-1, col);
        neighborUnion(value, row, col+1);
        neighborUnion(value, row, col-1);
        neighborUnion(value, row+1, col);

        counter++;
    }
    // union with open neighbor sites.
    private void neighborUnion(int value, int row, int col) {
        if (row <= 0 || col <= 0 || row > n || col > n)
            return;
        // if neighbor is open, then make them union.
        if (isOpen(row, col))
            quick.union(value, getValue(row, col));
    }
    // creating value for specific indexes.
    private int getValue(int row, int col) {
        if (row <= 0 || col <= 0 || row > n || col > n)
            throw new IllegalArgumentException("Coordinates start from (1,1)");
        return n * (row - 1) + col;
    }

    // is the site (row, col) open?
    public boolean isOpen(int row, int col)
    {
        if (row <= 0 || col <= 0 || row > n || col > n) {
            throw new IllegalArgumentException("Coordinates start from (1,1)");
        }

        // open sites are 1, blocked sites are 0.
        return (grid[row - 1][col - 1]);
    }

    // is the site (row, col) full?
    public boolean isFull(int row, int col)
    {
        if (row <= 0 || col <= 0 || row > n || col > n) {
            throw new IllegalArgumentException("Coordinates start from (1,1)");
        }

        // to be full, it has to be open and connected to the top at the same time.
        return grid[row - 1][col - 1] && this.quick.find(getValue(row, col)) == this.quick.find(top);
    }

    // returns the number of open sites
    public int numberOfOpenSites()
    {
        return counter;
    }

    // does the system percolate?
    public boolean percolates() { return quick.find(top) == quick.find(bot); }

}