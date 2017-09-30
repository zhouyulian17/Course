package seamCarving;

import java.awt.Color;
import edu.princeton.cs.algs4.Picture;

/*************************************************************************
 * Name: Yulian Zhou
 * Email: zhouyulian17@gmail.com
 *
 * Compilation:  javac SeamCarver.java
 * Execution:    none
 * Dependencies: Picture.java
 *
 * Description:  A data type that resizes a W-by-H image using the
 *               seam-carving technique.
 * http://coursera.cs.princeton.edu/algs4/assignments/seamCarving.html
 *
 *************************************************************************/

public class SeamCarver {
    
    private Picture P;       // the original picture
    private int[][] pixels;  // pixels[i][j] = index of pixel at (i, j) in P
    private int w;           // width of current picture
    private int h;           // height of current picture
    
    /**
     * Creates a seam carver object based on the given picture.
     */
    public SeamCarver(Picture picture) {
        P = new Picture(picture);
        w = P.width();
        h = P.height();
        pixels =  new int[w][h];
        for (int i = 0; i < w; i++) {
            for (int j = 0; j < h; j++) {
                pixels[i][j] = i + j * w;
            }
        }
    }
    
    /**
     * Returns current picture.
     */
    public Picture picture() {
        Picture currentPic = new Picture(w, h);
        for (int i = 0; i < w; i++) {
            for (int j = 0; j < h; j++) {
                int k = pixels[i][j];
                currentPic.set(i, j, P.get(k % P.width(), k / P.width()));
            }
        }
        return currentPic;
    }
    
    /**
     * Returns width of current picture.
     */
    public int width() { return w; }
    
    /**
     * Returns height of current picture.
     */
    public int height() { return h; }
    
    /**
     * Returns energy of pixel at column x and row y.
     */
    public double energy(int x, int y) {
        
        // throw exception if index is out of bound
        if (x < 0 || x > w - 1 || y < 0 || y > h - 1) {
            throw new IndexOutOfBoundsException();
        }
        
        // border pixels have energy 1000
        if (x == 0 || x == w - 1 || y == 0 || y == h - 1) return 1000;
        
        // energy at pixel (x, y) is the square root differences in red, green, and blue components
        // between pixel (x + 1, y) and (x - 1, y), and between pixel (x, y - 1) and (x, y + 1)
        int x1 = pixels[x - 1][y];
        int x2 = pixels[x + 1][y];
        int y1 = pixels[x][y - 1];
        int y2 = pixels[x][y + 1];
        Color cx1 = P.get(x1 % P.width(), x1 / P.width());
        Color cx2 = P.get(x2 % P.width(), x2 / P.width());
        Color cy1 = P.get(y1 % P.width(), y1 / P.width());
        Color cy2 = P.get(y2 % P.width(), y2 / P.width());
        double d = 0;
        d += (cx1.getRed() - cx2.getRed()) * (cx1.getRed() - cx2.getRed());
        d += (cx1.getGreen() - cx2.getGreen()) * (cx1.getGreen() - cx2.getGreen());
        d += (cx1.getBlue() - cx2.getBlue()) * (cx1.getBlue() - cx2.getBlue());
        d += (cy1.getRed() - cy2.getRed()) * (cy1.getRed() - cy2.getRed());
        d += (cy1.getGreen() - cy2.getGreen()) * (cy1.getGreen() - cy2.getGreen());
        d += (cy1.getBlue() - cy2.getBlue()) * (cy1.getBlue() - cy2.getBlue());
        return Math.sqrt(d);
    }
    
    /**
     *
     * Returns a sequence of indices for horizontal seam.
     */
    public int[] findHorizontalSeam() { return findSeam(false); }
    
    /**
     * Returns a sequence of indices for vertical seam.
     */
    public int[] findVerticalSeam() { return findSeam(true); }
    
    // find the sequence of indices with minimal energy seam
    private int[] findSeam(boolean transposed) {
        int width;
        int height;
        if (transposed) {
            width = h;
            height = w;
        }
        else {
            width = w;
            height = h;
        }
        
        // initialize edgeTo and energy matrix to record path and energy to pixel (i, j)
        int[][] edgeFrom = new int[width][height];
        double[][] energy = new double[width][height];
        for (int j = 0; j < height; j++) {
            edgeFrom[0][j] = j;
            energy[0][j] = 1000;
        }
        for (int i = 1; i < width; i++) {
            for (int j = 0; j < height; j++) {
                
                // minimal energy sum of previous pixel
                double e1 = Double.POSITIVE_INFINITY;
                if (j > 0) e1 = energy[i - 1][j - 1];
                double e3 = Double.POSITIVE_INFINITY;
                if (j < height - 1) e3 = energy[i - 1][j + 1];
                double e = energy[i - 1][j];
                int edge = j;
                if (e1 < e) {
                    e = e1;
                    edge = j - 1;
                }
                if (e3 < e) {
                    e = e3;
                    edge = j + 1;
                }
                
                // minimal energy sum of and path to current pixel
                energy[i][j] = e + energy(i, j, transposed);
                edgeFrom[i][j] = edge;
            }
        }
        
        // minimal energy sum of pixels in last row
        double min = Double.POSITIVE_INFINITY;
        int k = -1;
        for (int j = 0; j < height; j++) {
            if (min > energy[width - 1][j]) {
                min = energy[width - 1][j];
                k = j;
            }
        }
        
        // minimum energy path
        int[] seam = new int[width];
        seam[width - 1] = k;
        for (int i = width - 2; i > -1; i--) {
            seam[i] = edgeFrom[i + 1][k];
            k = seam[i];
        }
        return seam;
    }
    
    // energy at pixel (x, y) in either original or transposed picture.
    private double energy(int x, int y, boolean transposed) {
        if (transposed) return energy(y, x);
        return energy(x, y);
    }
    
    /**
     * Removes horizontal seam from current picture.
     */
    public void removeHorizontalSeam(int[] seam) {
        valid(seam, false);
        int[][] newPixels = new int[w][h - 1];
        for (int i = 0; i < w; i++) {
            for (int j = 0; j < h - 1; j++) {
                if (j < seam[i]) newPixels[i][j] = pixels[i][j];
                else newPixels[i][j] = pixels[i][j + 1];
            }
        }
        pixels = newPixels;
        h--;
    }
    
    /**
     * Remove vertical seam from current picture.
     */
    public void removeVerticalSeam(int[] seam) {
        valid(seam, true);
        int[][] newPixels = new int[w - 1][h];
        for (int i = 0; i < w - 1; i++) {
            for (int j = 0; j < h; j++) {
                if (i < seam[j]) newPixels[i][j] = pixels[i][j];
                else newPixels[i][j] = pixels[i + 1][j];
            }
        }
        pixels = newPixels;
        w--;
    }
    
    // is it seam[] a valid seam?
    private void valid(int[] seam, boolean transposed) {
        int width;
        int height;
        if (transposed) {
            width = h;
            height = w;
        }
        else {
            width = w;
            height = h;
        }
        if (seam.length != width) throw new IllegalArgumentException();
        int k = seam[0];
        if (k < 0 || k > height - 1) throw new IllegalArgumentException();
        for (int j = 1; j < width; j++) {
            
            // invalid if out of bound
            if (seam[j] < 0 || seam[j] > height - 1)  throw new IllegalArgumentException();
            
            // invalid if not adjacent
            if (seam[j] > k + 1 || seam[j] < k - 1) throw new IllegalArgumentException();
            k = seam[j];
        }
    }
}