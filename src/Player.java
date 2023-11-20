import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Player
{
    File sheetFile;
    BufferedImage sheet;
    BufferedImage sprite;
    int sheetWidth;
    int jumpLimit;
    int animationIndex;
    Rectangle rect;
    public static int rewards;
    public static int lives;
    public static int points;
    public static int streak;
    public static int highestStreak;
    String answer;

    public Player(int x, int y) throws IOException {
        rect = new Rectangle(x, y, 64, 64);
        this.sheetFile = new File("assets/ScarfKitten/idle.png");
        this.sheet = ImageIO.read(sheetFile);
        this.sprite =sheet.getSubimage(0,0, 32, 32);
        this.sheetWidth = 11;
        this.jumpLimit  = 1;
        this.animationIndex = 0;
        highestStreak = 0;
        lives = 3;
        rewards = 0;
        answer ="none";
        points = 0;
        streak = 0;
    }

    public void updateSprite()
    {
        animationIndex++;
        if (animationIndex == sheetWidth)
        {
            animationIndex = 0;
        }
        sprite = sheet.getSubimage(animationIndex*32,0,32,32);
    }

    public void updateSheet(String path) throws IOException {
        animationIndex = 0;
        sheetFile = new File(path);
        sheet = ImageIO.read(sheetFile);
        sprite = sheet.getSubimage(0,0, 32, 32);
        sheetWidth = sheet.getWidth() / 32;
    }


    public void landed() throws IOException {
        rect.y = rect.y - 5;
        updateSheet("assets/ScarfKitten/idle.png");
    }
    public void fall() throws IOException
    {
        updateSheet("assets/ScarfKitten/fall.png");
    }
    public void jump () throws IOException
    {
        if(jumpLimit == 1)
        {
            updateSheet("assets/ScarfKitten/jump.png");
            jumpLimit --;
        }
    }
}