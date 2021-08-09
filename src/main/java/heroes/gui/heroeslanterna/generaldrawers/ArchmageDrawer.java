package heroes.gui.heroeslanterna.generaldrawers;

import com.googlecode.lanterna.graphics.TextGraphics;
import heroes.gui.heroeslanterna.LanternaWrapper;
import heroes.gui.heroeslanterna.utils.Side;

import java.io.IOException;

public class ArchmageDrawer implements IGeneralDrawer {
    @Override
    public void draw(final LanternaWrapper tw, final Side s) throws IOException {
        int y_start = 2;
        final int x_start = (s == Side.RHS) ? tw.getTerminal().getTerminalSize().getColumns() - 34 : 5;

        TextGraphics tg = tw.getScreen().newTextGraphics();
        tg.putString(x_start, y_start++, "              _,._");
        tg.putString(x_start, y_start++, "  .||,       /_ _\\\\");
        tg.putString(x_start, y_start++, " \\.`',/      |'L'| |");
        tg.putString(x_start, y_start++, " = ,. =      | -,| L");
        tg.putString(x_start, y_start++, " / || \\    ,-'\\\"/,'`.");
        tg.putString(x_start, y_start++, "   ||     ,'   `,,. `.");
        tg.putString(x_start, y_start++, "   ,|____,' , ,;' \\| |");
        tg.putString(x_start, y_start++, "  (3|\\    _/|/'   _| |");
        tg.putString(x_start, y_start++, "   ||/,-''  | >-'' _,\\\\");
        tg.putString(x_start, y_start++, "   ||'      ==\\ ,-'  ,'");
        tg.putString(x_start, y_start++, "   ||       |  V \\ ,|");
        tg.putString(x_start, y_start++, "   ||       |    |` |");
        tg.putString(x_start, y_start++, "   ||       |    |   \\");
        tg.putString(x_start, y_start++, "   ||       |    \\    \\");
        tg.putString(x_start, y_start++, "   ||       |     |    \\");
        tg.putString(x_start, y_start++, "   ||       |      \\_,-'");
        tg.putString(x_start, y_start++, "   ||       |___,,--\")_\\");
        tg.putString(x_start, y_start++, "   ||         |_|   ccc/");
        tg.putString(x_start, y_start++, "   ||        ccc/       ");
        tg.putString(x_start, y_start, "   ||                ");
    }
}
