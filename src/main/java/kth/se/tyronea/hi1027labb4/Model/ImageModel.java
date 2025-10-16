package kth.se.tyronea.hi1027labb4.Model;

import kth.se.tyronea.hi1027labb4.Controller.PixelConverter;

/**
 * Fasadklass som representerar modellens centrala gränssnitt mot resten av systemet.
 * <p>
 * Denna klass fungerar som en *fasad* i MVC-arkitekturen och kapslar in den interna
 * hanteringen av pixlar, filter och bilddata. Den tillhandahåller ett tydligt och
 * avgränsat API för att läsa in, bearbeta och återställa bilder utan att den yttre
 * koden behöver känna till hur dessa operationer implementeras internt.
 * </p>
 *
 * <p>
 * Bilddata lagras som tvådimensionella arrayer av heltal ({@code int[][]})
 * där varje element representerar en pixel i ARGB-format (alfa, röd, grön, blå).
 * Klassen hanterar både originalbilden och en bearbetad version av bilden
 * samt erbjuder metoder för att applicera olika bildfilter via
 * {@link IPixelProcessor}-gränssnittet.
 * </p>
 *
 * <p>
 * Genom att agera fasad gör denna klass det möjligt för andra delar av programmet
 * (framförallt kontroller och vyer) att arbeta med bildbearbetning på en högre abstraktionsnivå.
 * </p>
 */

public class ImageModel {

    private int[][] originalPixels;
    private int[][] currentPixels;
    private int width, height;
    private boolean isLoaded;

    /**
     * Skapar en tom bildmodell (fasad) utan någon laddad bild.
     * <p>
     * {@link #isLoaded()} returnerar {@code false} tills en bild laddats in
     * via {@link #loadFromPixels(int[][])}.
     * </p>
     */

    public ImageModel(){
        this.isLoaded = false;
    }

    /**
     * Läser in en ny bild till modellen.
     * <p>
     * Bilden anges som en 2D-array av ARGB-pixlar. Metoden kontrollerar att
     * datan inte är tom och att alla rader har samma längd. Både original-
     * och aktuell bild kopieras internt för att säkerställa dataintegritet.
     * </p>
     *
     * @param pixels en 2D-array av ARGB-pixlar
     * @throws IllegalArgumentException om arrayen är null, tom eller oregelbunden
     */

    public void loadFromPixels(int [][] pixels){
        if(pixels == null || pixels.length == 0) throw new IllegalArgumentException("Image is null or empty");
        height = pixels.length;
        width = pixels[0].length;
        for(int[] row : pixels){
            if(row.length != width){
                throw new IllegalArgumentException("Rows not the same length");
            }
        }
        originalPixels = new int[height][width];
        currentPixels  = new int[height][width];
        for(int y = 0; y < height; y++){
            for (int x = 0; x < width; x++) {
                originalPixels[y][x] = pixels[y][x];
                currentPixels[y][x] = pixels[y][x];
            }
        }
        isLoaded = true;
    }

    /**
     * Returnerar en kopia av den nuvarande (bearbetade) bilden.
     *
     * @return en kopia av {@code currentPixels}
     * @throws IllegalStateException om ingen bild är laddad
     */

    public int[][] getCurrentPixels() {
        if(!isLoaded) throw new IllegalStateException("No image loaded");
        int[][] copy = new int[height][width];
        for(int y = 0; y < height; y++){
            for (int x = 0; x < width; x++){
                copy[y][x] = currentPixels[y][x];
            }
        }
        return copy;
    }

    /**
     * Returnerar en kopia av originalbilden.
     *
     * @return en kopia av {@code originalPixels}
     * @throws IllegalStateException om ingen bild är laddad
     */

    public int[][] getOriginalPixels() {
        if(!isLoaded) throw new IllegalStateException("No image loaded");
        int[][] copy = new int[height][width];
        for(int y = 0; y < height; y++){
            for(int x = 0; x < width; x++){
                copy[y][x] = originalPixels[y][x];
            }
        }
        return copy;
    }

    /**
     * Sätter den aktuella bilden till en ny uppsättning pixlar.
     *
     * @param currentPixels en 2D-array av ARGB-pixlar
     */

    public void setCurrentPixels(int[][] currentPixels) {
        this.currentPixels = currentPixels;
    }

    /**
     * Återställer nuvarande bild till originalet.
     * <p>
     * Denna metod kopierar tillbaka originalpixlarna till den aktuella bilden,
     * vilket effektivt tar bort alla tillämpade filter eller transformationer.
     * </p>
     *
     * @throws IllegalStateException om ingen bild är laddad
     */
    public void resetToOriginal(){
        if(!isLoaded) throw new IllegalStateException("No image loaded");
        for(int y = 0; y < height; y++){
            for(int x = 0; x < width; x++){
                currentPixels[y][x] = originalPixels[y][x];
            }
        }
    }

    /**
     * Genererar ett färghistogram för den nuvarande bilden.
     * <p>
     * Histogrammet visar hur ofta varje intensitetsnivå (0–255)
     * förekommer i röd, grön och blå kanal. Resultatet returneras
     * som en 256×3-array där varje rad motsvarar en intensitet.
     * </p>
     *
     * @return en 256×3-array med färgfrekvenser, eller {@code null} om ingen bild är laddad
     */

    public int[][] getHistogramForCurrentPixels(){
        if(currentPixels == null) return null;
        int[][] freq = new int[256][3];
        for(int y = 0; y < height; y++){
            for(int x = 0; x < width; x++){
                int argb = currentPixels[y][x];
                int red = PixelConverter.getRed(argb);
                int green = PixelConverter.getGreen(argb);
                int blue = PixelConverter.getBlue(argb);

                freq[red][0]++;
                freq[green][1]++;
                freq[blue][2]++;
            }
        }
        return freq;
    }

    /**
     * Anger om en bild har laddats i modellen.
     *
     * @return {@code true} om en bild är laddad, annars {@code false}
     */
    public boolean isLoaded() {
        return isLoaded;
    }

    /**
     * Returnerar bildens höjd i pixlar.
     *
     * @return antalet rader i bilden
     */
    public int getHeight() {
        return height;
    }

    /**
     * Returnerar bildens bredd i pixlar.
     *
     * @return antalet kolumner i bilden
     */
    public int getWidth() {
        return width;
    }

    /**
     * Applicerar en bildprocessor (filter) på den nuvarande bilden.
     * <p>
     * Metoden fungerar som en central ingångspunkt för bildbehandling.
     * Den tar emot ett objekt som implementerar {@link IPixelProcessor}
     * och uppdaterar den nuvarande bilden baserat på resultatet.
     * </p>
     *
     * @param processor den processor som ska användas (t.ex. {@link GreyScale} eller {@link Blur})
     * @throws IllegalStateException om ingen bild är laddad eller om resultatets dimensioner är felaktiga
     * @throws IllegalArgumentException om {@code processor} är {@code null}
     */
    public void apply(IPixelProcessor processor){
        if(!isLoaded) throw new IllegalStateException("No image loaded");
        if(processor == null) throw new IllegalArgumentException("Processor is null");

        int[][] result = processor.processImage(currentPixels);

        if (result.length != height || result[0].length != width)
            throw new IllegalStateException("Processor returned invalid dimensions");

        for(int y = 0; y < height; y++){
            for(int x = 0; x < width; x++){
                currentPixels[y][x] = result[y][x];
            }
        }
    }
}