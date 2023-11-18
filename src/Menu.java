import javax.swing.*;
import java.awt.*;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class Menu
{
    Font customFont = new Font("Arial", Font.PLAIN, 16);
    Font pickedActionFont = new Font("Arial", Font.PLAIN, 18);
    int pickedAction = 0;
    File savesFolder;
    public static File [] savesList;

    LinkedHashMap<String, Integer> gamePausedOptions = new LinkedHashMap<>();
    LinkedHashMap<String, Integer> mainMenuOptions = new LinkedHashMap<>();
    LinkedHashMap<String, Integer> saveMenuOptions = new LinkedHashMap<>();

    int screenWidth;
    int screenHeight;

    public Menu(int screenWidth, int screenHeight)
    {
        this.screenWidth = screenWidth;
        this.screenHeight = screenHeight;

        savesFolder = new File("saves");
        savesList = savesFolder.listFiles();

        gamePausedOptions.put("Zapis gry", 0);
        gamePausedOptions.put("Wczytanie gry", 1);
        gamePausedOptions.put("Wyswietl nagrody", 2);
        gamePausedOptions.put("Wyjście do głownego menu", 3);

        mainMenuOptions.put("Nowa gra", 0);
        mainMenuOptions.put("Wczytanie gry", 1);
        mainMenuOptions.put("Wyjście", 2);

        saveMenuOptions.put("Zapisz", 0);
        saveMenuOptions.put("Anuluj", 1);
    }

    public void gamePaused(Graphics g)
    {
        g.drawString("Gra zatrzymana", screenWidth/2 - 100, screenHeight / 4);
        g.setFont(customFont);
        int i = 0;
        for (String s : gamePausedOptions.keySet())
        {
            if(i == pickedAction)
            {
                g.setFont(pickedActionFont);
                g.drawString(s, screenWidth/2 - 60, screenHeight / 2 + i*30);
                i++;
                g.setFont(customFont);
                continue;
            }
            g.drawString(s, screenWidth/2 - 60, screenHeight / 2 + i*30);
            i++;
        }
    }
    public void mainMenu(Graphics g)
    {
        g.drawString("Matematyczne platformy", screenWidth / 2 - 140, screenHeight / 4);
        g.setFont(customFont);
        int i = 0;
        for (String s : mainMenuOptions.keySet())
        {
            if(i == pickedAction)
            {
                g.setFont(pickedActionFont);
                g.drawString(s, screenWidth/2 - 60, screenHeight / 2 + i*30);
                i++;
                g.setFont(customFont);
                continue;
            }
            g.drawString(s, screenWidth/2 - 60, screenHeight / 2 + i*30);
            i++;
        }
    }
    public void loadGame(Graphics g)
    {
        g.drawString("Wybierz plik do wczytania", screenWidth / 2 - 140, screenHeight / 4);
        g.setFont(customFont);

        if (savesList.length == 0)
        {
            g.setFont(pickedActionFont);
            g.drawString("Nie znalezniono żadnych zapisów!", screenWidth/2 - 100, screenHeight/2);
        }
        else
        {
            String name;
            int i = 0;
            for (File save : savesList)
            {
                name = save.getName();

                if(i == pickedAction)
                {
                    g.setFont(pickedActionFont);
                    g.drawString(name, screenWidth/2 - 30, screenHeight/2 + i * 30);
                    i++;
                    g.setFont(customFont);
                    continue;
                }
                g.drawString(name, screenWidth/2 - 30, screenHeight/2 + i * 30);
                i++;
            }
        }
    }
    public void createSave(Graphics g)
    {
        g.drawString("Zapisywanie gry", screenWidth/2 - 80, screenHeight / 4);
        g.setFont(customFont);
        g.drawString("Jak nazwać zapis?: ", screenWidth/2 - 70, screenHeight / 2 - 20);
        g.drawRect(screenWidth/2 - 60,screenHeight / 2,120,20);
        g.drawString(KeyHandler.saveName.toString(), screenWidth/2 - 55,screenHeight / 2 + 15);

        int i = 0;
        for (String s : saveMenuOptions.keySet())
        {
            if(i == pickedAction)
            {
                g.setFont(pickedActionFont);
                g.drawString(s, screenWidth/2 - 30, screenHeight - 200 + i*30);
                i++;
                g.setFont(customFont);
                continue;
            }
            g.drawString(s, screenWidth/2 - 30, screenHeight - 200 + i*30);
            i++;
        }
    }
    public void saveGame(String filePath)
    {
        String var1 = String.valueOf(Player.lives);
        String var2 = String.valueOf(Player.points);
        String var3 = String.valueOf(Player.streak);

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath)))
        {
            writer.write((var1 +"\n"));
            writer.write((var2 +"\n"));
            writer.write((var3 +"\n"));

        } catch (IOException e){
            System.out.println("Cannot save the game");
            e.printStackTrace();
        }
        savesList = savesFolder.listFiles();
    }
}
