package heroes.gui.menudrawers.generalmenudrawers;

import com.googlecode.lanterna.SGR;
import com.googlecode.lanterna.TerminalPosition;
import com.googlecode.lanterna.graphics.TextGraphics;
import heroes.gui.TerminalWrapper;
import heroes.gui.utils.Side;
import heroes.gui.utils.TextColorMap;

import java.io.IOException;
import java.util.EnumSet;

public class CommanderMenuDrawer implements IGeneralMenuDrawer {
    @Override
    public void draw(final TerminalWrapper tw, final TerminalPosition tp, final boolean isSelected) throws IOException {
        int y_start = tp.getRow();
        int x_start = tp.getColumn();

        TextGraphics tg = tw.getScreen().newTextGraphics();
        if (isSelected) {
            if (isSelected) {
                tg.setForegroundColor(TextColorMap.getColor("gold"));
                tg.putString(tw.getTerminal().getTerminalSize().getColumns() / 2 - 5, y_start + 25, "COMMANDER");
                tg.setForegroundColor(TextColorMap.getColor("lightblue"));
                tg.setModifiers(EnumSet.of(SGR.ITALIC));
                tg.putString(tw.getTerminal().getTerminalSize().getColumns() / 2 - 32, y_start + 27,
                        "His Majesty's Marshal of Army. Experienced warrior and general.");
                tg.putString(tw.getTerminal().getTerminalSize().getColumns() / 2 - 34, y_start + 28,
                        "The strongest knight of the Kingdom. His troops are excel in defense.");
                tg.clearModifiers();
                tg.setForegroundColor(TextColorMap.getColor("blue"));
                tg.putString(tw.getTerminal().getTerminalSize().getColumns() / 2 - 20, y_start + 30,
                        "Inspiration: ");
                tg.putString(tw.getTerminal().getTerminalSize().getColumns() / 2 - 7, y_start + 30,
                        "Defense +10% for all units.");
                tg.setForegroundColor(TextColorMap.getColor("gold"));
            }
        }
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