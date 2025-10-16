package kth.se.tyronea.hi1027labb4.Model;

import static kth.se.tyronea.hi1027labb4.Controller.PixelConverter.*;

/**
 * Applicerar en enkel Gaussisk oskärpa (blur) på en ARGB-bild.
 * <p>
 * Denna klass bearbetar varje pixel i bilden genom att använda en fast 3x3-viktmatris
 * {@code [[1,2,1],[2,4,2],[1,2,1]]} (summan = 16) för att mjuka upp färger och minska brus,
 * samtidigt som bildens övergripande struktur bevaras. Alfa-kanalen (transparensen)
 * påverkas inte av beräkningen.
 * </p>
 *
 * <p>
 * Vid bildkanter hanteras gränser genom att koordinater som hamnar utanför bilden
 * ”kläms” till närmaste giltiga pixel (kallas ofta för "edge replicate"). Detta
 * förhindrar indexfel och svarta kanter.
 * </p>
 *
 * <p>
 * Hjälpmetoder för att hämta och sätta ARGB-komponenter tillhandahålls via
 * {@link kth.se.tyronea.hi1027labb4.Controller.PixelConverter} (importerad statiskt).
 * </p>
 *
 * <p><b>Komplexitet:</b> O(bredd × höjd) med en liten konstant faktor.</p>
 */

public class Blur implements IPixelProcessor {

    /**
     * Bearbetar en bild genom att applicera en 3x3 Gaussisk-liknande filtermatris.
     * <p>
     * För varje pixel beräknas ett viktat medelvärde baserat på närliggande pixlar
     * enligt matrisen {@code [[1,2,1],[2,4,2],[1,2,1]]}, normaliserad genom division med 16.
     * Koordinater som hamnar utanför bilden justeras till närmaste giltiga pixel.
     * De resulterande röd-, grön- och blåkanalerna avrundas till närmaste heltal
     * och kombineras sedan med originalpixelns alfa-kanal.
     * </p>
     *
     * @param originalPixels en 2D-array av ARGB-pixlar där varje element representerar en pixel
     *                       som ett heltal ({@code int}) med packade färgkomponenter
     * @return en ny 2D-array med de bearbetade (blur:ade) pixlarna; originalet påverkas inte
     * @implNote Alfa-kanalen hämtas från mittenpixeln – oskärpan påverkar endast färgkanalerna.
     */

    @Override
    public int[][] processImage(int[][] originalPixels){
        int height = originalPixels.length;
        int width  = originalPixels[0].length;
        int[][] out = new int[height][width];

        int[][] w = {
                {1, 2, 1},
                {2, 4, 2},
                {1, 2, 1}
        };

        for (int x = 0; x < width; x++){
            for (int y = 0; y < height; y++){
                double r = 0, g = 0, b = 0;
                int a = getAlpha(originalPixels[y][x]);
                for (int dx = -1; dx <= 1; dx++) {
                    for (int dy = -1; dy <= 1; dy++) {

                        int xx = x + dx;
                        int yy = y + dy;

                        if (xx < 0) xx = 0;
                        if (xx >= width) xx = width - 1;
                        if (yy < 0) yy = 0;
                        if (yy >= height) yy = height - 1;

                        int p = originalPixels[yy][xx];
                        int weight = w[dy + 1][dx + 1];

                        r += getRed(p)   * weight;
                        g += getGreen(p) * weight;
                        b += getBlue(p)  * weight;
                    }
                }

                int red   = (int)Math.round(r / 16.0);
                int green = (int)Math.round(g / 16.0);
                int blue  = (int)Math.round(b / 16.0);

                out[y][x] = toArgbPixel(a, red, green, blue);
            }
        }
        return out;
    }
}
