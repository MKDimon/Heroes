package heroes.gui.heroeslanterna.menudrawers.generalmenudrawers;

import com.googlecode.lanterna.SGR;
import com.googlecode.lanterna.TerminalPosition;
import com.googlecode.lanterna.graphics.TextGraphics;
import heroes.gui.heroeslanterna.LanternaWrapper;
import heroes.gui.heroeslanterna.utils.Colors;

import java.io.IOException;
import java.util.EnumSet;

public class CommanderMenuDrawer implements IGeneralMenuDrawer {
    @Override
    public void draw(final LanternaWrapper tw, final TerminalPosition tp, final boolean isSelected) throws IOException {
        int y_start = tp.getRow();
        final int x_start = tp.getColumn();

        final TextGraphics tg = tw.getScreen().newTextGraphics();
        if (isSelected) {
            tg.setForegroundColor(Colors.GOLD.color());
            tg.putString(tw.getTerminal().getTerminalSize().getColumns() / 2 - 5, y_start + 25, "COMMANDER");
            tg.setForegroundColor(Colors.LIGHTBLUE.color());
            tg.setModifiers(EnumSet.of(SGR.ITALIC));
            tg.putString(tw.getTerminal().getTerminalSize().getColumns() / 2 - 32, y_start + 27,
                    "His Majesty's Marshal of Army. Experienced warrior and general.");
            tg.putString(tw.getTerminal().getTerminalSize().getColumns() / 2 - 34, y_start + 28,
                    "The strongest knight of the Kingdom. His troops are excel in defense.");
            tg.clearModifiers();
            tg.setForegroundColor(Colors.BLUE.color());
            tg.putString(tw.getTerminal().getTerminalSize().getColumns() / 2 - 20, y_start + 30,
                    "Inspiration: ");
            tg.putString(tw.getTerminal().getTerminalSize().getColumns() / 2 - 7, y_start + 30,
                    "Defense +10% for all gamecore.units.");
            tg.setForegroundColor(Colors.GOLD.color());
        }
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
