package heroes.gui;

import heroes.auxiliaryclasses.ActionTypes;
import heroes.gui.utils.GeneralDrawersMap;
import heroes.gui.generaldrawers.IGeneralDrawer;
import heroes.gui.utils.Side;

import java.io.IOException;

/**
 * Статический класс для рисования портретов генералов.
 */
public class TerminalGeneralDrawer {
    /**
     * Рисует портреты соответствующих каждой армии генералов.
     * @param tw экземпляр класса TerminalWrapper для обращения непосредственно к Screen
     * @param generalOne ActionType генерала первой армии. Так как у всех генералов разный ActionType, то можно
     *                   дифференцировать их именно по этому параметру.
     * @param generalTwo ActionType генерала второй армии
     * @throws IOException исключение из методов Лантерны пробрасывается выше.
     */
    public static void drawGenerals(final TerminalWrapper tw,
                                    final ActionTypes generalOne, final ActionTypes generalTwo) throws IOException {
        IGeneralDrawer genOne = GeneralDrawersMap.getDrawer(generalOne);
        IGeneralDrawer genTwo = GeneralDrawersMap.getDrawer(generalTwo);
        genOne.draw(tw, Side.LHS);
        genTwo.draw(tw, Side.RHS);
    }
}
