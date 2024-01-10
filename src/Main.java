import javax.swing.*;
import java.io.IOException;
/**
 * Główna klasa programu, zawiera metodę main, która jest punktem wejścia do programu.
 * Inicjalizuje i uruchamia główny obiekt gry oraz obsługuje ewentualne wyjątki.
 */
public class Main
{
    /**
     * Konstruktor klasy Main
     */
    public Main()
    {

    }
    /**
     * Metoda main - punkt wejścia do programu.
     * @param args Argumenty wiersza poleceń przekazywane do programu.
     * @throws IOException Obsługa wyjątku IOException, np. brak dostępu do plików z obrazami
     */
    public static void main(String[] args) throws IOException
    {
        JFrame window = new JFrame();
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setResizable(false);
        window.setTitle("Matematyczne platformy");

        GamePanel gamePanel = new GamePanel();
        window.add(gamePanel);

        window.pack();

        window.setLocationRelativeTo(null);
        window.setVisible(true);
        gamePanel.startGameThread();
    }
}