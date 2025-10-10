package kth.se.tyronea.hi1027labb4.Model;

import static kth.se.tyronea.hi1027labb4.Controller.PixelConverter.*;

public class Blur implements IPixelProcessor {

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
