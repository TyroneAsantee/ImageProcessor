package kth.se.tyronea.hi1027labb4.Model;

import static kth.se.tyronea.hi1027labb4.Controller.PixelConverter.*;

public class Sharpening implements IPixelProcessor {

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