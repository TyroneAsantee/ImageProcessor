package kth.se.tyronea.hi1027labb4.Controller;

/**
 * Static methods for conversions between an int representing ARGB-values
 * and the separate parts alpha, red, green and blue.
 */
public class PixelConverter {

    /**
     * Return the alpha value, i.e. opacity value, 0..255, from an int representing ARGB.
     */
    public static int getAlpha(int pixelValue) {
        return (pixelValue >> 24) & 0xFF;
    }

    /**
     * Return the red value, 0..255, from an int representing ARGB.
     */
    public static int getRed(int pixelValue) {
        return (pixelValue >> 16) & 0xFF;
    }

    /**
     * Return the green value, 0..255, from an int representing ARGB.
     */
    public static int getGreen(int pixelValue) {
        return (pixelValue >> 8) & 0xFF;
    }

    /**
     * Return the blue value, 0..255, from an int representing ARGB.
     */
    public static int getBlue(int pixelValue) {
        return (pixelValue) & 0xFF;
    }

    /**
     * Create one single int representing an ARGB value. The returned value can represent a pixel
     * in a pixel matrix.
     */
    public static int toArgbPixel(int alpha, int red, int green, int blue) {
        return (alpha << 24) | (red << 16) | (green << 8) | blue;
    }
}
