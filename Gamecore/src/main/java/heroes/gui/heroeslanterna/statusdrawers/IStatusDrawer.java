package heroes.gui.heroeslanterna.statusdrawers;

import heroes.gui.heroeslanterna.LanternaWrapper;
import heroes.mathutils.Pair;
import heroes.units.Unit;

public interface IStatusDrawer {
    void draw(final LanternaWrapper tw, final Pair<Integer, Integer> topLeftCorner, final Unit unit);
}
