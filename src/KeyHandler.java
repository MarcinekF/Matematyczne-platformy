import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;

/**
 * Klasa obsługująca zdarzenia wykonane na klawiaturze.
 * Implementuje interfejs KeyListener, umożliwiając reakcję na wciśnięcia i zwolnienia klawiszy.
 */
public class KeyHandler implements KeyListener
{

    public boolean rightPressed, leftPressed, spacePressed, downPressed, upPressed, enterPressed;

    public boolean saveNameFlag;
    public static StringBuilder saveName;
    /**
     * Konstruktor klasy KeyHandler.
     * Inicjuje flagi i obiekt StringBuilder do przechowywania nazwy zapisu.
     */
    public KeyHandler()
    {
        this.saveNameFlag = false;
        saveName = new StringBuilder();
    }

    /**
     * Obsługuje zdarzenie wciśnięcia klawisza na klawiaturze.
     * Reaguje na różne klawisze w zależności od aktualnego stanu gry.
     * Jeśli trwa wprowadzanie nazwy zapisu, dodaje znaki do StringBuildera.
     *
     * @param e Zdarzenie związane z wciśnięciem klawisza.
     */
    @Override
    public void keyTyped(KeyEvent e)
    {
        if ((Player.lives == 0) || (GamePanel.timeLeft < 1))
        {
            GamePanel.gameState = GamePanel.GameState.menu;
            Player.lives = 0;
            GamePanel.timeLeft = 10;
        }
        if((saveNameFlag) && (e.getKeyChar() != KeyEvent.VK_BACK_SPACE) && (saveName.length() < 12))
        {
            saveName.append(e.getKeyChar());
        }
    }
    /**
     * Obsługuje zdarzenie przytrzymania klawisza na klawiaturze.
     * Reaguje na różne klawisze w zależności od aktualnego stanu gry.
     * Kontroluje również ruchy postaci i interakcje w grze.
     *
     * @param e Zdarzenie związane z przytrzymaniem klawisza.
     */
    @Override
    public void keyPressed(KeyEvent e)
    {
        int code = e.getKeyCode();

        if (code == KeyEvent.VK_RIGHT)
        {
            rightPressed = true;
        }
        else if (code == KeyEvent.VK_LEFT)
        {
            leftPressed = true;
        }
        else if ((code == KeyEvent.VK_SPACE) && (GamePanel.gameState == GamePanel.GameState.running))
        {
            spacePressed = true;
        }
        else if(code == KeyEvent.VK_DOWN)
        {
            downPressed = true;
        }
        else if(code == KeyEvent.VK_UP)
        {
            upPressed = true;
        }
        else if(code == KeyEvent.VK_ENTER)
        {
            enterPressed = true;
        }
        if
        (code==KeyEvent.VK_ESCAPE)
        {
            switch (GamePanel.gameState)
            {
                case running -> GamePanel.gameState = GamePanel.GameState.paused;
                case paused -> GamePanel.gameState = GamePanel.GameState.running;
            }
        }
        if((code == KeyEvent.VK_BACK_SPACE) && (!saveName.isEmpty()))
        {
            saveName.deleteCharAt(saveName.length() - 1);
        }
    }

    /**
     * Obsługuje zdarzenie zwolnienia klawisza na klawiaturze.
     * Reaguje na różne klawisze w zależności od aktualnego stanu gry.
     * Kontroluje również ruchy postaci w grze.
     */
    @Override
    public void keyReleased(KeyEvent e) {
        int code = e.getKeyCode();
        if (code == KeyEvent.VK_RIGHT)
        {
            rightPressed = false;
        } else if (code == KeyEvent.VK_LEFT)
        {
            leftPressed = false;
        }
        if (code == KeyEvent.VK_SPACE)
        {
            spacePressed = false;
        }
        else if(code == KeyEvent.VK_DOWN)
        {
            downPressed = false;
        }
        else if(code == KeyEvent.VK_UP)
        {
            upPressed = false;
        }
    }
}