package heroes.gui.menudrawers.generalmenudrawers;

import com.googlecode.lanterna.SGR;
import com.googlecode.lanterna.TerminalPosition;
import com.googlecode.lanterna.graphics.TextGraphics;
import heroes.gui.TerminalWrapper;
import heroes.gui.utils.Colors;

import java.io.IOException;
import java.util.EnumSet;

public class SniperMenuDrawer implements IGeneralMenuDrawer {
    @Override
    public void draw(final TerminalWrapper tw, final TerminalPosition tp, final boolean isSelected) throws IOException {
        int y_start = tp.getRow() - 1;
        int x_start = tp.getColumn();

        TextGraphics tg = tw.getScreen().newTextGraphics();
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
                    "Accuracy +10% for all units.");
            tg.setForegroundColor(Colors.GOLD.color());
        }
        tg.putString(x_start, y_start + 2, "        -\\\\");
        tg.putString(x_start, y_start + 3, "           \\\\");
        tg.putString(x_start, y_start + 4, "            \\|");
        tg.putString(x_start, y_start + 5, "              \\#####\\");
        tg.putString(x_start, y_start + 6, "          ==###########>");
        tg.putString(x_start, y_start + 7, "           \\##==      \\");
        tg.putString(x_start, y_start + 8, "      ______ =       =|");
        tg.putString(x_start, y_start + 9, "  ,--' ,----`-,__ ___/'  --,====");
        tg.putString(x_start, y_start + 10, " \\               '        =======");
        tg.putString(x_start, y_start + 11, "  `,    __==    ___,-,__,--====");
        tg.putString(x_start, y_start + 12, "    `-,____,---'    /// \\");
        tg.putString(x_start, y_start + 13, "        #_         ///  |");
        tg.putString(x_start, y_start + 14, "         #        ##    ]");
        tg.putString(x_start, y_start + 15, "         #,             ]");
        tg.putString(x_start, y_start + 16, "          #_            |");
        tg.putString(x_start, y_start + 17, "           ##_       __/'");
        tg.putString(x_start, y_start + 18, "            ####='     |");
        tg.putString(x_start, y_start + 19, "             ###       |");
        tg.putString(x_start, y_start + 20, "             ##       _'");
        tg.putString(x_start, y_start + 21, "            ###=======]");
        tg.putString(x_start, y_start + 22, "           ///        |");


    }
}
