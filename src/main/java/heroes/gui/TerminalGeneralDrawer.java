package heroes.gui;

import heroes.auxiliaryclasses.ActionTypes;
import heroes.gui.utils.GeneralDrawersMap;
import heroes.gui.utils.Side;

import java.io.IOException;

/**
 * Статический класс для рисования портретов генералов.
 */
public class TerminalGeneralDrawer {
    /**
     * Рисует портреты соответствующих каждой армии генералов.
     * @param tw экземпляр класса TerminalWrapper для обращения непосредственно к Screen
     * @param general ActionType генерала. Так как у всех генералов разный ActionType, то можно
     *                   дифференцировать их именно по этому параметру.
     * @param side Сторона, на которой будет отрисован портрет.
     * @throws IOException исключение из методов Лантерны пробрасывается выше.
     */
    public static void drawGeneral(final TerminalWrapper tw,
                                   final ActionTypes general, final Side side) throws IOException {
        GeneralDrawersMap.getDrawer(general).draw(tw, side);
    }
}
