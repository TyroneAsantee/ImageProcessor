package kth.se.tyronea.hi1027labb4.View;

import javafx.geometry.Pos;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.layout.VBox;

/**
 * A customized JavaFX component, extending VBox, displaying a histogram over frequencies
 * per intensities. Lines are displayed for colors red, green and blue respectively.
 */
public class HistogramView extends VBox {

    private final XYChart.Series<Number, Number> seriesRed;
    private final XYChart.Series<Number, Number> seriesGreen;
    private final XYChart.Series<Number, Number> seriesBlue;

    public HistogramView() {
        seriesRed = new XYChart.Series<>();
        seriesGreen = new XYChart.Series<>();
        seriesBlue = new XYChart.Series<>();

        LineChart<Number, Number> histogramChart = new LineChart<>(new NumberAxis(), new NumberAxis());
        histogramChart.getData().addAll(seriesRed, seriesGreen, seriesBlue);
        histogramChart.setCreateSymbols(false); // no "dots"
        histogramChart.setLegendVisible(false);
        histogramChart.setTitle("Frequency per pixel intensity");
        this.getChildren().add(histogramChart);

        this.setAlignment(Pos.CENTER); // align children center
    }

    /**
     * Set, and display, values for this histogram chart. The values should be presented in an
     * int[256][3] matrix where the first index, 0..255, represents intensity, and the second index
     * represents respectively red (0), green (1) and blue (2).
     * The value at [intensity][color] itself represents the number of pixels in an image with that
     * specific intensity and color.
     *
     * @param histogramValues the values to display in this histogram,
     */
    public void updateView(int[][] histogramValues) {
        if (histogramValues.length != 256 || histogramValues[0].length != 3)
            throw new IllegalArgumentException("illegal dimensions");

        seriesRed.getData().clear();
        seriesGreen.getData().clear();
        seriesBlue.getData().clear();

        // add new data
        for (int i = 0; i < 256; i++) {
            seriesRed.getData().add(new XYChart.Data<>(i, histogramValues[i][0])); // red
            seriesGreen.getData().add(new XYChart.Data<>(i, histogramValues[i][1])); // green
            seriesBlue.getData().add(new XYChart.Data<>(i, histogramValues[i][2])); // blue
        }

        // styling
        seriesRed.getNode().setStyle("-fx-stroke: red; -fx-stroke-width: 1px");
        seriesGreen.getNode().setStyle("-fx-stroke: green; -fx-stroke-width: 1px");
        seriesBlue.getNode().setStyle("-fx-stroke: blue; -fx-stroke-width: 1px");
    }

    /**
     * CLear the display.
     */
    public void clear() {
        seriesRed.getData().clear();
        seriesGreen.getData().clear();
        seriesBlue.getData().clear();
    }
}
