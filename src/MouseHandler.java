import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

/**
 * Klasa obsługująca zdarzenia myszy.
 */
public class MouseHandler implements MouseListener
{
    /**
     * Metoda obsługująca zdarzenie kliknięcia myszą.
     */
    @Override
    public void mouseClicked(MouseEvent e) {
        int x = e.getPoint().x;
        int y = e.getPoint().y;

        if ((GamePanel.gameState == GamePanel.GameState.running)&& (x <=110 ) && (x >= 35) && (y >=540) && (y<=560))
        {
            GamePanel.gameState = GamePanel.GameState.paused;
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }
}
