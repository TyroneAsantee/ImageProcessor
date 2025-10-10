package kth.se.tyronea.hi1027labb4;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import kth.se.tyronea.hi1027labb4.Controller.Controller;
import kth.se.tyronea.hi1027labb4.Model.ImageModel;
import kth.se.tyronea.hi1027labb4.View.MainView;

import java.io.IOException;

public class Main extends Application {
    @Override
    public void start(Stage stage) {
        ImageModel model = new ImageModel();
        Controller controller = new Controller(model);
        MainView view = new MainView(controller);
        Scene scene = new Scene(view.getRoot(), 700, 600);

        stage.setTitle("Image Modifier");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}