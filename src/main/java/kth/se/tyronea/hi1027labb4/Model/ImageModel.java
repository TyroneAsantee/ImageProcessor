package kth.se.tyronea.hi1027labb4.Model;

public class ImageModel {

    private int[][] originalPixels;
    private int[][] currentPixels;
    private int width, height;
    private boolean isLoaded;

    public ImageModel(){
        this.isLoaded = false;
    }

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

    public void resetToOriginal(){
        if(!isLoaded) throw new IllegalStateException("No image loaded");
        for(int y = 0; y < height; y++){
            for(int x = 0; x < width; x++){
                currentPixels[y][x] = originalPixels[y][x];
            }
        }
    }

    public int getHeight() {
        return height;
    }

    public int getWidth() {
        return width;
    }

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