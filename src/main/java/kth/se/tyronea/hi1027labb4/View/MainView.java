package kth.se.tyronea.hi1027labb4.View;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.FileChooser;
import java.io.File;
import java.io.IOException;

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
    private MenuItem windowLevel;
    private MenuItem blur;
    private MenuItem sharpen;
    private MenuItem addGeneric;
    private MenuItem openFile;
    private MenuItem saveFile;
    private MenuItem reset;
    private HistogramView histogramView;
    private Slider levelSlider;
    private Slider windowSlider;
    private Label lblWindowValue;
    private Label lblLevelValue;
    private VBox leftPanel;

    public MainView(Controller controller){
        super();
        this.controller = controller;
        root = new BorderPane();

        fileChooser = new FileChooser();
        FileChooser.ExtensionFilter filter = new FileChooser.ExtensionFilter(
                "Image files", "*.png", "*.jpg", "*.bmp");
        fileChooser.getExtensionFilters().add(filter);

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
        windowLevel = new MenuItem("Window/Level");
        blur = new MenuItem("Blur");
        sharpen = new MenuItem("Sharpen");
        addGeneric = new MenuItem("Add generic image");
        openFile = new MenuItem("Open image from file");
        saveFile = new MenuItem("Save image to file");
        reset = new MenuItem("Reset");

        fileMenu.getItems().addAll(openFile, addGeneric, saveFile);
        processMenu.getItems().addAll(grayScale, blur, sharpen, windowLevel, reset);

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
    }

    public void createHistogramView(){
        histogramView = new HistogramView();
        histogramView.setPrefWidth(260);
        histogramView.setMinWidth(220);
        histogramView.setMaxWidth(320);
        BorderPane.setMargin(histogramView, new Insets(10, 0, 10, 10));

        windowSlider = new Slider(1, 255, 35);   // min=1 för att undvika div/0 i modellen
        windowSlider.setShowTickMarks(true);
        windowSlider.setShowTickLabels(true);
        windowSlider.setMajorTickUnit(50);
        windowSlider.setBlockIncrement(1);
        lblWindowValue = new Label("35");

        HBox windowRow = new HBox(8, new Label("Window"), windowSlider, lblWindowValue);
        windowRow.setAlignment(Pos.CENTER_LEFT);

        levelSlider = new Slider(0, 255, 75);
        levelSlider.setShowTickMarks(true);
        levelSlider.setShowTickLabels(true);
        levelSlider.setMajorTickUnit(50);
        levelSlider.setBlockIncrement(1);
        lblLevelValue = new Label("75");

        HBox levelRow = new HBox(8, new Label("Level"), levelSlider, lblLevelValue);
        HBox.setMargin(levelSlider, new Insets(0, 0, 0, 15));
        levelRow.setAlignment(Pos.CENTER_LEFT);

        leftPanel = new VBox(12, histogramView, new Separator(), windowRow, levelRow);
        leftPanel.setPadding(new Insets(10, 10, 10, 10));
        BorderPane.setMargin(leftPanel, new Insets(10, 0, 10, 10));

        root.setLeft(leftPanel);
    }

    public void bindSliderControls(Controller controller){
        levelSlider.valueProperty().addListener((obs, oldVal, newVal) -> {
            lblLevelValue.setText(String.valueOf(newVal.intValue()));

            int w = (int) windowSlider.getValue();
            int l = newVal.intValue();

            Image out = controller.onWindowLevelChanged(w, l);
            if(out == null){
                showAlert("Error binding slider controls");
            } else {
                imageView.setImage(out);
                int[][] freq = controller.getHistogramData();
                histogramView.updateView(freq);
            }
        });

        windowSlider.valueProperty().addListener((obs, oldVal, newVal) -> {
            lblWindowValue.setText(String.valueOf(newVal.intValue()));

            int w = newVal.intValue();
            int l = (int) levelSlider.getValue();

            Image out = controller.onWindowLevelChanged(w, l);
            if (out != null) {
                imageView.setImage(out);

                int[][] freq = controller.getHistogramData();
                histogramView.updateView(freq);
            } else {
                showAlert("Error binding slider controls");
            }
        });
    }

    public Parent getRoot(){
        return root;
    }

    public void addEventHandlers(Controller controller){

        EventHandler<ActionEvent> openFileHandler = new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                File imageFile = fileChooser.showOpenDialog(null);
                if (imageFile == null) return;
                try {
                    Image img = controller.onLoadImageFromFile(imageFile);
                    imageView.setImage(img);
                    updateHistogram(controller);
                } catch (IOException ex) {
                    showAlert("Couldn't read image file:\n" + ex.getMessage());
                }
            }
        };
        openFile.setOnAction(openFileHandler);


        EventHandler<ActionEvent> saveFileHandler = new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                File file = fileChooser.showSaveDialog(null);
                if (file == null) return;
                try {
                    controller.onSaveImageToFile(file);
                } catch (IOException ex) {
                    showAlert("Couldn't save image:\n" + ex.getMessage());
                }
            }
        };
        saveFile.setOnAction(saveFileHandler);

        EventHandler<ActionEvent> genericHandler = new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                try {
                    Image img = controller.onLoadImageFromResource(); // throws IOException
                    imageView.setImage(img);
                    updateHistogram(controller);
                } catch (IOException ex) {
                    showAlert(ex.getMessage());
                }
            }
        };
        addGeneric.setOnAction(genericHandler);


        EventHandler<ActionEvent> greyScaleHandler = new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                try {
                    Image img = controller.onGrayScaleSelected(); // kan kasta IllegalStateException
                    imageView.setImage(img);
                    updateHistogram(controller);
                } catch (Exception ex) {
                    showAlert(ex.getMessage());
                }
            }
        };
        grayScale.setOnAction(greyScaleHandler);


        EventHandler<ActionEvent> resetHandler = new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                try {
                    Image img = controller.onRevertToOriginal();
                    imageView.setImage(img);
                    updateHistogram(controller);
                } catch (Exception ex) {
                    showAlert(ex.getMessage());
                }
            }
        };
        reset.setOnAction(resetHandler);


        EventHandler<ActionEvent> sharpenHandler = new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                try {
                    Image img = controller.onSharpenSelected();
                    imageView.setImage(img);
                    updateHistogram(controller);
                } catch (Exception ex) {
                    showAlert(ex.getMessage());
                }
            }
        };
        sharpen.setOnAction(sharpenHandler);


        EventHandler<ActionEvent> blurHandler = new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                try {
                    Image img = controller.onBlurSelected();
                    imageView.setImage(img);
                    updateHistogram(controller);
                } catch (Exception ex) {
                    showAlert(ex.getMessage());
                }
            }
        };
        blur.setOnAction(blurHandler);

        EventHandler<ActionEvent> windowLevelHandler = new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                try {
                    Image img = controller.onWindowLevelSelected();
                    if (histogramView == null) {
                        createHistogramView();
                    }
                    bindSliderControls(controller); // se till att listeners där inne också fångar exceptions
                    imageView.setImage(img);
                    updateHistogram(controller);
                } catch (Exception ex) {
                    showAlert(ex.getMessage());
                }
            }
        };
        windowLevel.setOnAction(windowLevelHandler);
    }



    private void showAlert(String msg){
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle("Operation failed");
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }

    private void updateHistogram(Controller controller) {
        if (histogramView != null) {
            int[][] freq = controller.getHistogramData();
            if (freq != null) histogramView.updateView(freq);
        }
    }
}
