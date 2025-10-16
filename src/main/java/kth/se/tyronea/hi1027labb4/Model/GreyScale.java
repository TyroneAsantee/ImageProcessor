package kth.se.tyronea.hi1027labb4.Model;

import static kth.se.tyronea.hi1027labb4.Controller.PixelConverter.*;

/**
 * Omvandlar en färgbild till gråskala.
 * <p>
 * Denna klass bearbetar en ARGB-bild genom att beräkna ett genomsnitt av
 * röd-, grön- och blåkanalerna för varje pixel och sätter sedan alla tre
 * färgkomponenter till detta värde. Resultatet blir en bild i olika nyanser
 * av grått där ljushet baseras på pixelns ursprungliga färgintensitet.
 * </p>
 *
 * <p>
 * Alfa-kanalen (transparensen) påverkas inte utan kopieras oförändrad från
 * originalbilden. Hjälpmetoder för att extrahera och skapa ARGB-pixlar
 * tillhandahålls via {@link kth.se.tyronea.hi1027labb4.Controller.PixelConverter}.
 * </p>
 *
 * <p><b>Komplexitet:</b> O(bredd × höjd)</p>
 */

public class GreyScale implements IPixelProcessor{

    /**
     * Konverterar en bild till gråskala.
     * <p>
     * För varje pixel hämtas röd, grön och blå färgkomponent. Dessa tre
     * värden medelvärdesberäknas för att få en grå nivå (0–255) som sedan
     * används för alla färgkomponenter. Alfa-värdet behålls från originalet.
     * </p>
     *
     * @param originalPixels en 2D-array av ARGB-pixlar ({@code int}) där varje element
     *                       representerar en pixel i bilden
     * @return en ny 2D-array med gråskalepixlar; originalet påverkas inte
     * @implNote Ljusstyrkan beräknas som ett enkelt aritmetiskt medelvärde:
     *           (r + g + b) / 3.0. Mer avancerade metoder (t.ex. luminansviktning)
     *           används inte här.
     */

    @Override
    public int[][] processImage(int[][] originalPixels) {
        int height = originalPixels.length;
        int width  = originalPixels[0].length;
        int[][] out = new int[height][width];
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                int p = originalPixels[y][x];

                int a = getAlpha(p);
                int r = getRed(p);
                int g = getGreen(p);
                int b = getBlue(p);

                int gray = (int)Math.round((r + g + b) / 3.0);

                out[y][x] = toArgbPixel(a, gray, gray, gray);
            }
        }
        return out;

    }
}
