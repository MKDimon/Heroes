package heroes.gui.menudrawers;

import com.googlecode.lanterna.TerminalPosition;
import heroes.gui.TerminalLineDrawer;
import heroes.gui.TerminalWrapper;

public class MenuBoardDrawer {
    private static void drawBorders(final TerminalWrapper tw, final TerminalPosition topLeft,
                                    final TerminalPosition botRight) {
        TerminalLineDrawer.drawVerticalLine(tw, topLeft.getColumn(), topLeft.getRow(),
                botRight.getRow(), '|', "white");
        TerminalLineDrawer.drawVerticalLine(tw, topLeft.getColumn(), topLeft.getRow(),
                botRight.getRow(), '|', "white");
    }
}
