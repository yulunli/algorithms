import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.Picture;
import edu.princeton.cs.algs4.Topological;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

public class SeamCarver {
    private static final int RED_MASK = 0xFF0000, GREEN_MASK = 0xFF00, BLUE_MASK = 0xFF;
    private List<List<Integer>> colorMatrix = new ArrayList<>();

    public SeamCarver(Picture picture) {
        for (int row = 0; row < picture.height(); row++) {
            colorMatrix.add(new ArrayList<>());
            for (int col = 0; col < picture.width(); col++) {
                Color color = picture.get(col, row);
                colorMatrix.get(row).add(encodeColor(color));
            }
        }
    }

    public Picture picture() {
        Picture pic = new Picture(width(), height());
        for (int row = 0; row < height(); row++) {
            for (int col = 0; col < width(); col++) {
                pic.set(col, row, new Color(colorMatrix.get(row).get(col)));
            }
        }
        return pic;
    }

    public int width() {
        return colorMatrix.get(0).size();
    }

    public int height() {
        return colorMatrix.size();
    }

    public double energy(int col, int row) {
        if (col < 0 || col >= width() || row < 0 || row >= height()) {
            throw new java.lang.IndexOutOfBoundsException();
        } else if (row * col == 0 || col == width() - 1 || row == height() - 1) {
            return 1000;
        } else {
            int left = colorMatrix.get(row).get(col - 1);
            int right = colorMatrix.get(row).get(col + 1);
            int up = colorMatrix.get(row - 1).get(col);
            int down = colorMatrix.get(row + 1).get(col);
            return getDualGradientEnergy(left, right, up, down);
        }
    }

    public int[] findHorizontalSeam() {
        int innerGraphSize = width() * height();
        Digraph graph = new Digraph(innerGraphSize + 2);
        for (int col = 0; col < width(); col++) {
            for (int row = 0; row < height(); row++) {
                if (col == 0) {
                    graph.addEdge(0, row * width() + 1);
                }
                int pos = row * width() + col + 1;
                if (col < width() - 1) {
                    if (row > 0) {
                        graph.addEdge(pos, pos - width() + 1);
                    }
                    graph.addEdge(pos, pos + 1);
                    if (row < height() - 1) {
                        graph.addEdge(pos, pos + width() + 1);
                    }
                } else {
                    graph.addEdge(pos, innerGraphSize + 1);
                }
            }
        }
        int[] seam = findSeam(graph, width());
        int[] rowPositions = new int[width()];
        for (int col = 0; col < width(); col++) {
            rowPositions[col] = (seam[col] - 1) / width();
        }
        return rowPositions;
    }

    public int[] findVerticalSeam() {
        int innerGraphSize = width() * height();
        Digraph graph = new Digraph(innerGraphSize + 2);
        for (int row = 0; row < height(); row++) {
            for (int col = 0; col < width(); col++) {
                if (row == 0) {
                    graph.addEdge(0, col + 1);
                }
                int pos = row * width() + col + 1;
                if (row < height() - 1) {
                    if (col > 0) {
                        graph.addEdge(pos, pos + width() - 1);
                    }
                    graph.addEdge(pos, pos + width());
                    if (col < width() - 1) {
                        graph.addEdge(pos, pos + width() + 1);
                    }
                } else {
                    graph.addEdge(pos, innerGraphSize + 1);
                }
            }
        }
        int[] seam = findSeam(graph, height());
        int[] colPositions = new int[height()];
        for (int row = 0; row < height(); row++) {
            colPositions[row] = (seam[row] - 1) % width();
        }
        return colPositions;
    }

    public void removeHorizontalSeam(int[] seam) {
        if (height() < 2 || seam.length != width()) {
            throw new IllegalArgumentException();
        }
        int prevPixel = seam[0];
        for (int pixel : seam) {
            if (pixel < 0 || pixel >= height() || Math.abs(pixel - prevPixel) > 1) {
                throw new IllegalArgumentException("" + pixel);
            }
            prevPixel = pixel;
        }
        for (int col = 0; col < width(); col++) {
            int rowPos = seam[col];
            for (int row = rowPos + 1; row < height(); row++) {
                colorMatrix.get(row - 1).set(col, colorMatrix.get(row).get(col));
            }
        }
        colorMatrix.remove(colorMatrix.size() - 1);
    }

    public void removeVerticalSeam(int[] seam) {
        if (width() < 2 || seam.length != height()) {
            throw new IllegalArgumentException();
        }
        int prevPixel = seam[0];
        for (int pixel : seam) {
            if (pixel < 0 || pixel >= width() || Math.abs(pixel - prevPixel) > 1) {
                throw new IllegalArgumentException();
            }
            prevPixel = pixel;
        }
        for (int row = 0; row < seam.length; row++) {
            int col = seam[row];
            colorMatrix.get(row).remove(col);
        }
    }

    public static void main(String[] args) {
        Picture pic = new Picture("/home/yulun/algorithms/seamCarving/10x12.png");
        SeamCarver carver = new SeamCarver(pic);
        int[] seam = carver.findVerticalSeam();
        for (int i = 0; i < seam.length; i++) {
            System.out.println(seam[i]);
        }
        carver.removeVerticalSeam(carver.findVerticalSeam());
    }

    private int[] findSeam(Digraph graph, int simSize) {
        double[] distTo = new double[graph.V()];
        for (int i = 1; i < distTo.length; i++) {
            distTo[i] = Double.POSITIVE_INFINITY;
        }
        int[] vertexTo = new int[graph.V()];
        Topological topological = new Topological(graph);
        for (int from : topological.order()) {
            for (int to : graph.adj(from))
                relax(from, to, distTo, vertexTo);
        }
        int[] seam = new int[simSize];
        int tail = graph.V() - 1;
        for (int i = simSize - 1; i > -1; i--) {
            seam[i] = vertexTo[tail];
            tail = vertexTo[tail];
        }
        return seam;
    }

    private void relax(int from, int to, double[] distTo, int[] vertexTo) {
        int picSize = width() * height();
        double weight;
        if (to == picSize + 1) {
            weight = 0;
        } else {
            int col = (to - 1) % width();
            int row = (to - 1) / width();
            weight = energy(col, row);
        }
        if (distTo[to] > distTo[from] + weight) {
            distTo[to] = distTo[from] + weight;
            vertexTo[to] = from;
        }
    }

    private static double getDualGradientEnergy(int left, int right, int up, int down) {
        double deltaX = getDelta(left, right);
        double deltaY = getDelta(up, down);
        return Math.sqrt(deltaX + deltaY);
    }

    private static double getDelta(int c1, int c2) {
        double deltaR = getRed(c1) - getRed(c2);
        double deltaG = getGreen(c1) - getGreen(c2);
        double deltaB = getBlue(c1) - getBlue(c2);
        return deltaR * deltaR + deltaG * deltaG + deltaB * deltaB;
    }

    private static int encodeColor(Color color) {
        return (color.getRed() << 16) + (color.getGreen() << 8) + color.getBlue();
    }

    private static int getBlue(int color) {
        return color & BLUE_MASK;
    }

    private static int getGreen(int color) {
        return (color & GREEN_MASK) >> 8;
    }

    private static int getRed(int color) {
        return (color & RED_MASK) >> 16;
    }
}
