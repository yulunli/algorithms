import java.util.Comparator;

public class Point implements Comparable<Point> {
    private int x, y;
    public Point(int x, int y) {

    }

    public void draw() {

    }

    public void drawTo(Point that) {
    }

    @Override
    public String toString() {
        return null;
    }

    public int compareTo(Point that) {
        int y_ = compareInt(this.y, that.y);
        if (y_ == 0) {
            return compareInt(this.x, that.x);
        } else  {
            return y;
        }
    }

    private static int compareInt(int n1, int n2) {
        if (n1 < n2) {
            return -1;
        } else if (n1 > n2) {
            return 1;
        } else {
            return 0;
        }
    }

    public double slopeTo(Point that) {
        if (this.x == that.x) {
            if (this.y == that.y) {
                return Double.NEGATIVE_INFINITY;
            } else {
                return Double.POSITIVE_INFINITY;
            }
        }
        return (that.y - this.y) / (that.x - this.x);
    }

    public Comparator<Point> slopeOrder() {
        return new Comparator<Point>() {
            public int compare(Point o1, Point o2) {
                double slope1 = slopeTo(o1);
                double slope2 = slopeTo(o2);
                if (slope1 < slope2) {
                    return -1;
                } else if (slope1 > slope2) {
                    return 1;
                } else {
                    return 0;
                }
            }
        };
    }
}
