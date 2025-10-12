package kth.se.tyronea.hi1027labb4.Model;

import kth.se.tyronea.hi1027labb4.Controller.PixelConverter;
import kth.se.tyronea.hi1027labb4.Model.IPixelProcessor;

import static java.lang.Math.max;
import static java.lang.Math.min;

public class WindowLevel implements IPixelProcessor {
    private final int window;
    private final int level;

    public WindowLevel(int window, int level){
        this.window = window;
        this.level = level;
    }

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