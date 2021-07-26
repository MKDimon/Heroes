package heroes.gui.selectiondrawers;

import heroes.gui.TerminalLineDrawer;
import heroes.gui.TerminalWrapper;
import heroes.gui.utils.Colors;
import heroes.mathutils.Pair;

public class TerminalSelectionDrawer {
    public static void drawSelection(final TerminalWrapper tw, final Pair<Integer, Integer> topLeftCorner,
                                     final char c, final Colors color) {
        TerminalLineDrawer.drawVerticalLine(tw, topLeftCorner.getX() - 1,
                topLeftCorner.getY(), topLeftCorner.getY() + 8, c, color);

        TerminalLineDrawer.drawVerticalLine(tw, topLeftCorner.getX() + 8,
                topLeftCorner.getY(), topLeftCorner.getY() + 8, c, color);

    }
}
