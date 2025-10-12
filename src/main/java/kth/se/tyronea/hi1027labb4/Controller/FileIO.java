package kth.se.tyronea.hi1027labb4.Controller;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class FileIO {

    public static Image readImage(File file) {
        try {
            BufferedImage bi = ImageIO.read(file);
            if (bi == null) {
                System.err.println("Could not read file.");
                return null;
            }
            return SwingFXUtils.toFXImage(bi, null);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void writeImage(Image fxImage, File file) {
        try {
            BufferedImage bi = SwingFXUtils.fromFXImage(fxImage, null);
            String name = file.getName().toLowerCase();
            String format = (name.endsWith(".jpg") || name.endsWith(".jpeg")) ? "jpg"
                    : (name.endsWith(".bmp")) ? "bmp"
                    : "png";
            if (!name.contains(".")) {
                file = new File(file.getParentFile(), file.getName() + ".png");
                format = "png";
            }
            ImageIO.write(bi, format, file);
            System.out.println("Saved: " + file.getAbsolutePath());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
