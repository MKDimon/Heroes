package heroes.gui.generaldrawers;

import com.googlecode.lanterna.graphics.TextGraphics;
import heroes.gui.TerminalWrapper;
import heroes.gui.utils.Side;

import java.io.IOException;

public class CommanderDrawer implements IGeneralDrawer {
    @Override
    public void draw(final TerminalWrapper tw, final Side s) throws IOException {
        int y_start = 1;
        int x_start = (s == Side.RHS) ? tw.getTerminal().getTerminalSize().getColumns() - 30 : 5;

        TextGraphics tg = tw.getScreen().newTextGraphics();
        tg.putString(x_start, y_start + 1, "      _,.");
        tg.putString(x_start, y_start + 2, "    ,` -.)");
        tg.putString(x_start, y_start + 3, "  ( _/-\\\\-._");
        tg.putString(x_start, y_start + 4, " /,|`--._,-^|             ,");
        tg.putString(x_start, y_start + 5, " \\_| |`-._/||           ,'|");
        tg.putString(x_start, y_start + 6, "   |  `-, / |          /  /");
        tg.putString(x_start, y_start + 7, "   |    ||  |         /  /");
        tg.putString(x_start, y_start + 8, "    `r-.||_/   __    /  /");
        tg.putString(x_start, y_start + 9, "__,-<_     )`-/  `. /  /");
        tg.putString(x_start, y_start + 10, "'  \\   `---'   \\   /  /");
        tg.putString(x_start, y_start + 11, "    |           |./  /");
        tg.putString(x_start, y_start + 12, "    /           //  /");
        tg.putString(x_start, y_start + 13, "\\_/' \\         |/  /");
        tg.putString(x_start, y_start + 14, " |    |   _,^-'/  /");
        tg.putString(x_start, y_start + 15, " |    , ``  (\\/  /_");
        tg.putString(x_start, y_start + 16, "  \\,.->._    \\X-=/^");
        tg.putString(x_start, y_start + 17, "  (  /   `-._//^`");
        tg.putString(x_start, y_start + 18, "  `Y-.____(__}");
        tg.putString(x_start, y_start + 19, "     |     {__)");
        tg.putString(x_start, y_start + 20, "          ()");
    }
}
