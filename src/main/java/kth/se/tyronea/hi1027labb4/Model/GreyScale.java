package kth.se.tyronea.hi1027labb4.Model;

import static kth.se.tyronea.hi1027labb4.Controller.PixelConverter.*;

/**
 * Converts a color image to grayscale.
 * <p>
 * For each pixel, the arithmetic mean of its red, green, and blue components
 * is computed and assigned to all three channels. The alpha channel (transparency)
 * is preserved from the original pixel.
 * </p>
 *
 * <p>
 * Helper methods for ARGB extraction and composition are provided by
 * {@link kth.se.tyronea.hi1027labb4.Controller.PixelConverter}.
 * </p>
 */

public class GreyScale implements IPixelProcessor{

    /**
     * Converts an ARGB image to grayscale.
     * <p>
     * The grayscale intensity is computed as {@code round((r + g + b) / 3.0)}.
     * The alpha value is copied unchanged from the original pixel.
     * </p>
     *
     * @param originalPixels a 2D array of ARGB pixels ({@code int}) representing the image
     * @return a new 2D array containing the grayscale pixels; the input is not modified
     * @implNote This method uses a simple arithmetic mean, not luminance weighting.
     */

    @Override
    public int[][] processImage(int[][] originalPixels) {
        int height = originalPixels.length;
        int width  = originalPixels[0].length;
        int[][] out = new int[height][width];
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                int p = originalPixels[y][x];

                int a = getAlpha(p);
                int r = getRed(p);
                int g = getGreen(p);
                int b = getBlue(p);

                int gray = (int)Math.round((r + g + b) / 3.0);

                out[y][x] = toArgbPixel(a, gray, gray, gray);
            }
        }
        return out;

    }
}
