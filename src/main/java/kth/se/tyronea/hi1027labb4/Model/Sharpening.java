package kth.se.tyronea.hi1027labb4.Model;

import static kth.se.tyronea.hi1027labb4.Controller.PixelConverter.*;

/**
 * Applicerar ett skärpningsfilter (sharpening) på en bild.
 * <p>
 * Filtren i denna klass ökar bildens kontrast och framhäver detaljer genom att
 * först skapa en gråskaleversion av bilden, sedan en oskarpad (blur:ad) version,
 * och slutligen kombinera dessa för att förstärka kanter och konturer.
 * </p>
 *
 * <p>
 * Skärpningen beräknas genom formeln:
 * {@code sharpened = 2 * originalGray - blurredGray}.
 * Detta kallas ibland för en <i>unsharp mask</i>-metod och används ofta inom
 * bildbehandling för att förbättra upplevd skärpa.
 * </p>
 *
 * <p>
 * Resultatet begränsas till intensitetsintervallet 0–255, och alfa-kanalen
 * (transparensen) bevaras oförändrad från originalbilden.
 * </p>
 *
 * <p><b>Komplexitet:</b> O(bredd × höjd), då bilden bearbetas pixel för pixel.</p>
 */

public class Sharpening implements IPixelProcessor {

    /**
     * Bearbetar en bild för att öka skärpan.
     * <p>
     * Metoden använder en kombination av gråskala- och oskärpefilter:
     * <ol>
     *   <li>Först konverteras bilden till gråskala med {@link GreyScale}.</li>
     *   <li>Därefter suddas den gråskaleversionen ut med {@link Blur}.</li>
     *   <li>Slutligen beräknas skillnaden mellan den ursprungliga gråskalan
     *       och den oskarpa bilden, enligt formeln {@code 2 * original - blurred}.</li>
     * </ol>
     * Detta förstärker högfrekventa detaljer (kanter) i bilden.
     * </p>
     *
     * @param originalPixels en 2D-array av ARGB-pixlar som representerar originalbilden
     * @return en ny 2D-array med den skärpta bilden i gråskala
     */

    @Override
    public int[][] processImage(int[][] originalPixels) {
        int height = originalPixels.length;
        int width = originalPixels[0].length;
        int[][] out = new int[height][width];

        GreyScale greyFilter = new GreyScale();
        int[][] grayPixels = greyFilter.processImage(originalPixels);

        Blur blurFilter = new Blur();
        int[][] blurredPixels = blurFilter.processImage(grayPixels);

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int a = getAlpha(originalPixels[y][x]);

                int g0 = getRed(grayPixels[y][x]);
                int gb = getRed(blurredPixels[y][x]);

                int sharp = 2 * g0 - gb;
                if (sharp < 0) sharp = 0;
                if (sharp > 255) sharp = 255;

                out[y][x] = toArgbPixel(a, sharp, sharp, sharp);
            }
        }
        return out;
    }
}