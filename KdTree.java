import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;

import java.util.ArrayList;
import java.util.List;

public class KdTree {
    private Node root = null;
    private int size;

    public KdTree() {
    }

    public Iterable<Point2D> range(RectHV rect) {
        List<Point2D> res = new ArrayList<>();
        range(root, rect, res);
        return res;
    }
    
    public boolean contains(Point2D p) {
        return contains(root, p);
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public Point2D nearest(Point2D p) {
        if (isEmpty()) {
            return null;
        }
        return nearest(root, p);
    }

    public int size() {
        return size;
    }

    public void draw() {

    }

    public void insert(Point2D p) {
        root = insert(root, p, true);
    }

    private Point2D nearest(Node node, Point2D p) {
        if (node == null) {
            return null;
        } else if (node.point.equals(p)) {
            return node.point;
        } else {
            double nodeDist = node.point.distanceSquaredTo(p);
            double minDistToSquare;
            if (node.isVertical) {
                minDistToSquare = (p.x() - node.point.x()) * (p.x() - node.point.x());
            } else {
                minDistToSquare = (p.y() - node.point.y()) * (p.y() - node.point.y());
            }
            if ((node.isVertical && p.x() < node.point.x()) || (!node.isVertical && p.y() < node.point.y())) {
                Point2D left = nearest(node.left, p);
                if (left != null) {
                    double leftDist = left.distanceSquaredTo(p);
                    if (leftDist < nodeDist && leftDist <= minDistToSquare) {
                        return left;
                    } else {
                        Point2D right = nearest(node.right, p);
                        if (right != null) {
                            double rightDist = right.distanceSquaredTo(p);
                            Point2D res = node.point;
                            if (leftDist < nodeDist) {
                                res = left;
                                nodeDist = leftDist;
                            }
                            if (rightDist < nodeDist) {
                                res = right;
                            }
                            return res;
                        } else {
                            if (leftDist < nodeDist) {
                                return left;
                            } else {
                                return node.point;
                            }
                        }
                    }
                } else {
                    Point2D right = nearest(node.right, p);
                    if (right != null && right.distanceSquaredTo(p) < nodeDist) {
                        return right;
                    } else {
                        return node.point;
                    }
                }
            } else {
                Point2D right = nearest(node.right, p);
                if (right != null) {
                    double rightDist = right.distanceSquaredTo(p);
                    if (rightDist < nodeDist && rightDist <= minDistToSquare) {
                        return right;
                    } else {
                        Point2D left = nearest(node.left, p);
                        if (left != null) {
                            double leftDist = left.distanceSquaredTo(p);
                            Point2D res = node.point;
                            if (leftDist < nodeDist) {
                                res = left;
                                nodeDist = leftDist;
                            }
                            if (rightDist < nodeDist) {
                                res = right;
                            }
                            return res;
                        } else {
                            if (rightDist < nodeDist) {
                                return right;
                            } else {
                                return node.point;
                            }
                        }
                    }
                } else {
                    Point2D left = nearest(node.left, p);
                    if (left != null && left.distanceSquaredTo(p) < nodeDist) {
                        return left;
                    } else {
                        return node.point;
                    }
                }
            }
        }
    }

    private void range(Node node, RectHV rect, List<Point2D> res) {
        if (node != null) {
            if (rect.contains(node.point)) {
                res.add(node.point);
                range(node.left, rect, res);
                range(node.right, rect, res);
            } else {
                if (node.isVertical) {
                    if (rect.xmin() >= node.point.x()) {
                        range(node.right, rect, res);
                    } else if (rect.xmax() < node.point.x()) {
                        range(node.left, rect, res);
                    } else {
                        range(node.left, rect, res);
                        range(node.right, rect, res);
                    }
                } else {
                    if (rect.ymin() >= node.point.y()) {
                        range(node.right, rect, res);
                    } else if (rect.ymax() < node.point.y()) {
                        range(node.left, rect, res);
                    } else {
                        range(node.left, rect, res);
                        range(node.right, rect, res);
                    }
                }
            }
        }
    }

    private boolean contains(Node node, Point2D p) {
        if (node == null) {
            return false;
        } else if (node.point.equals(p)) {
            return true;
        } else {
            boolean res;
            if (node.isVertical) {
                if (p.x() < node.point.x()) {
                    res = contains(node.left, p);
                } else {
                    res = contains(node.right, p);
                }
            } else {
                if (p.y() < node.point.y()) {
                    res = contains(node.left, p);
                } else {
                    res = contains(node.right, p);
                }
            }
            return res;
        }
    }

    private Node insert(Node node, Point2D p, boolean isVertical) {
        if (node == null) {
            size++;
            return new Node(p, isVertical);
        } else if (node.point.equals(p)) {
            return node;
        } else {
            if (node.isVertical) {
                if (p.x() < node.point.x()) {
                    node.left = insert(node.left, p, false);
                } else {
                    node.right = insert(node.right, p, false);
                }
            } else {
                if (p.y() < node.point.y()) {
                    node.left = insert(node.left, p, true);
                } else {
                    node.right = insert(node.right, p, true);
                }
            }
            return node;
        }
    }

    private static class Node {
        private Node left;
        private Node right;
        private Point2D point;
        private boolean isVertical;

        public Node(Point2D p, boolean isVerticalSplit) {
            this.point = p;
            this.isVertical = isVerticalSplit;
        }

        public Point2D getPoint() {
            return point;
        }
    }
}
