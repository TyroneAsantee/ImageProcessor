package kth.se.tyronea.hi1027labb4;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class FileIO {

    public static Image readImage(File file) throws IOException {
        BufferedImage bi = ImageIO.read(file);
        if (bi == null) {
            throw new IOException("Couldn't read file");
        }
        return SwingFXUtils.toFXImage(bi, null);
    }

    public static void writeImage(Image fxImage, File file) throws IOException {
        BufferedImage bi = SwingFXUtils.fromFXImage(fxImage, null);
        String name = file.getName().toLowerCase();

        String format = (name.endsWith(".jpg") || name.endsWith(".jpeg")) ? "jpg"
                : (name.endsWith(".bmp")) ? "bmp"
                : "png";

        if (!name.contains(".")) {
            file = new File(file.getParentFile(), file.getName() + ".png");
            format = "png";
        }

        if (!ImageIO.write(bi, format, file)) {
            throw new IOException("Couldn't save file in format: " + format);
        }

        System.out.println("Saved: " + file.getAbsolutePath());
    }
}

