import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

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
    public Platform(int x, int y, String answer) throws IOException
    {
        rect = new Rectangle(x, y, 57, 10);
        this.answer = answer;
    }

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