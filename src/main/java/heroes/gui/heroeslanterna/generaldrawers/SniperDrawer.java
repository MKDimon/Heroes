package heroes.gui.heroeslanterna.generaldrawers;

import com.googlecode.lanterna.graphics.TextGraphics;
import heroes.gui.heroeslanterna.LanternaWrapper;
import heroes.gui.heroeslanterna.utils.Side;

import java.io.IOException;

public class SniperDrawer implements IGeneralDrawer {
    @Override
    public void draw(final LanternaWrapper tw, final Side s) throws IOException {
        int y_start = - 1;
        int x_start = (s == Side.RHS) ? tw.getTerminal().getTerminalSize().getColumns() - 34 : 2;

        TextGraphics tg = tw.getScreen().newTextGraphics();
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
