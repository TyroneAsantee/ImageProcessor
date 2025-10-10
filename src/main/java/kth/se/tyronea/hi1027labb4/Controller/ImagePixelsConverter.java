package kth.se.tyronea.hi1027labb4.Controller;

import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;


public class ImagePixelsConverter {

    public static int[][] imageToPixels(Image image) {
        int width = (int) image.getWidth();
        int height = (int) image.getHeight();

        int[][] pixelMatrix = new int[height][width];
        PixelReader pixelReader = image.getPixelReader();

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                pixelMatrix[y][x] = pixelReader.getArgb(x, y);
            }
        }

        return pixelMatrix;
    }

    public static Image pixelsToImage(int[][] pixelMatrix) {

        int width = pixelMatrix[0].length;
        int height = pixelMatrix.length;

        WritableImage writableImage = new WritableImage(width, height);
        PixelWriter pixelWriter = writableImage.getPixelWriter();

        int argbValue;
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                argbValue = pixelMatrix[y][x];
                pixelWriter.setArgb(x,y,argbValue);
            }
        }
        return writableImage;
    }
}