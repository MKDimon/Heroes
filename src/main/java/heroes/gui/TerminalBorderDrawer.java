package heroes.gui;

import java.io.IOException;

/**
 * Статический класс для рисования границ интерфейса игры.
 */
public class TerminalBorderDrawer {
    /**
     * Общий метод для рисования границ, по очереди вызывает все методы класса.
     * @param tw экземпляр класса TerminalWrapper для обращения непосредственно к Screen
     * @throws IOException исключение из методов Лантерны пробрасывается выше.
     */
    public static void drawBorders(final TerminalWrapper tw) throws IOException {
        drawInnerBorders(tw, "white");
        drawLogLine(tw, "orange");
        drawGeneralZone(tw, "red");
        drawBattlefield(tw, "grey");
    }

    /**
     * Рисует внешние границы (вдоль всей доступной области терминала) путем вызова класса TerminalLineDrawer.
     * @param tw экземпляр класса TerminalWrapper для обращения непосредственно к Screen
     * @param color цвет внешних границ.
     * @throws IOException исключение из методов Лантерны пробрасывается выше.
     */
    private static void drawInnerBorders(final TerminalWrapper tw, final String color) throws IOException {
        TerminalLineDrawer.drawHorizontalLine(tw,1, tw.getTerminal().getTerminalSize().getColumns() - 2, 0, '=', color);
        TerminalLineDrawer.drawHorizontalLine(tw, 1, tw.getTerminal().getTerminalSize().getColumns() - 2,
                tw.getTerminal().getTerminalSize().getRows() - 1, '=', color);
        TerminalLineDrawer.drawVerticalLine(tw, 0, 0, tw.getTerminal().getTerminalSize().getRows() - 1, '|', color);
        TerminalLineDrawer.drawVerticalLine(tw, tw.getTerminal().getTerminalSize().getColumns() - 1, 0,
                tw.getTerminal().getTerminalSize().getRows() - 1, '|', color);
    }

    /**
     * Рисует линию, которая разделяет область игровой информации и область вывода логов. Область вывода логов
     * составляет 30% от доступного пространства терминала (параметр вшит в метод).
     * @param tw экземпляр класса TerminalWrapper для обращения непосредственно к Screen
     * @param color цвет линии
     * @throws IOException исключение из методов Лантерны пробрасывается выше.
     */
    private static void drawLogLine(final TerminalWrapper tw, final String color) throws IOException {
        TerminalLineDrawer.drawHorizontalLine(tw, 1, tw.getTerminal().getTerminalSize().getColumns() - 2,
                tw.getTerminal().getTerminalSize().getRows() -
                        (int)((tw.getTerminal().getTerminalSize().getRows() - 1) * 0.3), '=', color);
    }

    /**
     * Рисует границы вокруг области портретов генералов.
     * @param tw экземпляр класса TerminalWrapper для обращения непосредственно к Screen
     * @param color цвет границ.
     * @throws IOException исключение из методов Лантерны пробрасывается выше.
     */
    private static void drawGeneralZone(final TerminalWrapper tw, final String color) throws  IOException {
        TerminalLineDrawer.drawVerticalLine(tw, 35, 1, tw.getTerminal().getTerminalSize().getRows() -
                (int)((tw.getTerminal().getTerminalSize().getRows() - 1) * 0.3) - 1, '|', color);
        TerminalLineDrawer.drawHorizontalLine(tw, 1, 34, 25, '=', color);

        TerminalLineDrawer.drawVerticalLine(tw, tw.getTerminal().getTerminalSize().getColumns() - 35, 1, tw.getTerminal().getTerminalSize().getRows() -
                (int)((tw.getTerminal().getTerminalSize().getRows() - 1) * 0.3) - 1, '|', color);
        TerminalLineDrawer.drawHorizontalLine(tw, tw.getTerminal().getTerminalSize().getColumns() - 34,
                tw.getTerminal().getTerminalSize().getColumns() - 2, 25, '=', color);
    }

    /**
     * Рисует границы поля боя.
     * @param tw экземпляр класса TerminalWrapper для обращения непосредственно к Screen
     * @param color цвет границ.
     * @throws IOException исключение из методов Лантерны пробрасывается выше.
     */
    private static void drawBattlefield(final TerminalWrapper tw, final String color) throws IOException {
        int center = tw.getTerminal().getTerminalSize().getColumns() / 2;
        TerminalLineDrawer.drawHorizontalLine(tw,  center - 35,center + 35, 3, '=', color);
        TerminalLineDrawer.drawHorizontalLine(tw,  center - 35,center + 35, 34, '=', color);
        TerminalLineDrawer.drawVerticalLine(tw, center - 35, 3, 34, '|', color);
        TerminalLineDrawer.drawVerticalLine(tw, center + 35, 3, 34, '|', color);

    }
}
