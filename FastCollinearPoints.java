import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FastCollinearPoints {
    private LineSegment[] segments;

    public FastCollinearPoints(Point[] points) {
        List<LineSegment> lines = new ArrayList<>();
        Arrays.sort(points);
        for (int i = 0; i < points.length - 1; i++) {
            if (points[i].compareTo(points[i + 1]) == 0) {
                throw new IllegalArgumentException();
            }
        }
        for (int i = 0; i < points.length; i++) {
            Point p = points[i];
            Point[] others = new Point[points.length - 1];
            System.arraycopy(points, 0, others, 0, i);
            System.arraycopy(points, i + 1, others, i, others.length - i);
            Arrays.sort(others, p.slopeOrder());
            int start = 0, end = 1;
            while (end < others.length) {
                boolean equalSlope = p.slopeTo(others[end]) == p.slopeTo(others[end - 1]);
                if (equalSlope) {
                    end++;
                }
                if (!equalSlope || end == others.length) {
                    if (end - start >= 3) {
                        Point[] segment = new Point[end - start + 1];
                        segment[segment.length - 1] = p;
                        for (int k = 0; k < segment.length - 1; k++) {
                            segment[k] = others[start + k];
                        }
                        Arrays.sort(segment);
                        LineSegment line = new LineSegment(segment[0], segment[segment.length - 1]);
                        boolean added = false;
                        for (LineSegment l : lines) {
                            if (l.toString().equals(line.toString())) {
                                added = true;
                                break;
                            }
                        }
                        if (!added) {
                            lines.add(line);
                        }
                    }
                    start = end;
                    end = start + 1;
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
