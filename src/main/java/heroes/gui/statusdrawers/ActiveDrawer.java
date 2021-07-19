package heroes.gui.statusdrawers;

import com.googlecode.lanterna.graphics.TextGraphics;
import heroes.gui.TerminalWrapper;
import heroes.gui.utils.TextColorMap;
import heroes.mathutils.Pair;
import heroes.units.Unit;

public class ActiveDrawer implements IStatusDrawer {
    @Override
    public void draw(final TerminalWrapper tw, final Pair<Integer, Integer> topLeftCorner, final Unit unit) {
        TextGraphics tg = tw.getScreen().newTextGraphics();
        tg.setForegroundColor(TextColorMap.getColor("lightblue"));

        if (unit.isActive() && unit.isAlive()) {
            tg.putString(topLeftCorner.getX(), topLeftCorner.getY() - 1, "xxxxxxxx");
        } else if (!unit.isActive() && unit.isAlive()) {
            tg.setForegroundColor(TextColorMap.getColor("darkestred"));
            tg.putString(topLeftCorner.getX(), topLeftCorner.getY() - 1, "xxxxxxxx");
        }

        tg.setForegroundColor(TextColorMap.getColor("white"));
    }
}
