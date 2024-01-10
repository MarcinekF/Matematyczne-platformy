import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Klasa zarządzająca menu w grze.
 */
public class Menu
{
    Font customFont = new Font("Arial", Font.PLAIN, 16);
    Font pickedActionFont = new Font("Arial", Font.PLAIN, 18);
    int pickedAction;
    File savesFolder;

    /**
     * Tablica plików zapisów gry w folderze.
     */
    public static File [] savesList;

    LinkedHashMap<String, Integer> gamePausedOptions = new LinkedHashMap<>();
    LinkedHashMap<String, Integer> mainMenuOptions = new LinkedHashMap<>();
    LinkedHashMap<String, Integer> saveMenuOptions = new LinkedHashMap<>();

    int screenWidth;
    int screenHeight;

    /**
     * Konstruktor klasy Menu.
     *
     * @param screenWidth Szerokosc ekranu
     * @param screenHeight Wysokosc ekranu
     */
    public Menu(int screenWidth, int screenHeight)
    {
        this.screenWidth = screenWidth;
        this.screenHeight = screenHeight;
        this.pickedAction = 0;

        savesFolder = new File("saves");
        savesList = savesFolder.listFiles();

        gamePausedOptions.put("Kontynuuj rozgrywkę", 0);
        gamePausedOptions.put("Zapis gry", 1);
        gamePausedOptions.put("Wczytanie gry", 2);
        gamePausedOptions.put("Wyswietl nagrody", 3);
        gamePausedOptions.put("Wyjście do głownego menu", 4);

        mainMenuOptions.put("Nowa gra", 0);
        mainMenuOptions.put("Wczytanie gry", 1);
        mainMenuOptions.put("Wyjście", 2);

        saveMenuOptions.put("Zapisz", 0);
        saveMenuOptions.put("Anuluj", 1);
    }

    /**
     * Metoda do wyświetlania menu w przypadku zatrzymania gry.
     *
     * @param g Obiekt Graphics do rysowania na ekranie
     */
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

    /**
     * Metoda do wyświetlania głównego menu gry.
     *
     * @param g Obiekt Graphics do rysowania na ekranie
     */
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

    /**
     * Wczytuje dane gry z pliku o określonej ścieżce.
     *
     * @param g Obiekt Graphics do rysowania na ekranie
     */
    public void loadGame(Graphics g)
    {
        g.drawString("Wybierz plik do wczytania", screenWidth / 2 - 110, screenHeight / 4);
        g.setFont(customFont);

        if (savesList.length == 0)
        {
            g.drawString("Nie znalezniono żadnych zapisów!", screenWidth/2 - 100, screenHeight/2);
            pickedAction = 0;
            g.setFont(pickedActionFont);
            g.drawString("Powrot", screenWidth/2 - 30,screenHeight/2 + 45);
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
            if(pickedAction == savesList.length )
            {
                g.setFont(pickedActionFont);
            }
            g.drawString("Powrot", screenWidth/2 - 30,screenHeight/2 + i * 30 + 10);
        }
    }

    /**
     * Metoda do tworzenia okna dla nowego zapisu gry.
     *
     * @param g Obiekt Graphics do rysowania na ekranie
     */
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

    /**
     * Metoda do wczytywania danych gry z pliku.
     *
     * @throws IOException Wyjątek rzucany gdy wystąpi problem z wczytaniem pliku graficznego
     */
    public void loadSave() throws IOException {

        String save = "saves/" + savesList[pickedAction].getName();
        try(BufferedReader br = new BufferedReader(new FileReader(save))){
            Player.lives = Integer.parseInt(br.readLine().trim());
            Player.points = Integer.parseInt(br.readLine().trim());
            Player.streak = Integer.parseInt(br.readLine().trim());
            Player.highestStreak = Integer.parseInt(br.readLine().trim());
            if (Player.highestStreak >=5)
            {
                GamePanel.totalTime = 25;
            }
            if (Player.highestStreak >=10)
            {
                GamePanel.totalTime = 20;
            }
            if (Player.highestStreak >=15)
            {
                GamePanel.totalTime = 15;
            }
        }
        pickedAction = 0;
    }

    /**
     * Metoda do zapisywania danych gry do pliku.
     *
     * @param filePath Nazwa ścieżki gdzie ma zostać zapisany plik gry
     */
    public void saveGame(String filePath)
    {
        String var1 = String.valueOf(Player.lives);
        String var2 = String.valueOf(Player.points);
        String var3 = String.valueOf(Player.streak);
        String var4 = String.valueOf(Player.highestStreak);

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath)))
        {
            writer.write((var1 +"\n"));
            writer.write((var2 +"\n"));
            writer.write((var3 +"\n"));
            writer.write((var4 +"\n"));

        } catch (IOException e){
            e.printStackTrace();
        }
        savesList = savesFolder.listFiles();
    }
}
