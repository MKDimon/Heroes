package heroes.gui.heroeslanterna.generaldrawers;

import com.googlecode.lanterna.graphics.TextGraphics;
import heroes.gui.heroeslanterna.LanternaWrapper;
import heroes.gui.heroeslanterna.utils.Side;

import java.io.IOException;

public class ArchmageDrawer implements IGeneralDrawer {
    @Override
    public void draw(final LanternaWrapper tw, final Side s) throws IOException {
        int y_start = 2;
        int x_start = (s == Side.RHS) ? tw.getTerminal().getTerminalSize().getColumns() - 34 : 5;

        TextGraphics tg = tw.getScreen().newTextGraphics();
        tg.putString(x_start, y_start + 1, "              _,._");
        tg.putString(x_start, y_start + 2, "  .||,       /_ _\\\\");
        tg.putString(x_start, y_start + 3, " \\.`',/      |'L'| |");
        tg.putString(x_start, y_start + 4, " = ,. =      | -,| L");
        tg.putString(x_start, y_start + 5, " / || \\    ,-'\\\"/,'`.");
        tg.putString(x_start, y_start + 6, "   ||     ,'   `,,. `.");
        tg.putString(x_start, y_start + 7, "   ,|____,' , ,;' \\| |");
        tg.putString(x_start, y_start + 8, "  (3|\\    _/|/'   _| |");
        tg.putString(x_start, y_start + 9, "   ||/,-''  | >-'' _,\\\\");
        tg.putString(x_start, y_start + 10, "   ||'      ==\\ ,-'  ,'");
        tg.putString(x_start, y_start + 11, "   ||       |  V \\ ,|");
        tg.putString(x_start, y_start + 12, "   ||       |    |` |");
        tg.putString(x_start, y_start + 13, "   ||       |    |   \\");
        tg.putString(x_start, y_start + 14, "   ||       |    \\    \\");
        tg.putString(x_start, y_start + 15, "   ||       |     |    \\");
        tg.putString(x_start, y_start + 16, "   ||       |      \\_,-'");
        tg.putString(x_start, y_start + 17, "   ||       |___,,--\")_\\");
        tg.putString(x_start, y_start + 18, "   ||         |_|   ccc/");
        tg.putString(x_start, y_start + 19, "   ||        ccc/       ");
        tg.putString(x_start, y_start + 20, "   ||                ");
    }
}
