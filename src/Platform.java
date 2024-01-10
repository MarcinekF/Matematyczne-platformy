import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * Klasa reprezentująca platformę w grze.
 */
public class Platform
{
    File sheetFile = new File("assets/fake_platform.png");
    BufferedImage sheet = ImageIO.read(sheetFile);
    BufferedImage sprite = sheet.getSubimage(0,0, 57, 30);
    int sheetWidth = 4;
    int animationIndex = 0;
    Rectangle rect;
    String answer;
    boolean Animated = false;

    /**
     * Konstruktor klasy Platform.
     *
     * @param x Położenie x platformy
     * @param y Położenie y platformy
     * @param answer Odpowiedź jaka widnieje przy platformie
     *
     * @throws IOException Wyjątek rzucany w przypadku błędu wczytywania plików graficznych.
     */
    public Platform(int x, int y, String answer) throws IOException
    {
        rect = new Rectangle(x, y, 57, 10);
        this.answer = answer;
    }

    /**
     * Metoda aktualizująca obecnie wyświetlaną klatkę animacji platformy.
     *
     * @return Jeśli zwraca True to platforma znika z gry. Gdy False, przesunięty zostanie tylko indeks animacji
     */
    public boolean updateSprite()
    {
        animationIndex++;
        if (animationIndex == sheetWidth)
        {
            animationIndex = 0;
            return true;
        }
        sprite = sheet.getSubimage(animationIndex*57,0,57,30);
        return false;
    }
}