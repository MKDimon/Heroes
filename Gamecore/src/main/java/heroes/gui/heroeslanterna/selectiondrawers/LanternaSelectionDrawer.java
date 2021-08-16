package heroes.gui.heroeslanterna.selectiondrawers;

import heroes.gui.heroeslanterna.LanternaLineDrawer;
import heroes.gui.heroeslanterna.LanternaWrapper;
import heroes.gui.heroeslanterna.utils.Colors;
import heroes.mathutils.Pair;

public class LanternaSelectionDrawer {
    public static void drawSelection(final LanternaWrapper tw, final Pair<Integer, Integer> topLeftCorner,
                                     final char c, final Colors color) {
        LanternaLineDrawer.drawVerticalLine(tw, topLeftCorner.getX() - 1,
                topLeftCorner.getY(), topLeftCorner.getY() + 8, c, color);

        LanternaLineDrawer.drawVerticalLine(tw, topLeftCorner.getX() + 8,
                topLeftCorner.getY(), topLeftCorner.getY() + 8, c, color);

    }
}
