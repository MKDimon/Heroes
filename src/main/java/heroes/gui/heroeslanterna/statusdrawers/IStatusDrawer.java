package heroes.gui.heroeslanterna.statusdrawers;

import gamecore.units.Unit;
import heroes.gui.heroeslanterna.LanternaWrapper;
import gamecore.mathutils.Pair;

public interface IStatusDrawer {
    void draw(final LanternaWrapper tw, final Pair<Integer, Integer> topLeftCorner, final Unit unit);
}
