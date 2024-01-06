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
    Shape shape;
    public static int rewards;
    public static int lives;
    public static int points;
    public static int streak;
    public static int highestStreak;
    boolean hit;
    int hitDelay;
    String answer;

    /**
     * Konstruktor klasy Player.
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
        rewards = 0;
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
     * Metoda aktualizująca arkusz animacji gracza na podstawie ścieżki do pliku.
    */
    public void updateSheet(String path) throws IOException {
        animationIndex = 0;
        sheetFile = new File(path);
        sheet = ImageIO.read(sheetFile);
        sprite = sheet.getSubimage(0,0, 32, 32);
        sheetWidth = sheet.getWidth() / 32;
    }

    /**
     * Metoda wywoływana po lądowaniu gracza.
     */
    public void landed() throws IOException {
        rect.y = rect.y - 5;
        updateSheet("assets/ScarfKitten/idle.png");
    }

    /**
     * Metoda wywoływana podczas spadania gracza.
     */
    public void fall() throws IOException
    {
        updateSheet("assets/ScarfKitten/fall.png");
    }

    /**
     * Metoda wywoływana podczas skoku gracza.
     *
     * @throws IOException Występuje w przypadku problemów z wczytaniem obrazów.
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