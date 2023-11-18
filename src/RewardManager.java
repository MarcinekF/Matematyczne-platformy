import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class RewardManager {
    File sheetFile;
    BufferedImage sheet;
    BufferedImage sprite;
    int index = 0;
    public static int streak;

    public RewardManager() throws IOException
    {
        this.sheetFile = new File("assets/Rewards.png");
        this.sheet = ImageIO.read(sheetFile);
        this.sprite = sheet.getSubimage(0,104,64,52);
        streak = 0;
    }

    public void paintNewReward(Graphics g)
    {
        g.drawImage(sprite,100,100,null);
    }

    public void updateSprite()
    {
        index ++;
        if (index ==8)
        {
            index = 0;
        }
        int y = 0;
        if((Player.streak) >= 5 && (Player.streak<10))
        {
            y = 52 * 2;
        }
        else if((Player.streak) >= 10 && (Player.streak<15))
        {
            y = 52;
        }
        sprite = sheet.getSubimage(index*64,y,64,52);
    }

    public void showRewards(Graphics g)
    {
        g.drawString("Funkcja w budowie", 100, 100);
    }
}
