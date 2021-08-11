package heroes.gui.heroeslanterna.generaldrawers;

import com.googlecode.lanterna.graphics.TextGraphics;
import heroes.gui.heroeslanterna.LanternaWrapper;
import heroes.gui.heroeslanterna.utils.Side;

import java.io.IOException;

public class SniperDrawer implements IGeneralDrawer {
    @Override
    public void draw(final LanternaWrapper tw, final Side s) throws IOException {
        int y_start = - 1;
        final int x_start = (s == Side.RHS) ? tw.getTerminal().getTerminalSize().getColumns() - 34 : 2;

        final TextGraphics tg = tw.getScreen().newTextGraphics();
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
