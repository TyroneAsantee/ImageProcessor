package kth.se.tyronea.hi1027labb4.Model;

import kth.se.tyronea.hi1027labb4.Controller.PixelConverter;

/**
 * Facade class that exposes the model's core interface to the rest of the system.
 * <p>
 * This class acts as a <em>facade</em> in an MVC architecture: it encapsulates
 * internal pixel storage, image filters, and processing details, and provides a
 * clear API to load, process, restore, and analyze images without exposing
 * implementation details.
 * </p>
 *
 * <p>
 * Image data is stored as two-dimensional arrays of ARGB pixels ({@code int[][]}),
 * with separate arrays for the original and the current (processed) image. Filters
 * can be applied through the {@link IPixelProcessor} interface.
 * </p>
 */

public class ImageModel {

    private int[][] originalPixels;
    private int[][] currentPixels;
    private int width, height;
    private boolean isLoaded;

    /**
     * Creates an empty image model (facade) with no image loaded.
     * <p>
     * {@link #isLoaded()} remains {@code false} until {@link #loadFromPixels(int[][])}
     * is called.
     * </p>
     */

    public ImageModel(){
        this.isLoaded = false;
    }

    /**
     * Loads an image from a 2D pixel array.
     * <p>
     * Validates that the array is non-null, non-empty, and rectangular. Both
     * the original and current arrays are initialized and populated with copies
     * of the provided pixel data.
     * </p>
     *
     * @param pixels a 2D array of ARGB pixels
     * @throws IllegalArgumentException if the array is null, empty, or ragged
     */

    public void loadFromPixels(int [][] pixels){
        if(pixels == null || pixels.length == 0) throw new IllegalArgumentException("Image is null or empty");
        height = pixels.length;
        width = pixels[0].length;
        for(int[] row : pixels){
            if(row.length != width){
                throw new IllegalArgumentException("Rows not the same length");
            }
        }
        originalPixels = new int[height][width];
        currentPixels  = new int[height][width];
        for(int y = 0; y < height; y++){
            for (int x = 0; x < width; x++) {
                originalPixels[y][x] = pixels[y][x];
                currentPixels[y][x] = pixels[y][x];
            }
        }
        isLoaded = true;
    }

    /**
     * Returns a defensive copy of the current (processed) image.
     *
     * @return a copy of {@code currentPixels}
     * @throws IllegalStateException if no image is loaded
     */
    public int[][] getCurrentPixels() {
        if(!isLoaded) throw new IllegalStateException("No image loaded");
        int[][] copy = new int[height][width];
        for(int y = 0; y < height; y++){
            for (int x = 0; x < width; x++){
                copy[y][x] = currentPixels[y][x];
            }
        }
        return copy;
    }

    /**
     * Returns a defensive copy of the original image.
     *
     * @return a copy of {@code originalPixels}
     * @throws IllegalStateException if no image is loaded
     */

    public int[][] getOriginalPixels() {
        if(!isLoaded) throw new IllegalStateException("No image loaded");
        int[][] copy = new int[height][width];
        for(int y = 0; y < height; y++){
            for(int x = 0; x < width; x++){
                copy[y][x] = originalPixels[y][x];
            }
        }
        return copy;
    }

    /**
     * Replaces the current image with the provided pixel array.
     *
     * @param currentPixels a 2D array of ARGB pixels
     */

    public void setCurrentPixels(int[][] currentPixels) {
        this.currentPixels = currentPixels;
    }

    /**
     * Resets the current image to the original image.
     *
     * @throws IllegalStateException if no image is loaded
     */
    public void resetToOriginal(){
        if(!isLoaded) throw new IllegalStateException("No image loaded");
        for(int y = 0; y < height; y++){
            for(int x = 0; x < width; x++){
                currentPixels[y][x] = originalPixels[y][x];
            }
        }
    }

    /**
     * Computes a color histogram for the current image.
     * <p>
     * Returns a 256×3 array where {@code freq[i][0]} is the count of red-channel
     * pixels with intensity {@code i}, {@code freq[i][1]} is green, and
     * {@code freq[i][2]} is blue.
     * </p>
     *
     * @return a 256×3 frequency array, or {@code null} if no image is present
     */

    public int[][] getHistogramForCurrentPixels(){
        if(currentPixels == null) return null;
        int[][] freq = new int[256][3];
        for(int y = 0; y < height; y++){
            for(int x = 0; x < width; x++){
                int argb = currentPixels[y][x];
                int red = PixelConverter.getRed(argb);
                int green = PixelConverter.getGreen(argb);
                int blue = PixelConverter.getBlue(argb);

                freq[red][0]++;
                freq[green][1]++;
                freq[blue][2]++;
            }
        }
        return freq;
    }

    /**
     * Indicates whether an image has been loaded.
     *
     * @return {@code true} if an image is loaded; {@code false} otherwise
     */
    public boolean isLoaded() {
        return isLoaded;
    }

    /**
     * Returns the image height in pixels (row count).
     *
     * @return the height
     */
    public int getHeight() {
        return height;
    }

    /**
     * Returns the image width in pixels (column count).
     *
     * @return the width
     */
    public int getWidth() {
        return width;
    }

    /**
     * Applies a pixel processor (filter) to the current image.
     * <p>
     * Delegates the processing to the provided {@link IPixelProcessor} and
     * updates the current image with the result. Dimensions must match the
     * original image.
     * </p>
     *
     * @param processor the processor to apply (e.g., {@link GreyScale} or {@link Blur})
     * @throws IllegalStateException if no image is loaded or if result dimensions are invalid
     * @throws IllegalArgumentException if {@code processor} is {@code null}
     */
    public void apply(IPixelProcessor processor){
        if(!isLoaded) throw new IllegalStateException("No image loaded");
        if(processor == null) throw new IllegalArgumentException("Processor is null");

        int[][] result = processor.processImage(currentPixels);

        if (result.length != height || result[0].length != width)
            throw new IllegalStateException("Processor returned invalid dimensions");

        for(int y = 0; y < height; y++){
            for(int x = 0; x < width; x++){
                currentPixels[y][x] = result[y][x];
            }
        }
    }
}