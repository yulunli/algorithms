import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BruteCollinearPoints {
    private LineSegment[] segments;

    public BruteCollinearPoints(Point[] points) {
        Arrays.sort(points);
        for (int i = 0; i < points.length - 1; i++) {
            if (points[i].compareTo(points[i + 1]) == 0) {
                throw new IllegalArgumentException();
            }
        }
        List<LineSegment> lines = new ArrayList<>();
        for (int i = 0; i < points.length - 3; i++) {
            Point p = points[i];
            for (int j = i + 1; j < points.length - 2; j++) {
                Point q = points[j];
                double pq = p.slopeTo(q);
                for (int m = j + 1; m < points.length - 1; m++) {
                    Point r = points[m];
                    double pr = p.slopeTo(r);
                    if (pq == pr) {
                        for (int n = m + 1; n < points.length; n++) {
                            Point s = points[n];
                            double ps = p.slopeTo(s);
                            if (pq == ps) {
                                LineSegment line = new LineSegment(p, s);
                                lines.add(line);
                            }
                        }
                    }
                }
            }
        }
        segments = lines.toArray(new LineSegment[lines.size()]);
    }

    public int numberOfSegments() {
        return segments.length;
    }

    public LineSegment[] segments() {
        return segments.clone();
    }
}
