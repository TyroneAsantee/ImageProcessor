module kth.se.tyronea.hi1027labb4 {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.swing;


    opens kth.se.tyronea.hi1027labb4 to javafx.fxml;
    exports kth.se.tyronea.hi1027labb4;
}