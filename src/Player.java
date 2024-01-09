import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * Klasa reprezentująca gracza w grze.
 */
public class Player
{
    File sheetFile;
    BufferedImage sheet;
    BufferedImage sprite;
    int sheetWidth;
    int jumpLimit;
    int animationIndex;
    Rectangle rect;

    /**
     * Ilość żyć gracza
     */
    public static int lives;

    /**
     * Ilość punktów zdobytych przez gracza
     */
    public static int points;
    /**
     * Nieprzerwana seria poprawnych odpowiedzi pod rząd
     */
    public static int streak;
    /**
     * Największa nieprzerwana seria poprawnych odpowiedzi pod rząd
     */
    public static int highestStreak;
    boolean hit;
    int hitDelay;
    String answer;

    /**
     * Konstruktor klasy Player.
     *
     * @param x Początkowa pozycja X postaci.
     * @param y Początkowa pozycja Y postaci.
     * @throws IOException Wyjątek rzucany w przypadku błędu wczytywania plików graficznych.
     */
    public Player(int x, int y) throws IOException {
        this.rect = new Rectangle(x, y, 32, 64);
        this.sheetFile = new File("assets/ScarfKitten/idle.png");
        this.sheet = ImageIO.read(sheetFile);
        this.sprite =sheet.getSubimage(0,0, 32, 32);
        this.sheetWidth = 11;
        this.jumpLimit  = 1;
        this.animationIndex = 0;
        this.hit = false;
        highestStreak = 0;
        lives = 3;
        answer ="none";
        points = 0;
        streak = 0;
    }

    /**
     * Metoda aktualizująca obecnie wyświetlaną klatkę animacji gracza.
     */
    public void updateSprite()
    {
        animationIndex++;
        if (animationIndex == sheetWidth)
        {
            animationIndex = 0;
        }
        sprite = sheet.getSubimage(animationIndex*32,0,32,32);
    }

    /**
     * Aktualizuje arkusz sprite'ów postaci na podstawie ścieżki do pliku.
     *
     * @param path Ścieżka do pliku z nowym arkuszem sprite'ów postaci.
     * @throws IOException Wyjątek rzucany w przypadku błędu wczytywania plików graficznych.
     */
    public void updateSheet(String path) throws IOException {
        animationIndex = 0;
        sheetFile = new File(path);
        sheet = ImageIO.read(sheetFile);
        sprite = sheet.getSubimage(0,0, 32, 32);
        sheetWidth = sheet.getWidth() / 32;
    }

    /**
     * Obsługuje moment lądowania postaci.
     *
     * @throws IOException Wyjątek rzucany w przypadku błędu wczytywania plików graficznych.
     */
    public void landed() throws IOException {
        rect.y = rect.y - 5;
        updateSheet("assets/ScarfKitten/idle.png");
    }

    /**
     * Obsługuje moment spadania postaci.
     *
     * @throws IOException Wyjątek rzucany w przypadku błędu wczytywania plików graficznych.
     */
    public void fall() throws IOException
    {
        updateSheet("assets/ScarfKitten/fall.png");
    }

    /**
     * Obsługuje moment skoku postaci.
     *
     * @throws IOException Wyjątek rzucany w przypadku błędu wczytywania plików graficznych.
     */
    public void jump () throws IOException
    {
        if(jumpLimit == 1)
        {
            updateSheet("assets/ScarfKitten/jump.png");
            jumpLimit --;
        }
    }

    /**
     * Metoda zliczająca czas pokazywania animacji utraty życia
     *
     *  @throws IOException Wyjątek rzucany w przypadku błędu wczytywania plików graficznych.
     */
    public void countHitTime() throws IOException {
        hitDelay++;
        if(hitDelay <= 8)
        {
            hit = false;
            hitDelay = 0;
            updateSheet("assets/ScarfKitten/idle.png");
        }
    }
}