import edu.princeton.cs.algs4.Picture;

import java.awt.Color;

public class SeamCarver {
    private Picture picture;
    private double[][] energyMap;

    public SeamCarver(Picture picture) {
        energyMap = new double[picture.height()][picture.width()];
        for (int i = 0; i < picture.height(); i++) {
            for (int j = 0; j < picture.width(); j++) {
                energyMap[i][j] = energy(i, j);
            }
        }
    }

    public Picture picture() {
        return picture;
    }

    public int width() {
        return picture.width();
    }

    public int height() {
        return picture.height();
    }

    public double energy(int x, int y) {
        if (x * y == 0 || x == width() - 1 || y == height() - 1) {
            return 1000;
        } else {
            Color left = picture.get(x - 1, y);
            Color right = picture.get(x + 1, y);
            Color up = picture.get(x, y - 1);
            Color down = picture.get(x, y + 1);
            double deltaX = Math.pow(right.getRed() - left.getRed(), 2) +
                    Math.pow(right.getGreen() - left.getGreen(), 2) + Math.pow(right.getBlue() - left.getBlue(), 2);
            double deltaY = Math.pow(down.getRed() - up.getRed(), 2) +
                    Math.pow(down.getGreen() - up.getGreen(), 2) + Math.pow(down.getBlue() - up.getBlue(), 2);
            return Math.sqrt(deltaX + deltaY);
        }
    }

    public int[] findHorizontalSeam() {
        return null;
    }

    public int[] findVerticalSeam() {
        return null;
    }

    public void removeHorizontalSeam(int[] seam) {

    }

    public void removeVerticalSeam(int[] seam) {

    }
}
