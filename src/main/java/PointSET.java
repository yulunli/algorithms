import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;

public class PointSET {
    private TreeSet<Point2D> points;

    public PointSET() {
        points = new TreeSet<>();
    }

    public boolean isEmpty() {
        return points.isEmpty();
    }

    public int size() {
        return points.size();
    }

    public void insert(Point2D p) {
        points.add(p);
    }

    public boolean contains(Point2D p) {
        return points.contains(p);
    }

    public void draw() {

    }

    public Iterable<Point2D> range(RectHV rect) {
        List<Point2D> res = new ArrayList<>();
        for (Point2D p : points) {
            if (rect.contains(p)) {
                res.add(p);
            }
        }
        return res;
    }

    public Point2D nearest(Point2D p) {
        Point2D res = null;
        if (!isEmpty()) {
            res = points.first();
            double minDist = p.distanceTo(res);
            for (Point2D point : points) {
                double dist = point.distanceTo(p);
                if (dist < minDist) {
                    res = point;
                    minDist = dist;
                }
            }
        }
        return res;
    }

    public static void main(String[] args) {

    }
}
