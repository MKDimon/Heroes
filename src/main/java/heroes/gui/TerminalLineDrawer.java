package heroes.gui;

import com.googlecode.lanterna.graphics.TextGraphics;
import heroes.gui.utils.TextColorMap;

import java.io.IOException;

public class TerminalLineDrawer {
    public static void drawHorizontalLine(final TerminalWrapper tw, final int x1, final int x2,
                                   final int y, final char c, final String color) throws IOException {
            TextGraphics tg = tw.getScreen().newTextGraphics();
            tg.setForegroundColor(TextColorMap.getColor(color));
            tg.drawLine(x1, y, x2, y, c);
            tg.setForegroundColor(TextColorMap.getColor("white"));
    }

    public static void drawVerticalLine(final TerminalWrapper tw,final int x, final int y1,
                                 final int y2, final char c, final String color) throws IOException {
        TextGraphics tg = tw.getScreen().newTextGraphics();
        tg.setForegroundColor(TextColorMap.getColor(color));
        tg.drawLine(x, y1, x, y2, c);
        tg.setForegroundColor(TextColorMap.getColor("white"));
    }
}
