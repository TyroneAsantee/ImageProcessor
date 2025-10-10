package kth.se.tyronea.hi1027labb4.Controller;

import javafx.scene.image.Image;
import kth.se.tyronea.hi1027labb4.Model.*;

import java.io.InputStream;


public class Controller {

    private ImageModel model;

    public Controller(ImageModel model){
        this.model = model;
    }

    /*public void onLoadImage(){
        LOAD IMAGE TO FILE
    }*/

    /*public void onSaveImage(){
        SAVE IMAGE TO FILE
    }*/


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