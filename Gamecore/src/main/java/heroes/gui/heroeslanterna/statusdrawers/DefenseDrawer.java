package heroes.gui.heroeslanterna.statusdrawers;

import com.googlecode.lanterna.graphics.TextGraphics;
import heroes.gui.heroeslanterna.LanternaWrapper;
import heroes.gui.heroeslanterna.utils.Colors;
import heroes.mathutils.Pair;
import heroes.units.Unit;

public class DefenseDrawer implements IStatusDrawer {
    @Override
    public void draw(final LanternaWrapper tw, final Pair<Integer, Integer> topLeftCorner, final Unit unit) {
        final TextGraphics tg = tw.getScreen().newTextGraphics();

        tg.setForegroundColor(Colors.LIGHTBLUE.color());
        final int def = unit.getDefenseArmor();
        if (def > 0) {
            if (topLeftCorner.getX() > tw.getScreen().getTerminalSize().getColumns() / 2) {
                final int gap = 3;
                tg.putString(topLeftCorner.getX() - gap, topLeftCorner.getY() , "  xx");
                tg.putString(topLeftCorner.getX() - gap, topLeftCorner.getY() + 1," xx");
                tg.putString(topLeftCorner.getX() - gap, topLeftCorner.getY() + 2,"xx");
                tg.putString(topLeftCorner.getX() - gap, topLeftCorner.getY() + 3,"xx");
                tg.putString(topLeftCorner.getX() - gap, topLeftCorner.getY() + 4,"xx");
                tg.putString(topLeftCorner.getX() - gap, topLeftCorner.getY() + 5,"xx");
                tg.putString(topLeftCorner.getX() - gap, topLeftCorner.getY() + 6," xx");
                tg.putString(topLeftCorner.getX() - gap, topLeftCorner.getY() + 7,"  xx");
            } else {
                tg.putString(topLeftCorner.getX() + 8, topLeftCorner.getY() , "xx");
                tg.putString(topLeftCorner.getX() + 8, topLeftCorner.getY() + 1," xx");
                tg.putString(topLeftCorner.getX() + 8, topLeftCorner.getY() + 2,"  xx");
                tg.putString(topLeftCorner.getX() + 8, topLeftCorner.getY() + 3,"  xx");
                tg.putString(topLeftCorner.getX() + 8, topLeftCorner.getY() + 4,"  xx");
                tg.putString(topLeftCorner.getX() + 8, topLeftCorner.getY() + 5,"  xx");
                tg.putString(topLeftCorner.getX() + 8, topLeftCorner.getY() + 6," xx");
                tg.putString(topLeftCorner.getX() + 8, topLeftCorner.getY() + 7,"xx");
            }

        }
        tg.setForegroundColor(Colors.WHITE.color());
    }
}
