package kth.se.tyronea.hi1027labb4.View;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.FileChooser;
import java.io.File;

import kth.se.tyronea.hi1027labb4.Controller.Controller;

public class MainView extends VBox {
    private FileChooser fileChooser;
    private Image image = null;
    private MenuBar menuBar;
    private Controller controller;
    private ImageView imageView;
    private ScrollPane pane;
    private BorderPane root;
    private MenuItem grayScale;
    private MenuItem blur;
    private MenuItem sharpen;
    private MenuItem addGeneric;
    private MenuItem openFile;
    private MenuItem saveFile;
    private MenuItem reset;
    private HistogramView histogramView;

    public MainView(Controller controller){
        super();
        this.controller = controller;
        root = new BorderPane();

        // === Filväljare (lärarens kod) ===
        fileChooser = new FileChooser();
        FileChooser.ExtensionFilter filter = new FileChooser.ExtensionFilter(
                "Image files", "*.png", "*.jpg", "*.bmp");
        fileChooser.getExtensionFilters().add(filter);

        createMenuBar();
        createUIComponent();
        addEventHandlers(controller);

        root.setTop(menuBar);
        root.setCenter(pane);
        root.setLeft(histogramView);
    }

    private void createMenuBar(){
        Menu fileMenu = new Menu("File");
        Menu processMenu = new Menu("Process");
        Menu helpMenu = new Menu("Help");


        grayScale = new MenuItem("GrayScale");
        blur = new MenuItem("Blur");
        sharpen = new MenuItem("Sharpen");
        addGeneric = new MenuItem("Add generic image");
        openFile = new MenuItem("Open image from file");
        saveFile = new MenuItem("Save image to file");
        reset = new MenuItem("Reset");


        fileMenu.getItems().addAll(openFile, addGeneric, saveFile);
        processMenu.getItems().addAll(grayScale, blur, sharpen, reset);

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
        BorderPane.setMargin(pane, new Insets(0, 0, 0, 12));

        histogramView = new HistogramView();
        histogramView.setPrefWidth(260);
        histogramView.setMinWidth(220);
        histogramView.setMaxWidth(320);
        BorderPane.setMargin(histogramView, new Insets(10, 0, 10, 10));
    }

    public Parent getRoot(){
        return root;
    }


    protected void onOpenImageFile() {
        File imageFile = fileChooser.showOpenDialog(null);
        if (imageFile != null) {
            image = new Image(imageFile.toURI().toString());
            Image img = controller.onLoadImageFromFile(image);
            if (img == null) {
                showAlert();
            } else {
                imageView.setImage(img);
            }
        }
    }

    protected void onSaveImageFile() {
        File file = fileChooser.showSaveDialog(null);
        if (file != null) {
            controller.onSaveImageToFile(file);
        }
    }


    public void addEventHandlers(Controller controller){
        openFile.setOnAction(event -> onOpenImageFile());
        saveFile.setOnAction(event -> onSaveImageFile());

        EventHandler<ActionEvent> genericHandler = new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                Image img = controller.onLoadImageFromResource();
                if(img == null) {
                    showAlert();
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
                    showAlert();
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
                    showAlert();
                } else {
                    imageView.setImage(img);
                }
            }
        };
        reset.setOnAction(resetHandler);


        EventHandler<ActionEvent> sharpenHandler = new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                Image img = controller.onSharpenSelected();
                if(img == null){
                    showAlert();
                } else {
                    imageView.setImage(img);
                }
            }
        };
        sharpen.setOnAction(sharpenHandler);


        EventHandler<ActionEvent> blurHandler = new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                Image img = controller.onBlurSelected();
                if(img == null){
                    showAlert();
                } else {
                    imageView.setImage(img);
                }
            }
        };
        blur.setOnAction(blurHandler);
    }

    private void showAlert(){
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle("Operation failed");
        alert.setHeaderText("Couldn't load image");
        alert.setContentText("Make sure an image is loaded and available.");
        alert.showAndWait();
    }
}
