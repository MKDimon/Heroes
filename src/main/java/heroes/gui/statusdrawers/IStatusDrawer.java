package heroes.gui.statusdrawers;

import heroes.gui.TerminalWrapper;
import heroes.mathutils.Pair;
import heroes.units.Unit;

public interface IStatusDrawer {
    void draw(final TerminalWrapper tw, final Pair<Integer, Integer> topLeftCorner, final Unit unit);
}
