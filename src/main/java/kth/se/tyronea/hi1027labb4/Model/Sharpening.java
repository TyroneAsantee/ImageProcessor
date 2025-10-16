package kth.se.tyronea.hi1027labb4.Model;

import static kth.se.tyronea.hi1027labb4.Controller.PixelConverter.*;

/**
 * Applies a sharpening filter to an image.
 * <p>
 * The method first produces a grayscale version, then a blurred version of
 * that grayscale image, and finally enhances edges using the relation
 * {@code sharpened = 2 * originalGray - blurredGray}. This is a common
 * unsharp mask technique to boost perceived sharpness.
 * </p>
 *
 * <p>
 * The result is clamped to the range 0–255 and emitted as a grayscale image.
 * The alpha channel (transparency) is copied from the original pixel.
 * </p>
 */

public class Sharpening implements IPixelProcessor {

    /**
     * Sharpens the image by enhancing high-frequency details.
     * <p>
     * Steps:
     * <ol>
     *   <li>Convert to grayscale using {@link GreyScale}.</li>
     *   <li>Blur the grayscale image using {@link Blur}.</li>
     *   <li>Combine as {@code 2 * gray - blurred}, then clamp to [0, 255].</li>
     * </ol>
     * </p>
     *
     * @param originalPixels a 2D array of ARGB pixels representing the source image
     * @return a new 2D array containing the sharpened grayscale image
     */

    @Override
    public int[][] processImage(int[][] originalPixels) {
        int height = originalPixels.length;
        int width = originalPixels[0].length;
        int[][] out = new int[height][width];

        GreyScale greyFilter = new GreyScale();
        int[][] grayPixels = greyFilter.processImage(originalPixels);

        Blur blurFilter = new Blur();
        int[][] blurredPixels = blurFilter.processImage(grayPixels);

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int a = getAlpha(originalPixels[y][x]);

                int g0 = getRed(grayPixels[y][x]);
                int gb = getRed(blurredPixels[y][x]);

                int sharp = 2 * g0 - gb;
                if (sharp < 0) sharp = 0;
                if (sharp > 255) sharp = 255;

                out[y][x] = toArgbPixel(a, sharp, sharp, sharp);
            }
        }
        return out;
    }
}