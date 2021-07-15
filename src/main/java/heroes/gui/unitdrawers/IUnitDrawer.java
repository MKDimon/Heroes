package heroes.gui.unitdrawers;

import com.googlecode.lanterna.graphics.TextImage;
import heroes.gui.TerminalWrapper;
import heroes.mathutils.Pair;
import heroes.units.UnitTypes;

public interface IUnitDrawer {
    TextImage formTextImage(final boolean isGeneral);
    void draw(final TerminalWrapper tw, final Pair<Integer, Integer> topLeftCorner,
              final boolean isGeneral);
}
