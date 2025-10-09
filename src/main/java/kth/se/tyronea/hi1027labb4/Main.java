package kth.se.tyronea.hi1027labb4;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import kth.se.tyronea.hi1027labb4.View.MainView;

import java.io.IOException;

public class Main extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        MainView view = new MainView();
        Scene scene = new Scene(view, 400, 400);

        stage.setTitle("Image Modifier");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}