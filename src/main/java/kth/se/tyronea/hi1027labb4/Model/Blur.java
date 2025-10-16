package kth.se.tyronea.hi1027labb4.Model;

import static kth.se.tyronea.hi1027labb4.Controller.PixelConverter.*;

/**
 * Applies a simple Gaussian blur to an ARGB image.
 * <p>
 * The processor convolves the input image with a fixed 3x3 kernel
 * {@code [[1,2,1],[2,4,2],[1,2,1]]} (sum = 16) to softly smooth colors
 * while preserving overall structure. The alpha channel is preserved
 * from the current pixel; only RGB components are blurred.
 * </p>
 *
 * <p>
 * Edges are handled by clamping coordinates to the nearest valid pixel
 * (also known as "edge replicate"). This prevents out-of-bounds access
 * and avoids black borders.
 * </p>
 *
 * <p>
 * Utility methods for ARGB extraction and composition are provided via
 * {@link kth.se.tyronea.hi1027labb4.Controller.PixelConverter} (statically imported).
 * </p>
 */

public class Blur implements IPixelProcessor {

    /**
     * Blurs an image using a 3x3 Gaussian-like kernel.
     * <p>
     * For each pixel, the surrounding 3×3 neighborhood is weighted by
     * {@code [[1,2,1],[2,4,2],[1,2,1]]} and normalized by 16. Out-of-range
     * coordinates are clamped to remain within the image bounds. The resulting
     * red, green, and blue channels are rounded to the nearest integer and
     * recombined with the original pixel's alpha channel.
     * </p>
     *
     * @param originalPixels a non-null, rectangular 2D array of ARGB pixels
     *                       indexed as {@code [row][column]}
     * @return a new 2D array containing the blurred ARGB pixels; input is not modified
     * @implNote The alpha component is taken from the center pixel (no blur on alpha).
     */

    @Override
    public int[][] processImage(int[][] originalPixels){
        int height = originalPixels.length;
        int width  = originalPixels[0].length;
        int[][] out = new int[height][width];

        int[][] w = {
                {1, 2, 1},
                {2, 4, 2},
                {1, 2, 1}
        };

        for (int x = 0; x < width; x++){
            for (int y = 0; y < height; y++){
                double r = 0, g = 0, b = 0;
                int a = getAlpha(originalPixels[y][x]);
                for (int dx = -1; dx <= 1; dx++) {
                    for (int dy = -1; dy <= 1; dy++) {

                        int xx = x + dx;
                        int yy = y + dy;

                        if (xx < 0) xx = 0;
                        if (xx >= width) xx = width - 1;
                        if (yy < 0) yy = 0;
                        if (yy >= height) yy = height - 1;

                        int p = originalPixels[yy][xx];
                        int weight = w[dy + 1][dx + 1];

                        r += getRed(p)   * weight;
                        g += getGreen(p) * weight;
                        b += getBlue(p)  * weight;
                    }
                }

                int red   = (int)Math.round(r / 16.0);
                int green = (int)Math.round(g / 16.0);
                int blue  = (int)Math.round(b / 16.0);

                out[y][x] = toArgbPixel(a, red, green, blue);
            }
        }
        return out;
    }
}
