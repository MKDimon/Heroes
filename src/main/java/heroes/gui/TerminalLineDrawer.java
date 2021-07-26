package heroes.gui;

import com.googlecode.lanterna.graphics.TextGraphics;
import heroes.gui.utils.TextColorMap;
/**
 * Обертка над группой методов Лантерны, позволяющая рисовать горизонтальные и вертикальные линии с заданным цветом.
 */
public class TerminalLineDrawer {
    /**
     * Метод рисует горизонтальную линию путем обращения к Screen в TerminalWrapper. Создает новое текстовое поле,
     * наполняет его и отправляет во внутренний буфер терминала.
     * @param tw экземпляр класса TerminalWrapper для обращения непосредственно к Screen
     * @param x1 левая граница отрисовки (column 1)
     * @param x2 правая граница отрисовки (column 2)
     * @param y строка, в которой будет нарисована линия (row)
     * @param c символ, из которого будет состоять линия
     * @param color цвет линии
     */
    public static void drawHorizontalLine(final TerminalWrapper tw, final int x1, final int x2,
                                   final int y, final char c, final String color) {
            TextGraphics tg = tw.newTG();
            tg.setForegroundColor(TextColorMap.getColor(color));
            tg.drawLine(x1, y, x2, y, c);
            tg.setForegroundColor(TextColorMap.getColor("white"));
    }

    /**
     *Метод рисует вертикальную линию путем обращения к Screen в TerminalWrapper. Создает новое текстовое поле,
     * наполняет его и отправляет во внутренний буфер терминала.
     * @param tw экземпляр класса TerminalWrapper для обращения непосредственно к Screen
     * @param x колонка, в которой будет нарисована линия (column)
     * @param y1 верхняя граница отрисовки (row 1)
     * @param y2 нижняя граница отрисовки (row 2)
     * @param c символ, из которого будет состоять линия
     * @param color цвет линии
     */
    public static void drawVerticalLine(final TerminalWrapper tw,final int x, final int y1,
                                 final int y2, final char c, final String color) {
        TextGraphics tg = tw.newTG();
        tg.setForegroundColor(TextColorMap.getColor(color));
        tg.drawLine(x, y1, x, y2, c);
        tg.setForegroundColor(TextColorMap.getColor("white"));
    }
}
