import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
/**
 * Klasa zarządzająca nagrodami w grze, takimi jak puchary za osiągnięcia gracza.
 */
public class RewardManager {
    File sheetFile;
    BufferedImage sheet;
    BufferedImage sprite;

    BufferedImage spriteTrophyNo5;
    BufferedImage spriteTrophyNo10;
    BufferedImage spriteTrophyNo15;

    int index = 0;

    Font customFont = new Font("Arial", Font.PLAIN, 18);
    Font warningFont = new Font("Arial", Font.PLAIN, 16);

    /**
     * Konstruktor klasy RewardManager.
     * Inicjuje plik arkusza nagród oraz wczytuje obrazy nagród.
     *
     * @throws IOException Wyjątek rzucany w przypadku błędu wczytywania plików graficznych.
     */
    public RewardManager() throws IOException
    {
        this.sheetFile = new File("assets/Rewards.png");
        this.sheet = ImageIO.read(sheetFile);
        this.sprite = sheet.getSubimage(0,104,64,52);
        this.spriteTrophyNo5 = sheet.getSubimage(448,104,64,52);
        this.spriteTrophyNo10 = sheet.getSubimage(448,52,64,52);
        this.spriteTrophyNo15 = sheet.getSubimage(448,0,64,52);
    }

    /**
     * Metoda rysująca nową nagrodę na ekranie.
     */
    public void paintNewReward(Graphics g, int screenWidth, int screenHeight)
    {
        g.drawImage(sprite,screenWidth / 2 + 170,screenHeight - 200,null);
        g.drawString(Player.streak + " odpowiedzi pod rząd!",screenWidth/2 + 90, screenHeight - 120);
    }

    /**
     * Metoda aktualizująca obecnie wyświetlaną klatkę animacji nagrody.
     */
    public void updateSprite()
    {
        index ++;
        if (index ==8)
        {
            index = 0;
        }
        int y = 0;
        if((Player.streak >= 5 )&& (Player.streak <10))
        {
            y = 52 * 2;
        }
        else if((Player.streak) >= 10 && (Player.streak < 15))
        {
            y = 52;
        }
        sprite = sheet.getSubimage(index*64,y,64,52);
    }

    /**
     * Metoda wyświetlająca ekran z przeglądem pucharów (nagród) dla gracza.
     */
    public void showRewards(Graphics g, int screenWidth, int screenHeight)
    {
        g.drawString("Przegląd pucharów", screenWidth/2 - 80, 100);
        if(Player.highestStreak < 5)
        {
            g.setFont(warningFont);
            g.drawString("Jeszcze nie otrzymano nagród", screenWidth/2 - 75, screenHeight / 2);
        }
        else if(Player.highestStreak < 10)
        {
            g.drawImage(spriteTrophyNo5, screenWidth/2 , screenHeight / 2 - 50,null);
        }
        else if(Player.highestStreak < 15)
        {
            g.drawImage(spriteTrophyNo5, screenWidth/2 - 40, screenHeight / 2 - 50,null);
            g.drawImage(spriteTrophyNo10, screenWidth/2 + 30, screenHeight / 2 - 50,null);
        }
        else if (Player.highestStreak >=15)
        {
            g.drawImage(spriteTrophyNo5, screenWidth/2 - 80, screenHeight / 2 - 50,null);
            g.drawImage(spriteTrophyNo10, screenWidth/2 - 10 , screenHeight / 2 - 50,null);
            g.drawImage(spriteTrophyNo15, screenWidth/2 + 60, screenHeight / 2 - 50,null);
        }
        g.setFont(customFont);
        g.drawString("Powrót", screenWidth/2 , screenHeight - 100);
    }
}
