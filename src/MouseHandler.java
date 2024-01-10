import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

/**
 * Klasa obsługująca zdarzenia myszy.
 */
public class MouseHandler implements MouseListener
{
    /**
     * Konstruktor klasy MouseHandler
     */
    public MouseHandler()
    {

    }
    /**
     * Obsługuje kliknięcie myszy.
     *
     * @param e Zdarzenie myszy.
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

    /**
     * Obsługuje naciśnięcie przycisku myszy.
     *
     * @param e Zdarzenie myszy.
     */
    @Override
    public void mousePressed(MouseEvent e) {

    }

    /**
     * Obsługuje zwolnienie przycisku myszy.
     *
     * @param e Zdarzenie myszy.
     */
    @Override
    public void mouseReleased(MouseEvent e) {

    }

    /**
     * Obsługuje wejście myszy na obszar komponentu.
     *
     * @param e Zdarzenie myszy.
     */
    @Override
    public void mouseEntered(MouseEvent e) {

    }

    /**
     * Obsługuje wyjście myszy z obszaru komponentu.
     *
     * @param e Zdarzenie myszy.
     */
    @Override
    public void mouseExited(MouseEvent e) {

    }
}
