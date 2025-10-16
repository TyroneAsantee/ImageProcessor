package kth.se.tyronea.hi1027labb4.Model;

import kth.se.tyronea.hi1027labb4.Controller.PixelConverter;
import kth.se.tyronea.hi1027labb4.Model.IPixelProcessor;

import static java.lang.Math.max;
import static java.lang.Math.min;

/**
 * Adjusts image contrast and brightness using the window/level principle.
 * <p>
 * A target intensity range (the "window") centered at a given level is mapped
 * linearly to the full display range [0, 255]. Values below the lower bound
 * become 0 (black), values above the upper bound become 255 (white), and
 * values inside the window are scaled proportionally.
 * </p>
 *
 * <p>
 * The output is a grayscale image where all three color channels (R, G, B)
 * are set to the same adjusted intensity. The alpha channel is preserved.
 * </p>
 */

public class WindowLevel implements IPixelProcessor {
    private final int window;
    private final int level;

    /**
     * Creates a new window/level filter.
     *
     * @param window the width of the intensity window (contrast)
     * @param level  the center of the window (brightness level)
     */
    public WindowLevel(int window, int level){
        this.window = window;
        this.level = level;
    }

    /**
     * Applies window/level mapping to the image.
     * <p>
     * The lower and upper bounds are computed as
     * {@code lower = max(0, level - window/2)} and
     * {@code upper = min(255, level + window/2)}. Each pixel's intensity is
     * derived from the red channel and then mapped to [0, 255] according to
     * the window; the resulting value is used for all RGB channels.
     * </p>
     *
     * @param originalPixels a 2D array of ARGB pixels
     * @return a new 2D array containing the window/level adjusted grayscale image
     */
    @Override
    public int[][] processImage(int[][] originalPixels) {
        int height = originalPixels.length;
        int width = originalPixels[0].length;

        int[][] newPixels = new int[height][width];

        int lower = max(0, level - window/2);
        int upper = min(255, level + window/2);

        for(int y = 0; y < height; y++){
            for(int x = 0; x < width; x++){
                int argb = originalPixels[y][x];
                int alpha = PixelConverter.getAlpha(argb);
                int red = PixelConverter.getRed(argb);
                int green = PixelConverter.getGreen(argb);
                int blue = PixelConverter.getBlue(argb);
                int newIntensity;
                if(red <= lower){
                    newIntensity = 0;
                } else if (red >= upper) {
                    newIntensity = 255;
                } else {
                    newIntensity = (red - lower) * 255 / (upper - lower);
                }

                int newPixel = PixelConverter.toArgbPixel(alpha, newIntensity, newIntensity, newIntensity);
                newPixels[y][x] = newPixel;
            }
        }
        return newPixels;
    }
}