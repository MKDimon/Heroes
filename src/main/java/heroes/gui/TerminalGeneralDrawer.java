package heroes.gui;

import com.googlecode.lanterna.SGR;
import com.googlecode.lanterna.graphics.TextGraphics;
import heroes.gui.utils.Colors;
import heroes.gui.utils.GeneralDrawersMap;
import heroes.gui.utils.GeneralNamesMap;
import heroes.gui.utils.Side;
import heroes.units.General;

import java.io.IOException;
import java.util.EnumSet;

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
                                   final General general, final Side side) throws IOException {
        GeneralDrawersMap.getDrawer(general.getActionType()).draw(tw, side);
        drawStatus(tw, general, side);
    }

    private static void drawStatus(final TerminalWrapper tw, final General general, final Side side) {
        int x_start = (side == Side.RHS) ? tw.getTerminalSize().getColumns() - 34 : 1;
        int y_start = 25;
        TextGraphics tg = tw.newTG();
        tg.setForegroundColor(Colors.GOLD.color());
        tg.setModifiers(EnumSet.of(SGR.ITALIC));
        tg.putString(x_start + 13, y_start - 2, GeneralNamesMap.getName(general.getActionType()));
        tg.setForegroundColor(Colors.WHITE.color());
    }
}
