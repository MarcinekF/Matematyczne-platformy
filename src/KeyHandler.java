import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;

public class KeyHandler implements KeyListener {

    public boolean rightPressed, leftPressed, spacePressed, downPressed, upPressed, enterPressed;

    public boolean saveNameFlag;
    public static StringBuilder saveName;
    public KeyHandler()
    {
        this.saveNameFlag = false;
        saveName = new StringBuilder();
    }
    @Override
    public void keyTyped(KeyEvent e)
    {
        if ((Player.lives == 0) || (GamePanel.timeLeft < 1))
        {
            GamePanel.gameState = GamePanel.GameState.menu;
            Player.lives = 0;
            GamePanel.timeLeft = 10;
        }
        if((saveNameFlag) && (e.getKeyChar() != KeyEvent.VK_BACK_SPACE))
        {
            saveName.append(e.getKeyChar());
        }
    }

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