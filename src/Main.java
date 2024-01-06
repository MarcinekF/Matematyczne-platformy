import javax.swing.*;
import java.io.IOException;
/**
 * Główna klasa programu, która zawiera metodę main.
 */
public class Main
{
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