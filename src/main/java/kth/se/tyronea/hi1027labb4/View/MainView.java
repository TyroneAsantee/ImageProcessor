package kth.se.tyronea.hi1027labb4.View;

import javafx.scene.Node;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;

import java.util.Collection;

public class MainView extends VBox {
    private MenuBar menuBar;

    public MainView(){
        super();
        createMenuBar();
        Collection<Node> children = this.getChildren();
        BorderPane borderPane = new BorderPane();
        ImageView imageView = new ImageView();
        children.add(menuBar);
        children.add(borderPane);
    }

    private void createMenuBar(){
        Menu fileMenu = new Menu("File");
        Menu processMenu = new Menu("Process");
        Menu helpMenu = new Menu("Help");

        MenuItem grayScale = new MenuItem("GrayScale");
        MenuItem blur = new MenuItem("Blur");
        MenuItem sharpen = new MenuItem("Sharpen");
        processMenu.getItems().addAll(grayScale, blur, sharpen);

        menuBar = new MenuBar();
        menuBar.getMenus().addAll(fileMenu, processMenu, helpMenu);
    }
}