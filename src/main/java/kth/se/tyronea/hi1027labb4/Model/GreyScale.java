package kth.se.tyronea.hi1027labb4.Model;

import static kth.se.tyronea.hi1027labb4.Controller.PixelConverter.*;

public class GreyScale implements IPixelProcessor{

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
