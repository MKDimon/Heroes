package heroes.gui.heroeslanterna;

import com.googlecode.lanterna.graphics.TextGraphics;
import heroes.gamelogic.Board;
import heroes.gamelogic.Fields;
import heroes.gui.heroeslanterna.utils.Colors;

/**
 * Статический класс для отображения информации о ходе игры.
 */
public class LanternaInformationDrawer {
    /**
     * Вызывает private методы отображения информации.
     * @param tw экземпляр класса LanternaWrapper для обращения к Screen
     * @param board экземпляр класса Board, в котором содержится необходимая для отображения информации.
     */
    public static void drawInfo(final LanternaWrapper tw, final Board board){
        drawRound(tw, board);
        drawTurn(tw, board);
    }

    /**
     * Отображает номер текущего раунда.
     * @param tw экземпляр класса LanternaWrapper для обращения к Screen
     * @param board экземпляр класса Board, в котором содержится необходимая для отображения информации.
     */
    private static void drawRound(final LanternaWrapper tw, final Board board) {
        final int center = tw.getTerminalSize().getColumns() / 2;
        final TextGraphics tg = tw.newTG();
        tg.setForegroundColor(Colors.ORANGE.color());
        tg.putString(center, 1, "| Current round: " + board.getCurNumRound());
        tg.setForegroundColor(Colors.WHITE.color());
    }

    /**
     * Отображает, какой игрок ходит: левый или правый.
     * @param tw экземпляр класса LanternaWrapper для обращения к Screen
     * @param board экземпляр класса Board, в котором содержится необходимая для отображения информации.
     */
    private static void drawTurn(final LanternaWrapper tw, final Board board) {
        final int center = tw.getTerminalSize().getColumns() / 2;
        final TextGraphics tg = tw.newTG();
        tg.setForegroundColor(Colors.ORANGE.color());
        if (board.getCurrentPlayer() == Fields.PLAYER_ONE) {
            tg.putString(center - 21, 1, "Turn of LEFT player");
        } else {
            tg.putString(center - 20, 1, "Turn of RIGHT player");
        }
        tg.setForegroundColor(Colors.WHITE.color());

    }
}
