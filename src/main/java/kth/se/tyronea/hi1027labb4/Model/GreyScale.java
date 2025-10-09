package kth.se.tyronea.hi1027labb4.Model;

import static kth.se.tyronea.hi1027labb4.Controller.PixelConverter.*;

public class GreyScale implements IPixelProcessor{

    @Override
    public int[][] processImage(int[][] originalPixels) {
        int width = originalPixels.length;
        int height = originalPixels[0].length;
        int[][] out = new int[width][height];
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                int p = originalPixels[x][y];

                int a = getAlpha(p);
                int r = getRed(p);
                int g = getGreen(p);
                int b = getBlue(p);

                int gray = (int)Math.round(0.299 * r + 0.587 * g + 0.114 * b);

                out[x][y] = toArgbPixel(a, gray, gray, gray);
            }
        }
        return out;

    }
}
