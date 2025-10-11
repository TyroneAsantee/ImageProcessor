package kth.se.tyronea.hi1027labb4.Controller;

import javafx.scene.image.Image;
import kth.se.tyronea.hi1027labb4.Model.*;
import java.io.InputStream;
import java.io.File;
import java.io.File;
import java.awt.image.BufferedImage;
import javafx.embed.swing.SwingFXUtils;
import javax.imageio.ImageIO;


public class Controller {

    private ImageModel model;

    public Controller(ImageModel model){
        this.model = model;
    }


    public Image onLoadImageFromResource(){
        try {
            InputStream input = getClass().getResourceAsStream("/sharpen.png");
            if (input == null) {
                System.err.println("Could not find picture");
                return null;
            }

            Image fxImage = new Image(input);

            int[][] pixels = ImagePixelsConverter.imageToPixels(fxImage);

            model.loadFromPixels(pixels);

            int[][] current = model.getCurrentPixels();
            Image result = ImagePixelsConverter.pixelsToImage(current);

            return result;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public Image onLoadImageFromFile(Image fxImage) {
        try {
            int[][] pixels = ImagePixelsConverter.imageToPixels(fxImage);
            model.loadFromPixels(pixels);
            int[][] current = model.getCurrentPixels();
            return ImagePixelsConverter.pixelsToImage(current);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public void onSaveImageToFile(File file) {
        try {
            int[][] current = model.getCurrentPixels();
            if (current == null) {
                System.err.println("No image loaded to save.");
                return;
            }

            Image fxImage = ImagePixelsConverter.pixelsToImage(current);
            BufferedImage bufferedImage = SwingFXUtils.fromFXImage(fxImage, null);

            String name = file.getName().toLowerCase();
            String format = (name.endsWith(".jpg") || name.endsWith(".jpeg")) ? "jpg"
                    : (name.endsWith(".bmp")) ? "bmp"
                    : "png";

            if (!name.contains(".")) {
                file = new File(file.getParentFile(), file.getName() + ".png");
                format = "png";
            }

            ImageIO.write(bufferedImage, format, file);
            System.out.println("Image saved to: " + file.getAbsolutePath());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    public Image onGrayScaleSelected(){
        try {
            model.apply(new GreyScale());
            int[][] cur = model.getCurrentPixels();
            return ImagePixelsConverter.pixelsToImage(cur);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public Image onRevertToOriginal(){
        try{
            model.resetToOriginal();
            int[][] cur = model.getCurrentPixels();
            return ImagePixelsConverter.pixelsToImage(cur);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public Image onBlurSelected(){
        try{
            model.apply(new Blur());
            int[][] cur = model.getCurrentPixels();
            return ImagePixelsConverter.pixelsToImage(cur);
        } catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    public Image onSharpenSelected(){
        try{
            model.apply(new Sharpening());
            int[][] cur = model.getCurrentPixels();
            return ImagePixelsConverter.pixelsToImage(cur);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}