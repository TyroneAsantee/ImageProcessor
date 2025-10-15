package kth.se.tyronea.hi1027labb4.Controller;

import javafx.scene.image.Image;
import kth.se.tyronea.hi1027labb4.FileIO;
import kth.se.tyronea.hi1027labb4.Model.*;

import java.io.IOException;
import java.io.InputStream;
import java.io.File;



public class Controller {

    private ImageModel model;

    public Controller(ImageModel model){
        this.model = model;
    }


    public Image onLoadImageFromResource() throws IOException {
        InputStream input = getClass().getResourceAsStream("/sharpen.png");
        if (input == null) {
            throw new IOException("Couldn't find resource");
        }

        Image fxImage = new Image(input);
        int[][] pixels = ImagePixelsConverter.imageToPixels(fxImage);
        model.loadFromPixels(pixels);

        return ImagePixelsConverter.pixelsToImage(model.getCurrentPixels());
    }


    public Image onLoadImageFromFile(File file) throws IOException {
        Image fxImage = FileIO.readImage(file);
        if (fxImage == null) {
            throw new IOException("Couldn't read file: " + file.getName());
        }
        int[][] pixels = ImagePixelsConverter.imageToPixels(fxImage);
        model.loadFromPixels(pixels);
        return ImagePixelsConverter.pixelsToImage(model.getCurrentPixels());
    }


    public void onSaveImageToFile(File file) throws IOException {
        int[][] current = model.getCurrentPixels();
        if (current == null) {
            throw new IOException("No image to save.");
        }

        Image fxImage = ImagePixelsConverter.pixelsToImage(current);
        FileIO.writeImage(fxImage, file);
    }

    public Image onGrayScaleSelected(){
        if (!model.isLoaded()) {
            throw new IllegalStateException("No image loaded");
        }
        model.apply(new GreyScale());
        int[][] cur = model.getCurrentPixels();
        return ImagePixelsConverter.pixelsToImage(cur);
    }

    public Image onRevertToOriginal(){
        if (!model.isLoaded()) {
            throw new IllegalStateException("No image loaded");
        }
        model.resetToOriginal();
        int[][] cur = model.getCurrentPixels();
        return ImagePixelsConverter.pixelsToImage(cur);

    }

    public Image onBlurSelected(){
        if (!model.isLoaded()) {
            throw new IllegalStateException("No image loaded");
        }
        model.apply(new Blur());
        int[][] cur = model.getCurrentPixels();
        return ImagePixelsConverter.pixelsToImage(cur);
    }

    public Image onSharpenSelected(){
        if (!model.isLoaded()) {
            throw new IllegalStateException("No image loaded");
        }
        model.apply(new Sharpening());
        int[][] cur = model.getCurrentPixels();
        return ImagePixelsConverter.pixelsToImage(cur);
    }

    public Image onWindowLevelSelected(){
        if (!model.isLoaded()) {
            throw new IllegalStateException("No image loaded");
        }
        model.apply(new GreyScale());
        int[][] cur = model.getCurrentPixels();
        return ImagePixelsConverter.pixelsToImage(cur);
    }

    public Image onWindowLevelChanged(int window, int level){
        if(window < 1 ) window = 1;
        if(!model.isLoaded()) return null;
        int[][] original = model.getOriginalPixels();
        int[][] gray = new GreyScale().processImage(original);
        int[][] out  = new WindowLevel(window, level).processImage(gray);

        model.setCurrentPixels(out);
        return ImagePixelsConverter.pixelsToImage(out);
    }

    public int[][] getHistogramData(){
        if(!model.isLoaded()) return null;
        return model.getHistogramForCurrentPixels();
    }
}