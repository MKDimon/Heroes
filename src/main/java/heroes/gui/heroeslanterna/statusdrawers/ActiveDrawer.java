package heroes.gui.heroeslanterna.statusdrawers;

import com.googlecode.lanterna.graphics.TextGraphics;
import heroes.gui.heroeslanterna.LanternaWrapper;
import heroes.gui.heroeslanterna.utils.Colors;
import heroes.mathutils.Pair;
import heroes.units.Unit;

public class ActiveDrawer implements IStatusDrawer {
    @Override
    public void draw(final LanternaWrapper tw, final Pair<Integer, Integer> topLeftCorner, final Unit unit) {
        TextGraphics tg = tw.getScreen().newTextGraphics();
        tg.setForegroundColor(Colors.LIGHTBLUE.color());

        if (unit.isActive() && unit.isAlive()) {
            tg.putString(topLeftCorner.getX(), topLeftCorner.getY() - 1, "xxxxxxxx");
        } else if (!unit.isActive() && unit.isAlive()) {
            tg.setForegroundColor(Colors.DARKESTRED.color());
            tg.putString(topLeftCorner.getX(), topLeftCorner.getY() - 1, "xxxxxxxx");
        }

        tg.setForegroundColor(Colors.WHITE.color());
    }
}
