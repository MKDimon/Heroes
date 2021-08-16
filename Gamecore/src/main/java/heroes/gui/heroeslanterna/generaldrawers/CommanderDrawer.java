package heroes.gui.heroeslanterna.generaldrawers;

import com.googlecode.lanterna.graphics.TextGraphics;
import heroes.gui.heroeslanterna.LanternaWrapper;
import heroes.gui.heroeslanterna.utils.Side;

import java.io.IOException;

public class CommanderDrawer implements IGeneralDrawer {
    @Override
    public void draw(final LanternaWrapper tw, final Side s) throws IOException {
        int y_start = 1;
        final int x_start = (s == Side.RHS) ? tw.getTerminal().getTerminalSize().getColumns() - 30 : 5;

        final TextGraphics tg = tw.getScreen().newTextGraphics();
        tg.putString(x_start, y_start++, "      _,.");
        tg.putString(x_start, y_start++, "    ,` -.)");
        tg.putString(x_start, y_start++, "  ( _/-\\\\-._");
        tg.putString(x_start, y_start++, " /,|`--._,-^|             ,");
        tg.putString(x_start, y_start++, " \\_| |`-._/||           ,'|");
        tg.putString(x_start, y_start++, "   |  `-, / |          /  /");
        tg.putString(x_start, y_start++, "   |    ||  |         /  /");
        tg.putString(x_start, y_start++, "    `r-.||_/   __    /  /");
        tg.putString(x_start, y_start++, "__,-<_     )`-/  `. /  /");
        tg.putString(x_start, y_start++, "'  \\   `---'   \\   /  /");
        tg.putString(x_start, y_start++, "    |           |./  /");
        tg.putString(x_start, y_start++, "    /           //  /");
        tg.putString(x_start, y_start++, "\\_/' \\         |/  /");
        tg.putString(x_start, y_start++, " |    |   _,^-'/  /");
        tg.putString(x_start, y_start++, " |    , ``  (\\/  /_");
        tg.putString(x_start, y_start++, "  \\,.->._    \\X-=/^");
        tg.putString(x_start, y_start++, "  (  /   `-._//^`");
        tg.putString(x_start, y_start++, "  `Y-.____(__}");
        tg.putString(x_start, y_start++, "     |     {__)");
        tg.putString(x_start, y_start, "          ()");
    }
}
