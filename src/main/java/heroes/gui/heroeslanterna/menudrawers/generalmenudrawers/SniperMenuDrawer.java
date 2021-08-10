package heroes.gui.heroeslanterna.menudrawers.generalmenudrawers;

import com.googlecode.lanterna.SGR;
import com.googlecode.lanterna.TerminalPosition;
import com.googlecode.lanterna.graphics.TextGraphics;
import heroes.gui.heroeslanterna.LanternaWrapper;
import heroes.gui.heroeslanterna.utils.Colors;

import java.io.IOException;
import java.util.EnumSet;

public class SniperMenuDrawer implements IGeneralMenuDrawer {
    @Override
    public void draw(final LanternaWrapper tw, final TerminalPosition tp, final boolean isSelected) throws IOException {
        int y_start = tp.getRow();
        final int x_start = tp.getColumn();

        final TextGraphics tg = tw.getScreen().newTextGraphics();
        if (isSelected) {
            tg.setForegroundColor(Colors.GOLD.color());
            tg.putString(tw.getTerminal().getTerminalSize().getColumns() / 2 - 3, y_start + 26, "SNIPER");
            tg.setForegroundColor(Colors.LIGHTBLUE.color());
            tg.setModifiers(EnumSet.of(SGR.ITALIC));
            tg.putString(tw.getTerminal().getTerminalSize().getColumns() / 2 - 27, y_start + 28,
                    "His Majesty's Jagermeister, skilled bowman and hunter.");
            tg.putString(tw.getTerminal().getTerminalSize().getColumns() / 2 - 40, y_start + 29,
                    "Sniper is the best in archery. He trains his troops to be more accurate in fight.");
            tg.clearModifiers();
            tg.setForegroundColor(Colors.BLUE.color());
            tg.putString(tw.getTerminal().getTerminalSize().getColumns() / 2 - 20, y_start + 31,
                    "Inspiration: ");
            tg.putString(tw.getTerminal().getTerminalSize().getColumns() / 2 - 7, y_start + 31,
                    "Accuracy +10% for all gamecore.units.");
            tg.setForegroundColor(Colors.GOLD.color());
        }
        tg.putString(x_start, y_start++, "        -\\\\");
        tg.putString(x_start, y_start++, "           \\\\");
        tg.putString(x_start, y_start++, "            \\|");
        tg.putString(x_start, y_start++, "              \\#####\\");
        tg.putString(x_start, y_start++, "          ==###########>");
        tg.putString(x_start, y_start++, "           \\##==      \\");
        tg.putString(x_start, y_start++, "      ______ =       =|");
        tg.putString(x_start, y_start++, "  ,--' ,----`-,__ ___/'  --,====");
        tg.putString(x_start, y_start++, " \\               '        =======");
        tg.putString(x_start, y_start++, "  `,    __==    ___,-,__,--====");
        tg.putString(x_start, y_start++, "    `-,____,---'    /// \\");
        tg.putString(x_start, y_start++, "        #_         ///  |");
        tg.putString(x_start, y_start++, "         #        ##    ]");
        tg.putString(x_start, y_start++, "         #,             ]");
        tg.putString(x_start, y_start++, "          #_            |");
        tg.putString(x_start, y_start++, "           ##_       __/'");
        tg.putString(x_start, y_start++, "            ####='     |");
        tg.putString(x_start, y_start++, "             ###       |");
        tg.putString(x_start, y_start++, "             ##       _'");
        tg.putString(x_start, y_start++, "            ###=======]");
        tg.putString(x_start, y_start, "           ///        |");


    }
}
