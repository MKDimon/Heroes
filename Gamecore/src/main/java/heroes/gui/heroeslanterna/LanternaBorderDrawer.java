package heroes.gui.heroeslanterna;

import heroes.gui.heroeslanterna.utils.Colors;

import java.io.IOException;

/**
 * Статический класс для рисования границ интерфейса игры.
 */
public class LanternaBorderDrawer {
    /**
     * Общий метод для рисования границ, по очереди вызывает все методы класса.
     * @param tw экземпляр класса LanternaWrapper для обращения непосредственно к Screen
     */
    public static void drawBorders(final LanternaWrapper tw) {
        drawInnerBorders(tw, Colors.WHITE);
        drawLogLine(tw, Colors.ORANGE);
        drawGeneralZone(tw, Colors.RED);
        drawBattlefield(tw, Colors.GREEN);
    }

    /**
     * Рисует внешние границы (вдоль всей доступной области терминала) путем вызова класса LanternaLineDrawer.
     * @param tw экземпляр класса LanternaWrapper для обращения непосредственно к Screen
     * @param color цвет внешних границ.
     */
    private static void drawInnerBorders(final LanternaWrapper tw, final Colors color) {
        LanternaLineDrawer.drawHorizontalLine(tw,1, tw.getTerminalSize().getColumns() - 2, 0, '=', color);
        LanternaLineDrawer.drawHorizontalLine(tw, 1, tw.getTerminalSize().getColumns() - 2,
                tw.getTerminalSize().getRows() - 1, '=', color);
        LanternaLineDrawer.drawVerticalLine(tw, 0, 0, tw.getTerminalSize().getRows() - 1, '|', color);
        LanternaLineDrawer.drawVerticalLine(tw, tw.getTerminalSize().getColumns() - 1, 0,
                tw.getTerminalSize().getRows() - 1, '|', color);
    }

    /**
     * Рисует линию, которая разделяет область игровой информации и область вывода логов. Область вывода логов
     * составляет 30% от доступного пространства терминала (параметр вшит в метод).
     * @param tw экземпляр класса LanternaWrapper для обращения непосредственно к Screen
     * @param color цвет линии
     */
    private static void drawLogLine(final LanternaWrapper tw, final Colors color) {
        LanternaLineDrawer.drawHorizontalLine(tw, 1, tw.getTerminalSize().getColumns() - 2,
                tw.getTerminalSize().getRows() -
                        (int)((tw.getTerminalSize().getRows() - 1) * 0.3), '=', color);
    }

    /**
     * Рисует границы вокруг области портретов генералов.
     * @param tw экземпляр класса LanternaWrapper для обращения непосредственно к Screen
     * @param color цвет границ.
     * @throws IOException исключение из методов Лантерны пробрасывается выше.
     */
    private static void drawGeneralZone(final LanternaWrapper tw, final Colors color) {
        LanternaLineDrawer.drawVerticalLine(tw, 35, 1, tw.getTerminalSize().getRows() -
                (int)((tw.getTerminalSize().getRows() - 1) * 0.3) - 1, '|', color);
        LanternaLineDrawer.drawHorizontalLine(tw, 1, 34, 25, '=', color);

        LanternaLineDrawer.drawVerticalLine(tw, tw.getTerminalSize().getColumns() - 35, 1, tw.getTerminalSize().getRows() -
                (int)((tw.getTerminalSize().getRows() - 1) * 0.3) - 1, '|', color);
        LanternaLineDrawer.drawHorizontalLine(tw, tw.getTerminalSize().getColumns() - 34,
                tw.getTerminalSize().getColumns() - 2, 25, '=', color);
    }

    /**
     * Рисует границы поля боя.
     * @param tw экземпляр класса LanternaWrapper для обращения непосредственно к Screen
     * @param color цвет границ.
     */
    private static void drawBattlefield(final LanternaWrapper tw, final Colors color) {
        final int center = tw.getTerminalSize().getColumns() / 2;
        LanternaLineDrawer.drawHorizontalLine(tw,  center - 35,center + 35, 3, '=', color);
        LanternaLineDrawer.drawHorizontalLine(tw,  center - 35,center + 35, 34, '=', color);
        LanternaLineDrawer.drawVerticalLine(tw, center - 35, 3, 34, '|', color);
        LanternaLineDrawer.drawVerticalLine(tw, center + 35, 3, 34, '|', color);
    }
}
