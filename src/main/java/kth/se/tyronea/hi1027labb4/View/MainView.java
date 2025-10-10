package kth.se.tyronea.hi1027labb4.View;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import kth.se.tyronea.hi1027labb4.Controller.Controller;

public class MainView extends VBox {
    private MenuBar menuBar;
    private Controller controller;
    private ImageView imageView;
    private ScrollPane pane;
    private BorderPane root;
    private MenuItem grayScale;
    private MenuItem blur;
    private MenuItem sharpen;
    private MenuItem addGeneric;
    private MenuItem reset;

    public MainView(Controller controller){
        super();
        this.controller = controller;
        root = new BorderPane();
        createMenuBar();
        createUIComponent();
        addEventHandlers(controller);
        root.setTop(menuBar);
        root.setCenter(pane);
    }

    private void createMenuBar(){
        Menu fileMenu = new Menu("File");
        Menu processMenu = new Menu("Process");
        Menu helpMenu = new Menu("Help");

        grayScale = new MenuItem("GrayScale");
        blur = new MenuItem("Blur");
        sharpen = new MenuItem("Sharpen");
        addGeneric = new MenuItem("Add generic image");
        reset = new MenuItem("Reset");
        processMenu.getItems().addAll(grayScale, blur, sharpen, reset);
        fileMenu.getItems().add(addGeneric);

        menuBar = new MenuBar();
        menuBar.getMenus().addAll(fileMenu, processMenu, helpMenu);
    }

    private void createUIComponent(){
        imageView = new ImageView();
        imageView.setSmooth(true);
        imageView.setPreserveRatio(true);
        imageView.setFitWidth(600);
        imageView.setFitHeight(400);
        pane = new ScrollPane();
        pane.setContent(imageView);
        pane.setFitToHeight(true);
        pane.setFitToWidth(true);
    }


    public Parent getRoot(){
            return root;
    }

    public void addEventHandlers(Controller controller){
        EventHandler<ActionEvent> genericHandler = new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                Image img = controller.onLoadImageFromResource();
                if(img == null) {
                    Alert alert = showAlert();
                    alert.showAndWait();
                } else {
                    imageView.setImage(img);
                }
            }
        };
        addGeneric.setOnAction(genericHandler);

        EventHandler<ActionEvent> greyScaleHandler = new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                Image img = controller.onGrayScaleSelected();
                if(img == null) {
                    Alert alert = showAlert();
                    alert.showAndWait();
                } else {
                    imageView.setImage(img);
                }
            }
        };
        grayScale.setOnAction(greyScaleHandler);

        EventHandler<ActionEvent> resetHandler = new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                Image img = controller.onRevertToOriginal();
                if(img == null) {
                    Alert alert = showAlert();
                    alert.showAndWait();
                } else {
                    imageView.setImage(img);
                }
            }
        };
        reset.setOnAction(resetHandler);
    }

    private Alert showAlert(){
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle("Operation failed");
        alert.setHeaderText("Couldn't load image");
        alert.setContentText("Make sure an image is loaded and available.");
        return alert;
    }

}