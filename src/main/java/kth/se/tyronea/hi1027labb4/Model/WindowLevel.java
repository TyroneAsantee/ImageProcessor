package kth.se.tyronea.hi1027labb4.Model;

import kth.se.tyronea.hi1027labb4.Controller.PixelConverter;
import kth.se.tyronea.hi1027labb4.Model.IPixelProcessor;

import static java.lang.Math.max;
import static java.lang.Math.min;

/**
 * Justerar bildens kontrast och ljushet med hjälp av "window level"-principen.
 * <p>
 * Detta filter används för att framhäva en specifik intensitetsintervall (ett "fönster")
 * i bilden genom att omfördela gråskalevärden. Intensiteter under fönstrets nedre gräns
 * blir helt svarta (0), och de över den övre gränsen blir helt vita (255).
 * Värden inom fönstret skalas linjärt till hela spannet 0–255.
 * </p>
 *
 * <p>
 * Parametrarna styr justeringen enligt följande:
 * <ul>
 *   <li><b>window</b> – anger bredden på intensitetsområdet (kontrast).</li>
 *   <li><b>level</b> – anger mittpunkten för fönstret (ljushetsnivå).</li>
 * </ul>
 * </p>
 *
 * <p>
 * Filtrets resultat blir en gråskaleversion av bilden där varje pixel
 * visas med justerad intensitet baserat på angivet fönster och nivå.
 * Alfa-kanalen bevaras oförändrad.
 * </p>
 *
 * <p><b>Exempel:</b><br>
 * Om <code>window = 100</code> och <code>level = 128</code>,
 * blir fönstret 78–178. Alla intensiteter under 78 blir svarta,
 * över 178 blir vita, och däremellan skalas linjärt.</p>
 */

public class WindowLevel implements IPixelProcessor {
    private final int window;
    private final int level;

    /**
     * Skapar ett nytt window/level-filter.
     *
     * @param window bredden på intensitetsområdet som ska förstärkas
     * @param level mittpunkten för fönstret (styr ljushetsnivån)
     */
    public WindowLevel(int window, int level){
        this.window = window;
        this.level = level;
    }

    /**
     * Bearbetar en bild med window/level-justering.
     * <p>
     * Varje pixels intensitet (baserat på röd kanal) jämförs mot
     * fönstrets nedre och övre gräns:
     * <ul>
     *   <li>Om värdet är under gränsen → sätts till 0 (svart).</li>
     *   <li>Om värdet är över gränsen → sätts till 255 (vit).</li>
     *   <li>Om värdet är inom intervallet → skalas proportionellt till 0–255.</li>
     * </ul>
     * </p>
     *
     * <p>
     * Resultatet skrivs som en ny bild i gråskala, där alla tre färgkomponenter
     * (R, G, B) får samma värde. Alfa-kanalen kopieras från originalet.
     * </p>
     *
     * @param originalPixels en 2D-array av ARGB-pixlar ({@code int}) som representerar bilden
     * @return en ny 2D-array med den window/level-justerade bilden
     */
    @Override
    public int[][] processImage(int[][] originalPixels) {
        int height = originalPixels.length;
        int width = originalPixels[0].length;

        int[][] newPixels = new int[height][width];

        int lower = max(0, level - window/2);
        int upper = min(255, level + window/2);

        for(int y = 0; y < height; y++){
            for(int x = 0; x < width; x++){
                int argb = originalPixels[y][x];
                int alpha = PixelConverter.getAlpha(argb);
                int red = PixelConverter.getRed(argb);
                int green = PixelConverter.getGreen(argb);
                int blue = PixelConverter.getBlue(argb);
                int newIntensity;
                if(red <= lower){
                    newIntensity = 0;
                } else if (red >= upper) {
                    newIntensity = 255;
                } else {
                    newIntensity = (red - lower) * 255 / (upper - lower);
                }

                int newPixel = PixelConverter.toArgbPixel(alpha, newIntensity, newIntensity, newIntensity);
                newPixels[y][x] = newPixel;
            }
        }
        return newPixels;
    }
}